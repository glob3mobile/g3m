

package com.glob3mobile.pointcloud.octree.berkeleydb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.glob3mobile.pointcloud.octree.PersistentOctree;
import com.glob3mobile.utils.Geodetic3D;
import com.glob3mobile.utils.Sector;
import com.glob3mobile.utils.Utils;
import com.sleepycat.je.Cursor;
import com.sleepycat.je.CursorConfig;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;
import com.sleepycat.je.Transaction;
import com.sleepycat.je.TransactionConfig;

import es.igosoftware.io.GIOUtils;
import es.igosoftware.util.GUndeterminateProgress;


public class BerkeleyDBOctree
         implements
            PersistentOctree {

   // private static final ILogger LOGGER              = GLogger.instance();
   // private static final Charset UTF8                = Charset.forName("UTF-8");

   private static final int    DEFAULT_MAX_BUFFER_SIZE      = 64 * 1024;
   private static final int    DEFAULT_MAX_POINTS_PER_TITLE = 64 * 1024;
   private static final String NODE_DATABASE_NAME           = "Node";
   private static final String NODE_DATA_DATABASE_NAME      = "NodeData";


   public static void delete(final File cloudDirectory,
                             final String cloudName) {
      final File envHome = new File(cloudDirectory, cloudName);
      if (!envHome.exists()) {
         return;
      }

      final File cachedStatisticsFile = new File(cloudDirectory, "_stats_" + cloudName + ".ser");
      cachedStatisticsFile.delete();

      try {
         GIOUtils.cleanDirectory(envHome, false);
      }
      catch (final IOException e) {
         throw new RuntimeException(e);
      }
   }


   public static PersistentOctree open(final File cloudDirectory,
                                       final String cloudName,
                                       final boolean createIfNotExists,
                                       final long cacheSizeInBytes) {
      return open(cloudDirectory, cloudName, createIfNotExists, DEFAULT_MAX_BUFFER_SIZE, DEFAULT_MAX_POINTS_PER_TITLE,
               cacheSizeInBytes);
   }


   public static PersistentOctree open(final File cloudDirectory,
                                       final String cloudName,
                                       final boolean createIfNotExists,
                                       final int maxBufferSize,
                                       final int maxPointsPerTitle,
                                       final long cacheSizeInBytes) {

      return new BerkeleyDBOctree(cloudDirectory, cloudName, createIfNotExists, maxBufferSize, maxPointsPerTitle, false,
               cacheSizeInBytes);
   }


   public static PersistentOctree openReadOnly(final File cloudDirectory,
                                               final String cloudName,
                                               final long cacheSizeInBytes) {
      return new BerkeleyDBOctree(cloudDirectory, cloudName, false, 0, 0, true, cacheSizeInBytes);
   }


   private final String           _cloudName;

   private final List<Geodetic3D> _buffer;
   private final int              _maxBufferSize;
   private final int              _maxPointsPerTitle;
   private double                 _minLatitudeInRadians;
   private double                 _minLongitudeInRadians;
   private double                 _minHeight;
   private double                 _maxLatitudeInRadians;
   private double                 _maxLongitudeInRadians;
   private double                 _maxHeight;
   private double                 _sumLatitudeInRadians;
   private double                 _sumLongitudeInRadians;
   private double                 _sumHeight;

   private final Environment      _env;
   private final Database         _nodeDB;
   private final Database         _nodeDataDB;
   private final boolean          _readOnly;
   private final File             _cachedStatisticsFile;


   private BerkeleyDBOctree(final File cloudDirectory,
                            final String cloudName,
                            final boolean createIfNotExists,
                            final int maxBufferSize,
                            final int maxPointsPerTitle,
                            final boolean readOnly,
                            final long cacheSizeInBytes) {
      final File envHome = new File(cloudDirectory, cloudName);
      if (createIfNotExists) {
         if (!envHome.exists()) {
            envHome.mkdirs();
         }
      }

      final String readOnlyMsg = readOnly ? " read-only" : " read-write";
      System.out.println("- opening" + readOnlyMsg + " cloud name \"" + cloudName + "\" (" + envHome.getAbsoluteFile() + ")");

      _readOnly = readOnly;
      _cloudName = cloudName;

      _maxBufferSize = maxBufferSize;
      _buffer = new ArrayList<>(maxBufferSize);
      resetBufferBounds();

      _maxPointsPerTitle = maxPointsPerTitle;


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
      //      dbConfig.setSortedDuplicates(true);

      _nodeDB = _env.openDatabase(null, NODE_DATABASE_NAME, dbConfig);
      _nodeDataDB = _env.openDatabase(null, NODE_DATA_DATABASE_NAME, dbConfig);

      _cachedStatisticsFile = new File(cloudDirectory, "_stats_" + cloudName + ".ser");
   }


   int getMaxPointsPerTile() {
      return _maxPointsPerTitle;
   }


   @Override
   synchronized public void close() {
      flush();
      _nodeDataDB.close();
      _nodeDB.close();
      _env.close();
   }


   private void resetBufferBounds() {
      _minLatitudeInRadians = Double.POSITIVE_INFINITY;
      _minLongitudeInRadians = Double.POSITIVE_INFINITY;
      _minHeight = Double.POSITIVE_INFINITY;

      _maxLatitudeInRadians = Double.NEGATIVE_INFINITY;
      _maxLongitudeInRadians = Double.NEGATIVE_INFINITY;
      _maxHeight = Double.NEGATIVE_INFINITY;

      _sumLatitudeInRadians = 0.0;
      _sumLongitudeInRadians = 0.0;
      _sumHeight = 0.0;
   }


   @Override
   synchronized public void addPoint(final Geodetic3D point) {
      if (_readOnly) {
         throw new RuntimeException("Can't add points to readonly OT");
      }

      _buffer.add(point);

      final double latitudeInRadians = point._latitude._radians;
      final double longitudeInRadians = point._longitude._radians;
      final double height = point._height;

      _sumLatitudeInRadians += latitudeInRadians;
      _sumLongitudeInRadians += longitudeInRadians;
      _sumHeight += height;


      if (latitudeInRadians < _minLatitudeInRadians) {
         _minLatitudeInRadians = latitudeInRadians;
      }
      if (latitudeInRadians > _maxLatitudeInRadians) {
         _maxLatitudeInRadians = latitudeInRadians;
      }

      if (longitudeInRadians < _minLongitudeInRadians) {
         _minLongitudeInRadians = longitudeInRadians;
      }
      if (longitudeInRadians > _maxLongitudeInRadians) {
         _maxLongitudeInRadians = longitudeInRadians;
      }

      if (height < _minHeight) {
         _minHeight = height;
      }
      if (height > _maxHeight) {
         _maxHeight = height;
      }


      if (_buffer.size() == _maxBufferSize) {
         flush();
      }
   }


   @Override
   synchronized public void flush() {
      final int bufferSize = _buffer.size();
      if (bufferSize > 0) {
         deleteCachedStatistics();

         final Sector targetSector = Sector.fromRadians( //
                  _minLatitudeInRadians, _minLongitudeInRadians, //
                  _maxLatitudeInRadians, _maxLongitudeInRadians);

         final Sector boundsSector = Sector.getBounds(_buffer);
         if (!targetSector.equals(boundsSector)) {
            throw new RuntimeException("LOGIC ERROR");
         }


         final TileHeader header = TileHeader.deepestEnclosingTileHeader(targetSector);
         //final TileHeader header = TileHeader.ROOT_TILE_HEADER;

         //         for (final Geodetic3D point : _buffer) {
         //            if (!targetSector.contains(point._latitude, point._longitude)) {
         //               throw new RuntimeException("Logic Error");
         //            }
         //            if (!header._sector.contains(point._latitude, point._longitude)) {
         //               throw new RuntimeException("Logic Error");
         //            }
         //         }

         final double averageLatitudeInRadians = _sumLatitudeInRadians / bufferSize;
         final double averageLongitudeInRadians = _sumLongitudeInRadians / bufferSize;
         final double averageHeight = _sumHeight / bufferSize;

         final Geodetic3D averagePoint = Geodetic3D.fromRadians(averageLatitudeInRadians, averageLongitudeInRadians,
                  averageHeight);


         final TransactionConfig txnConfig = new TransactionConfig();
         final Transaction txn = _env.beginTransaction(null, txnConfig);

         final PointsSet pointsSet = new PointsSet(new ArrayList<Geodetic3D>(_buffer), averagePoint);
         BerkeleyDBOctreeNode.insertPoints(txn, this, header, pointsSet);

         txn.commit();

         _buffer.clear();
         resetBufferBounds();
      }
   }


   @Override
   synchronized public void optimize() {
      // LOGGER.logInfo("Optimizing...");
      // final long start = System.currentTimeMillis();

      _env.compress();
      _env.cleanLog();

      // final long elapsed = System.currentTimeMillis() - start;
      //LOGGER.logInfo("Optimized in " + elapsed + "ms");
   }


   @Override
   public void acceptDepthFirstVisitor(final PersistentOctree.Visitor visitor) {
      visitor.start();

      final CursorConfig config = new CursorConfig();
      config.setReadUncommitted(false);

      try (final Cursor cursor = _nodeDB.openCursor(null, config)) {
         final DatabaseEntry keyEntry = new DatabaseEntry();
         final DatabaseEntry dataEntry = new DatabaseEntry();

         while (cursor.getNext(keyEntry, dataEntry, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
            final byte[] key = keyEntry.getData();
            final byte[] data = dataEntry.getData();

            final BerkeleyDBOctreeNode tile = BerkeleyDBOctreeNode.fromDB(null, this, key, data, false);
            final boolean keepGoing = visitor.visit(tile);
            if (!keepGoing) {
               break;
            }
         }
      }

      visitor.stop();
   }

   private static enum CursorSituation {
      NotFoundSelfNorDescendants,
      FoundDescendants,
      FoundSelf,
      FoundNothing;
   }


   private static CursorSituation getCursorSituation(final Cursor cursor,
                                                     final DatabaseEntry keyEntry,
                                                     final DatabaseEntry dataEntry,
                                                     final byte[] id) {
      final OperationStatus status = cursor.getSearchKeyRange(keyEntry, dataEntry, LockMode.READ_UNCOMMITTED);
      switch (status) {
         case SUCCESS: {
            final byte[] key = keyEntry.getData();

            if (!Utils.hasSamePrefix(key, id)) {
               return CursorSituation.NotFoundSelfNorDescendants;
            }
            else if (Utils.isGreaterThan(key, id)) {
               return CursorSituation.FoundDescendants;
            }
            else {
               return CursorSituation.FoundSelf;
            }
         }
         case NOTFOUND: {
            return CursorSituation.FoundNothing;
         }
         default:
            throw new RuntimeException("Status not supported: " + status);
      }
   }


   private void visitParent(final com.sleepycat.je.Transaction txn,
                            final Cursor cursor,
                            final DatabaseEntry keyEntry,
                            final DatabaseEntry dataEntry,
                            final byte[] id,
                            final PersistentOctree.Visitor visitor) {
      if (cursor.getPrev(keyEntry, dataEntry, LockMode.READ_UNCOMMITTED) == OperationStatus.SUCCESS) {
         final byte[] key = keyEntry.getData();
         if (Utils.hasSamePrefix(id, key)) {
            final BerkeleyDBOctreeNode node = BerkeleyDBOctreeNode.fromDB(txn, this, key, dataEntry.getData(), false);
            visitor.visit(node);
         }
      }
   }


   private void visitDescendants(final com.sleepycat.je.Transaction txn,
                                 final Cursor cursor,
                                 final DatabaseEntry keyEntry,
                                 final DatabaseEntry dataEntry,
                                 final byte[] id,
                                 final PersistentOctree.Visitor visitor) {

      byte[] key = keyEntry.getData();

      BerkeleyDBOctreeNode node = BerkeleyDBOctreeNode.fromDB(txn, this, key, dataEntry.getData(), false);
      if (!visitor.visit(node)) {
         return;
      }

      while (cursor.getNext(keyEntry, dataEntry, LockMode.READ_UNCOMMITTED) == OperationStatus.SUCCESS) {
         key = keyEntry.getData();
         if (!Utils.hasSamePrefix(key, id)) {
            break;
         }
         node = BerkeleyDBOctreeNode.fromDB(txn, this, key, dataEntry.getData(), false);
         if (!visitor.visit(node)) {
            return;
         }
      }
   }


   @Override
   public void acceptDepthFirstVisitor(final Sector sector,
                                       final PersistentOctree.Visitor visitor) {
      visitor.start();

      final TileHeader header = TileHeader.deepestEnclosingTileHeader(sector);

      //System.out.println("==> " + Utils.toIDString(header._id) + "   " + header._sector);

      final byte[] id = header._id;


      final CursorConfig cursorConfig = new CursorConfig();

      final com.sleepycat.je.Transaction txn = null;
      try (final Cursor cursor = _nodeDB.openCursor(txn, cursorConfig)) {
         final DatabaseEntry keyEntry = new DatabaseEntry(id);
         final DatabaseEntry dataEntry = new DatabaseEntry();

         final CursorSituation situation = getCursorSituation(cursor, keyEntry, dataEntry, id);
         switch (situation) {
            case NotFoundSelfNorDescendants: {
               visitParent(txn, cursor, keyEntry, dataEntry, id, visitor);
               break;
            }
            case FoundDescendants: {
               visitDescendants(txn, cursor, keyEntry, dataEntry, id, visitor);
               break;
            }
            case FoundSelf: {
               final BerkeleyDBOctreeNode node = BerkeleyDBOctreeNode.fromDB(txn, this, id, dataEntry.getData(), false);
               visitor.visit(node);
               break;
            }
            case FoundNothing: {
               break;
            }
            default:
               throw new RuntimeException("Invalid situation: " + situation);
         }
      }

      visitor.stop();
   }


   Database getNodeDataDB() {
      return _nodeDataDB;
   }


   Database getNodeDB() {
      return _nodeDB;
   }


   Environment getEnvironment() {
      return _env;
   }


   BerkeleyDBOctreeNode readNode(final Transaction txn,
                                 final byte[] id,
                                 final boolean loadPoints) {
      final DatabaseEntry keyEntry = new DatabaseEntry(id);
      final DatabaseEntry dataEntry = new DatabaseEntry();

      final OperationStatus status = _nodeDB.get(txn, keyEntry, dataEntry, LockMode.DEFAULT);
      switch (status) {
         case SUCCESS:
            return BerkeleyDBOctreeNode.fromDB(txn, this, id, dataEntry.getData(), loadPoints);
         case NOTFOUND:
            return null;
         default:
            throw new RuntimeException("Status not supported: " + status);
      }
   }


   private static class BerkeleyDBStatistics
   implements
   PersistentOctree.Visitor,
   PersistentOctree.Statistics,
   Serializable {

      private static final long      serialVersionUID = 2L;

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


      private BerkeleyDBStatistics(final String cloudName,
                                   final GUndeterminateProgress progress) {
         _cloudName = cloudName;
         _progress = progress;
      }


      @Override
      public void start() {
         _nodesCount = 0;
         _pointsCount = 0;
         _minPointsCountPerNode = Integer.MAX_VALUE;
         _maxPointsCountPerNode = Integer.MIN_VALUE;
         _sumDepth = 0;
         _minDepth = Integer.MAX_VALUE;
         _maxDepth = Integer.MIN_VALUE;
      }


      @Override
      public boolean visit(final PersistentOctree.Node node) {
         final Sector nodeSector = node.getSector();
         _sector = (_sector == null) ? nodeSector : _sector.mergedWith(nodeSector);

         if (_progress != null) {
            _progress.stepDone();
         }

         for (final Geodetic3D point : node.getPoints()) {
            final double height = point._height;
            if (height < _minHeight) {
               _minHeight = height;
            }
            if (height > _maxHeight) {
               _maxHeight = height;
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
      public void stop() {
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
         System.out.println("   Points/Node: Average=" + ((float) getAveragePointsPerNode()) + //
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
      public double getAveragePointsPerNode() {
         return (double) _pointsCount / _nodesCount;
      }

   }


   private void deleteCachedStatistics() {
      if (_cachedStatisticsFile.exists()) {
         _cachedStatisticsFile.delete();
      }
   }


   private BerkeleyDBStatistics getCachedStatistics() {
      if (!_cachedStatisticsFile.exists()) {
         return null;
      }

      try (final ObjectInputStream in = new ObjectInputStream(new FileInputStream(_cachedStatisticsFile))) {
         return (BerkeleyDBStatistics) in.readObject();
      }
      catch (final ClassNotFoundException e) {
         throw new RuntimeException(e);
      }
      catch (final IOException e) {
         throw new RuntimeException(e);
      }

   }


   private void saveCachedStatistics(final BerkeleyDBStatistics statistics) {
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
   public PersistentOctree.Statistics getStatistics(final boolean showProgress) {

      final BerkeleyDBStatistics cachedStatistics = getCachedStatistics();
      if (cachedStatistics != null) {
         return cachedStatistics;
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

      final BerkeleyDBStatistics statistics = new BerkeleyDBStatistics(_cloudName, progress);
      acceptDepthFirstVisitor(statistics);
      saveCachedStatistics(statistics);
      return statistics;
   }


   @Override
   public String getCloudName() {
      return _cloudName;
   }


}
