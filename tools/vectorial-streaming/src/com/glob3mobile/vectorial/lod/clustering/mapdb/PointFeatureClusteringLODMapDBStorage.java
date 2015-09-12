

package com.glob3mobile.vectorial.lod.clustering.mapdb;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOError;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
import com.glob3mobile.vectorial.lod.clustering.PointFeatureClusteringLODStorage;
import com.glob3mobile.vectorial.lod.clustering.nodes.CInnerNodeData;
import com.glob3mobile.vectorial.lod.clustering.nodes.CInnerNodeHeader;
import com.glob3mobile.vectorial.lod.clustering.nodes.CLeafNodeData;
import com.glob3mobile.vectorial.lod.clustering.nodes.CLeafNodeHeader;
import com.glob3mobile.vectorial.lod.clustering.nodes.CNodeData;
import com.glob3mobile.vectorial.lod.clustering.nodes.CNodeHeader;
import com.glob3mobile.vectorial.storage.PointFeature;
import com.glob3mobile.vectorial.storage.PointFeatureCluster;
import com.glob3mobile.vectorial.storage.PointFeaturesSet;
import com.glob3mobile.vectorial.storage.QuadKey;
import com.glob3mobile.vectorial.storage.QuadKeyComparator;
import com.glob3mobile.vectorial.storage.QuadKeyUtils;
import com.glob3mobile.vectorial.utils.MapDBUtils;


