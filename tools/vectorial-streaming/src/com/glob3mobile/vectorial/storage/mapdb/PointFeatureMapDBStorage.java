

package com.glob3mobile.vectorial.storage.mapdb;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOError;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.mapdb.BTreeMap;
import org.mapdb.DB;
import org.mapdb.DB.BTreeMapMaker;
import org.mapdb.DBMaker;
import org.mapdb.Serializer;

import com.glob3mobile.geo.Geodetic2D;
import com.glob3mobile.geo.Sector;
import com.glob3mobile.utils.UndeterminateProgress;
import com.glob3mobile.vectorial.storage.PointFeature;
import com.glob3mobile.vectorial.storage.PointFeatureStorage;
import com.glob3mobile.vectorial.storage.PointFeaturesSet;
import com.glob3mobile.vectorial.storage.QuadKey;
import com.glob3mobile.vectorial.storage.QuadKeyComparator;
import com.glob3mobile.vectorial.utils.MapDBUtils;


public class PointFeatureMapDBStorage
   implements
      PointFeatureStorage {


   public static void delete(final File directory,
                             final String name) throws IOException {
      if (directory.exists()) {
         final File[] children = directory.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(final File dir,
                                  final String childName) {
               return childName.startsWith(name);
            }
         });

         for (final File child : children) {
            if (!child.delete()) {
               throw new IOException("Can't delete " + child);
            }
         }

         if (directory.list().length == 0) {
            directory.delete();
         }
      }
   }


   public static PointFeatureStorage createEmpty(final Sector sector,
                                                 final File directory,
                                                 final String name,
                                                 final int maxBufferSize,
                                                 final int maxFeaturesPerNode) throws IOException {
      delete(directory, name);

      return new PointFeatureMapDBStorage(sector, directory, name, maxBufferSize, maxFeaturesPerNode);
   }


   public static PointFeatureStorage open(final File directory,
                                          final String name) throws IOException {
      final boolean readOnly = false;
      return new PointFeatureMapDBStorage(directory, name, readOnly);
   }


   public static PointFeatureStorage openReadOnly(final File directory,
                                                  final String name) throws IOException {
      final boolean readOnly = true;
      return new PointFeatureMapDBStorage(directory, name, readOnly);
   }


   private final Sector                               _sector;
   private final File                                 _directory;
   private final String                               _name;
   private final boolean                              _readOnly;
   private final DB                                   _db;


   private double                                     _minLatRad;
   private double                                     _minLonRad;
   private double                                     _maxLatRad;
   private double                                     _maxLonRad;

   private final int                                  _maxBufferSize;
   private final List<PointFeature>                   _buffer;
   private final int                                  _maxFeaturesPerNode;


   private final QuadKey                              _rootKey;

   private final BTreeMap<byte[], NodeHeader>         _nodesHeaders;

   //   private final BTreeMap<byte[], List<PointFeature>> _nodesFeatures;
   private final BTreeMap<byte[], List<MapDBFeature>> _nodesFeatures;
   private final BTreeMap<Long, Map<String, Object>>  _properties;

   private final BTreeMap<String, Object>             _metadata;


   private static class MapDBFeaturesSerializer
      implements
         Serializer<List<MapDBFeature>>,
         Serializable {

      private static final long serialVersionUID = 1L;


      @Override
      public void serialize(final DataOutput out,
                            final List<MapDBFeature> features) throws IOException {
         out.writeInt(features.size());
         for (final MapDBFeature feature : features) {
            SerializerUtils.serializeGeodetic2D(out, feature._position);
            out.writeLong(feature._propertiesID);
         }
      }


      @Override
      public List<MapDBFeature> deserialize(final DataInput in,
                                            final int available) throws IOException {
         final int size = in.readInt();
         final List<MapDBFeature> features = new ArrayList<>(size);
         for (int i = 0; i < size; i++) {
            final Geodetic2D position = SerializerUtils.deserializeGeodetic2D(in);
            final long propertiesID = in.readLong();
            final MapDBFeature feature = new MapDBFeature(position, propertiesID);
            features.add(feature);
         }
         return features;
      }


      @Override
      public int fixedSize() {
         return -1;
      }
   }


   private static class PropertiesSerializer
      implements
         Serializer<Map<String, Object>>,
         Serializable {


      private static final long serialVersionUID = 1L;


      @Override
      public void serialize(final DataOutput out,
                            final Map<String, Object> value) throws IOException {
         SerializerUtils.serializeMap(out, value);
      }


      @Override
      public Map<String, Object> deserialize(final DataInput in,
                                             final int available) throws IOException {
         return SerializerUtils.deserializeMap(in);
      }


      @Override
      public int fixedSize() {
         return -1;
      }

   }


   private PointFeatureMapDBStorage(final Sector sector,
                                    final File directory,
                                    final String name,
                                    final int maxBufferSize,
                                    final int maxFeaturesPerNode) throws IOException {
      // constructor for a new Storage

      _sector = sector;
      _directory = directory;
      _name = name;
      _maxBufferSize = maxBufferSize;
      _buffer = new ArrayList<>(_maxBufferSize);
      resetBufferBounds();
      _maxFeaturesPerNode = maxFeaturesPerNode;

      _readOnly = false;
      _rootKey = new QuadKey(new byte[] {}, _sector);

      if (!_directory.exists()) {
         if (!_directory.mkdirs()) {
            throw new IOException("Can't create directory: " + _directory);
         }
      }

      if (!_directory.isDirectory()) {
         throw new IOException(_directory + " is not a directory");
      }

      final File file = new File(_directory, _name);
      final DBMaker maker = DBMaker.newFileDB(file);
      // maker.cacheLRUEnable();
      maker.compressionEnable();

      try {
         _db = maker.make();
      }
      catch (final IOError | UnsupportedOperationException e) {
         throw new IOException(e.getMessage());
      }

      final QuadKeyComparator comparator = new QuadKeyComparator();

      final BTreeMapMaker nodesHeadersMaker = _db //
      .createTreeMap("NodesHeaders") //
      .counterEnable() //
      .comparator(comparator) //
      .valueSerializer(new NodeHeaderSerializer());
      _nodesHeaders = nodesHeadersMaker.makeOrGet();


      final BTreeMapMaker nodesFeaturesMaker = _db //
      .createTreeMap("NodesFeatures") //
      .counterEnable() //
      .comparator(comparator) //
      .valueSerializer(new MapDBFeaturesSerializer());
      _nodesFeatures = nodesFeaturesMaker.makeOrGet();

      // private final BTreeMap<Long, Map<String, Object>>           _properties;

      final BTreeMapMaker propertiesMaker = _db //
      .createTreeMap("Properties") //
      .counterEnable() //
      .valueSerializer(new PropertiesSerializer());
      _properties = propertiesMaker.makeLongMap();

      _metadata = _db.createTreeMap("Metadata").counterEnable().makeOrGet();

      saveMetadata();
   }


   private void saveMetadata() {
      MapDBUtils.saveSector(_metadata, "sector", _sector);
      MapDBUtils.saveInt(_metadata, "maxBufferSize", _maxBufferSize);
      MapDBUtils.saveInt(_metadata, "maxFeaturesPerNode", _maxFeaturesPerNode);
      _db.commit();
   }


   private PointFeatureMapDBStorage(final File directory,
                                    final String name,
                                    final boolean readOnly) throws IOException {
      // constructor for a already created Storage

      _directory = directory;
      _name = name;
      _readOnly = readOnly;

      if (!_directory.exists()) {
         throw new IOException(_directory + " doesn't exists");
      }
      if (!_directory.isDirectory()) {
         throw new IOException(_directory + " is not a directory");
      }

      final File file = new File(_directory, _name);
      final DBMaker maker = DBMaker.newFileDB(file);
      maker.compressionEnable();
      if (_readOnly) {
         maker.strictDBGet();
         maker.readOnly();
      }
      else {
         maker.strictDBGet();
      }

      try {
         _db = maker.make();
      }
      catch (final IOError | UnsupportedOperationException e) {
         throw new IOException(e.getMessage());
      }


      final QuadKeyComparator comparator = new QuadKeyComparator();

      final BTreeMapMaker nodesHeadersMaker = _db //
      .createTreeMap("NodesHeaders") //
      .counterEnable() //
      .comparator(comparator) //
      .valueSerializer(new NodeHeaderSerializer());

      _nodesHeaders = nodesHeadersMaker.makeOrGet();


      final BTreeMapMaker nodesFeaturesMaker = _db //
      .createTreeMap("NodesFeatures") //
      .counterEnable() //
      .comparator(comparator) //
      .valueSerializer(new MapDBFeaturesSerializer());

      _nodesFeatures = nodesFeaturesMaker.makeOrGet();

      _metadata = _db.createTreeMap("Metadata").counterEnable().makeOrGet();

      _sector = MapDBUtils.readSector(_metadata, "sector");
      _rootKey = new QuadKey(new byte[] {}, _sector);

      _maxBufferSize = MapDBUtils.readInt(_metadata, "maxBufferSize");
      _buffer = new ArrayList<>(_maxBufferSize);
      resetBufferBounds();
      _maxFeaturesPerNode = MapDBUtils.readInt(_metadata, "maxFeaturesPerNode");


      final BTreeMapMaker propertiesMaker = _db //
      .createTreeMap("Properties") //
      .counterEnable() //
      .valueSerializer(new PropertiesSerializer());
      _properties = propertiesMaker.makeOrGet();
   }


   @Override
   synchronized public void close() throws IOException {
      flush();
      _db.close();
   }


   private void resetBufferBounds() {
      _minLatRad = Double.POSITIVE_INFINITY;
      _minLonRad = Double.POSITIVE_INFINITY;

      _maxLatRad = Double.NEGATIVE_INFINITY;
      _maxLonRad = Double.NEGATIVE_INFINITY;
   }


   @Override
   synchronized public void flush() throws IOException {
      final int bufferSize = _buffer.size();
      if (bufferSize > 0) {
         //deleteCachedStatistics();
         try {
            final Sector minimumSector = Sector.fromRadians( //
                     _minLatRad, _minLonRad, //
                     _maxLatRad, _maxLonRad);

            //            final Sector boundsSector = getBounds(_buffer);
            //            if (!minimumSector.equals(boundsSector)) {
            //               throw new RuntimeException("LOGIC ERROR");
            //            }

            final QuadKey quadKey = QuadKey.deepestEnclosingNodeKey(_rootKey, minimumSector);


            final PointFeaturesSet featuresSet = new PointFeaturesSet(minimumSector, new ArrayList<>(_buffer));
            PointFeatureMapDBNode.insertFeatures(this, quadKey, featuresSet);

            _db.commit();
         }
         catch (final IOError | UnsupportedOperationException e) {
            throw new IOException(e.getMessage());
         }


         _buffer.clear();
         resetBufferBounds();
      }
   }


   public int getMaxFeaturesPerNode() {
      return _maxFeaturesPerNode;
   }


   @Override
   synchronized public void optimize() throws IOException {
      flush();
      try {
         _db.compact();
      }
      catch (final IOError | UnsupportedOperationException e) {
         throw new IOException(e.getMessage());
      }
   }


   BTreeMap<byte[], NodeHeader> getNodesHeadersMap() {
      return _nodesHeaders;
   }


   //   BTreeMap<byte[], List<PointFeature>> getNodesFeatures() {
   //      return _nodesFeatures;
   //   }


   BTreeMap<byte[], List<MapDBFeature>> getNodesFeaturesMap() {
      return _nodesFeatures;
   }


   BTreeMap<Long, Map<String, Object>> getPropertiesMap() {
      return _properties;
   }


   @Override
   synchronized public void addFeature(final PointFeature feature) throws IOException {
      if (_readOnly) {
         throw new RuntimeException("Readonly: Can't add");
      }

      if (!_sector.contains(feature._position)) {
         throw new RuntimeException("Feature doesn't fit into the given sector");
      }

      _buffer.add(feature);

      final Geodetic2D position = feature._position;

      final double latRad = position._latitude._radians;
      final double lonRad = position._longitude._radians;

      if (latRad < _minLatRad) {
         _minLatRad = latRad;
      }
      if (latRad > _maxLatRad) {
         _maxLatRad = latRad;
      }

      if (lonRad < _minLonRad) {
         _minLonRad = lonRad;
      }
      if (lonRad > _maxLonRad) {
         _maxLonRad = lonRad;
      }


      if (_buffer.size() == _maxBufferSize) {
         flush();
      }
   }


   @Override
   public void acceptDepthFirstVisitor(final PointFeatureStorage.NodeVisitor visitor) {
      visitor.start();

      final List<MapDBFeature> features = null;

      final BTreeMap<byte[], NodeHeader> nodesHeaders = getNodesHeadersMap();
      for (final Map.Entry<byte[], NodeHeader> entry : nodesHeaders.entrySet()) {
         final byte[] id = entry.getKey();
         final NodeHeader header = entry.getValue();
         final PointFeatureMapDBNode node = new PointFeatureMapDBNode(this, id, header, features);
         final boolean keepGoing = visitor.visit(node);
         if (!keepGoing) {
            break;
         }
      }

      visitor.stop();
   }


   private static class PvtStatistics
      implements
         PointFeatureStorage.Statistics {

      private final String _storageName;

      private final long   _featuresCount;
      private final int    _nodesCount;
      private final int    _minFeaturesPerNode;
      private final int    _maxFeaturesPerNode;
      private final double _averageFeaturesPerNode;
      private final int    _minNodeDepth;
      private final int    _maxNodeDepth;
      private final double _averageNodeDepth;


      private PvtStatistics(final String storageName,
                            final long featuresCount,
                            final int nodesCount,
                            final int minFeaturesPerNode,
                            final int maxFeaturesPerNode,
                            final double averageFeaturesPerNode,
                            final int minNodeDepth,
                            final int maxNodeDepth,
                            final double averageNodeDepth) {
         _storageName = storageName;
         _featuresCount = featuresCount;
         _nodesCount = nodesCount;
         _minFeaturesPerNode = minFeaturesPerNode;
         _maxFeaturesPerNode = maxFeaturesPerNode;
         _averageFeaturesPerNode = averageFeaturesPerNode;
         _minNodeDepth = minNodeDepth;
         _maxNodeDepth = maxNodeDepth;
         _averageNodeDepth = averageNodeDepth;
      }


      @Override
      public long getFeaturesCount() {
         return _featuresCount;
      }


      @Override
      public int getNodesCount() {
         return _nodesCount;
      }


      @Override
      public int getMinFeaturesPerNode() {
         return _minFeaturesPerNode;
      }


      @Override
      public int getMaxFeaturesPerNode() {
         return _maxFeaturesPerNode;
      }


      @Override
      public double getAverageFeaturesPerNode() {
         return _averageFeaturesPerNode;
      }


      @Override
      public void show() {
         System.out.println("--------------------------------------------------------------");
         System.out.println(" Storage: " + _storageName);
         System.out.println("  Features: " + _featuresCount);
         System.out.println("  Nodes Count: " + _nodesCount);
         System.out.println("  Features/Node: " + //
                            "min=" + _minFeaturesPerNode + //
                            ", max=" + _maxFeaturesPerNode + //
                            ", avg=" + (float) _averageFeaturesPerNode);
         System.out.println("  Node Depth: " + //
                            "min=" + _minNodeDepth + //
                            ", max=" + _maxNodeDepth + //
                            ", avg=" + (float) _averageNodeDepth);
         System.out.println("--------------------------------------------------------------");
      }


      @Override
      public int getMaxNodeDepth() {
         return _maxNodeDepth;
      }


      @Override
      public int getMinNodeDepth() {
         return _minNodeDepth;
      }


      @Override
      public double getAverageNodeDepth() {
         return _averageNodeDepth;
      }

   }


   private static class StatisticsGatherer
      implements
         PointFeatureStorage.NodeVisitor {

      private final String          _name;
      private final boolean         _showProgress;

      private UndeterminateProgress _progress   = null;
      private Statistics            _statistics = null;


      private long                  _featuresCount;
      private int                   _nodesCount;
      private int                   _minFeaturesPerNode;
      private int                   _maxFeaturesPerNode;
      private int                   _minNodeDepth;
      private int                   _maxNodeDepth;
      private long                  _sumDepth;


      private StatisticsGatherer(final String name,
                                 final boolean showProgress) {
         _name = name;
         _showProgress = showProgress;
      }


      @Override
      public void start() {
         _featuresCount = 0;
         _nodesCount = 0;

         _minFeaturesPerNode = Integer.MAX_VALUE;
         _maxFeaturesPerNode = Integer.MIN_VALUE;

         _sumDepth = 0;
         _minNodeDepth = Integer.MAX_VALUE;
         _maxNodeDepth = Integer.MIN_VALUE;

         _statistics = null;
         _progress = !_showProgress ? null : new UndeterminateProgress(true) {
            @Override
            public void informProgress(final long stepsDone,
                                       final long elapsed) {
               System.out.println(_name + " - Gathering statistics:" + progressString(stepsDone, elapsed));
            }
         };
      }


      @Override
      public boolean visit(final PointFeatureStorage.Node node) {
         _nodesCount++;

         final int nodeFeaturesCount = node.getFeaturesCount();

         _featuresCount += nodeFeaturesCount;
         _minFeaturesPerNode = Math.min(_minFeaturesPerNode, nodeFeaturesCount);
         _maxFeaturesPerNode = Math.max(_maxFeaturesPerNode, nodeFeaturesCount);

         final int nodeDepth = node.getDepth();
         _sumDepth += nodeDepth;
         _minNodeDepth = Math.min(_minNodeDepth, nodeDepth);
         _maxNodeDepth = Math.max(_maxNodeDepth, nodeDepth);


         if (_progress != null) {
            _progress.stepDone();
         }

         return true;
      }


      @Override
      public void stop() {
         _statistics = new PvtStatistics( //
                  _name, //
                  _featuresCount, //
                  _nodesCount, //
                  _minFeaturesPerNode, //
                  _maxFeaturesPerNode, //
                  (double) _featuresCount / _nodesCount, //
                  _minNodeDepth, //
                  _maxNodeDepth, //
                  (double) _sumDepth / _nodesCount);

         if (_progress != null) {
            _progress.finish();
            _progress = null;
         }
      }
   }


   @Override
   public PointFeatureStorage.Statistics getStatistics(final boolean showProgress) {
      final StatisticsGatherer gatherer = new StatisticsGatherer(_name, showProgress);
      acceptDepthFirstVisitor(gatherer);
      return gatherer._statistics;
   }


   @Override
   public String getName() {
      return _name;
   }


   @Override
   public Sector getSector() {
      return _sector;
   }


}
