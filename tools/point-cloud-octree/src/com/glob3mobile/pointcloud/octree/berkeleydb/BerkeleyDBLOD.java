

package com.glob3mobile.pointcloud.octree.berkeleydb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.glob3mobile.pointcloud.octree.Geodetic3D;
import com.glob3mobile.pointcloud.octree.PersistentLOD;
import com.glob3mobile.pointcloud.octree.Sector;
import com.glob3mobile.pointcloud.octree.Utils;
import com.sleepycat.je.Cursor;
import com.sleepycat.je.CursorConfig;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;
import com.sleepycat.je.TransactionConfig;

import es.igosoftware.io.GIOUtils;
import es.igosoftware.util.GUndeterminateProgress;


public class BerkeleyDBLOD
implements
PersistentLOD {


   public static PersistentLOD openReadOnly(final File cloudDirectory,
                                            final String cloudName) {
      return new BerkeleyDBLOD(cloudDirectory, cloudName, false, true);
   }


   public static PersistentLOD open(final File cloudDirectory,
                                    final String cloudName,
                                    final boolean createIfNotExists) {
      return new BerkeleyDBLOD(cloudDirectory, cloudName, createIfNotExists, false);
   }


   public static void delete(final File cloudDirectory,
                             final String cloudName) {
      final File envHome = new File(cloudDirectory, cloudName);
      if (!envHome.exists()) {
         return;
      }

      try {
         GIOUtils.cleanDirectory(envHome, false);
      }
      catch (final IOException e) {
         throw new RuntimeException(e);
      }
   }


   private static final String NODE_DATABASE_NAME      = "LODNode";
   private static final String NODE_DATA_DATABASE_NAME = "LODNodeData";


   private final boolean       _readOnly;
   private final String        _cloudName;
   private final Environment   _env;
   private final Database      _nodeDB;
   private final Database      _nodeDataDB;
   private final File          _cachedStatisticsFile;


   private BerkeleyDBLOD(final File cloudDirectory,
                         final String cloudName,
                         final boolean createIfNotExists,
                         final boolean readOnly) {
      _readOnly = readOnly;
      _cloudName = cloudName;

      final File envHome = new File(cloudDirectory, cloudName);
      if (createIfNotExists) {
         if (!envHome.exists()) {
            envHome.mkdirs();
         }
      }

      final String readOnlyMsg = readOnly ? " read-only" : " read-write";
      System.out.println("- opening" + readOnlyMsg + " cloud name \"" + cloudName + "\" (" + envHome.getAbsoluteFile() + ")");


      final EnvironmentConfig envConfig = new EnvironmentConfig();
      envConfig.setAllowCreate(createIfNotExists);
      envConfig.setTransactional(true);
      envConfig.setReadOnly(readOnly);
      final int totalBytes = 1024 * 1024 * 1024;
      envConfig.setCacheSize(totalBytes);
      _env = new Environment(envHome, envConfig);

      final DatabaseConfig dbConfig = new DatabaseConfig();
      dbConfig.setAllowCreate(createIfNotExists);
      dbConfig.setTransactionalVoid(true);
      dbConfig.setKeyPrefixing(true);
      dbConfig.setReadOnly(readOnly);
      dbConfig.setSortedDuplicates(true);

      _nodeDB = _env.openDatabase(null, NODE_DATABASE_NAME, dbConfig);
      _nodeDataDB = _env.openDatabase(null, NODE_DATA_DATABASE_NAME, dbConfig);

      _cachedStatisticsFile = new File(cloudDirectory, "_stats_" + cloudName + ".ser");
   }


   @Override
   public void close() {
      _nodeDataDB.close();
      _nodeDB.close();
      _env.close();
   }


   @Override
   public String getCloudName() {
      return _cloudName;
   }


   static class BerkeleyDBTransaction
   implements
   PersistentLOD.Transaction {

      final com.sleepycat.je.Transaction _txn;


      private BerkeleyDBTransaction(final com.sleepycat.je.Transaction txn) {
         _txn = txn;
      }


      @Override
      public void commit() {
         _txn.commit();
      }


      @Override
      public void rollback() {
         _txn.abort();
      }
   }

   private static final TransactionConfig DEFAULT_TRANSACTION_CONFIG = new TransactionConfig();


   @Override
   public PersistentLOD.Transaction createTransaction() {
      return new BerkeleyDBTransaction(_env.beginTransaction(null, DEFAULT_TRANSACTION_CONFIG));
   }


   Database getNodeDB() {
      return _nodeDB;
   }


   Database getNodeDataDB() {
      return _nodeDataDB;
   }


   @Override
   public void put(final PersistentLOD.Transaction transaction,
                   final String id,
                   //final boolean dirty,
                   final int level,
                   final List<Geodetic3D> points) {
      if (_readOnly) {
         throw new RuntimeException("Can't add points to readonly OT");
      }

      deleteCachedStatistics();

      final BerkeleyDBLODNode node = BerkeleyDBLODNode.create(this, Utils.toBinaryID(id), level, points);
      node.save(getBerkeleyDBTransaction(transaction));
   }


   @Override
   public void acceptDepthFirstVisitor(final PersistentLOD.Transaction transaction,
                                       final PersistentLOD.Visitor visitor) {
      visitor.start(transaction);

      final CursorConfig config = new CursorConfig();
      config.setReadUncommitted(false);

      final com.sleepycat.je.Transaction txn = getBerkeleyDBTransaction(transaction);
      try (final Cursor cursor = _nodeDB.openCursor(txn, config)) {
         final DatabaseEntry keyEntry = new DatabaseEntry();
         final DatabaseEntry dataEntry = new DatabaseEntry();

         while (cursor.getNext(keyEntry, dataEntry, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
            final byte[] key = keyEntry.getData();
            final byte[] data = dataEntry.getData();

            final BerkeleyDBLODNode node = BerkeleyDBLODNode.fromDB(txn, this, key, data, false);
            final boolean keepGoing = visitor.visit(transaction, node);
            if (!keepGoing) {
               break;
            }
         }
      }

      visitor.stop(transaction);
   }


   private static com.sleepycat.je.Transaction getBerkeleyDBTransaction(final PersistentLOD.Transaction transaction) {
      return (transaction == null) ? null : ((BerkeleyDBTransaction) transaction)._txn;
   }


   //   private PersistentLOD.Level getAncestorContribution(final byte[] id,
   //                                                       final Sector sector) {
   //      final DatabaseEntry dataEntry = new DatabaseEntry();
   //      final com.sleepycat.je.Transaction txn = null;
   //      final OperationStatus status = _nodeDB.get(txn, new DatabaseEntry(id), dataEntry, LockMode.DEFAULT);
   //      switch (status) {
   //         case NOTFOUND: {
   //            return null;
   //         }
   //         case SUCCESS: {
   //            final BerkeleyDBLODNode node = BerkeleyDBLODNode.fromDB(txn, this, id, dataEntry.getData(), true);
   //
   //            final List<Geodetic3D> resultPoints = new ArrayList<Geodetic3D>(node.getPointsCount());
   //            for (final Geodetic3D point : node.getPoints()) {
   //               if (sector.contains(point._latitude, point._longitude)) {
   //                  resultPoints.add(point);
   //               }
   //            }
   //
   //            return resultPoints.isEmpty() ? null : new PersistentLOD.Level(id.length, resultPoints);
   //         }
   //         default: {
   //            throw new RuntimeException("Unsupported status=" + status);
   //         }
   //      }
   //   }


   private static enum Situation {
      NotFoundSelfNorDescendants,
      FoundDescendants,
      FoundSelf,
      FoundNothing;
   }


   private Situation getCursorSituation(final Cursor cursor,
                                        final DatabaseEntry keyEntry,
                                        final DatabaseEntry dataEntry,
                                        final byte[] id) {
      final OperationStatus status = cursor.getSearchKeyRange(keyEntry, dataEntry, LockMode.DEFAULT);
      switch (status) {
         case SUCCESS: {
            final byte[] key = keyEntry.getData();

            if (!Utils.hasSamePrefix(key, id)) {
               return Situation.NotFoundSelfNorDescendants;
            }
            else if (Utils.isGreaterThan(key, id)) {
               return Situation.FoundDescendants;
            }
            else {
               return Situation.FoundSelf;
            }
         }
         case NOTFOUND: {
            return Situation.FoundNothing;
         }
         default:
            throw new RuntimeException("Status not supported: " + status);
      }

   }


   private List<PersistentLOD.Level> getLODLevelsForSelf(final Cursor cursor,
                                                         final DatabaseEntry keyEntry,
            final DatabaseEntry dataEntry,
            final byte[] id) {

      final List<PersistentLOD.Level> result = new ArrayList<PersistentLOD.Level>();

      final com.sleepycat.je.Transaction txn = null;
      BerkeleyDBLODNode node = BerkeleyDBLODNode.fromDB(txn, this, id, dataEntry.getData(), true);
      result.add(new PersistentLOD.Level(node.getLODLevel(), node.getPoints()));

      while (cursor.getNext(keyEntry, dataEntry, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
         final byte[] key = keyEntry.getData();
         if (Utils.isGreaterThan(key, id)) {
            break;
         }
         node = BerkeleyDBLODNode.fromDB(txn, this, id, dataEntry.getData(), true);
         result.add(new PersistentLOD.Level(node.getLODLevel(), node.getPoints()));
      }

      return result;
   }


   private static class NodeSet {
      private final byte[]                  _id;
      private final List<BerkeleyDBLODNode> _levels = new ArrayList<BerkeleyDBLODNode>();
      private int                           _maxLevel;


      private NodeSet(final byte[] id,
                      final BerkeleyDBLODNode level) {
         _id = id;
         _levels.add(level);
         _maxLevel = level.getLODLevel();
      }


      private void add(final BerkeleyDBLODNode level) {
         _levels.add(level);
         _maxLevel = Math.max(_maxLevel, level.getLODLevel());
      }


      private void putInto(final int depth,
                           final Map<Integer, List<Geodetic3D>> accumulated) {
         final int deltaDepth = _id.length - depth;

         final int lodLevelsToRemove = deltaDepth * 2;

         final int descentantMaxLevel = _levels.size() - lodLevelsToRemove;
         for (int i = 0; i < descentantMaxLevel; i++) {
            final BerkeleyDBLODNode node = _levels.get(i);
            put(accumulated, node.getLODLevel() + lodLevelsToRemove, node.getPoints());
         }

         //System.out.println(deltaDepth + "  " + this);
      }


      private static void put(final Map<Integer, List<Geodetic3D>> accumulated,
                              final int level,
                              final List<Geodetic3D> points) {
         final List<Geodetic3D> current = accumulated.get(level);
         if (current == null) {
            accumulated.put(level, new ArrayList<Geodetic3D>(points));
         }
         else {
            current.addAll(points);
         }
      }


      @Override
      public String toString() {
         return "[DescendantSet maxLevel=" + _maxLevel + ", levels=" + _levels + "]";
      }

   }


   private List<PersistentLOD.Level> getLODLevelsFromDescendants(final Cursor cursor,
                                                                 final DatabaseEntry keyEntry,
                                                                 final DatabaseEntry dataEntry,
                                                                 final byte[] id) {

      //      final List<BerkeleyDBLODNode> descendantLevels = new ArrayList<BerkeleyDBLODNode>();

      final Map<Integer, List<Geodetic3D>> accumulated = new HashMap<Integer, List<Geodetic3D>>();

      final com.sleepycat.je.Transaction txn = null;
      byte[] key = keyEntry.getData();
      BerkeleyDBLODNode descendant = BerkeleyDBLODNode.fromDB(txn, this, key, dataEntry.getData(), false);
      //System.out.println(descendant);
      //byte[] currentKey = key;
      NodeSet descendantSet = new NodeSet(key, descendant);
      //      descendantLevels.add(descendant);

      while (cursor.getNext(keyEntry, dataEntry, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
         key = keyEntry.getData();
         if (!Utils.hasSamePrefix(key, id)) {
            break;
         }
         descendant = BerkeleyDBLODNode.fromDB(txn, this, key, dataEntry.getData(), false);
         if (!Arrays.equals(key, descendantSet._id)) {
            descendantSet.putInto(id.length, accumulated);
            descendantSet = new NodeSet(key, descendant);
         }
         else {
            descendantSet.add(descendant);
         }
      }

      //      final int deltaDepth = descendantSet._id.length - id.length;
      //      System.out.println(deltaDepth + "  " + descendantSet);
      descendantSet.putInto(id.length, accumulated);


      final List<PersistentLOD.Level> result = new ArrayList<PersistentLOD.Level>();

      final List<Integer> mapKeys = new ArrayList<Integer>(accumulated.keySet());
      Collections.sort(mapKeys);
      for (final Integer mapKey : mapKeys) {
         final List<Geodetic3D> points = accumulated.get(mapKey);
         result.add(new PersistentLOD.Level(mapKey, points));
      }

      return result;
   }


   private List<BerkeleyDBLODNode> readNodeSet(final com.sleepycat.je.Transaction txn,
                                               final byte[] id,
                                               final boolean loadPoints) {
      final CursorConfig cursorConfig = new CursorConfig();

      final List<BerkeleyDBLODNode> result = new ArrayList<BerkeleyDBLODNode>();

      try (final Cursor cursor = _nodeDB.openCursor(txn, cursorConfig)) {
         final DatabaseEntry keyEntry = new DatabaseEntry(id);
         final DatabaseEntry dataEntry = new DatabaseEntry();

         final OperationStatus status = cursor.getSearchKeyRange(keyEntry, dataEntry, LockMode.DEFAULT);
         switch (status) {
            case SUCCESS: {
               final byte[] key = keyEntry.getData();
               if (!Arrays.equals(id, key)) {
                  break;
               }

               result.add(BerkeleyDBLODNode.fromDB(txn, this, id, dataEntry.getData(), loadPoints));
            }
            case NOTFOUND: {
               return result;
            }
            default:
               throw new RuntimeException("Status not supported: " + status);
         }
      }

      return result;
   }


   private List<BerkeleyDBLODNode> getAncestor(final com.sleepycat.je.Transaction txn,
            final byte[] id,
            final boolean loadPoints) {
      byte[] ancestorId = Utils.removeTrailing(id);
      while (ancestorId != null) {
         final List<BerkeleyDBLODNode> ancestorSet = readNodeSet(txn, ancestorId, loadPoints);
         if (!ancestorSet.isEmpty()) {
            return ancestorSet;
         }
         ancestorId = Utils.removeTrailing(ancestorId);
      }
      return null;
   }


   private List<Level> getLODLevelsForParent(final Cursor cursor,
                                             final DatabaseEntry keyEntry,
                                             final DatabaseEntry dataEntry,
                                             final byte[] id) {
      final int _DIEGO_AT_WORK;

      final com.sleepycat.je.Transaction txn = null;

      final List<BerkeleyDBLODNode> ancestor = getAncestor(txn, id, true);

      System.out.println(ancestor);

      //      NodeSet ancestorSet = null;
      //
      //      byte[] ancestorKey = null;
      //      while (cursor.getPrev(keyEntry, dataEntry, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
      //         final byte[] key = keyEntry.getData();
      //         if (ancestorKey == null) {
      //            if (Utils.hasSamePrefix(id, key)) {
      //               ancestorKey = key;
      //               //System.out.println(Utils.toIDString(key) + " target=" + Utils.toIDString(id));
      //               //System.out.println("Found ancestor=" + Utils.toIDString(ancestorKey) + " target=" + Utils.toIDString(id));
      //               final BerkeleyDBLODNode level = BerkeleyDBLODNode.fromDB(txn, this, key, dataEntry.getData(), false);
      //               ancestorSet = new NodeSet(key, level);
      //            }
      //            else {
      //               if (!Arrays.equals(ancestorKey, key)) {
      //                  break;
      //               }
      //
      //               final BerkeleyDBLODNode level = BerkeleyDBLODNode.fromDB(txn, this, key, dataEntry.getData(), false);
      //               ancestorSet.add(level);
      //            }
      //         }
      //      }
      //
      //      System.out.println(ancestorSet);

      final List<Level> result = null;
      return result;
   }


   @Override
   public List<PersistentLOD.Level> getLODLevels(final String id) {
      //      final byte[] binaryID = Utils.toBinaryID(id);
      //      final List<byte[]> ancestorsIDs = Utils.getPathFromRoot(binaryID);
      //
      //      final Sector sector = TileHeader.sectorFor(binaryID);
      //
      //      final List<PersistentLOD.Level> result = new ArrayList<PersistentLOD.Level>(ancestorsIDs.size());
      //
      //      for (final byte[] ancestorID : ancestorsIDs) {
      //         final PersistentLOD.Level level = getAncestorContribution(ancestorID, sector);
      //         if (level != null) {
      //            result.add(level);
      //         }
      //      }

      final byte[] binaryID = Utils.toBinaryID(id);


      final CursorConfig cursorConfig = new CursorConfig();

      final com.sleepycat.je.Transaction txn = null;
      try (final Cursor cursor = _nodeDB.openCursor(txn, cursorConfig)) {
         final DatabaseEntry keyEntry = new DatabaseEntry(binaryID);
         final DatabaseEntry dataEntry = new DatabaseEntry();
         final Situation situation = getCursorSituation(cursor, keyEntry, dataEntry, binaryID);
         System.out.println(situation);

         switch (situation) {
            case NotFoundSelfNorDescendants:
               return getLODLevelsForParent(cursor, keyEntry, dataEntry, binaryID);

            case FoundDescendants:
               return getLODLevelsFromDescendants(cursor, keyEntry, dataEntry, binaryID);

            case FoundSelf:
               return getLODLevelsForSelf(cursor, keyEntry, dataEntry, binaryID);

            case FoundNothing:
               return Collections.emptyList();

            default:
               throw new RuntimeException("Invalid situation: " + situation);
         }

      }


      //      final CursorConfig cursorConfig = new CursorConfig();
      //
      //      final com.sleepycat.je.Transaction txn = null;
      //      try (final Cursor cursor = _nodeDB.openCursor(txn, cursorConfig)) {
      //         final DatabaseEntry keyEntry = new DatabaseEntry(binaryID);
      //         final DatabaseEntry dataEntry = new DatabaseEntry();
      //         final OperationStatus status = cursor.getSearchKeyRange(keyEntry, dataEntry, LockMode.DEFAULT);
      //         if (status == OperationStatus.SUCCESS) {
      //            byte[] key = keyEntry.getData();
      //
      //            Situation situation;
      //
      //            if (!Utils.hasSamePrefix(key, binaryID)) {
      //               situation = Situation.NotFoundSelfNorDescendants;
      //               return result;
      //            }
      //            if (Utils.isGreaterThan(key, binaryID)) {
      //               situation = Situation.FoundDescendants;
      //               result.add(fromDB(txn, octree, key, dataEntry.getData(), loadPoints));
      //            }
      //            else {
      //               situation = Situation.FoundSelf;
      //            }
      //
      //            while (cursor.getNext(keyEntry, dataEntry, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
      //               key = keyEntry.getData();
      //               if (!Utils.hasSamePrefix(key, binaryID)) {
      //                  return result;
      //               }
      //               if (Utils.isGreaterThan(key, binaryID)) {
      //                  result.add(fromDB(txn, octree, keyEntry.getData(), dataEntry.getData(), loadPoints));
      //               }
      //            }
      //         }
      //      }


      //      return result;
   }


   @Override
   public Sector getSector(final String id) {
      return TileHeader.sectorFor(Utils.toBinaryID(id));
   }


   private static class BerkeleyLODDBStatistics
   implements
   PersistentLOD.Visitor,
   PersistentLOD.Statistics,
   Serializable {

      private static final long      serialVersionUID = 1L;

      private final String           _cloudName;
      private GUndeterminateProgress _progress;

      private long                   _nodesCount;
      private long                   _pointsCount;
      private long                   _sumDepth;
      private int                    _minDepth;
      private int                    _maxDepth;
      private int                    _minPointsCountPerNode;
      private int                    _maxPointsCountPerNode;
      private Sector                 _sector;
      private double                 _minHeight       = Double.POSITIVE_INFINITY;
      private double                 _maxHeight       = Double.NEGATIVE_INFINITY;
      private final boolean          _fast;


      private BerkeleyLODDBStatistics(final String cloudName,
                                      final boolean fast,
                                      final GUndeterminateProgress progress) {
         _cloudName = cloudName;
         _fast = fast;
         _progress = progress;
      }


      @Override
      public void start(final PersistentLOD.Transaction transaction) {
         _nodesCount = 0;
         _pointsCount = 0;
         _minPointsCountPerNode = Integer.MAX_VALUE;
         _maxPointsCountPerNode = Integer.MIN_VALUE;
         _sumDepth = 0;
         _minDepth = Integer.MAX_VALUE;
         _maxDepth = Integer.MIN_VALUE;
      }


      @Override
      public boolean visit(final PersistentLOD.Transaction transaction,
                           final PersistentLOD.Node node) {
         final Sector nodeSector = node.getSector();
         _sector = (_sector == null) ? nodeSector : _sector.mergedWith(nodeSector);

         if (_progress != null) {
            _progress.stepDone();
         }

         if (!_fast) {
            for (final Geodetic3D point : node.getPoints()) {
               final double height = point._height;
               if (height < _minHeight) {
                  _minHeight = height;
               }
               if (height > _maxHeight) {
                  _maxHeight = height;
               }
            }
         }

         _nodesCount++;
         final int nodePointsCount = node.getPointsCount();
         _pointsCount += nodePointsCount;
         if (nodePointsCount < _minPointsCountPerNode) {
            _minPointsCountPerNode = nodePointsCount;
         }
         if (nodePointsCount > _maxPointsCountPerNode) {
            _maxPointsCountPerNode = nodePointsCount;
         }

         final int depth = node.getDepth();
         _sumDepth += depth;
         if (depth < _minDepth) {
            _minDepth = depth;
         }
         if (depth > _maxDepth) {
            _maxDepth = depth;
         }
         return true;
      }


      @Override
      public void stop(final PersistentLOD.Transaction transaction) {
         if (_progress != null) {
            _progress.finish();
            _progress = null;
         }
      }


      @Override
      public void show() {
         System.out.println("======================================================================");
         System.out.println(" " + _cloudName);
         System.out.println("   Points: " + _pointsCount);
         System.out.println("   Sector: " + _sector);
         System.out.println("   Heights: " + _minHeight + "/" + _maxHeight + " (delta=" + (_maxHeight - _minHeight) + ")");
         System.out.println("   Nodes: " + _nodesCount);
         System.out.println("   Depth: " + _minDepth + "/" + _maxDepth + ", Average=" + ((float) _sumDepth / _nodesCount));
         System.out.println("   Points/Node: Average=" + ((float) _pointsCount / _nodesCount) + //
                  ", Min=" + _minPointsCountPerNode + //
                  ", Max=" + _maxPointsCountPerNode);
         System.out.println("======================================================================");


         // final StatsConfig config = new StatsConfig();
         // final EnvironmentStats stats = _env.getStats(config);
         // System.out.println(stats);
      }


      @Override
      public long getPointsCount() {
         return _pointsCount;
      }


      @Override
      public Sector getSector() {
         return _sector;
      }


      @Override
      public double getMinHeight() {
         return _minHeight;
      }


      @Override
      public double getMaxHeight() {
         return _maxHeight;
      }


      @Override
      public int getMinPointsPerNode() {
         return _minPointsCountPerNode;
      }


      @Override
      public int getMaxPointsPerNode() {
         return _maxPointsCountPerNode;
      }


      @Override
      public String getPointCloudName() {
         return _cloudName;
      }

   }


   private void deleteCachedStatistics() {
      if (_cachedStatisticsFile.exists()) {
         _cachedStatisticsFile.delete();
      }
   }


   private BerkeleyLODDBStatistics getCachedStatistics() {
      if (!_cachedStatisticsFile.exists()) {
         return null;
      }

      try (final ObjectInputStream in = new ObjectInputStream(new FileInputStream(_cachedStatisticsFile))) {
         return (BerkeleyLODDBStatistics) in.readObject();
      }
      catch (final ClassNotFoundException e) {
         throw new RuntimeException(e);
      }
      catch (final IOException e) {
         throw new RuntimeException(e);
      }

   }


   private void saveCachedStatistics(final BerkeleyLODDBStatistics statistics) {
      if (_cachedStatisticsFile.exists()) {
         _cachedStatisticsFile.delete();
      }

      try (final ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(_cachedStatisticsFile, false))) {
         out.writeObject(statistics);
         out.flush();
      }
      catch (final IOException e) {
         throw new RuntimeException(e);
      }
   }


   @Override
   public PersistentLOD.Statistics getStatistics(final boolean fast,
                                                 final boolean showProgress) {

      final BerkeleyLODDBStatistics cachedStatistics = getCachedStatistics();
      if (cachedStatistics != null) {
         if (fast || !cachedStatistics._fast) {
            return cachedStatistics;
         }
      }

      final GUndeterminateProgress progress;
      if (showProgress) {
         progress = new GUndeterminateProgress(10, true) {
            @Override
            public void informProgress(final long stepsDone,
                                       final long elapsed) {
               System.out.println("- gathering statistics for \"" + _cloudName + "\"" + progressString(stepsDone, elapsed));
            }
         };
      }
      else {
         progress = null;
      }

      final BerkeleyLODDBStatistics statistics = new BerkeleyLODDBStatistics(_cloudName, fast, progress);
      final Transaction transaction = null;
      acceptDepthFirstVisitor(transaction, statistics);
      saveCachedStatistics(statistics);
      return statistics;
   }

}
