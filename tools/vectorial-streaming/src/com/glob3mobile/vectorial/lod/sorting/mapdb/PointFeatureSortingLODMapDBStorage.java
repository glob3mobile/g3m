

package com.glob3mobile.vectorial.lod.sorting.mapdb;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOError;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;

import org.mapdb.BTreeMap;
import org.mapdb.DB;
import org.mapdb.DB.BTreeMapMaker;
import org.mapdb.DBMaker;

import com.glob3mobile.geo.Geodetic2D;
import com.glob3mobile.geo.Sector;
import com.glob3mobile.utils.Progress;
import com.glob3mobile.utils.UndeterminateProgress;
import com.glob3mobile.vectorial.lod.sorting.PointFeatureSortingLODStorage;
import com.glob3mobile.vectorial.storage.PointFeature;
import com.glob3mobile.vectorial.storage.PointFeaturesSet;
import com.glob3mobile.vectorial.storage.QuadKey;
import com.glob3mobile.vectorial.storage.QuadKeyComparator;
import com.glob3mobile.vectorial.storage.QuadKeyUtils;
import com.glob3mobile.vectorial.storage.mapdb.NodeHeader;
import com.glob3mobile.vectorial.storage.mapdb.NodeHeaderSerializer;
import com.glob3mobile.vectorial.storage.mapdb.PointFeaturesSerializer;
import com.glob3mobile.vectorial.utils.CollectionUtils;
import com.glob3mobile.vectorial.utils.MapDBUtils;


