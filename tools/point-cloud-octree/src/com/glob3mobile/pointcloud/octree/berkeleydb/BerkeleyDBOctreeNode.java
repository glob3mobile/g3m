

package com.glob3mobile.pointcloud.octree.berkeleydb;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.glob3mobile.pointcloud.octree.PersistentOctree;
import com.glob3mobile.utils.Angle;
import com.glob3mobile.utils.Geodetic3D;
import com.glob3mobile.utils.Sector;
import com.glob3mobile.utils.Utils;
import com.sleepycat.je.Cursor;
import com.sleepycat.je.CursorConfig;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;
import com.sleepycat.je.Transaction;


public class BerkeleyDBOctreeNode
   implements
      PersistentOctree.Node {


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


   static Angle calculateSplitLatitude(final Angle lowerLatitude,
                                       final Angle upperLatitude) {
      final double middleV = (getMercatorV(lowerLatitude) / 2.0) + (getMercatorV(upperLatitude) / 2.0);
      return toLatitude(middleV);
   }


   private final BerkeleyDBOctree _octree;
   private final byte[]           _id;
   private final Sector           _sector;
   private Geodetic3D             _averagePoint;
   private final Format           _format;
   private int                    _pointsCount;
   private List<Geodetic3D>       _points = null;


   private BerkeleyDBOctreeNode(final BerkeleyDBOctree octree,
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

      //      final int remove_debug_code;
      //      checkConsistency();
   }


   private BerkeleyDBOctreeNode(final BerkeleyDBOctree octree,
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

      //      final int remove_debug_code;
      //      checkConsistency();
   }


   //   private void checkConsistency() {
   //      final Sector sector = TileHeader.sectorFor(_id);
   //      if (!sector.equals(_sector)) {
   //         throw new RuntimeException("Logic error");
   //      }
   //   }


   @Override
   public int getDepth() {
      return _id.length;
   }


   byte[] getBerkeleyDBKey() {
      return Arrays.copyOf(_id, _id.length);
   }


   @Override
   public String getID() {
      return Utils.toIDString(_id);
   }


   @Override
   public Sector getSector() {
      return _sector;
   }


   @Override
   public String toString() {
      return "MercatorTile [id=" + getID() + //
             ", depth=" + getDepth() + //
             ", points=" + _pointsCount + //
             ", sector=" + _sector + //
             "]";
   }


   private byte[] createNodeEntry(final Format format) {
      final byte version = 1;
      final byte subversion = 0;
      final byte formatID = format._formatID;

      final int entrySize = ByteBufferUtils.sizeOf(version) + //
                            ByteBufferUtils.sizeOf(subversion) + //
                            ByteBufferUtils.sizeOf(_sector) + //
                            ByteBufferUtils.sizeOf(_pointsCount) + //
                            ByteBufferUtils.sizeOf(format, _averagePoint) + //
                            ByteBufferUtils.sizeOf(formatID);

      final ByteBuffer byteBuffer = ByteBuffer.allocate(entrySize);
      byteBuffer.put(version);
      byteBuffer.put(subversion);
      ByteBufferUtils.put(byteBuffer, _sector);
      byteBuffer.putInt(_pointsCount);
      ByteBufferUtils.put(byteBuffer, format, _averagePoint);
      byteBuffer.put(formatID);

      return byteBuffer.array();
   }


   private byte[] createNodeDataEntry(final Format format) {
      final int entrySize = ByteBufferUtils.sizeOf(format, _points);
      final ByteBuffer byteBuffer = ByteBuffer.allocate(entrySize);
      ByteBufferUtils.put(byteBuffer, format, _points);
      return byteBuffer.array();
   }


   private static BerkeleyDBOctreeNode getAncestorOrSameLevel(final Transaction txn,
                                                              final BerkeleyDBOctree octree,
                                                              final byte[] id) {
      byte[] ancestorId = id;
      while (ancestorId != null) {
         final BerkeleyDBOctreeNode ancestor = octree.readNode(txn, ancestorId, true);
         if (ancestor != null) {
            return ancestor;
         }
         ancestorId = Utils.removeTrailing(ancestorId);
      }
      return null;
   }


   private static List<BerkeleyDBOctreeNode> getDescendants(final Transaction txn,
                                                            final BerkeleyDBOctree octree,
                                                            final byte[] id,
                                                            final boolean loadPoints) {
      final List<BerkeleyDBOctreeNode> result = new ArrayList<BerkeleyDBOctreeNode>();

      final Database nodeDB = octree.getNodeDB();

      final CursorConfig cursorConfig = new CursorConfig();

      try (final Cursor cursor = nodeDB.openCursor(txn, cursorConfig)) {
         final DatabaseEntry keyEntry = new DatabaseEntry(id);
         final DatabaseEntry dataEntry = new DatabaseEntry();
         final OperationStatus status = cursor.getSearchKeyRange(keyEntry, dataEntry, LockMode.DEFAULT);
         if (status == OperationStatus.SUCCESS) {
            byte[] key = keyEntry.getData();

            if (!Utils.hasSamePrefix(key, id)) {
               return result;
            }
            if (Utils.isGreaterThan(key, id)) {
               result.add(fromDB(txn, octree, key, dataEntry.getData(), loadPoints));
            }

            while (cursor.getNext(keyEntry, dataEntry, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
               key = keyEntry.getData();
               if (!Utils.hasSamePrefix(key, id)) {
                  return result;
               }
               if (Utils.isGreaterThan(key, id)) {
                  result.add(fromDB(txn, octree, keyEntry.getData(), dataEntry.getData(), loadPoints));
               }
            }
         }
      }

      //      final int REMOVE_DEBUG_CODE;
      //      for (final BerkeleyDBMercatorTile descendant : result) {
      //         if (!Utils.hasSamePrefix(id, descendant._id)) {
      //            throw new RuntimeException("Logic error");
      //         }
      //         if (!Utils.isGreaterThan(descendant._id, id)) {
      //            throw new RuntimeException("Logic error");
      //         }
      //      }

      return result;
   }


   static void insertPoints(final Transaction txn,
                            final BerkeleyDBOctree octree,
                            final TileHeader header,
                            final PointsSet pointsSet) {
      final byte[] id = header._id;

      final BerkeleyDBOctreeNode ancestor = getAncestorOrSameLevel(txn, octree, id);
      if (ancestor != null) {
         // System.out.println("==> found ancestor (" + ancestor.getID() + ") for tile " + toString(id));

         ancestor.mergePoints(txn, pointsSet);
         return;
      }

      final List<BerkeleyDBOctreeNode> descendants = getDescendants(txn, octree, id, false);
      if ((descendants != null) && !descendants.isEmpty()) {
         splitPointsIntoDescendants(txn, octree, header, pointsSet, descendants);
         return;
      }

      final BerkeleyDBOctreeNode tile = new BerkeleyDBOctreeNode(octree, id, header._sector, pointsSet);
      tile.rawSave(txn);
   }


   private static void splitPointsIntoDescendants(final Transaction txn,
                                                  final BerkeleyDBOctree octree,
                                                  final TileHeader header,
                                                  final PointsSet pointsSet,
                                                  final List<BerkeleyDBOctreeNode> descendants) {
      final List<Geodetic3D> points = new ArrayList<Geodetic3D>(pointsSet._points);
      for (final BerkeleyDBOctreeNode descendant : descendants) {
         final PointsSet descendantPointsSet = extractPoints(descendant._sector, points);
         if (descendantPointsSet != null) {
            // System.out.println(">>> tile " + toString(header._id) + " split " + descendantPointsSet.size()
            // + " points into descendant " + toString(descendant._id) + " (" + points.size()
            // + " points not yet distributed)");

            descendant.getPoints(txn); // force points load
            descendant.mergePoints(txn, descendantPointsSet);
            descendant._points = null; // release points' memory
         }
      }

      if (!points.isEmpty()) {
         final List<TileHeader> descendantsHeaders = descendantsHeadersOfLevel(header, header.getLevel() + 1);
         for (final TileHeader descendantHeader : descendantsHeaders) {
            final PointsSet descendantPointsSet = extractPoints(descendantHeader._sector, points);
            if (descendantPointsSet != null) {
               // System.out.println(">>> 2ND tile " + toString(header._id) + " split " + descendantPointsSet.size()
               // + " points into descendant " + toString(descendantHeader._id) + " (" + points.size()
               // + " points not yet distributed)");

               insertPoints(txn, octree, descendantHeader, descendantPointsSet);
            }
         }

         if (!points.isEmpty()) {
            throw new RuntimeException("Logic error!");
         }
      }
   }


   private static List<TileHeader> descendantsHeadersOfLevel(final TileHeader header,
                                                             final int level) {
      final List<TileHeader> result = new ArrayList<TileHeader>();
      descendantsHeadersOfLevel(result, level, header);
      return result;
   }


   private static void descendantsHeadersOfLevel(final List<TileHeader> result,
                                                 final int level,
                                                 final TileHeader header) {
      if (header.getLevel() == level) {
         result.add(header);
      }
      else {
         final TileHeader[] children = header.createChildren();
         for (final TileHeader child : children) {
            descendantsHeadersOfLevel(result, level, child);
         }
      }
   }


   private void rawSave(final Transaction txn) {
      //      for (final Geodetic3D p : _points) {
      //         if (!_sector.contains(p._latitude, p._longitude)) {
      //            throw new RuntimeException("LOGIC ERROR!!");
      //         }
      //      }

      if (getPoints().size() > _octree.getMaxPointsPerTile()) {
         split(txn);
         return;
      }

      //      if (getPoints().size() > _octree.getMaxPointsPerTile()) {
      //         System.out.println("***** logic error, tile " + getID() + " has more points than threshold (" + getPoints().size() + ">"
      //                            + _octree.getMaxPointsPerTile() + ")");
      //      }


      final Database nodeDB = _octree.getNodeDB();
      final Database nodeDataDB = _octree.getNodeDataDB();

      final Format format = Format.LatLonHeight;

      final byte[] id = getBerkeleyDBKey();

      final DatabaseEntry key = new DatabaseEntry(id);

      OperationStatus status = nodeDB.put(txn, key, new DatabaseEntry(createNodeEntry(format)));
      if (status != OperationStatus.SUCCESS) {
         throw new RuntimeException("Status not supported: " + status);
      }
      status = nodeDataDB.put(txn, key, new DatabaseEntry(createNodeDataEntry(format)));
      if (status != OperationStatus.SUCCESS) {
         throw new RuntimeException("Status not supported: " + status);
      }

      final boolean checkInvariants = false;
      if (checkInvariants) {
         checkInvariants(txn);
      }
   }


   private void checkInvariants(final Transaction txn) {
      final BerkeleyDBOctreeNode ancestor = getAncestor(txn, _octree, _id);
      final List<BerkeleyDBOctreeNode> descendants = getDescendants(txn, _octree, _id, false);

      if ((ancestor != null) || !descendants.isEmpty()) {
         System.out.println("***** INVARIANT FAILED: " + //
                            "for tile=" + Utils.toIDString(_id) + //
                            ", ancestor=" + ancestor + //
                            ", descendants=" + descendants);
      }
   }


   private static BerkeleyDBOctreeNode getAncestor(final Transaction txn,
                                                   final BerkeleyDBOctree octree,
                                                   final byte[] id) {
      byte[] ancestorId = Utils.removeTrailing(id);
      while (ancestorId != null) {
         final BerkeleyDBOctreeNode ancestor = octree.readNode(txn, ancestorId, true);
         if (ancestor != null) {
            return ancestor;
         }
         ancestorId = Utils.removeTrailing(ancestorId);
      }
      return null;
   }


   private void mergePoints(final Transaction txn,
                            final PointsSet newPointsSet) {
      final int mergedPointsLength = getPointsCount() + newPointsSet.size();
      if (mergedPointsLength > _octree.getMaxPointsPerTile()) {
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

      final List<Geodetic3D> extracted = new ArrayList<Geodetic3D>();

      final Iterator<Geodetic3D> iterator = points.iterator();
      while (iterator.hasNext()) {
         final Geodetic3D point = iterator.next();
         if (sector.contains(point._latitude, point._longitude)) {
            extracted.add(point);

            sumLatitudeInRadians += point._latitude._radians;
            sumLongitudeInRadians += point._longitude._radians;
            sumHeight += point._height;

            iterator.remove();
         }
      }


      final int extractedSize = extracted.size();
      if (extractedSize == 0) {
         return null;
      }

      final double averageLatitudeInRadians = sumLatitudeInRadians / extractedSize;
      final double averageLongitudeInRadians = sumLongitudeInRadians / extractedSize;
      final double averageHeight = sumHeight / extractedSize;

      final Geodetic3D averagePoint = Geodetic3D.fromRadians(averageLatitudeInRadians, averageLongitudeInRadians, averageHeight);

      return new PointsSet(extracted, averagePoint);
   }


   private void split(final Transaction txn,
                      final PointsSet newPointsSet) {

      remove(txn);

      final int oldPointsCount = getPointsCount();
      final int newPointsSize = newPointsSet.size();
      final int mergedPointsSize = oldPointsCount + newPointsSize;

      final List<Geodetic3D> mergedPoints = new ArrayList<Geodetic3D>(mergedPointsSize);
      mergedPoints.addAll(getPoints(txn));
      mergedPoints.addAll(newPointsSet._points);


      final TileHeader header = new TileHeader(_id, _sector);
      final TileHeader[] children = header.createChildren();
      for (final TileHeader child : children) {
         final PointsSet childPointsSet = extractPoints(child._sector, mergedPoints);
         if (childPointsSet != null) {
            // System.out.println(">>> tile " + getID() + " split " + childPointsSet.size() + " points into " + toString(child._id));
            insertPoints(txn, _octree, child, childPointsSet);
         }
      }

      if (!mergedPoints.isEmpty()) {
         throw new RuntimeException("Logic error!");
      }
   }


   private void split(final Transaction txn) {

      remove(txn);

      final List<Geodetic3D> points = new ArrayList<Geodetic3D>(getPoints(txn));

      final TileHeader header = new TileHeader(_id, _sector);
      final TileHeader[] children = header.createChildren();
      for (final TileHeader child : children) {
         final PointsSet childPointsSet = extractPoints(child._sector, points);
         if (childPointsSet != null) {
            // System.out.println(">>> tile " + getID() + " split " + childPointsSet.size() + " points into " + toString(child._id));
            insertPoints(txn, _octree, child, childPointsSet);
         }
      }

      if (!points.isEmpty()) {
         throw new RuntimeException("Logic error!");
      }
   }


   private void remove(final Transaction txn) {
      final DatabaseEntry key = new DatabaseEntry(_id);
      @SuppressWarnings("unused")
      OperationStatus status = _octree.getNodeDB().delete(txn, key);
      //      if (status != OperationStatus.SUCCESS) {
      //         throw new RuntimeException("Unsupported status=" + status);
      //      }
      status = _octree.getNodeDataDB().delete(txn, key);
      //      if (status != OperationStatus.SUCCESS) {
      //         throw new RuntimeException("Unsupported status=" + status);
      //      }
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
      mergedPoints.addAll(getPoints(txn));
      mergedPoints.addAll(newPointsSet._points);

      final Geodetic3D mergedAveragePoints = weightedAverage( //
               _averagePoint, oldPointsCount, //
               newPointsSet._averagePoint, newPointsSize);

      //System.out.println(" merged " + mergedPointsSize + " points, old=" + oldPointsCount + ", new=" + newPointsSize);

      _pointsCount = mergedPointsSize;
      _points = mergedPoints;
      _averagePoint = mergedAveragePoints;

      rawSave(txn);
   }


   static BerkeleyDBOctreeNode fromDB(final Transaction txn,
                                      final BerkeleyDBOctree octree,
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

      final Sector sector = ByteBufferUtils.getSector(byteBuffer);

      final int pointsCount = byteBuffer.getInt();
      final Geodetic3D averagePoint = ByteBufferUtils.getGeodetic3D(byteBuffer);

      final byte formatID = byteBuffer.get();
      final Format format = Format.getFromID(formatID);

      final BerkeleyDBOctreeNode tile = new BerkeleyDBOctreeNode(octree, id, sector, pointsCount, averagePoint, format);
      if (loadPoints) {
         tile._points = tile.loadPoints(txn);
      }
      return tile;
   }


   @Override
   public List<Geodetic3D> getPoints() {
      return getPoints(null);
   }


   private List<Geodetic3D> getPoints(final Transaction txn) {
      if (_points == null) {
         _points = loadPoints(txn);
      }
      return _points;
   }


   @Override
   public int getPointsCount() {
      return _pointsCount;
   }


   private List<Geodetic3D> loadPoints(final Transaction txn) {
      final Database nodeDataDB = _octree.getNodeDataDB();

      final DatabaseEntry dataEntry = new DatabaseEntry();
      final OperationStatus status = nodeDataDB.get(txn, new DatabaseEntry(_id), dataEntry, LockMode.DEFAULT);
      if (status != OperationStatus.SUCCESS) {
         throw new RuntimeException("Unsupported status=" + status);
      }

      final ByteBuffer byteBuffer = ByteBuffer.wrap(dataEntry.getData());


      final List<Geodetic3D> points = ByteBufferUtils.getPoints(byteBuffer, _format, _pointsCount);

      if (_pointsCount != points.size()) {
         throw new RuntimeException("Inconsistency in pointsCount");
      }

      return Collections.unmodifiableList(points);
   }


   @Override
   public Geodetic3D getAveragePoint() {
      return _averagePoint;
   }


}
