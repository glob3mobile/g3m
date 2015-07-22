

package com.glob3mobile.pointcloud.octree.berkeleydb;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.glob3mobile.pointcloud.octree.PersistentLOD;
import com.glob3mobile.pointcloud.octree.berkeleydb.BerkeleyDBLOD.BerkeleyDBTransaction;
import com.glob3mobile.utils.Geodetic3D;
import com.glob3mobile.utils.Sector;
import com.glob3mobile.utils.Utils;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;


public class BerkeleyDBLODNode
         implements
            PersistentLOD.Node {


   private static class BerkeleyDBLODNodeLevel
            implements
               PersistentLOD.NodeLevel {


      private static BerkeleyDBLODNodeLevel fromDB(final com.sleepycat.je.Transaction txn,
                                                   final BerkeleyDBLOD db,
                                                   final Format format,
                                                   final byte[] id,
                                                   final int lodLevel,
                                                   final boolean loadPoints,
                                                   final int pointsCount,
                                                   final Geodetic3D averagePoint) {
         if (loadPoints) {
            final List<Geodetic3D> points = loadPoints(txn, db, id, lodLevel, format, pointsCount, averagePoint);
            return new BerkeleyDBLODNodeLevel(db, id, lodLevel, points, averagePoint);
         }
         return new BerkeleyDBLODNodeLevel(db, id, lodLevel, pointsCount, format, averagePoint);
      }


      private final BerkeleyDBLOD _db;
      private final byte[]        _id;
      private final int           _lodLevel;
      private final int           _pointsCount;
      private List<Geodetic3D>    _points;
      private final Format        _format;
      private final Geodetic3D    _averagePoint;


      private BerkeleyDBLODNodeLevel(final BerkeleyDBLOD db,
                                     final byte[] id,
                                     final int lodLevel,
                                     final List<Geodetic3D> points,
                                     final Geodetic3D averagePoint) {
         _db = db;
         _id = id;
         _lodLevel = lodLevel;
         _pointsCount = points.size();
         _points = Collections.unmodifiableList(new ArrayList<Geodetic3D>(points));
         _format = null;
         _averagePoint = averagePoint;
      }


      private BerkeleyDBLODNodeLevel(final BerkeleyDBLOD db,
                                     final byte[] id,
                                     final int lodLevel,
                                     final int pointsCount,
                                     final Format format,
                                     final Geodetic3D averagePoint) {
         _db = db;
         _id = id;
         _lodLevel = lodLevel;
         _pointsCount = pointsCount;
         _format = format;
         _points = null;
         _averagePoint = averagePoint;
      }


      static private List<Geodetic3D> loadPoints(final com.sleepycat.je.Transaction txn,
                                                 final BerkeleyDBLOD db,
                                                 final byte[] id,
                                                 final int lodLevel,
                                                 final Format format,
                                                 final int pointsCount,
               final Geodetic3D averagePoint) {
         final DatabaseEntry keyEntry = createNodeLevelDataKey(id, lodLevel);
         final DatabaseEntry dataEntry = new DatabaseEntry();

         final Database nodeLevelDataDB = db.getNodeLevelData();

         final OperationStatus status = nodeLevelDataDB.get(txn, keyEntry, dataEntry, LockMode.DEFAULT);
         switch (status) {
            case SUCCESS: {
               final ByteBuffer byteBuffer = ByteBuffer.wrap(dataEntry.getData());
               final List<Geodetic3D> points = ByteBufferUtils.getPoints(byteBuffer, format, pointsCount, averagePoint);

               if (pointsCount != points.size()) {
                  throw new RuntimeException("Inconsistency in pointsCount");
               }

               return Collections.unmodifiableList(points);
            }
            case NOTFOUND:
               throw new RuntimeException("Logic Error: data not found for node=" + Utils.toIDString(id) + ", level=" + lodLevel);
            default:
               throw new RuntimeException("Status not supported: " + status);
         }
      }


      @Override
      public List<Geodetic3D> getPoints(final PersistentLOD.Transaction transaction) {
         if (_points == null) {
            final BerkeleyDBTransaction berkeleyDBTransaction = (BerkeleyDBLOD.BerkeleyDBTransaction) transaction;
            final com.sleepycat.je.Transaction txn = (berkeleyDBTransaction == null) ? null : berkeleyDBTransaction._txn;
            _points = loadPoints(txn, _db, _id, _lodLevel, _format, _pointsCount, _averagePoint);
         }
         return _points;
      }


      @Override
      public int getPointsCount() {
         return _pointsCount;
      }


      @Override
      public int getLevel() {
         return _lodLevel;
      }


      private static DatabaseEntry createNodeLevelDataKey(final byte[] id,
                                                          final int lodLevel) {
         final int size = ByteBufferUtils.sizeOf(id) + //
                  ByteBufferUtils.sizeOf((byte) 255) + //
                          ByteBufferUtils.sizeOf(lodLevel);
         final ByteBuffer byteBuffer = ByteBuffer.allocate(size);
         byteBuffer.put(id);
         byteBuffer.put((byte) 255);
         byteBuffer.putInt(lodLevel);
         return new DatabaseEntry(byteBuffer.array());
      }


      private DatabaseEntry createNodeLevelDataEntry(final Format format) {
         final int entrySize = ByteBufferUtils.sizeOf(format, _points, _averagePoint);
         final ByteBuffer byteBuffer = ByteBuffer.allocate(entrySize);
         ByteBufferUtils.put(byteBuffer, format, _points, _averagePoint);
         return new DatabaseEntry(byteBuffer.array());
      }


      private void save(final com.sleepycat.je.Transaction txn,
                        final Format format) {
         final Database nodeLevelDataDB = _db.getNodeLevelData();

         nodeLevelDataDB.put(txn, createNodeLevelDataKey(_id, _lodLevel), createNodeLevelDataEntry(format));
      }


      @Override
      public String toString() {
         return "[BerkeleyDBLODNodeLevel id=" + Utils.toIDString(_id) + ", lodLevel=" + _lodLevel + ", points=" + _pointsCount
                  + ", averagePoint=" + _averagePoint + "]";
      }


   }


   static BerkeleyDBLODNode create(final BerkeleyDBLOD db,
                                   final byte[] id,
                                   final List<List<Geodetic3D>> levelsPoints) {
      final int levelsCount = levelsPoints.size();
      final List<BerkeleyDBLODNodeLevel> levels = new ArrayList<BerkeleyDBLODNodeLevel>(levelsCount);
      final Geodetic3D averagePoint = calculateAverage(levelsPoints);
      for (int i = 0; i < levelsCount; i++) {
         final List<Geodetic3D> levelPoints = levelsPoints.get(i);
         levels.add(new BerkeleyDBLODNodeLevel(db, id, i, levelPoints, averagePoint));
      }
      return new BerkeleyDBLODNode(db, id, levels, averagePoint);
   }


   private static Geodetic3D calculateAverage(final List<List<Geodetic3D>> levelsPoints) {
      double sumLatitude = 0;
      double sumLongitude = 0;
      double sumHeight = 0;
      long pointsCount = 0;

      for (final List<Geodetic3D> levelPoints : levelsPoints) {
         for (final Geodetic3D point : levelPoints) {
            sumLatitude += point._latitude._radians;
            sumLongitude += point._longitude._radians;
            sumHeight += point._height;
            pointsCount++;
         }
      }

      return Geodetic3D.fromRadians(sumLatitude / pointsCount, sumLongitude / pointsCount, sumHeight / pointsCount);
   }


   static BerkeleyDBLODNode fromDB(final com.sleepycat.je.Transaction txn,
                                   final BerkeleyDBLOD db,
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

      final int pointsCount = byteBuffer.getInt();

      final byte formatID = byteBuffer.get();
      final Format format = Format.getFromID(formatID);

      final int levelsCount = byteBuffer.getInt();
      final int[] levelsPointsCount = new int[levelsCount];
      for (int i = 0; i < levelsCount; i++) {
         levelsPointsCount[i] = byteBuffer.getInt();
      }

      final Geodetic3D averagePoint = ByteBufferUtils.getGeodetic3D(format, byteBuffer);

      return new BerkeleyDBLODNode(txn, db, id, pointsCount, levelsPointsCount, format, loadPoints, averagePoint);
   }

   private final BerkeleyDBLOD                _db;
   private final byte[]                       _id;
   private final int                          _levelsCount;
   private final List<BerkeleyDBLODNodeLevel> _levels;
   private final int[]                        _levelsPointsCount;
   private final int                          _pointsCount;
   private Sector                             _sector = null;
   private final Geodetic3D                   _averagePoint;


   private BerkeleyDBLODNode(final BerkeleyDBLOD db,
                             final byte[] id,
                             final List<BerkeleyDBLODNodeLevel> levels,
                             final Geodetic3D averagePoint) {
      _db = db;
      _id = id;
      _levels = levels;
      _levelsCount = levels.size();
      _levelsPointsCount = new int[_levelsCount];
      int pointsCount = 0;
      for (int i = 0; i < _levelsCount; i++) {
         final BerkeleyDBLODNodeLevel level = levels.get(i);
         final int levelPointsCount = level.getPointsCount();
         pointsCount += levelPointsCount;
         _levelsPointsCount[i] = levelPointsCount;
      }
      _pointsCount = pointsCount;

      _averagePoint = averagePoint;
   }


   private BerkeleyDBLODNode(final com.sleepycat.je.Transaction txn,
                             final BerkeleyDBLOD db,
                             final byte[] id,
                             final int pointsCount,
                             final int[] levelsPointsCount,
                             final Format format,
                             final boolean loadPoints,
                             final Geodetic3D averagePoint) {
      _db = db;
      _id = id;
      _pointsCount = pointsCount;

      _levelsCount = levelsPointsCount.length;
      _levelsPointsCount = levelsPointsCount;

      _averagePoint = averagePoint;

      _levels = new ArrayList<BerkeleyDBLODNode.BerkeleyDBLODNodeLevel>(_levelsCount);
      for (int i = 0; i < _levelsCount; i++) {
         _levels.add(BerkeleyDBLODNodeLevel.fromDB(txn, db, format, id, i, loadPoints, _levelsPointsCount[i], _averagePoint));
      }
   }


   private DatabaseEntry createNodeEntry(final Format format) {
      final byte version = 1;
      final byte subversion = 0;
      final byte formatID = format._formatID;

      final int levelsCount = _levels.size();
      final int entrySize = ByteBufferUtils.sizeOf(version) + //
               ByteBufferUtils.sizeOf(subversion) + //
                            ByteBufferUtils.sizeOf(_pointsCount) + //
                            ByteBufferUtils.sizeOf(formatID) + //
                            ByteBufferUtils.sizeOf(levelsCount) + //
                            (ByteBufferUtils.sizeOfInt() * levelsCount) + //
                            ByteBufferUtils.sizeOf(format, _averagePoint);

      final ByteBuffer byteBuffer = ByteBuffer.allocate(entrySize);
      byteBuffer.put(version);
      byteBuffer.put(subversion);
      byteBuffer.putInt(_pointsCount);
      byteBuffer.put(formatID);
      byteBuffer.putInt(levelsCount);
      for (final BerkeleyDBLODNodeLevel level : _levels) {
         byteBuffer.putInt(level.getPointsCount());
      }
      ByteBufferUtils.put(byteBuffer, format, _averagePoint);

      return new DatabaseEntry(byteBuffer.array());
   }


   void save(final com.sleepycat.je.Transaction txn) {
      final Database nodeDB = _db.getNodeDB();

      final Format format = Format.LatLonHeight;

      final DatabaseEntry idEntry = new DatabaseEntry(_id);
      nodeDB.put(txn, idEntry, createNodeEntry(format));
      for (final BerkeleyDBLODNodeLevel level : _levels) {
         level.save(txn, format);
      }

   }


   @Override
   public String getID() {
      return Utils.toIDString(_id);
   }


   @Override
   public int getPointsCount() {
      return _pointsCount;
   }


   @Override
   public Sector getSector() {
      if (_sector == null) {
         _sector = TileHeader.sectorFor(_id);
      }
      return _sector;
   }


   @Override
   public String toString() {
      return "[BerkeleyDBLODNode id=" + Utils.toIDString(_id) + ", points=" + _pointsCount + ", levels=" + _levels + "]";
   }


   @Override
   public int getDepth() {
      return _id.length;
   }


   @Override
   public int getLevelsCount() {
      return _levelsCount;
   }


   @Override
   public int[] getLevelsPointsCount() {
      return _levelsPointsCount;
   }


   @Override
   public List<PersistentLOD.NodeLevel> getLevels() {
      return Collections.unmodifiableList(new ArrayList<PersistentLOD.NodeLevel>(_levels));
   }


}
