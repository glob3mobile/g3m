

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
import java.util.List;

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
                                            final String cloudName,
                                            final long cacheSizeInBytes) {
      return new BerkeleyDBLOD(cloudDirectory, cloudName, false, true, cacheSizeInBytes);
   }


   public static PersistentLOD open(final File cloudDirectory,
                                    final String cloudName,
                                    final boolean createIfNotExists,
                                    final long cacheSizeInBytes) {
      return new BerkeleyDBLOD(cloudDirectory, cloudName, createIfNotExists, false, cacheSizeInBytes);
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


   private static final String NODE_DATABASE_NAME            = "LODNode";
   private static final String NODE_LEVEL_DATA_DATABASE_NAME = "LODNodeLevelData";


   private final boolean       _readOnly;
   private final String        _cloudName;
   private final Environment   _env;
   private final Database      _nodeDB;
   private final Database      _nodeLevelDataDB;
   private final File          _cachedStatisticsFile;


   private BerkeleyDBLOD(final File cloudDirectory,
                         final String cloudName,
                         final boolean createIfNotExists,
                         final boolean readOnly,
                         final long cacheSizeInBytes) {
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
      if (cacheSizeInBytes > 0) {
         envConfig.setCacheSize(cacheSizeInBytes);
      }
      envConfig.setSharedCache(true);
      _env = new Environment(envHome, envConfig);

      final DatabaseConfig dbConfig = new DatabaseConfig();
      dbConfig.setAllowCreate(createIfNotExists);
      dbConfig.setTransactionalVoid(true);
      dbConfig.setKeyPrefixing(true);
      dbConfig.setReadOnly(readOnly);
      dbConfig.setSortedDuplicates(false);
      _nodeDB = _env.openDatabase(null, NODE_DATABASE_NAME, dbConfig);
      _nodeLevelDataDB = _env.openDatabase(null, NODE_LEVEL_DATA_DATABASE_NAME, dbConfig);

      _cachedStatisticsFile = new File(cloudDirectory, "_stats_" + cloudName + ".ser");
   }


   @Override
   public void close() {
      _nodeLevelDataDB.close();
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


   Database getNodeLevelData() {
      return _nodeLevelDataDB;
   }


   @Override
   public void put(final PersistentLOD.Transaction transaction,
                   final String id,
                   final List<List<Geodetic3D>> levelsPoints) {
      if (_readOnly) {
         throw new RuntimeException("Can't add points to readonly OT");
      }

      deleteCachedStatistics();

      final BerkeleyDBLODNode node = BerkeleyDBLODNode.create(this, Utils.toBinaryID(id), levelsPoints);
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
      final OperationStatus status = cursor.getSearchKeyRange(keyEntry, dataEntry, LockMode.READ_UNCOMMITTED);
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


   @Override
   public PersistentLOD.NodeLayout getNodeLayout(final String id) {
      final byte[] binaryID = Utils.toBinaryID(id);

      final CursorConfig cursorConfig = new CursorConfig();

      final com.sleepycat.je.Transaction txn = null;
      try (final Cursor cursor = _nodeDB.openCursor(txn, cursorConfig)) {
         final DatabaseEntry keyEntry = new DatabaseEntry(binaryID);
         final DatabaseEntry dataEntry = new DatabaseEntry();
         dataEntry.setPartial(0, 0, true);

         final Situation situation = getCursorSituation(cursor, keyEntry, dataEntry, binaryID);
         switch (situation) {
            case NotFoundSelfNorDescendants: {
               return getNodeLayoutFromParent(cursor, keyEntry, dataEntry, binaryID);
            }
            case FoundDescendants: {
               return getNodeLayoutFromDescendants(cursor, keyEntry, dataEntry, binaryID);
            }
            case FoundSelf: {
               return new PersistentLOD.NodeLayout(id, Arrays.asList(id));
            }
            case FoundNothing: {
               return new PersistentLOD.NodeLayout(id, Collections.<String> emptyList());
            }
            default:
               throw new RuntimeException("Invalid situation: " + situation);
         }
      }
   }


   private PersistentLOD.NodeLayout getNodeLayoutFromParent(final Cursor cursor,
                                                            final DatabaseEntry keyEntry,
                                                            final DatabaseEntry dataEntry,
                                                            final byte[] id) {
      final List<String> nodesID = new ArrayList<>(1);

      if (cursor.getPrev(keyEntry, dataEntry, LockMode.READ_UNCOMMITTED) == OperationStatus.SUCCESS) {
         final byte[] key = keyEntry.getData();
         if (Utils.hasSamePrefix(id, key)) {
            nodesID.add(Utils.toIDString(key));
         }
      }

      return new NodeLayout(Utils.toIDString(id), nodesID);
   }


   private PersistentLOD.NodeLayout getNodeLayoutFromDescendants(final Cursor cursor,
                                                                 final DatabaseEntry keyEntry,
                                                                 final DatabaseEntry dataEntry,
                                                                 final byte[] id) {
      final int maxLevelDelta = 4;
      final int maxDescendantDepth = id.length + maxLevelDelta;
      //      final int maxDescendantDepth = 1000;

      final List<String> nodesID = new ArrayList<>();

      byte[] key = keyEntry.getData();
      if (key.length <= maxDescendantDepth) {
         nodesID.add(Utils.toIDString(key));
      }

      while (cursor.getNext(keyEntry, dataEntry, LockMode.READ_UNCOMMITTED) == OperationStatus.SUCCESS) {
         key = keyEntry.getData();
         if (!Utils.hasSamePrefix(key, id)) {
            break;
         }
         if (key.length <= maxDescendantDepth) {
            nodesID.add(Utils.toIDString(key));
         }
      }

      return new NodeLayout(Utils.toIDString(id), nodesID);
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
      private long                   _nodeLevelsCount;
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
         _nodeLevelsCount = 0;
      }


      @Override
      public boolean visit(final PersistentLOD.Transaction transaction,
                           final PersistentLOD.Node node) {
         final Sector nodeSector = node.getSector();
         _sector = (_sector == null) ? nodeSector : _sector.mergedWith(nodeSector);

         if (_progress != null) {
            _progress.stepDone();
         }

         _nodesCount++;

         _nodeLevelsCount += node.getLevelsCount();

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


         if (!_fast) {
            for (final PersistentLOD.NodeLevel level : node.getLevels()) {
               for (final Geodetic3D point : level.getPoints(transaction)) {
                  final double height = point._height;
                  if (height < _minHeight) {
                     _minHeight = height;
                  }
                  if (height > _maxHeight) {
                     _maxHeight = height;
                  }
               }
            }
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
         System.out.println("    Depth: " + _minDepth + "/" + _maxDepth + ", Average=" + ((float) _sumDepth / _nodesCount));
         System.out.println("    Points/Node: Average=" + ((float) _pointsCount / _nodesCount) + //
                  ", Min=" + _minPointsCountPerNode + //
                  ", Max=" + _maxPointsCountPerNode);
         System.out.println("   Levels: " + _nodeLevelsCount);
         System.out.println("    Levels/Node=" + ((float) _nodeLevelsCount / _nodesCount));
         System.out.println("    Points/Level: Average=" + ((float) _pointsCount / _nodeLevelsCount));
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


      @Override
      public int getMinDepth() {
         return _minDepth;
      }


      @Override
      public int getMaxDepth() {
         return _maxDepth;
      }


      @Override
      public double getAverageDepth() {
         return (double) _sumDepth / _nodesCount;
      }


      @Override
      public long getLevelsCount() {
         return _nodeLevelsCount;
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
