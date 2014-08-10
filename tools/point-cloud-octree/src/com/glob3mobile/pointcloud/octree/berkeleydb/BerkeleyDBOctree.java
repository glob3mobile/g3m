

package com.glob3mobile.pointcloud.octree.berkeleydb;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.Sector;

import com.glob3mobile.pointcloud.octree.PersistentOctree;
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
import com.sleepycat.je.Transaction;
import com.sleepycat.je.TransactionConfig;

import es.igosoftware.io.GIOUtils;


public class BerkeleyDBOctree
implements
PersistentOctree {

   // private static final ILogger LOGGER              = GLogger.instance();
   // private static final Charset UTF8                = Charset.forName("UTF-8");

   private static final int    DEFAULT_BUFFER_SIZE          = 1024 * 8;
   private static final int    DEFAULT_MAX_POINTS_PER_TITLE = 1024 * 8;
   private static final String NODE_DATABASE_NAME           = "Node";
   private static final String NODE_DATA_DATABASE_NAME      = "NodeData";


   public static void delete(final String cloudName) {
      final File envHome = new File(cloudName);
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


   public static PersistentOctree open(final String cloudName,
                                       final boolean createIfNotExists) {
      return open(cloudName, createIfNotExists, DEFAULT_BUFFER_SIZE, DEFAULT_MAX_POINTS_PER_TITLE);
   }


   public static PersistentOctree open(final String cloudName,
                                       final boolean createIfNotExists,
                                       final int bufferSize,
                                       final int maxPointsPerTitle) {
      return new BerkeleyDBOctree(cloudName, createIfNotExists, bufferSize, maxPointsPerTitle);
   }


   private final List<Geodetic3D> _buffer;
   private final int              _bufferSize;
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


   private BerkeleyDBOctree(final String cloudName,
                            final boolean createIfNotExists,
                            final int bufferSize,
                            final int maxPointsPerTitle) {

      _bufferSize = bufferSize;
      _buffer = new ArrayList<>(bufferSize);
      resetBufferBounds();

      _maxPointsPerTitle = maxPointsPerTitle;

      final File envHome = new File(cloudName);
      if (createIfNotExists) {
         if (!envHome.exists()) {
            envHome.mkdirs();
         }
      }

      final EnvironmentConfig envConfig = new EnvironmentConfig();
      envConfig.setAllowCreate(createIfNotExists);
      envConfig.setTransactional(true);
      _env = new Environment(envHome, envConfig);

      final DatabaseConfig dbConfig = new DatabaseConfig();
      dbConfig.setAllowCreate(createIfNotExists);
      dbConfig.setTransactionalVoid(true);
      dbConfig.setKeyPrefixing(true);
      //      dbConfig.setSortedDuplicates(true);

      _nodeDB = _env.openDatabase(null, NODE_DATABASE_NAME, dbConfig);
      _nodeDataDB = _env.openDatabase(null, NODE_DATA_DATABASE_NAME, dbConfig);
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


      if (_buffer.size() == _bufferSize) {
         flush();
      }
   }


   @Override
   synchronized public void flush() {
      final int bufferSize = _buffer.size();
      if (bufferSize > 0) {
         final Geodetic3D lower = Utils.fromRadians(_minLatitudeInRadians, _minLongitudeInRadians, _minHeight);
         final Geodetic3D upper = Utils.fromRadians(_maxLatitudeInRadians, _maxLongitudeInRadians, _maxHeight);

         final Sector targetSector = new Sector(lower.asGeodetic2D(), upper.asGeodetic2D());

         final double averageLatitudeInRadians = _sumLatitudeInRadians / bufferSize;
         final double averageLongitudeInRadians = _sumLongitudeInRadians / bufferSize;
         final double averageHeight = _sumHeight / bufferSize;

         final Geodetic3D averagePoint = Utils.fromRadians(averageLatitudeInRadians, averageLongitudeInRadians, averageHeight);

         final BerkeleyDBMercatorTile.TileHeader header = BerkeleyDBMercatorTile.deepestEnclosingTileHeader(targetSector);

         final TransactionConfig txnConfig = new TransactionConfig();
         final Transaction txn = _env.beginTransaction(null, txnConfig);


         final PointsSet pointsSet = new PointsSet(new ArrayList<Geodetic3D>(_buffer), averagePoint);
         BerkeleyDBMercatorTile.insertPoints(txn, this, header, pointsSet);

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
   public void acceptVisitor(final PersistentOctree.Visitor visitor) {
      visitor.start();

      final CursorConfig config = new CursorConfig();
      config.setReadUncommitted(false);

      try (final Cursor cursor = _nodeDB.openCursor(null, config)) {
         final DatabaseEntry keyEntry = new DatabaseEntry();
         final DatabaseEntry dataEntry = new DatabaseEntry();

         while (cursor.getNext(keyEntry, dataEntry, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
            final byte[] key = keyEntry.getData();
            final byte[] data = dataEntry.getData();

            final BerkeleyDBMercatorTile tile = BerkeleyDBMercatorTile.fromDB(null, this, key, data, false);
            final boolean keepGoing = visitor.visit(tile);
            if (!keepGoing) {
               break;
            }
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


   BerkeleyDBMercatorTile readTile(final Transaction txn,
                                   final byte[] id,
                                   final boolean loadPoints) {
      final DatabaseEntry keyEntry = new DatabaseEntry(id);
      final DatabaseEntry dataEntry = new DatabaseEntry();

      final OperationStatus status = _nodeDB.get(txn, keyEntry, dataEntry, LockMode.DEFAULT);
      if (status == OperationStatus.SUCCESS) {
         return BerkeleyDBMercatorTile.fromDB(txn, this, id, dataEntry.getData(), loadPoints);
      }
      return null;
   }


}
