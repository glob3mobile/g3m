

package com.glob3mobile.pointcloud.octree.berkeleydb;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.Sector;

import com.glob3mobile.pointcloud.octree.MercatorTile;
import com.glob3mobile.pointcloud.octree.PersistentOctree;
import com.glob3mobile.pointcloud.octree.Utils;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.Transaction;

import es.igosoftware.io.GIOUtils;
import es.igosoftware.logging.GLogger;
import es.igosoftware.logging.ILogger;


public class BerkeleyDBOctree
implements
PersistentOctree {

   private static final ILogger LOGGER              = GLogger.instance();


   private static final int     DEFAULT_BUFFER_SIZE = 1024 * 64;
   private static final String  NODE_DATABASE_NAME  = "Node";


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

      //      final EnvironmentConfig envConfig = new EnvironmentConfig();
      //      try (final Environment env = new Environment(envHome, envConfig)) {
      //         for (final String dbName : env.getDatabaseNames()) {
      //            LOGGER.logInfo("Removing database \"" + dbName + "\"...");
      //            env.removeDatabase(null, dbName);
      //         }
      //      }
   }


   public static PersistentOctree open(final String cloudName,
                                       final boolean createIfNotExists,
                                       final boolean compress) {
      return open(cloudName, createIfNotExists, DEFAULT_BUFFER_SIZE, compress);
   }


   private static PersistentOctree open(final String cloudName,
                                        final boolean createIfNotExists,
                                        final int bufferSize,
                                        final boolean compress) {
      return new BerkeleyDBOctree(cloudName, createIfNotExists, bufferSize, compress);
   }


   private final boolean          _compress;

   private final List<Geodetic3D> _buffer;
   private final int              _bufferSize;
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


   private BerkeleyDBOctree(final String cloudName,
                            final boolean createIfNotExists,
                            final int bufferSize,
                            final boolean compress) {
      _compress = compress;

      _bufferSize = bufferSize;
      _buffer = new ArrayList<>(bufferSize);
      resetBufferBounds();

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
      dbConfig.setDeferredWrite(true);
      //      dbConfig.setTransactionalVoid(true);

      //      final Comparator<byte[]> btreeComparator;
      //      dbConfig.setBtreeComparator(btreeComparator);

      //      Comparator<byte[]> duplicateComparator;
      //      dbConfig.setDuplicateComparator(duplicateComparator);


      dbConfig.setKeyPrefixing(true);
      dbConfig.setSortedDuplicates(true);

      _nodeDB = _env.openDatabase(null, NODE_DATABASE_NAME, dbConfig);
   }


   @Override
   synchronized public void close() {
      flush();

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

         final MercatorTile tile = MercatorTile.deepestEnclosingTile(targetSector);

         //         final String tileID = tile.getIDString();

         final double averageLatitudeInRadians = _sumLatitudeInRadians / bufferSize;
         final double averageLongitudeInRadians = _sumLongitudeInRadians / bufferSize;
         final double averageHeight = _sumHeight / bufferSize;

         final Geodetic3D averagePoint = Utils.fromRadians(averageLatitudeInRadians, averageLongitudeInRadians, averageHeight);

         //         LOGGER.logInfo("Flushing buffer of " + bufferSize + //
         //                  // ", average=" + Utils.toString(averagePoint) + //
         //                  // ", bounds=(" + Utils.toString(lowerPoint) + " / " + Utils.toString(upperPoint) + ")" + //
         //                  " into tile=" + tile.getIDString() + " level=" + tile.getLevel() + "...");


         final float[] values = new float[bufferSize * 3];
         int i = 0;
         for (final Geodetic3D point : _buffer) {
            final float deltaLatitudeInRadians = (float) (point._latitude._radians - averageLatitudeInRadians);
            final float deltaLongitudeInRadians = (float) (point._longitude._radians - averageLongitudeInRadians);
            final float deltaHeight = (float) (point._height - averageHeight);

            values[i++] = deltaLatitudeInRadians;
            values[i++] = deltaLongitudeInRadians;
            values[i++] = deltaHeight;
         }


         _buffer.clear();
         resetBufferBounds();

         saveNode(tile, averagePoint, values);
      }
   }


   private static enum Format {
      LatLonHeight((byte) 1, 3);

      private final byte _formatID;
      private final int  _floatsPerPoint;


      Format(final byte formatID,
               final int floatsPerPoint) {
         _formatID = formatID;
         _floatsPerPoint = floatsPerPoint;
      }


      private int sizeOf(final float[] values) {
         return values.length * 4;
      }
   }


   private static byte[] createDataEntry(final MercatorTile tile,
                                         final Geodetic3D averagePoint,
                                         final float[] values,
                                         final boolean compress) {

      final Format format = Format.LatLonHeight;

      final byte version = 1;
      final byte subversion = 0;

      final Sector sector = tile.getSector();
      final double lowerLatitude = sector._lower._latitude._radians;
      final double lowerLongitude = sector._lower._longitude._radians;
      final double upperLatitude = sector._upper._latitude._radians;
      final double upperLongitude = sector._upper._longitude._radians;

      final int pointsCount = values.length / format._floatsPerPoint;

      final double averageLatitude = averagePoint._latitude._radians;
      final double averageLongitude = averagePoint._longitude._radians;
      final double averageHeight = averagePoint._height;


      final byte formatID = format._formatID;

      final int entrySize = sizeOf(version) + //
               sizeOf(subversion) + //
               sizeOf(lowerLatitude) + //
               sizeOf(lowerLongitude) + //
               sizeOf(upperLatitude) + //
               sizeOf(upperLongitude) + //
               sizeOf(pointsCount) + //
               sizeOf(averageLatitude) + //
               sizeOf(averageLongitude) + //
               sizeOf(averageHeight) + //
               sizeOf(formatID) + //
               format.sizeOf(values);


      final ByteBuffer byteBuffer = ByteBuffer.allocate(entrySize);
      byteBuffer.put(version);
      byteBuffer.put(subversion);
      byteBuffer.putDouble(lowerLatitude);
      byteBuffer.putDouble(lowerLongitude);
      byteBuffer.putDouble(upperLatitude);
      byteBuffer.putDouble(upperLongitude);
      byteBuffer.putInt(pointsCount);
      byteBuffer.putDouble(averageLatitude);
      byteBuffer.putDouble(averageLongitude);
      byteBuffer.putDouble(averageHeight);
      byteBuffer.put(formatID);
      for (final float value : values) {
         byteBuffer.putFloat(value);
      }

      final byte[] array = compress ? GIOUtils.compress(byteBuffer.array()) : byteBuffer.array();
      //      LOGGER.logInfo("Generated buffer of " + array.length + " bytes");
      return array;
   }


   @SuppressWarnings("unused")
   private static int sizeOf(final double any) {
      return 8;
   }


   @SuppressWarnings("unused")
   private static int sizeOf(final int any) {
      return 4;
   }


   @SuppressWarnings("unused")
   private static int sizeOf(final byte any) {
      return 1;
   }


   private void saveNode(final MercatorTile tile,
                         final Geodetic3D averagePoint,
                         final float[] values) {

      try {
         final String key = tile.getIDString();
         final DatabaseEntry keyEntry = new DatabaseEntry(key.getBytes("UTF-8"));

         // final String data = "test";
         // final DatabaseEntry dataEntry = new DatabaseEntry(data.getBytes("UTF-8"));
         final DatabaseEntry dataEntry = new DatabaseEntry(createDataEntry(tile, averagePoint, values, _compress));

         final Transaction txn = null;
         _nodeDB.put(txn, keyEntry, dataEntry);

         _nodeDB.sync();
      }
      catch (final UnsupportedEncodingException e) {
         throw new RuntimeException(e);
      }
   }


   @Override
   synchronized public void optimize() {
      LOGGER.logInfo("Optimizing...");
      final long start = System.currentTimeMillis();
      _env.compress();
      _env.cleanLog();

      final long elapsed = System.currentTimeMillis() - start;
      LOGGER.logInfo("Optimized in " + elapsed + "ms");
   }


}
