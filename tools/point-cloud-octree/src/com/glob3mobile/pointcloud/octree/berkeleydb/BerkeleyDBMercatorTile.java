

package com.glob3mobile.pointcloud.octree.berkeleydb;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.glob3.mobile.generated.Angle;
import org.glob3.mobile.generated.Geodetic2D;
import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.Sector;

import com.glob3mobile.pointcloud.octree.PersistentOctree;
import com.glob3mobile.pointcloud.octree.Utils;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.Environment;
import com.sleepycat.je.Transaction;
import com.sleepycat.je.TransactionConfig;


public class BerkeleyDBMercatorTile
implements
PersistentOctree.Node {


   private static final byte[]                 ROOT_ID   = {};
   private static final BerkeleyDBMercatorTile ROOT_TILE = new BerkeleyDBMercatorTile(ROOT_ID, Sector.FULL_SPHERE);


   static BerkeleyDBMercatorTile createDeepestEnclosingTile(final Sector targetSector,
                                                            final Geodetic3D averagePoint,
                                                            final List<Geodetic3D> buffer) {
      final BerkeleyDBMercatorTile tile = deepestEnclosingTile(targetSector);

      final int bufferSize = buffer.size();
      final double averageLatitudeInRadians = averagePoint._latitude._radians;
      final double averageLongitudeInRadians = averagePoint._longitude._radians;
      final double averageHeight = averagePoint._height;

      final float[] values = new float[bufferSize * 3];
      int i = 0;
      for (final Geodetic3D point : buffer) {
         final float deltaLatitudeInRadians = (float) (point._latitude._radians - averageLatitudeInRadians);
         final float deltaLongitudeInRadians = (float) (point._longitude._radians - averageLongitudeInRadians);
         final float deltaHeight = (float) (point._height - averageHeight);

         values[i++] = deltaLatitudeInRadians;
         values[i++] = deltaLongitudeInRadians;
         values[i++] = deltaHeight;
      }

      tile.setValues(averagePoint, values);

      return tile;
   }


   private static BerkeleyDBMercatorTile deepestEnclosingTile(final Sector targetSector) {
      return deepestEnclosingTile(ROOT_TILE, targetSector);
   }


   private static BerkeleyDBMercatorTile deepestEnclosingTile(final BerkeleyDBMercatorTile candidate,
                                                              final Sector targetSector) {
      final BerkeleyDBMercatorTile[] children = candidate.createChildren();
      for (final BerkeleyDBMercatorTile child : children) {
         if (child._sector.fullContains(targetSector)) {
            return deepestEnclosingTile(child, targetSector);
         }
      }
      return candidate;
   }


   private static double _upperLimitInDegrees = 85.0511287798;
   private static double _lowerLimitInDegrees = -85.0511287798;


   private static double getMercatorV(final Angle latitude) {
      if (latitude._degrees >= _upperLimitInDegrees) {
         return 0;
      }
      if (latitude._degrees <= _lowerLimitInDegrees) {
         return 1;
      }

      final double pi4 = Math.PI * 4;

      final double latSin = Math.sin(latitude._radians);
      return 1.0 - ((Math.log((1.0 + latSin) / (1.0 - latSin)) / pi4) + 0.5);
   }


   private static Angle toLatitude(final double v) {
      final double exp = Math.exp(-2 * Math.PI * (1.0 - v - 0.5));
      final double atan = Math.atan(exp);
      return Angle.fromRadians((Math.PI / 2) - (2 * atan));
   }


   private static Angle calculateSplitLatitude(final Angle lowerLatitude,
                                               final Angle upperLatitude) {
      final double middleV = (getMercatorV(lowerLatitude) + getMercatorV(upperLatitude)) / 2;

      return toLatitude(middleV);
   }


   private final byte[]           _id;
   private final Sector           _sector;
   private Geodetic3D             _averagePoint = null;
   private float[]                _values       = null;
   private final List<Geodetic3D> _points;


   private BerkeleyDBMercatorTile[] createChildren() {
      final Geodetic2D lower = _sector._lower;
      final Geodetic2D upper = _sector._upper;

      final Angle splitLongitude = Angle.midAngle(lower._longitude, upper._longitude);
      final Angle splitLatitude = calculateSplitLatitude(lower._latitude, upper._latitude);

      final Sector s0 = new Sector( //
               new Geodetic2D(splitLatitude, lower._longitude), //
               new Geodetic2D(upper._latitude, splitLongitude));

      final Sector s1 = new Sector( //
               new Geodetic2D(splitLatitude, splitLongitude), //
               new Geodetic2D(upper._latitude, upper._longitude));

      final Sector s2 = new Sector( //
               new Geodetic2D(lower._latitude, lower._longitude), //
               new Geodetic2D(splitLatitude, splitLongitude));

      final Sector s3 = new Sector( //
               new Geodetic2D(lower._latitude, splitLongitude), //
               new Geodetic2D(splitLatitude, upper._longitude));


      final BerkeleyDBMercatorTile child0 = createChild((byte) 0, s0);
      final BerkeleyDBMercatorTile child1 = createChild((byte) 1, s1);
      final BerkeleyDBMercatorTile child2 = createChild((byte) 2, s2);
      final BerkeleyDBMercatorTile child3 = createChild((byte) 3, s3);
      return new BerkeleyDBMercatorTile[] { child0, child1, child2, child3 };
   }


   private BerkeleyDBMercatorTile createChild(final byte index,
                                              final Sector sector) {
      final int length = getLevel();
      final byte[] childId = new byte[length + 1];
      System.arraycopy(_id, 0, childId, 0, length);
      childId[length] = index;
      return new BerkeleyDBMercatorTile(childId, sector);
   }


   private BerkeleyDBMercatorTile(final byte[] id,
                                  final Sector sector) {
      _id = id;
      _sector = sector;
      _points = null;
   }


   public BerkeleyDBMercatorTile(final byte[] id,
                                 final Sector sector,
                                 final List<Geodetic3D> points) {
      _id = id;
      _sector = sector;
      _points = Collections.unmodifiableList(points);
   }


   private void setValues(final Geodetic3D averagePoint,
                          final float[] values) {
      if (_values != null) {
         throw new RuntimeException("values already set");
      }
      _averagePoint = averagePoint;
      _values = values;
   }


   int getLevel() {
      return _id.length;
   }


   byte[] getIDBinary() {
      return Arrays.copyOf(_id, _id.length);
   }


   @Override
   public String getID() {
      final StringBuilder builder = new StringBuilder();
      for (final byte each : _id) {
         builder.append(each);
      }
      return builder.toString();
   }


   Sector getSector() {
      return _sector;
   }


   @Override
   public String toString() {
      return "MercatorTile [id=" + getID() + ", sector=" + Utils.toString(_sector) + ", level=" + getLevel() + "]";
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


      private static Format getFromID(final byte formatID) {
         for (final Format each : Format.values()) {
            if (each._formatID == formatID) {
               return each;
            }
         }
         throw new RuntimeException("Invalid FormatID=" + formatID);
      }
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


   private byte[] createDataEntry() {

      final Format format = Format.LatLonHeight;

      final byte version = 1;
      final byte subversion = 0;

      final Sector sector = getSector();
      final double lowerLatitude = sector._lower._latitude._radians;
      final double lowerLongitude = sector._lower._longitude._radians;
      final double upperLatitude = sector._upper._latitude._radians;
      final double upperLongitude = sector._upper._longitude._radians;

      final int pointsCount = _values.length / format._floatsPerPoint;

      final double averageLatitude = _averagePoint._latitude._radians;
      final double averageLongitude = _averagePoint._longitude._radians;
      final double averageHeight = _averagePoint._height;


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
                            format.sizeOf(_values);


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
      for (final float value : _values) {
         byteBuffer.putFloat(value);
      }

      final byte[] array = byteBuffer.array();
      //      LOGGER.logInfo("Generated buffer of " + array.length + " bytes");
      return array;
   }


   void save(final Environment env,
             final Database nodeDB) {
      final DatabaseEntry keyEntry = new DatabaseEntry(getIDBinary());
      final DatabaseEntry dataEntry = new DatabaseEntry(createDataEntry());

      final TransactionConfig tnxConfig = new TransactionConfig();
      final Transaction txn = env.beginTransaction(null, tnxConfig);
      nodeDB.put(txn, keyEntry, dataEntry);
      txn.commit();
   }


   static BerkeleyDBMercatorTile fromDB(final byte[] id,
                                        final byte[] data) {
      final ByteBuffer byteBuffer = ByteBuffer.wrap(data);


      final byte version = byteBuffer.get();
      if (version != 1) {
         throw new RuntimeException("Invalid version=" + version);
      }
      final byte subversion = byteBuffer.get();
      if (subversion != 0) {
         throw new RuntimeException("Invalid subversion=" + subversion);
      }

      final double lowerLatitude = byteBuffer.getDouble();
      final double lowerLongitude = byteBuffer.getDouble();
      final double upperLatitude = byteBuffer.getDouble();
      final double upperLongitude = byteBuffer.getDouble();
      final Sector sector = new Sector( //
               Geodetic2D.fromRadians(lowerLatitude, lowerLongitude), //
               Geodetic2D.fromRadians(upperLatitude, upperLongitude));

      final int pointsCount = byteBuffer.getInt();
      final double averageLatitude = byteBuffer.getDouble();
      final double averageLongitude = byteBuffer.getDouble();
      final double averageHeight = byteBuffer.getDouble();
      final byte formatID = byteBuffer.get();
      final Format format = Format.getFromID(formatID);

      switch (format) {
         case LatLonHeight:
            final List<Geodetic3D> points = new ArrayList<Geodetic3D>(pointsCount);

            for (int i = 0; i < pointsCount; i++) {
               final double lat = byteBuffer.getFloat() + averageLatitude;
               final double lon = byteBuffer.getFloat() + averageLongitude;
               final double height = byteBuffer.getFloat() + averageHeight;

               final Geodetic3D point = new Geodetic3D( //
                        Angle.fromRadians(lat), //
                        Angle.fromRadians(lon), //
                        height);
               points.add(point);
            }

            return new BerkeleyDBMercatorTile(id, sector, points);

         default:
            throw new RuntimeException("Unsupported format: " + format);
      }

   }


   @Override
   public List<Geodetic3D> getPoints() {
      return _points;
   }

}