public class PointFeatureClusteringLODMapDBStorage
   implements
      PointFeatureClusteringLODStorage {


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


   public static PointFeatureClusteringLODStorage createEmpty(final Sector sector,
                                                              final File directory,
                                                              final String name,
                                                              final int maxFeaturesPerNode) throws IOException {
      PointFeatureClusteringLODMapDBStorage.delete(directory, name);

      return new PointFeatureClusteringLODMapDBStorage(sector, directory, name, maxFeaturesPerNode);
   }


   public static PointFeatureClusteringLODStorage openReadOnly(final File directory,
                                                               final String name) throws IOException {
      return new PointFeatureClusteringLODMapDBStorage(directory, name);
   }


   private final Sector                        _sector;
   private final QuadKey                       _rootKey;
   private final String                        _name;
   private final boolean                       _readOnly;
   private final int                           _maxFeaturesPerNode;

   private final DB                            _db;
   private final BTreeMap<byte[], CNodeHeader> _nodesHeaders;
   private final BTreeMap<byte[], CNodeData>   _nodesFeatures;
   private final NavigableSet<byte[]>          _pendingNodes;
   private final BTreeMap<String, Object>      _metadata;


   private PointFeatureClusteringLODMapDBStorage(final Sector sector,
                                                 final File directory,
                                                 final String name,
                                                 final int maxFeaturesPerNode) throws IOException {
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
      .valueSerializer(new CNodeHeaderSerializer());

      _nodesHeaders = nodesHeadersMaker.makeOrGet();


      final BTreeMapMaker nodesFeaturesMaker = _db //
      .createTreeMap("NodesFeatures") //
      .counterEnable() //
      .comparator(quadKeyComparator) //
      .valueSerializer(new CNodeDataSerializer());

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


   private PointFeatureClusteringLODMapDBStorage(final File directory,
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
      .valueSerializer(new CNodeHeaderSerializer());

      _nodesHeaders = nodesHeadersMaker.makeOrGet();


      final BTreeMapMaker nodesFeaturesMaker = _db //
      .createTreeMap("NodesFeatures") //
      .counterEnable() //
      .comparator(quadKeyComparator) //
      .valueSerializer(new CNodeDataSerializer());

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

      saveNodeAsLeaf(id, nodeSector, minimumSector, features);

      for (final byte[] ancestorID : QuadKeyUtils.ancestors(id)) {
         if (!_pendingNodes.contains(ancestorID)) {
            _pendingNodes.add(ancestorID);
            final Sector ancestorNodeSector = QuadKey.sectorFor(_rootKey, ancestorID);
            final Sector ancestorMinimumSector = null;
            final List<PointFeatureCluster> ancestorClusters = Collections.emptyList();
            saveInnerNode(ancestorID, ancestorNodeSector, ancestorMinimumSector, ancestorClusters);
         }
      }
   }


   private void saveInnerNode(final byte[] id,
                              final Sector nodeSector,
                              final Sector minimumSector,
                              final List<PointFeatureCluster> clusters) {
      validateClusters(nodeSector, minimumSector, clusters);
      _nodesHeaders.put(id, new CInnerNodeHeader(nodeSector, minimumSector, clusters.size()));
      _nodesFeatures.put(id, new CInnerNodeData(clusters));
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


   private void saveNodeAsLeaf(final byte[] id,
                               final Sector nodeSector,
                               final Sector minimumSector,
                               final List<PointFeature> features) {
      validateFeatures(nodeSector, minimumSector, features);
      assertIsNull(_nodesHeaders.put(id, new CLeafNodeHeader(nodeSector, minimumSector, features.size())));
      assertIsNull(_nodesFeatures.put(id, new CLeafNodeData(features)));
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
         PointFeatureClusteringLODStorage.Statistics {

      private final String _storageName;
      private final long   _clustersCount;
      private final long   _featuresCount;
      private final int    _leafNodesCount;
      private final int    _innerNodesCount;
      private final int    _minFeaturesPerLeafNode;
      private final int    _maxFeaturesPerLeafNode;
      private final double _averageFeaturesPerLeafNode;
      private final int    _minClustersPerInnerNode;
      private final int    _maxClustersPerInnerNode;
      private final double _averageClustersPerInnerNode;
      private final int    _maxNodeDepth;
      private final int    _minNodeDepth;
      private final double _averageNodeDepth;


      private PvtStatistics(final String storageName,
                            final long clustersCount,
                            final long featuresCount,
                            final int leafNodesCount,
                            final int innerNodesCount,
                            final int minFeaturesPerLeafNode,
                            final int maxFeaturesPerLeafNode,
                            final double averageFeaturesPerLeafNode,
                            final int minClustersPerInnerNode,
                            final int maxClustersPerInnerNode,
                            final double averageClustersPerInnerNode,
                            final int maxNodeDepth,
                            final int minNodeDepth,
                            final double averageNodeDepth) {
         _storageName = storageName;
         _clustersCount = clustersCount;
         _featuresCount = featuresCount;
         _leafNodesCount = leafNodesCount;
         _innerNodesCount = innerNodesCount;
         _minFeaturesPerLeafNode = minFeaturesPerLeafNode;
         _maxFeaturesPerLeafNode = maxFeaturesPerLeafNode;
         _averageFeaturesPerLeafNode = averageFeaturesPerLeafNode;
         _minClustersPerInnerNode = minClustersPerInnerNode;
         _maxClustersPerInnerNode = maxClustersPerInnerNode;
         _averageClustersPerInnerNode = averageClustersPerInnerNode;
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
         return _leafNodesCount + _innerNodesCount;
      }


      @Override
      public int getLeafNodesCount() {
         return _leafNodesCount;
      }


      @Override
      public int getInnerNodesCount() {
         return _innerNodesCount;
      }


      @Override
      public int getMinFeaturesPerLeafNode() {
         return _minFeaturesPerLeafNode;
      }


      @Override
      public int getMaxFeaturesPerLeafNode() {
         return _maxFeaturesPerLeafNode;
      }


      @Override
      public double getAverageFeaturesPerLeafNode() {
         return _averageFeaturesPerLeafNode;
      }


      @Override
      public int getMinClustersPerInnerNode() {
         return _minClustersPerInnerNode;
      }


      @Override
      public int getMaxClustersPerInnerNode() {
         return _maxClustersPerInnerNode;
      }


      @Override
      public double getAverageClustersPerInnerNode() {
         return _averageClustersPerInnerNode;
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
         System.out.println("  Nodes Count: " + (_innerNodesCount + _leafNodesCount));
         System.out.println("    InnerNodes: " + _innerNodesCount);
         System.out.println("    LeafsNodes: " + _leafNodesCount);
         System.out.println("  Clusters/InnerNode: " + //
                            "min=" + _minClustersPerInnerNode + //
                            ", max=" + _maxClustersPerInnerNode + //
                            ", avg=" + (float) _averageClustersPerInnerNode);
         System.out.println("  Features/LeafNode: " + //
                            "min=" + _minFeaturesPerLeafNode + //
                            ", max=" + _maxFeaturesPerLeafNode + //
                            ", avg=" + (float) _averageFeaturesPerLeafNode);
         System.out.println("  Node Depth: " + //
                            "min=" + _minNodeDepth + //
                            ", max=" + _maxNodeDepth + //
                            ", avg=" + (float) _averageNodeDepth);
         System.out.println("--------------------------------------------------------------");
      }


   }


   private static class StatisticsGatherer
      implements
         PointFeatureClusteringLODStorage.NodeVisitor {

      private final String          _name;
      private final boolean         _showProgress;

      private UndeterminateProgress _progress   = null;
      private Statistics            _statistics = null;


      private long                  _featuresCount;
      private long                  _clustersCount;
      private int                   _innerNodesCount;
      private int                   _leafNodesCount;
      private int                   _minFeaturesPerLeafNode;
      private int                   _maxFeaturesPerLeafNode;
      private int                   _minClustersPerInnerNode;
      private int                   _maxClustersPerInnerNode;
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

         _innerNodesCount = 0;
         _leafNodesCount = 0;

         _minFeaturesPerLeafNode = Integer.MAX_VALUE;
         _maxFeaturesPerLeafNode = Integer.MIN_VALUE;

         _minClustersPerInnerNode = Integer.MAX_VALUE;
         _maxClustersPerInnerNode = Integer.MIN_VALUE;

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
      public boolean visit(final PointFeatureClusteringLODStorage.InnerNode node) {
         _innerNodesCount++;

         final int clustersCount = node.getClustersCount();

         _clustersCount += clustersCount;
         _minClustersPerInnerNode = Math.min(_minClustersPerInnerNode, clustersCount);
         _maxClustersPerInnerNode = Math.max(_maxClustersPerInnerNode, clustersCount);

         final int nodeDepth = node.getDepth();
         _sumDepth += nodeDepth;
         _minNodeDepth = Math.min(_minNodeDepth, nodeDepth);
         _maxNodeDepth = Math.max(_maxNodeDepth, nodeDepth);

         //         final Geodetic2D nodeAveragePosition = node.getAveragePosition();
         //
         //         _sumLatRadians += (nodeAveragePosition._latitude._radians * nodeFeaturesCount);
         //         _sumLonRadians += (nodeAveragePosition._longitude._radians * nodeFeaturesCount);

         if (_progress != null) {
            _progress.stepDone();
         }

         return true;
      }


      @Override
      public boolean visit(final PointFeatureClusteringLODStorage.LeafNode node) {
         _leafNodesCount++;

         final int nodeFeaturesCount = node.getFeaturesCount();

         _featuresCount += nodeFeaturesCount;
         _minFeaturesPerLeafNode = Math.min(_minFeaturesPerLeafNode, nodeFeaturesCount);
         _maxFeaturesPerLeafNode = Math.max(_maxFeaturesPerLeafNode, nodeFeaturesCount);

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
         //         _statistics = new PvtStatistics( //
         //                  _name, //
         //                  _featuresCount, //
         //                  Geodetic2D.fromRadians(_sumLatRadians / _featuresCount, _sumLonRadians / _featuresCount), //
         //                  _nodesCount, //
         //                  _minFeaturesPerNode, //
         //                  _maxFeaturesPerNode, //
         //                  (double) _featuresCount / _nodesCount, //
         //                  _minNodeDepth, //
         //                  _maxNodeDepth, //
         //                  (double) _sumDepth / _nodesCount);

         _statistics = new PvtStatistics( //
                  _name, //
                  _clustersCount, //
                  _featuresCount, //
                  _leafNodesCount, //
                  _innerNodesCount, //
                  _minFeaturesPerLeafNode, //
                  _maxFeaturesPerLeafNode, //
                  (double) _featuresCount / _leafNodesCount, //
                  _minClustersPerInnerNode, //
                  _maxClustersPerInnerNode, //
                  (double) _clustersCount / _innerNodesCount, //
                  _maxNodeDepth, //
                  _minNodeDepth, //
                  (double) _sumDepth / (_leafNodesCount + _innerNodesCount) //
         );

         if (_progress != null) {
            _progress.finish();
            _progress = null;
         }
      }
   }


   @Override
   synchronized public PointFeatureClusteringLODStorage.Statistics getStatistics(final boolean showProgress) {
      final StatisticsGatherer gatherer = new StatisticsGatherer(_name, showProgress);
      acceptDepthFirstVisitor(gatherer);
      return gatherer._statistics;
   }


   private static abstract class PvtNode
      implements
         PointFeatureClusteringLODStorage.Node {
      protected final PointFeatureClusteringLODMapDBStorage _storage;
      protected final byte[]                                _id;
      private final Sector                                  _nodeSector;
      private final Sector                                  _minimumSector;


      protected PvtNode(final PointFeatureClusteringLODMapDBStorage storage,
                        final byte[] id,
                        final CNodeHeader header) {
         _storage = storage;
         _id = id;
         _nodeSector = header.getNodeSector();
         _minimumSector = header.getMinimumSector();
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


      protected abstract boolean acceptVisitor(PointFeatureClusteringLODStorage.NodeVisitor visitor);

   }

   private static class PvtInnerNode
      extends
         PvtNode
      implements
         PointFeatureClusteringLODStorage.InnerNode {

      private final int                 _clustersCount;
      private List<PointFeatureCluster> _clusters;


      private PvtInnerNode(final PointFeatureClusteringLODMapDBStorage storage,
                           final byte[] id,
                           final CInnerNodeHeader header) {
         super(storage, id, header);
         _clustersCount = header.getClustersCount();
      }


      @Override
      public int getClustersCount() {
         return _clustersCount;
      }


      @Override
      public List<PointFeatureCluster> getClusters() {
         if (_clusters == null) {
            final CInnerNodeData nodeData = (CInnerNodeData) _storage._nodesFeatures.get(_id);
            _clusters = Collections.unmodifiableList(nodeData.getClusters());
         }
         return _clusters;
      }


      @Override
      public List<String> getChildrenIDs() {
         return _storage.getChildrenIDs(_id);
      }


      @Override
      protected boolean acceptVisitor(final PointFeatureClusteringLODStorage.NodeVisitor visitor) {
         return visitor.visit(this);
      }
   }


   private static class PvtLeafNode
      extends
         PvtNode
      implements
         PointFeatureClusteringLODStorage.LeafNode {


      private final int          _featuresCount;
      private List<PointFeature> _features = null;


      private PvtLeafNode(final PointFeatureClusteringLODMapDBStorage storage,
                          final byte[] id,
                          final CLeafNodeHeader header) {
         super(storage, id, header);
         _featuresCount = header.getFeaturesCount();
      }


      @Override
      public int getFeaturesCount() {
         return _featuresCount;
      }


      @Override
      public List<PointFeature> getFeatures() {
         if (_features == null) {
            final CLeafNodeData nodeData = (CLeafNodeData) _storage._nodesFeatures.get(_id);
            _features = Collections.unmodifiableList(nodeData.getFeatures());
         }
         return _features;
      }


      @Override
      protected boolean acceptVisitor(final PointFeatureClusteringLODStorage.NodeVisitor visitor) {
         return visitor.visit(this);
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
   synchronized public void acceptDepthFirstVisitor(final PointFeatureClusteringLODStorage.NodeVisitor visitor) {
      visitor.start();

      for (final Map.Entry<byte[], CNodeHeader> entry : _nodesHeaders.entrySet()) {
         final PvtNode node = createPvtNode(entry);
         final boolean keepGoing = node.acceptVisitor(visitor);
         if (!keepGoing) {
            break;
         }
      }

      visitor.stop();
   }


   private PvtNode createPvtNode(final Map.Entry<byte[], CNodeHeader> entry) {
      final CNodeHeader header = entry.getValue();
      if (header instanceof CInnerNodeHeader) {
         return new PvtInnerNode(this, entry.getKey(), (CInnerNodeHeader) header);
      }
      else if (header instanceof CLeafNodeHeader) {
         return new PvtLeafNode(this, entry.getKey(), (CLeafNodeHeader) header);
      }
      else {
         throw new RuntimeException("Unsupported header: " + entry);
      }
   }


   @Override
   public void processPendingNodes(final boolean verbose) {
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
      private final byte[]      _id;
      private final CNodeHeader _header;


      private Child(final byte[] id,
                    final CNodeHeader header) {
         _id = id;
         _header = header;
      }

   }


   private void addChild(final byte[] key,
                         final byte childIndex,
                         final List<Child> children) {
      final byte[] childID = QuadKeyUtils.append(key, childIndex);
      final CNodeHeader childHeader = _nodesHeaders.get(childID);
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


   private void processPendingNode(final byte[] key) {
      final List<Child> children = getChildren(key);
      if (children.isEmpty()) {
         throw new RuntimeException("LOGIC ERROR");
      }

      Sector minimumSector = null;
      final List<PointFeatureCluster> clusters = new ArrayList<>(children.size());
      for (final Child child : children) {
         minimumSector = child._header.getMinimumSector().mergedWith(minimumSector);
         final CNodeData childNodeData = _nodesFeatures.get(child._id);
         if (childNodeData == null) {
            throw new RuntimeException("LOGIC ERROR");
         }
         clusters.add(childNodeData.createCluster());
      }

      if (clusters.isEmpty()) {
         throw new RuntimeException("LOGIC ERROR");
      }

      final Sector nodeSector = QuadKey.sectorFor(_rootKey, key);
      saveInnerNode(key, nodeSector, minimumSector, clusters);
   }


}
