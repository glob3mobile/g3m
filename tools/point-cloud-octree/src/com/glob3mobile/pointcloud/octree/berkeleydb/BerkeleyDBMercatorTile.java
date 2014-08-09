

package com.glob3mobile.pointcloud.octree.berkeleydb;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.glob3.mobile.generated.Angle;
import org.glob3.mobile.generated.Geodetic2D;
import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.Sector;

import com.glob3mobile.pointcloud.octree.PersistentOctree;
import com.glob3mobile.pointcloud.octree.Utils;
import com.sleepycat.je.Cursor;
import com.sleepycat.je.CursorConfig;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;
import com.sleepycat.je.Transaction;


public class BerkeleyDBMercatorTile
implements
PersistentOctree.Node {

   private static final int MAX_POINTS_PER_TILE = 1024 * 64;


   private static enum Format {
      LatLonHeight((byte) 1);

      private final byte _formatID;


      Format(final byte formatID) {
         _formatID = formatID;
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


   static class TileHeader {
      private final byte[] _id;
      private final Sector _sector;


      private TileHeader(final byte[] id,
                         final Sector sector) {
         _id = id;
         _sector = sector;
      }


      private TileHeader[] createChildren() {
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


         final TileHeader child0 = createChild((byte) 0, s0);
         final TileHeader child1 = createChild((byte) 1, s1);
         final TileHeader child2 = createChild((byte) 2, s2);
         final TileHeader child3 = createChild((byte) 3, s3);
         return new TileHeader[] { child0, child1, child2, child3 };
      }


      private TileHeader createChild(final byte index,
                                     final Sector sector) {
         final int length = _id.length;
         final byte[] childId = new byte[length + 1];
         System.arraycopy(_id, 0, childId, 0, length);
         childId[length] = index;
         return new TileHeader(childId, sector);
      }

   }


   private static final byte[]     ROOT_ID          = {};
   private static final TileHeader ROOT_TILE_HEADER = new TileHeader(ROOT_ID, Sector.FULL_SPHERE);


   static TileHeader deepestEnclosingTileHeader(final Sector targetSector) {
      return deepestEnclosingTile(ROOT_TILE_HEADER, targetSector);
   }


   private static TileHeader deepestEnclosingTile(final TileHeader candidate,
                                                  final Sector targetSector) {
      final TileHeader[] children = candidate.createChildren();
      for (final TileHeader child : children) {
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


   private final BerkeleyDBOctree _octree;
   private final byte[]           _id;
   private final Sector           _sector;
   private Geodetic3D             _averagePoint;
   private final Format           _format;
   private int                    _pointsCount;
   private List<Geodetic3D>       _points = null;


   private BerkeleyDBMercatorTile(final BerkeleyDBOctree octree,
                                  final byte[] id,
                                  final Sector sector,
                                  final int pointsCount,
                                  final Geodetic3D averagePoint,
                                  final Format format) {
      _octree = octree;
      _id = id;
      _sector = sector;
      _pointsCount = pointsCount;
      _averagePoint = averagePoint;
      _format = format;
   }


   private BerkeleyDBMercatorTile(final BerkeleyDBOctree octree,
                                  final byte[] id,
                                  final Sector sector,
                                  final PointsSet pointsSet) {
      _octree = octree;
      _id = id;
      _sector = sector;
      _pointsCount = pointsSet.size();
      _points = pointsSet._points;
      _averagePoint = pointsSet._averagePoint;
      _format = null;
   }


   private int getLevel() {
      return _id.length;
   }


   byte[] getBerkeleyDBKey() {
      return Arrays.copyOf(_id, _id.length);
   }


   private static String toString(final byte[] id) {
      final StringBuilder builder = new StringBuilder();
      for (final byte each : id) {
         builder.append(each);
      }
      return builder.toString();
   }


   @Override
   public String getID() {
      return toString(_id);
   }


   @Override
   public Sector getSector() {
      return _sector;
   }


   @Override
   public String toString() {
      return "MercatorTile [id=" + getID() + //
               ", sector=" + Utils.toString(_sector) + //
               ", level=" + getLevel() + //
               ", points=" + _pointsCount + //
               "]";
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


   private byte[] createNodeEntry(final Format format) {
      final byte version = 1;
      final byte subversion = 0;

      final Sector sector = getSector();
      final double lowerLatitude = sector._lower._latitude._radians;
      final double lowerLongitude = sector._lower._longitude._radians;
      final double upperLatitude = sector._upper._latitude._radians;
      final double upperLongitude = sector._upper._longitude._radians;

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
               sizeOf(_pointsCount) + //
               sizeOf(averageLatitude) + //
               sizeOf(averageLongitude) + //
               sizeOf(averageHeight) + //
               sizeOf(formatID);

      final ByteBuffer byteBuffer = ByteBuffer.allocate(entrySize);
      byteBuffer.put(version);
      byteBuffer.put(subversion);
      byteBuffer.putDouble(lowerLatitude);
      byteBuffer.putDouble(lowerLongitude);
      byteBuffer.putDouble(upperLatitude);
      byteBuffer.putDouble(upperLongitude);
      byteBuffer.putInt(_pointsCount);
      byteBuffer.putDouble(averageLatitude);
      byteBuffer.putDouble(averageLongitude);
      byteBuffer.putDouble(averageHeight);
      byteBuffer.put(formatID);

      return byteBuffer.array();
   }


   private float[] createValues() {
      final int bufferSize = _points.size();
      final double averageLatitudeInRadians = _averagePoint._latitude._radians;
      final double averageLongitudeInRadians = _averagePoint._longitude._radians;
      final double averageHeight = _averagePoint._height;

      final float[] values = new float[bufferSize * 3];
      int i = 0;
      for (final Geodetic3D point : _points) {
         final float deltaLatitudeInRadians = (float) (point._latitude._radians - averageLatitudeInRadians);
         final float deltaLongitudeInRadians = (float) (point._longitude._radians - averageLongitudeInRadians);
         final float deltaHeight = (float) (point._height - averageHeight);

         values[i++] = deltaLatitudeInRadians;
         values[i++] = deltaLongitudeInRadians;
         values[i++] = deltaHeight;
      }

      return values;
   }


   private byte[] createNodeDataEntry(final Format format) {
      final float[] values = createValues();
      final int entrySize = format.sizeOf(values);

      final ByteBuffer byteBuffer = ByteBuffer.allocate(entrySize);
      for (final float value : values) {
         byteBuffer.putFloat(value);
      }

      return byteBuffer.array();
   }


   private static BerkeleyDBMercatorTile getAncestorOrSameLevel(final Transaction txn,
                                                                final BerkeleyDBOctree octree,
                                                                final byte[] id) {
      byte[] ancestorId = id;
      while (ancestorId != null) {
         final BerkeleyDBMercatorTile ancestor = octree.readTile(txn, ancestorId, true);
         if (ancestor != null) {
            return ancestor;
         }
         ancestorId = removeTrailing(ancestorId);
      }
      return null;
   }


   private static List<BerkeleyDBMercatorTile> getDescendants(final BerkeleyDBOctree octree,
                                                              final byte[] id,
                                                              final boolean loadPoints) {
      final List<BerkeleyDBMercatorTile> result = new ArrayList<BerkeleyDBMercatorTile>();

      final Database nodeDB = octree.getNodeDB();

      final CursorConfig cursorConfig = new CursorConfig();

      try (final Cursor cursor = nodeDB.openCursor(null, cursorConfig)) {
         final DatabaseEntry keyEntry = new DatabaseEntry(id);
         final DatabaseEntry dataEntry = new DatabaseEntry();
         final OperationStatus status = cursor.getSearchKeyRange(keyEntry, dataEntry, LockMode.DEFAULT);
         if (status == OperationStatus.SUCCESS) {
            result.add(fromDB(octree, keyEntry.getData(), dataEntry.getData(), loadPoints));

            while (cursor.getNext(keyEntry, dataEntry, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
               final byte[] key = keyEntry.getData();
               if (Utils.isGreaterThan(key, id)) {
                  break;
               }
               result.add(fromDB(octree, keyEntry.getData(), dataEntry.getData(), loadPoints));
            }
         }
      }

      return result;
   }


   static void save(final Transaction txn,
                    final BerkeleyDBOctree octree,
                    final TileHeader header,
                    final PointsSet pointsSet) {
      final byte[] id = header._id;

      final BerkeleyDBMercatorTile ancestor = getAncestorOrSameLevel(txn, octree, id);
      if (ancestor != null) {
         //System.out.println("==> found ancestor (" + ancestor.getID() + ") for tile " + toString(id));

         ancestor.mergePoints(txn, pointsSet);
         return;
      }

      final List<BerkeleyDBMercatorTile> descendants = getDescendants(octree, id, true);
      if ((descendants != null) && !descendants.isEmpty()) {
         final StringBuilder builder = new StringBuilder();

         builder.append("[ ");
         for (final BerkeleyDBMercatorTile e : descendants) {
            builder.append(e.getID());
            builder.append(" ");
         }
         builder.append("]");

         System.out.println(">>> tile " + toString(id) + " has " + descendants.size() + " descendants " + builder.toString());

         return;
      }

      final BerkeleyDBMercatorTile tile = new BerkeleyDBMercatorTile(octree, id, header._sector, pointsSet);
      tile.rawSave(txn);
   }


   private void rawSave(final Transaction txn) {
      final Database nodeDB = _octree.getNodeDB();
      final Database nodeDataDB = _octree.getNodeDataDB();

      final Format format = Format.LatLonHeight;

      final DatabaseEntry key = new DatabaseEntry(getBerkeleyDBKey());

      nodeDB.put(txn, key, new DatabaseEntry(createNodeEntry(format)));
      nodeDataDB.put(txn, key, new DatabaseEntry(createNodeDataEntry(format)));
   }


   private void mergePoints(final Transaction txn,
                            final PointsSet newPointsSet) {
      final int mergedPointsLength = getPointsCount() + newPointsSet.size();
      if (mergedPointsLength > MAX_POINTS_PER_TILE) {
         split(txn, newPointsSet);
      }
      else {
         updateFromPoints(txn, newPointsSet);
      }
   }


   private static PointsSet extractPoints(final Sector sector,
                                          final List<Geodetic3D> points) {
      double sumLatitudeInRadians = 0;
      double sumLongitudeInRadians = 0;
      double sumHeight = 0;

      final List<Geodetic3D> result = new ArrayList<Geodetic3D>();

      final Iterator<Geodetic3D> iterator = points.iterator();
      while (iterator.hasNext()) {
         final Geodetic3D point = iterator.next();
         if (sector.contains(point._latitude, point._longitude)) {
            result.add(point);

            sumLatitudeInRadians += point._latitude._radians;
            sumLongitudeInRadians += point._longitude._radians;
            sumHeight += point._height;

            iterator.remove();
         }
      }

      final int bufferSize = result.size();
      final double averageLatitudeInRadians = sumLatitudeInRadians / bufferSize;
      final double averageLongitudeInRadians = sumLongitudeInRadians / bufferSize;
      final double averageHeight = sumHeight / bufferSize;

      final Geodetic3D averagePoint = Utils.fromRadians(averageLatitudeInRadians, averageLongitudeInRadians, averageHeight);

      return new PointsSet(result, averagePoint);
   }


   private void split(final Transaction txn,
                      final PointsSet newPointsSet) {

      remove(txn);

      final int oldPointsCount = getPointsCount();
      final int newPointsSize = newPointsSet.size();
      final int mergedPointsSize = oldPointsCount + newPointsSize;

      final List<Geodetic3D> mergedPoints = new ArrayList<Geodetic3D>(mergedPointsSize);
      mergedPoints.addAll(getPoints());
      mergedPoints.addAll(newPointsSet._points);


      final TileHeader header = new TileHeader(_id, _sector);
      final TileHeader[] children = header.createChildren();
      for (final TileHeader child : children) {
         final PointsSet childPointsSet = extractPoints(child._sector, mergedPoints);

         if (!childPointsSet.isEmpty()) {
            System.out.println(">>> tile " + getID() + " split " + childPointsSet.size() + " points into " + toString(child._id));

            save(txn, _octree, child, childPointsSet);
         }
      }

      if (!mergedPoints.isEmpty()) {
         throw new RuntimeException("Logic error!");
      }
   }


   private void remove(final Transaction txn) {
      final DatabaseEntry key = new DatabaseEntry(_id);
      _octree.getNodeDB().delete(txn, key);
      _octree.getNodeDataDB().delete(txn, key);
   }


   private static double weightedAverage(final double value1,
                                         final int count1,
                                         final double value2,
                                         final int count2) {
      return ((value1 * count1) + (value2 * count2)) / (count1 + count2);
   }


   private static Angle weightedAverage(final Angle value1,
                                        final int count1,
                                        final Angle value2,
                                        final int count2) {
      return Angle.fromRadians(weightedAverage(value1._radians, count1, value2._radians, count2));
   }


   private static Geodetic3D weightedAverage(final Geodetic3D value1,
                                             final int count1,
                                             final Geodetic3D value2,
                                             final int count2) {

      final Angle averageLatitude = weightedAverage( //
               value1._latitude, count1, //
               value2._latitude, count2);

      final Angle averageLongitude = weightedAverage( //
               value1._longitude, count1, //
               value2._longitude, count2);

      final double averageHeight = weightedAverage( //
               value1._height, count1, //
               value2._height, count2);

      return new Geodetic3D(averageLatitude, averageLongitude, averageHeight);
   }


   private void updateFromPoints(final Transaction txn,
                                 final PointsSet newPointsSet) {
      final int oldPointsCount = getPointsCount();
      final int newPointsSize = newPointsSet.size();
      final int mergedPointsSize = oldPointsCount + newPointsSize;

      final List<Geodetic3D> mergedPoints = new ArrayList<Geodetic3D>(mergedPointsSize);
      mergedPoints.addAll(getPoints());
      mergedPoints.addAll(newPointsSet._points);

      final Geodetic3D mergedAveragePoints = weightedAverage( //
               _averagePoint, oldPointsCount, //
               newPointsSet._averagePoint, newPointsSize);

      System.out.println(" merged " + mergedPointsSize + " points, old=" + oldPointsCount + ", new=" + newPointsSize);

      _pointsCount = mergedPointsSize;
      _points = mergedPoints;
      _averagePoint = mergedAveragePoints;

      rawSave(txn);
   }


   //   private BerkeleyDBMercatorTile getAncestorOrSameLevel() {
   //      //      byte[] ancestorId = removeTrailing(_id);
   //      byte[] ancestorId = _id;
   //      while (ancestorId != null) {
   //         final BerkeleyDBMercatorTile ancestor = _octree.readTile(ancestorId);
   //         if (ancestor != null) {
   //            return ancestor;
   //         }
   //         ancestorId = removeTrailing(ancestorId);
   //      }
   //      return null;
   //   }


   private static byte[] removeTrailing(final byte[] id) {
      final int length = id.length;
      if (length == 0) {
         return null;
      }
      return Arrays.copyOf(id, length - 1);
   }


   static BerkeleyDBMercatorTile fromDB(final BerkeleyDBOctree octree,
                                        final byte[] id,
                                        final byte[] data,
                                        final boolean loadPoints) {
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

      final Geodetic3D averagePoint = new Geodetic3D( //
               Angle.fromRadians(averageLatitude), //
               Angle.fromRadians(averageLongitude), //
               averageHeight);


      final byte formatID = byteBuffer.get();
      final Format format = Format.getFromID(formatID);

      final BerkeleyDBMercatorTile tile = new BerkeleyDBMercatorTile(octree, id, sector, pointsCount, averagePoint, format);
      if (loadPoints) {
         tile.getPoints(); // force points load
      }
      return tile;
   }


   @Override
   public List<Geodetic3D> getPoints() {
      if (_points == null) {
         _points = loadPoints();
      }
      return _points;
   }


   @Override
   public int getPointsCount() {
      return _pointsCount;
   }


   private List<Geodetic3D> loadPoints() {
      switch (_format) {
         case LatLonHeight:

            final Database nodeDataDB = _octree.getNodeDataDB();

            final DatabaseEntry dataEntry = new DatabaseEntry();
            final OperationStatus status = nodeDataDB.get(null, new DatabaseEntry(_id), dataEntry, LockMode.READ_COMMITTED);
            if (status != OperationStatus.SUCCESS) {
               throw new RuntimeException("Unsupported status=" + status);
            }

            final ByteBuffer byteBuffer = ByteBuffer.wrap(dataEntry.getData());

            final List<Geodetic3D> points = new ArrayList<Geodetic3D>(_pointsCount);

            final double averageLatitude = _averagePoint._latitude._radians;
            final double averageLongitude = _averagePoint._longitude._radians;
            final double averageHeight = _averagePoint._height;

            for (int i = 0; i < _pointsCount; i++) {
               final double lat = byteBuffer.getFloat() + averageLatitude;
               final double lon = byteBuffer.getFloat() + averageLongitude;
               final double height = byteBuffer.getFloat() + averageHeight;

               final Geodetic3D point = new Geodetic3D( //
                        Angle.fromRadians(lat), //
                        Angle.fromRadians(lon), //
                        height);
               points.add(point);
            }

            if (_pointsCount != points.size()) {
               throw new RuntimeException("Inconsistency in pointsCount");
            }
            return Collections.unmodifiableList(points);

         default:
            throw new RuntimeException("Unsupported format: " + _format);
      }

   }


   @Override
   public Geodetic3D getAveragePoint() {
      return _averagePoint;
   }


}