public class PointFeatureSortingLODMapDBStorage
   implements
      PointFeatureSortingLODStorage {


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


   public static PointFeatureSortingLODStorage createEmpty(final Sector sector,
                                                           final File directory,
                                                           final String name,
                                                           final int maxFeaturesPerNode) throws IOException {
      PointFeatureSortingLODMapDBStorage.delete(directory, name);

      return new PointFeatureSortingLODMapDBStorage(sector, directory, name, maxFeaturesPerNode);
   }


   public static PointFeatureSortingLODStorage openReadOnly(final File directory,
                                                            final String name) throws IOException {
      return new PointFeatureSortingLODMapDBStorage(directory, name);
   }


   private final Sector                               _sector;
   private final QuadKey                              _rootKey;
   private final String                               _name;
   private final boolean                              _readOnly;
   private final int                                  _maxFeaturesPerNode;

   private final DB                                   _db;
   private final BTreeMap<byte[], NodeHeader>         _nodesHeaders;
   private final BTreeMap<byte[], List<PointFeature>> _nodesFeatures;
   private final NavigableSet<byte[]>                 _pendingNodes;
   private final BTreeMap<String, Object>             _metadata;


   private PointFeatureSortingLODMapDBStorage(final Sector sector,
                                              final File directory,
                                              final String name,
                                              final int maxFeaturesPerNode) throws IOException {
      // Constructor for new LOD-Storage
      _sector = sector;
      _rootKey = new QuadKey(new byte[] {}, _sector);

      _name = name;

      _readOnly = false;
      _db = open(directory, name, _readOnly);

      final QuadKeyComparator quadKeyComparator = new QuadKeyComparator();

      final BTreeMapMaker nodesHeadersMaker = _db //
      .createTreeMap("NodesHeaders") //
      .counterEnable() //
      .comparator(quadKeyComparator) //
      .valueSerializer(new NodeHeaderSerializer());

      _nodesHeaders = nodesHeadersMaker.makeOrGet();


      final BTreeMapMaker nodesFeaturesMaker = _db //
      .createTreeMap("NodesFeatures") //
      .counterEnable() //
      .comparator(quadKeyComparator) //
      .valueSerializer(new PointFeaturesSerializer());

      _nodesFeatures = nodesFeaturesMaker.makeOrGet();

      _pendingNodes = _db //
      .createTreeSet("PendingNodes") //
      .counterEnable() //
      .comparator(quadKeyComparator).makeOrGet();

      _metadata = _db.createTreeMap("Metadata").counterEnable().makeOrGet();

      _maxFeaturesPerNode = maxFeaturesPerNode;
      saveMetadata();
   }


   private void saveMetadata() {
      MapDBUtils.saveSector(_metadata, "sector", _sector);
      MapDBUtils.saveInt(_metadata, "maxFeaturesPerNode", _maxFeaturesPerNode);
      _db.commit();
   }


   private PointFeatureSortingLODMapDBStorage(final File directory,
                                              final String name) throws IOException {
      // Constructor for a alread existing LOD-Storage, read only

      _name = name;

      _readOnly = true;
      _db = open(directory, name, _readOnly);

      final QuadKeyComparator quadKeyComparator = new QuadKeyComparator();

      final BTreeMapMaker nodesHeadersMaker = _db //
      .createTreeMap("NodesHeaders") //
      .counterEnable() //
      .comparator(quadKeyComparator) //
      .valueSerializer(new NodeHeaderSerializer());

      _nodesHeaders = nodesHeadersMaker.makeOrGet();


      final BTreeMapMaker nodesFeaturesMaker = _db //
      .createTreeMap("NodesFeatures") //
      .counterEnable() //
      .comparator(quadKeyComparator) //
      .valueSerializer(new PointFeaturesSerializer());

      _nodesFeatures = nodesFeaturesMaker.makeOrGet();

      _pendingNodes = _db //
      .createTreeSet("PendingNodes") //
      .counterEnable() //
      .comparator(quadKeyComparator).makeOrGet();

      _metadata = _db.createTreeMap("Metadata").counterEnable().makeOrGet();

      _sector = MapDBUtils.readSector(_metadata, "sector");
      _rootKey = new QuadKey(new byte[] {}, _sector);
      _maxFeaturesPerNode = MapDBUtils.readInt(_metadata, "maxFeaturesPerNode");
   }


   private static DB open(final File directory,
                          final String lodName,
                          final boolean readOnly) throws IOException {
      if (directory.exists()) {
         if (!directory.isDirectory()) {
            throw new IOException(directory + " is not a directory");
         }
      }
      else {
         if (!readOnly) {
            if (!directory.mkdirs()) {
               throw new IOException("Can't create directory: " + directory);
            }
         }
      }


      final File file = new File(directory, lodName);
      final DBMaker maker = DBMaker.newFileDB(file);
      maker.compressionEnable();

      if (readOnly) {
         maker.readOnly();
         maker.strictDBGet();
      }

      final DB db;
      try {
         db = maker.make();
      }
      catch (final IOError | UnsupportedOperationException e) {
         throw new IOException(e.getMessage());
      }
      return db;
   }


   @Override
   synchronized public void close() {
      _db.close();
   }


   @Override
   public String getName() {
      return _name;
   }


   @Override
   synchronized public void addLeafNode(final String id,
                                        final Sector nodeSector,
                                        final Sector minimumSector,
                                        final List<PointFeature> features) {
      if (_readOnly) {
         throw new RuntimeException("Read Only");
      }

      addLeafNode(QuadKeyUtils.toBinaryID(id), nodeSector, minimumSector, features);
      _db.commit();
   }


   private void addLeafNode(final byte[] id,
                            final Sector nodeSector,
                            final Sector minimumSector,
                            final List<PointFeature> features) {
      if (features.size() > _maxFeaturesPerNode) {
         if (split(id, nodeSector, features)) {
            return;
         }
      }

      saveLeafNode(id, nodeSector, minimumSector, features);
   }


   private static class ChildSplitResult {
      private final QuadKey          _key;
      private final PointFeaturesSet _featuresSet;


      private ChildSplitResult(final QuadKey key,
                               final PointFeaturesSet featuresSet) {
         super();
         _key = key;
         _featuresSet = featuresSet;
      }

   }

   private static final int MAX_SPLIT_DEPTH = 8;


   private static List<ChildSplitResult> splitIntoChildren(final byte[] id,
                                                           final Sector nodeSector,
                                                           final List<PointFeature> features) {
      final QuadKey key = new QuadKey(id, nodeSector);
      return splitIntoChildren(key, features, 0);
   }


   private static List<ChildSplitResult> splitIntoChildren(final QuadKey key,
                                                           final List<PointFeature> features,
                                                           final int splitDepth) {
      final int featuresSize = features.size(); // save the size here, to be compare after the features get cleared in PointFeaturesSet.extractFeatures()

      final QuadKey[] childrenKeys = key.createChildren();
      final List<ChildSplitResult> result = new ArrayList<>(childrenKeys.length);
      for (final QuadKey childKey : childrenKeys) {
         final PointFeaturesSet childPointFeaturesSet = PointFeaturesSet.extractFeatures(childKey._sector, features);
         if (childPointFeaturesSet != null) {
            final List<PointFeature> childFeatures = childPointFeaturesSet._features;
            if ((childFeatures.size() == featuresSize) && (splitDepth < MAX_SPLIT_DEPTH)) {
               return splitIntoChildren(childKey, childFeatures, splitDepth + 1);
            }
            result.add(new ChildSplitResult(childKey, childPointFeaturesSet));
         }
      }
      if (!features.isEmpty()) {
         throw new RuntimeException("Logic error!");
      }
      if (result.size() == 0) {
         throw new RuntimeException("Logic error!");
      }
      return result;
   }


   private boolean split(final byte[] id,
                         final Sector nodeSector,
                         final List<PointFeature> sourceFeatures) {
      final List<PointFeature> features = new ArrayList<>(sourceFeatures);

      final List<ChildSplitResult> splits = splitIntoChildren(id, nodeSector, features);
      if (splits.size() == 1) {
         System.out.println("- can't split \"" + QuadKeyUtils.toIDString(id) + "\"");
         return false;
      }

      for (final ChildSplitResult split : splits) {
         final QuadKey childKey = split._key;
         final PointFeaturesSet childFeaturesSet = split._featuresSet;
         addLeafNode( //
                  childKey._id, //
                  childKey._sector, //
                  childFeaturesSet._minimumSector, //
                  childFeaturesSet._features);
      }

      return true;
   }


   private void saveLeafNode(final byte[] id,
                             final Sector nodeSector,
                             final Sector minimumSector,
                             final List<PointFeature> features) {

      saveNode(id, nodeSector, minimumSector, features);

      for (final byte[] ancestorID : QuadKeyUtils.ancestors(id)) {
         if (!_pendingNodes.contains(ancestorID)) {
            _pendingNodes.add(ancestorID);
            saveEmptyAncestorNode(ancestorID);
         }
      }
   }


   private void saveNode(final byte[] id,
                         final Sector nodeSector,
                         final Sector minimumSector,
                         final List<PointFeature> features) {
      validateFeatures(nodeSector, minimumSector, features);
      assertIsNull(_nodesHeaders.put(id, new NodeHeader(nodeSector, minimumSector, features.size())));
      assertIsNull(_nodesFeatures.put(id, features));
   }


   private void saveEmptyAncestorNode(final byte[] id) {
      final Sector nodeSector = QuadKey.sectorFor(_rootKey, id);
      assertIsNull(_nodesHeaders.put(id, new NodeHeader(nodeSector, null, 0)));
      assertIsNull(_nodesFeatures.put(id, Collections.emptyList()));
   }


   private static void validateFeatures(final Sector nodeSector,
                                        final Sector minimumSector,
                                        final List<PointFeature> features) {
      if (minimumSector != null) {
         if (!nodeSector.fullContains(minimumSector)) {
            throw new RuntimeException("LOGIC ERROR");
         }
      }
      for (final PointFeature feature : features) {
         final Geodetic2D position = feature._position;
         if (!nodeSector.contains(position)) {
            throw new RuntimeException("LOGIC ERROR");
         }
         if (minimumSector != null) {
            if (!minimumSector.contains(position)) {
               throw new RuntimeException("LOGIC ERROR");
            }
         }
      }
   }


   private static void assertIsNull(final Object obj) {
      if (obj != null) {
         throw new RuntimeException("LOGIC ERROR");
      }
   }


   @Override
   synchronized public void optimize() throws IOException {
      if (_readOnly) {
         throw new RuntimeException("Read Only");
      }

      try {
         _db.compact();
      }
      catch (final IOError | UnsupportedOperationException e) {
         throw new IOException(e.getMessage());
      }
   }


   @Override
   public void processPendingNodes(final Comparator<PointFeature> featuresComparator,
                                   final boolean verbose) {
      if (_readOnly) {
         throw new RuntimeException("Read Only");
      }

      int currentLevel = getPendingNodesMaxLevel(verbose);

      final Progress progress = new Progress(_pendingNodes.size()) {
         @Override
         public void informProgress(final long stepsDone,
                                    final double percent,
                                    final long elapsed,
                                    final long estimatedMsToFinish) {
            if (verbose) {
               System.out.println(getName() + ": 3/4 Processing Pending Nodes: "
                                  + progressString(stepsDone, percent, elapsed, estimatedMsToFinish));
            }
         }
      };

      while (currentLevel >= 0) {
         final Iterator<byte[]> it = _pendingNodes.iterator();
         while (it.hasNext()) {
            final byte[] key = it.next();
            if (key.length == currentLevel) {
               processPendingNode(key, featuresComparator);
               it.remove();
               _db.commit();

               progress.stepDone();
            }
         }
         currentLevel--;
      }

      if (!_pendingNodes.isEmpty()) {
         throw new RuntimeException("Logic Error: PendingNodes=" + _pendingNodes.size());
      }

      progress.finish();
   }


   private int getPendingNodesMaxLevel(final boolean verbose) {
      final Progress progress = new Progress(_pendingNodes.size()) {
         @Override
         public void informProgress(final long stepsDone,
                                    final double percent,
                                    final long elapsed,
                                    final long estimatedMsToFinish) {
            if (verbose) {
               System.out.println(getName() + ": 2/4 Processing Pending Nodes: "
                                  + progressString(stepsDone, percent, elapsed, estimatedMsToFinish));
            }
         }
      };

      int maxLevel = Integer.MIN_VALUE;
      for (final byte[] key : _pendingNodes) {
         maxLevel = Math.max(maxLevel, key.length);
         progress.stepDone();
      }

      progress.finish();

      return maxLevel;
   }


   private static class Child {
      private final byte[]     _id;
      private final NodeHeader _header;


      private Child(final byte[] id,
                    final NodeHeader header) {
         _id = id;
         _header = header;
      }

   }


   private void addChild(final byte[] key,
                         final byte childIndex,
                         final List<Child> children) {
      final byte[] childID = QuadKeyUtils.append(key, childIndex);
      final NodeHeader childHeader = _nodesHeaders.get(childID);
      if (childHeader != null) {
         children.add(new Child(childID, childHeader));
      }
   }


   private List<Child> getChildren(final byte[] key) {
      final List<Child> result = new ArrayList<>(4);
      addChild(key, (byte) 0, result);
      addChild(key, (byte) 1, result);
      addChild(key, (byte) 2, result);
      addChild(key, (byte) 3, result);
      return result;
   }


   private void processPendingNode(final byte[] key,
                                   final Comparator<PointFeature> featuresComparator) {


      final List<Child> children = getChildren(key);
      if (children.isEmpty()) {
         throw new RuntimeException("LOGIC ERROR");
      }

      Sector topMinimumSector = null;
      final List<PointFeature> allFeatures = new ArrayList<>();
      for (final Child child : children) {
         topMinimumSector = child._header._minimumSector.mergedWith(topMinimumSector);
         final List<PointFeature> childFeatures = _nodesFeatures.get(child._id);
         if ((childFeatures == null) || childFeatures.isEmpty()) {
            throw new RuntimeException("LOGIC ERROR");
         }
         allFeatures.addAll(childFeatures);
      }

      Collections.sort(allFeatures, featuresComparator);


      //final float topFeaturesPercent = (float) 1 / Math.max(children.size(), 2);
      final float topFeaturesPercent = 0.25f;
      final int topFeaturesCount = Math.max(1, Math.round(allFeatures.size() * topFeaturesPercent));

      final List<PointFeature> topFeatures = allFeatures.subList(0, topFeaturesCount);
      saveNode(key, topFeatures, topMinimumSector);

      final List<PointFeature> restFeatures = new ArrayList<>(allFeatures.subList(topFeaturesCount, allFeatures.size()));
      for (final Child child : children) {
         extractAndSaveChildFeatures(child, restFeatures);
      }
      if (!restFeatures.isEmpty()) {
         throw new RuntimeException("LOGIC ERROR");
      }
   }


   private void extractAndSaveChildFeatures(final Child child,
                                            final List<PointFeature> restFeatures) {
      final byte[] childID = child._id;
      final Sector childSector = child._header._nodeSector;

      final List<PointFeature> childFeatures = new ArrayList<>();
      final Iterator<PointFeature> it = restFeatures.iterator();
      while (it.hasNext()) {
         final PointFeature feature = it.next();
         if (childSector.contains(feature._position)) {
            childFeatures.add(feature);
            it.remove();
         }
      }

      if (childFeatures.isEmpty()) {
         removeNode(childID);
      }
      else {
         if (childFeatures.size() == 1) {
            // consider moving this single feature up to my parent
         }
         // final  Sector minimumSector = child._header._minimumSector;
         final Sector minimumSector = getMinimumSector(childFeatures);
         saveNode(childID, childFeatures, minimumSector);
      }
   }


   private static Sector getMinimumSector(final List<PointFeature> features) {
      double minLatRad = Double.POSITIVE_INFINITY;
      double minLonRad = Double.POSITIVE_INFINITY;
      double maxLatRad = Double.NEGATIVE_INFINITY;
      double maxLonRad = Double.NEGATIVE_INFINITY;

      for (final PointFeature feature : features) {
         final Geodetic2D point = feature._position;

         final double latRad = point._latitude._radians;
         final double lonRad = point._longitude._radians;

         if (latRad < minLatRad) {
            minLatRad = latRad;
         }
         if (latRad > maxLatRad) {
            maxLatRad = latRad;
         }

         if (lonRad < minLonRad) {
            minLonRad = lonRad;
         }
         if (lonRad > maxLonRad) {
            maxLonRad = lonRad;
         }
      }

      return Sector.fromRadians(minLatRad, minLonRad, maxLatRad, maxLonRad);
   }


   private void removeNode(final byte[] id) {
      _nodesHeaders.remove(id);
      _nodesFeatures.remove(id);
   }


   private void saveNode(final byte[] id,
                         final List<PointFeature> features,
                         final Sector minimumSector) {
      final Sector nodeSector = QuadKey.sectorFor(_rootKey, id);

      validateFeatures(nodeSector, minimumSector, features);


      _nodesHeaders.put(id, new NodeHeader(nodeSector, minimumSector, features.size()));
      _nodesFeatures.put(id, features);
   }


   private static class PvtStatistics
      implements
         PointFeatureSortingLODStorage.Statistics {

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
         PointFeatureSortingLODStorage.NodeVisitor {

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
      public boolean visit(final PointFeatureSortingLODStorage.Node node) {
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
   synchronized public PointFeatureSortingLODStorage.Statistics getStatistics(final boolean showProgress) {
      final StatisticsGatherer gatherer = new StatisticsGatherer(_name, showProgress);
      acceptDepthFirstVisitor(gatherer);
      return gatherer._statistics;
   }

   private static class PvtNode
      implements
         PointFeatureSortingLODStorage.Node {

      private final PointFeatureSortingLODMapDBStorage _storage;
      private final byte[]                             _id;
      private final Sector                             _nodeSector;
      private final Sector                             _minimumSector;
      private final int                                _featuresCount;
      private List<PointFeature>                       _features = null;


      private PvtNode(final PointFeatureSortingLODMapDBStorage storage,
                      final byte[] id,
                      final NodeHeader header) {
         _storage = storage;
         _id = id;
         _nodeSector = header._nodeSector;
         _minimumSector = header._minimumSector;
         _featuresCount = header._featuresCount;
      }


      @Override
      public String getID() {
         return QuadKeyUtils.toIDString(_id);
      }


      @Override
      public Sector getNodeSector() {
         return _nodeSector;
      }


      @Override
      public Sector getMinimumSector() {
         return _minimumSector;
      }


      @Override
      public int getFeaturesCount() {
         return _featuresCount;
      }


      @Override
      public int getDepth() {
         return _id.length;
      }


      @Override
      public List<PointFeature> getFeatures() {
         if (_features == null) {
            _features = Collections.unmodifiableList(_storage._nodesFeatures.get(_id));
         }
         return _features;
      }


      @Override
      public List<String> getChildrenIDs() {
         return _storage.getChildrenIDs(_id);
      }


   }


   @Override
   synchronized public void acceptDepthFirstVisitor(final PointFeatureSortingLODStorage.NodeVisitor visitor) {
      visitor.start();

      for (final Map.Entry<byte[], NodeHeader> nodeHeader : _nodesHeaders.entrySet()) {
         final boolean keepGoing = visitor.visit(new PvtNode(this, nodeHeader.getKey(), nodeHeader.getValue()));
         if (!keepGoing) {
            break;
         }
      }

      visitor.stop();
   }


   @Override
   public Sector getSector() {
      return _sector;
   }


   @Override
   public List<PointFeatureSortingLODStorage.Node> getNodesFor(final Sector searchSector) {
      final QuadKey candidate = QuadKey.deepestEnclosingNodeKey(_rootKey, searchSector);
      return getNodesUpTo(candidate);
   }


   @Override
   public List<PointFeatureSortingLODStorage.Node> getNodesFor(final Geodetic2D position) {
      final int maxLevel = getStatistics(false).getMaxNodeDepth();
      final QuadKey candidate = QuadKey.deepestEnclosingNodeKey(_rootKey, position, maxLevel);
      return getNodesUpTo(candidate);
   }


   private List<PointFeatureSortingLODStorage.Node> getNodesUpTo(final QuadKey candidate) {
      final byte[] key = candidate._id;

      final int keyLength = key.length;
      final List<PointFeatureSortingLODStorage.Node> result = new ArrayList<>(keyLength);
      for (int i = 0; i < (keyLength + 1); i++) {
         final byte[] k = Arrays.copyOf(key, i);
         result.add(getNode(k));
      }
      return Collections.unmodifiableList(CollectionUtils.pruneTrailingNulls(result));
   }


   private PointFeatureSortingLODStorage.Node getNode(final byte[] id) {
      final NodeHeader header = _nodesHeaders.get(id);
      return (header == null) ? null : new PvtNode(this, id, header);
   }


   @Override
   public List<PointFeatureSortingLODStorage.Node> getAllNodesOfDepth(final int depth) {
      final List<Node> result = new ArrayList<>();
      for (final byte[] key : _nodesHeaders.keySet()) {
         if (key.length == depth) {
            result.add(getNode(key));
         }
      }
      return Collections.unmodifiableList(result);
   }


   private List<String> getChildrenIDs(final byte[] id) {
      final List<String> result = new ArrayList<>(4);

      append(result, id, (byte) 0);
      append(result, id, (byte) 1);
      append(result, id, (byte) 2);
      append(result, id, (byte) 3);

      return Collections.unmodifiableList(result);
   }


   private void append(final List<String> result,
                       final byte[] id,
                       final byte childIndex) {
      final byte[] childID = QuadKeyUtils.append(id, childIndex);
      if (_nodesHeaders.containsKey(childID)) {
         result.add(QuadKeyUtils.toIDString(childID));
      }
   }


   @Override
   public Node getNode(final String id) {
      return getNode(QuadKeyUtils.toBinaryID(id));
   }


}
