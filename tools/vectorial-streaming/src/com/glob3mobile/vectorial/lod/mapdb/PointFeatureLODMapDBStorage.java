

package com.glob3mobile.vectorial.lod.mapdb;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOError;
import java.io.IOException;
import java.util.ArrayList;
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
import com.glob3mobile.vectorial.lod.PointFeatureLODStorage;
import com.glob3mobile.vectorial.storage.PointFeature;
import com.glob3mobile.vectorial.storage.PointFeatureCluster;
import com.glob3mobile.vectorial.storage.PointFeaturesSet;
import com.glob3mobile.vectorial.storage.QuadKey;
import com.glob3mobile.vectorial.storage.QuadKeyComparator;
import com.glob3mobile.vectorial.storage.QuadKeyUtils;
import com.glob3mobile.vectorial.utils.MapDBUtils;


public class PointFeatureLODMapDBStorage
   implements
      PointFeatureLODStorage {


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


   public static PointFeatureLODStorage createEmpty(final Sector sector,
                                                    final File directory,
                                                    final String name,
                                                    final int maxFeaturesPerNode,
                                                    final Comparator<PointFeature> featuresComparator,
                                                    final boolean createClusters) throws IOException {
      PointFeatureLODMapDBStorage.delete(directory, name);

      return new PointFeatureLODMapDBStorage(sector, directory, name, maxFeaturesPerNode, featuresComparator, createClusters);
   }


   public static PointFeatureLODStorage openReadOnly(final File directory,
                                                     final String name) throws IOException {
      return new PointFeatureLODMapDBStorage(directory, name);
   }


   private final Sector                          _sector;
   private final QuadKey                         _rootKey;
   private final String                          _name;
   private final boolean                         _readOnly;
   private final int                             _maxFeaturesPerNode;
   private final Comparator<PointFeature>        _featuresComparator;
   private final boolean                         _createClusters;

   private final DB                              _db;
   private final BTreeMap<byte[], LODNodeHeader> _nodesHeaders;
   private final BTreeMap<byte[], LODNodeData>   _nodesFeatures;
   private final NavigableSet<byte[]>            _pendingNodes;
   private final BTreeMap<String, Object>        _metadata;


   private PointFeatureLODMapDBStorage(final Sector sector,
                                       final File directory,
                                       final String name,
                                       final int maxFeaturesPerNode,
                                       final Comparator<PointFeature> featuresComparator,
                                       final boolean createClusters) throws IOException {
      // Constructor for new Cluster-Storage
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
      .valueSerializer(new LODNodeHeaderSerializer());

      _nodesHeaders = nodesHeadersMaker.makeOrGet();


      final BTreeMapMaker nodesFeaturesMaker = _db //
      .createTreeMap("NodesFeatures") //
      .counterEnable() //
      .comparator(quadKeyComparator) //
      .valueSerializer(new LODNodeDataSerializer());

      _nodesFeatures = nodesFeaturesMaker.makeOrGet();

      _pendingNodes = _db //
      .createTreeSet("PendingNodes") //
      .counterEnable() //
      .comparator(quadKeyComparator).makeOrGet();

      _metadata = _db.createTreeMap("Metadata").counterEnable().makeOrGet();

      _maxFeaturesPerNode = maxFeaturesPerNode;
      _featuresComparator = featuresComparator;
      _createClusters = createClusters;

      saveMetadata();
   }


   private void saveMetadata() {
      MapDBUtils.saveSector(_metadata, "sector", _sector);
      MapDBUtils.saveInt(_metadata, "maxFeaturesPerNode", _maxFeaturesPerNode);
      _db.commit();
   }


   private PointFeatureLODMapDBStorage(final File directory,
                                       final String name) throws IOException {
      // Constructor for a alread existing Cluster-Storage, read only

      _name = name;

      _readOnly = true;

      _db = open(directory, name, _readOnly);

      final QuadKeyComparator quadKeyComparator = new QuadKeyComparator();

      final BTreeMapMaker nodesHeadersMaker = _db //
      .createTreeMap("NodesHeaders") //
      .counterEnable() //
      .comparator(quadKeyComparator) //
      .valueSerializer(new LODNodeHeaderSerializer());

      _nodesHeaders = nodesHeadersMaker.makeOrGet();


      final BTreeMapMaker nodesFeaturesMaker = _db //
      .createTreeMap("NodesFeatures") //
      .counterEnable() //
      .comparator(quadKeyComparator) //
      .valueSerializer(new LODNodeDataSerializer());

      _nodesFeatures = nodesFeaturesMaker.makeOrGet();

      _pendingNodes = _db //
      .createTreeSet("PendingNodes") //
      .counterEnable() //
      .comparator(quadKeyComparator).makeOrGet();

      _metadata = _db.createTreeMap("Metadata").counterEnable().makeOrGet();

      _sector = MapDBUtils.readSector(_metadata, "sector");
      _rootKey = new QuadKey(new byte[] {}, _sector);
      _maxFeaturesPerNode = MapDBUtils.readInt(_metadata, "maxFeaturesPerNode");

      _featuresComparator = null; // ignored in readOnly mode
      _createClusters = false; // ignored in readOnly mode
   }


   private static DB open(final File directory,
                          final String clusterName,
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


      final File file = new File(directory, clusterName);
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
   public String getName() {
      return _name;
   }


   @Override
   synchronized public void close() {
      _db.close();
   }


   @Override
   public Sector getSector() {
      return _sector;
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
   synchronized public void addLeafNode(final String id,
                                        final Sector nodeSector,
                                        final Sector minimumSector,
                                        final List<PointFeature> features) {
      if (_readOnly) {
         throw new RuntimeException("Read Only");
      }

      final List<PointFeature> sortedFeatures;
      if (_featuresComparator != null) {
         sortedFeatures = new ArrayList<>(features);
         Collections.sort(sortedFeatures, _featuresComparator);
      }
      else {
         sortedFeatures = features;
      }

      addLeafNode(QuadKeyUtils.toBinaryID(id), nodeSector, minimumSector, sortedFeatures);
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

      createLeafNode(id, nodeSector, minimumSector, features);

      for (final byte[] ancestorID : QuadKeyUtils.ancestors(id)) {
         if (!_pendingNodes.contains(ancestorID)) {
            _pendingNodes.add(ancestorID);
            createEmptyInnerNode(ancestorID);
         }
      }
   }


   private void createLeafNode(final byte[] id,
                               final Sector nodeSector,
                               final Sector minimumSector,
                               final List<PointFeature> features) {
      validateFeatures(nodeSector, minimumSector, features);
      assertIsNull(_nodesHeaders.put(id, new LODNodeHeader(nodeSector, minimumSector, 0, features.size())));
      assertIsNull(_nodesFeatures.put(id, new LODNodeData(Collections.emptyList(), features)));
   }


   private void createEmptyInnerNode(final byte[] id) {
      assertIsNull(_nodesHeaders.put(id, new LODNodeHeader(QuadKey.sectorFor(_rootKey, id), null, 0, 0)));
      assertIsNull(_nodesFeatures.put(id, new LODNodeData(Collections.emptyList(), Collections.emptyList())));
   }


   private void saveInnerNode(final byte[] id,
                              final Sector nodeSector,
                              final Sector minimumSector,
                              final List<PointFeatureCluster> clusters,
                              final List<PointFeature> features) {
      validateClusters(nodeSector, minimumSector, clusters);
      validateFeatures(nodeSector, minimumSector, features);
      _nodesHeaders.put(id, new LODNodeHeader(nodeSector, minimumSector, clusters.size(), features.size()));
      _nodesFeatures.put(id, new LODNodeData(clusters, features));
   }


   private static void validateClusters(final Sector nodeSector,
                                        final Sector minimumSector,
                                        final List<PointFeatureCluster> clusters) {
      if (minimumSector != null) {
         if (!nodeSector.fullContains(minimumSector)) {
            throw new RuntimeException("LOGIC ERROR");
         }
      }
      for (final PointFeatureCluster cluster : clusters) {
         final Geodetic2D position = cluster._position;
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


   private static class PvtStatistics
      implements
         PointFeatureLODStorage.Statistics {

      private final String _storageName;
      private final long   _clustersCount;
      private final long   _featuresCount;
      private final int    _nodesCount;
      private final int    _minFeaturesPerNode;
      private final int    _maxFeaturesPerNode;
      private final double _averageFeaturesPerNode;
      private final int    _minClustersPerNode;
      private final int    _maxClustersPerNode;
      private final double _averageClustersPerNode;
      private final int    _maxNodeDepth;
      private final int    _minNodeDepth;
      private final double _averageNodeDepth;


      private PvtStatistics(final String storageName,
                            final long clustersCount,
                            final long featuresCount,
                            final int nodesCount,
                            final int minFeaturesPerNode,
                            final int maxFeaturesPerNode,
                            final double averageFeaturesPerNode,
                            final int minClustersPerNode,
                            final int maxClustersPerNode,
                            final double averageClustersPerNode,
                            final int maxNodeDepth,
                            final int minNodeDepth,
                            final double averageNodeDepth) {
         _storageName = storageName;
         _clustersCount = clustersCount;
         _featuresCount = featuresCount;
         _nodesCount = nodesCount;
         _minFeaturesPerNode = minFeaturesPerNode;
         _maxFeaturesPerNode = maxFeaturesPerNode;
         _averageFeaturesPerNode = averageFeaturesPerNode;
         _minClustersPerNode = minClustersPerNode;
         _maxClustersPerNode = maxClustersPerNode;
         _averageClustersPerNode = averageClustersPerNode;
         _maxNodeDepth = maxNodeDepth;
         _minNodeDepth = minNodeDepth;
         _averageNodeDepth = averageNodeDepth;
      }


      @Override
      public long getClustersCount() {
         return _clustersCount;
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
      public int getMinClustersPerNode() {
         return _minClustersPerNode;
      }


      @Override
      public int getMaxClustersPerNode() {
         return _maxClustersPerNode;
      }


      @Override
      public double getAverageClustersPerNode() {
         return _averageClustersPerNode;
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


      @Override
      public void show() {
         System.out.println("--------------------------------------------------------------");
         System.out.println(" Storage: " + _storageName);
         System.out.println("  Features: " + _featuresCount);
         System.out.println("  Nodes Count: " + _nodesCount);
         System.out.println("  Clusters/Node: " + //
                            "min=" + _minClustersPerNode + //
                            ", max=" + _maxClustersPerNode + //
                            ", avg=" + (float) _averageClustersPerNode);
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


   }


   private static class StatisticsGatherer
      implements
         PointFeatureLODStorage.NodeVisitor {

      private final String          _name;
      private final boolean         _showProgress;

      private UndeterminateProgress _progress   = null;
      private Statistics            _statistics = null;


      private long                  _featuresCount;
      private long                  _clustersCount;
      private int                   _nodesCount;
      private int                   _minFeaturesPerNode;
      private int                   _maxFeaturesPerNode;
      private int                   _minClustersPerNode;
      private int                   _maxClustersPerNode;
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
         _clustersCount = 0;

         _nodesCount = 0;

         _minFeaturesPerNode = Integer.MAX_VALUE;
         _maxFeaturesPerNode = Integer.MIN_VALUE;

         _minClustersPerNode = Integer.MAX_VALUE;
         _maxClustersPerNode = Integer.MIN_VALUE;

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
      public boolean visit(final PointFeatureLODStorage.Node node) {
         _nodesCount++;

         final int clustersCount = node.getClustersCount();

         _clustersCount += clustersCount;
         _minClustersPerNode = Math.min(_minClustersPerNode, clustersCount);
         _maxClustersPerNode = Math.max(_maxClustersPerNode, clustersCount);

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
                  _clustersCount, //
                  _featuresCount, //
                  _nodesCount, //
                  _minFeaturesPerNode, //
                  _maxFeaturesPerNode, //
                  (double) _featuresCount / _nodesCount, //
                  _minClustersPerNode, //
                  _maxClustersPerNode, //
                  (double) _clustersCount / _nodesCount, //
                  _maxNodeDepth, //
                  _minNodeDepth, //
                  (double) _sumDepth / _nodesCount //
         );

         if (_progress != null) {
            _progress.finish();
            _progress = null;
         }
      }
   }


   @Override
   synchronized public PointFeatureLODStorage.Statistics getStatistics(final boolean showProgress) {
      final StatisticsGatherer gatherer = new StatisticsGatherer(_name, showProgress);
      acceptDepthFirstVisitor(gatherer);
      return gatherer._statistics;
   }


   private static class PvtNode
      implements
         PointFeatureLODStorage.Node {
      protected final PointFeatureLODMapDBStorage _storage;
      protected final byte[]                      _id;
      private final Sector                        _nodeSector;
      private final Sector                        _minimumSector;
      private final int                           _clustersCount;
      private List<PointFeatureCluster>           _clusters;
      private final int                           _featuresCount;
      private List<PointFeature>                  _features;


      protected PvtNode(final PointFeatureLODMapDBStorage storage,
                        final byte[] id,
                        final LODNodeHeader header) {
         _storage = storage;
         _id = id;
         _nodeSector = header.getNodeSector();
         _minimumSector = header.getMinimumSector();
         _clustersCount = header.getClustersCount();
         _featuresCount = header.getFeaturesCount();
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
      public int getDepth() {
         return _id.length;
      }


      @Override
      public int getClustersCount() {
         return _clustersCount;
      }


      @Override
      public List<PointFeatureCluster> getClusters() {
         if (_clusters == null) {
            final LODNodeData nodeData = _storage._nodesFeatures.get(_id);
            _clusters = Collections.unmodifiableList(nodeData.getClusters());
         }
         return _clusters;
      }


      @Override
      public List<String> getChildrenIDs() {
         return _storage.getChildrenIDs(_id);
      }


      @Override
      public int getFeaturesCount() {
         return _featuresCount;
      }


      @Override
      public List<PointFeature> getFeatures() {
         if (_features == null) {
            final LODNodeData nodeData = _storage._nodesFeatures.get(_id);
            _features = Collections.unmodifiableList(nodeData.getFeatures());
         }
         return _features;
      }

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
   synchronized public void acceptDepthFirstVisitor(final PointFeatureLODStorage.NodeVisitor visitor) {
      visitor.start();

      for (final Map.Entry<byte[], LODNodeHeader> entry : _nodesHeaders.entrySet()) {
         final LODNodeHeader header = entry.getValue();
         final PvtNode node = new PvtNode(this, entry.getKey(), header);
         final boolean keepGoing = visitor.visit(node);
         if (!keepGoing) {
            break;
         }
      }

      visitor.stop();
   }


   @Override
   public void createLOD(final boolean verbose) {
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
               System.out.println(getName() + ": 3/4 Creating LOD Levels: "
                                  + progressString(stepsDone, percent, elapsed, estimatedMsToFinish));
            }
         }
      };

      while (currentLevel >= 0) {
         final Iterator<byte[]> it = _pendingNodes.iterator();
         while (it.hasNext()) {
            final byte[] key = it.next();
            if (key.length == currentLevel) {
               processPendingNode(key);
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
               System.out.println(getName() + ": 2/4 Calculating Quadtree depth: "
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
      private final byte[]        _id;
      private final LODNodeHeader _header;
      private final LODNodeData   _data;


      private Child(final byte[] id,
                    final LODNodeHeader header,
                    final LODNodeData data) {
         _id = id;
         _header = header;
         _data = data;
      }

   }


   private void addChild(final byte[] key,
                         final byte childIndex,
                         final List<Child> children) {
      final byte[] childID = QuadKeyUtils.append(key, childIndex);
      final LODNodeHeader childHeader = _nodesHeaders.get(childID);
      if (childHeader != null) {
         final LODNodeData childData = _nodesFeatures.get(childID);
         children.add(new Child(childID, childHeader, childData));
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


   //   private static Geodetic2D averagePosition(final List<PointFeatureCluster> clusters,
   //                                             final List<PointFeature> features) {
   //      double sumLat = 0;
   //      double sumLon = 0;
   //      long sumSize = 0;
   //      for (final PointFeatureCluster cluster : clusters) {
   //         final Geodetic2D position = cluster._position;
   //         final long clusterSize = cluster._size;
   //         sumLat += position._latitude._radians * clusterSize;
   //         sumLon += position._longitude._radians * clusterSize;
   //         sumSize += clusterSize;
   //      }
   //      for (final PointFeature feature : features) {
   //         final Geodetic2D position = feature._position;
   //         sumLat += position._latitude._radians;
   //         sumLon += position._longitude._radians;
   //         sumSize++;
   //      }
   //      return Geodetic2D.fromRadians(sumLat / sumSize, sumLon / sumSize);
   //   }


   private void removeNode(final byte[] id) {
      _nodesHeaders.remove(id);
      _nodesFeatures.remove(id);
   }


   private void saveNode(final byte[] id,
                         final Sector nodeSector,
                         final Sector minimumSector,
                         final List<PointFeatureCluster> clusters,
                         final List<PointFeature> features) {
      validateClusters(nodeSector, minimumSector, clusters);
      validateFeatures(nodeSector, minimumSector, features);
      _nodesHeaders.put(id, new LODNodeHeader(nodeSector, minimumSector, clusters.size(), features.size()));
      _nodesFeatures.put(id, new LODNodeData(clusters, features));
   }


   private void extractAndSaveChildFeatures(final Child child,
                                            final List<PointFeature> restFeatures) {
      final byte[] childID = child._id;
      final Sector childSector = child._header.getNodeSector();

      final List<PointFeature> childFeatures = new ArrayList<>();
      final Iterator<PointFeature> it = restFeatures.iterator();
      while (it.hasNext()) {
         final PointFeature feature = it.next();
         if (childSector.contains(feature._position)) {
            childFeatures.add(feature);
            it.remove();
         }
      }

      final List<PointFeatureCluster> childClusters = child._data.getClusters();

      if (childFeatures.isEmpty() && childClusters.isEmpty()) {
         removeNode(childID);
      }
      else {
         saveNode(childID, childSector, child._header.getMinimumSector(), childClusters, childFeatures);
      }
   }


   private void processPendingNode(final byte[] key) {
      List<Child> children = getChildren(key);
      if (children.isEmpty()) {
         throw new RuntimeException("LOGIC ERROR");
      }

      final List<PointFeature> features = (_featuresComparator == null) //
                                                                       ? Collections.emptyList() //
                                                                       : borrowFeaturesFromChildren(children);
      Sector minimumSector = calculateMinimumSector(features);

      children = getChildren(key); // ask again for the children, they can be removed in borrowFeaturesFromChildren();

      final List<PointFeatureCluster> clusters;
      if (_createClusters) {
         clusters = new ArrayList<>(children.size());
         for (final Child child : children) {
            minimumSector = child._header.getMinimumSector().mergedWith(minimumSector);
            final LODNodeData childNodeData = _nodesFeatures.get(child._id);
            if (childNodeData == null) {
               throw new RuntimeException("LOGIC ERROR");
            }
            clusters.add(childNodeData.createCluster());
         }

         if (clusters.isEmpty()) {
            throw new RuntimeException("LOGIC ERROR");
         }
      }
      else {
         clusters = Collections.emptyList();
      }


      final Sector nodeSector = QuadKey.sectorFor(_rootKey, key);
      saveInnerNode(key, nodeSector, minimumSector, clusters, features);
   }


   private static Sector calculateMinimumSector(final List<PointFeature> features) {
      if (features.isEmpty()) {
         return null;
      }

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


   private List<PointFeature> borrowFeaturesFromChildren(final List<Child> children) {
      final List<PointFeature> allFeatures = new ArrayList<>();
      for (final Child child : children) {
         final LODNodeData childNodeData = _nodesFeatures.get(child._id);
         allFeatures.addAll(childNodeData.getFeatures());
      }

      Collections.sort(allFeatures, _featuresComparator);

      //final float topFeaturesPercent = (float) 1 / Math.max(children.size(), 2);
      final float topFeaturesPercent = 0.25f;
      final int topFeaturesCount = Math.max(1, Math.round(allFeatures.size() * topFeaturesPercent));

      final List<PointFeature> topFeatures = allFeatures.subList(0, topFeaturesCount);

      final List<PointFeature> restFeatures = new ArrayList<>(allFeatures.subList(topFeaturesCount, allFeatures.size()));
      for (final Child child : children) {
         extractAndSaveChildFeatures(child, restFeatures);
      }
      if (!restFeatures.isEmpty()) {
         throw new RuntimeException("LOGIC ERROR");
      }

      return topFeatures;
   }


   @Override
   public List<PointFeatureLODStorage.Node> getAllNodesOfDepth(final int depth) {
      final List<Node> result = new ArrayList<>();
      for (final byte[] key : _nodesHeaders.keySet()) {
         if (key.length == depth) {
            result.add(getNode(key));
         }
      }
      return Collections.unmodifiableList(result);
   }


   @Override
   public PointFeatureLODStorage.Node getNode(final String id) {
      return getNode(QuadKeyUtils.toBinaryID(id));
   }


   private PointFeatureLODStorage.Node getNode(final byte[] id) {
      final LODNodeHeader header = _nodesHeaders.get(id);
      return (header == null) ? null : new PvtNode(this, id, header);
   }


}
