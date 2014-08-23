

package com.glob3mobile.pointcloud.octree.berkeleydb;

import static com.glob3mobile.pointcloud.octree.berkeleydb.ByteBufferUtils.put;
import static com.glob3mobile.pointcloud.octree.berkeleydb.ByteBufferUtils.sizeOf;
import static com.glob3mobile.pointcloud.octree.berkeleydb.ByteBufferUtils.sizeOfInt;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.glob3mobile.pointcloud.octree.Geodetic3D;
import com.glob3mobile.pointcloud.octree.PersistentLOD;
import com.glob3mobile.pointcloud.octree.Sector;
import com.glob3mobile.pointcloud.octree.Utils;
import com.glob3mobile.pointcloud.octree.berkeleydb.BerkeleyDBLOD.BerkeleyDBTransaction;
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
                                                   final int pointsCount) {
         if (loadPoints) {
            final List<Geodetic3D> points = loadPoints(txn, db, id, lodLevel, format, pointsCount);
            return new BerkeleyDBLODNodeLevel(db, id, lodLevel, points);
         }
         return new BerkeleyDBLODNodeLevel(db, id, lodLevel, pointsCount, format);
      }


      private final BerkeleyDBLOD _db;
      private final byte[]        _id;
      private final int           _lodLevel;
      private final int           _pointsCount;
      private List<Geodetic3D>    _points;
      private final Format        _format;


      private BerkeleyDBLODNodeLevel(final BerkeleyDBLOD db,
                                     final byte[] id,
                                     final int lodLevel,
                                     final List<Geodetic3D> points) {
         _db = db;
         _id = id;
         _lodLevel = lodLevel;
         _pointsCount = points.size();
         _points = new ArrayList<Geodetic3D>(points);
         _format = null;
      }


      private BerkeleyDBLODNodeLevel(final BerkeleyDBLOD db,
                                     final byte[] id,
                                     final int lodLevel,
                                     final int pointsCount,
                                     final Format format) {
         _db = db;
         _id = id;
         _lodLevel = lodLevel;
         _pointsCount = pointsCount;
         _format = format;
         _points = null;
      }


      static private List<Geodetic3D> loadPoints(final com.sleepycat.je.Transaction txn,
                                                 final BerkeleyDBLOD db,
                                                 final byte[] id,
                                                 final int lodLevel,
                                                 final Format format,
               final int pointsCount) {
         //throw new RuntimeException("not yet implemented");


         final DatabaseEntry keyEntry = createNodeLevelDataKey(id, lodLevel);
         final DatabaseEntry dataEntry = new DatabaseEntry();

         final Database nodeLevelDataDB = db.getNodeLevelData();

         final OperationStatus status = nodeLevelDataDB.get(txn, keyEntry, dataEntry, LockMode.DEFAULT);
         switch (status) {
            case SUCCESS: {
               final ByteBuffer byteBuffer = ByteBuffer.wrap(dataEntry.getData());
               return ByteBufferUtils.getPoints(byteBuffer, format, pointsCount);
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
            _points = loadPoints(txn, _db, _id, _lodLevel, _format, _pointsCount);
         }
         return _points;
      }


      public int getPointsCount() {
         return _pointsCount;
      }


      @Override
      public int getLevel() {
         return _lodLevel;
      }


      private static DatabaseEntry createNodeLevelDataKey(final byte[] id,
                                                          final int lodLevel) {
         final int size = sizeOf(id) + //
                          sizeOf((byte) 255) + //
                          sizeOf(lodLevel);
         final ByteBuffer byteBuffer = ByteBuffer.allocate(size);
         byteBuffer.put(id);
         byteBuffer.put((byte) 255);
         byteBuffer.putInt(lodLevel);
         return new DatabaseEntry(byteBuffer.array());
      }


      //      private DatabaseEntry createNodeLevelWithPointsEntry(final Format format) {
      //         final byte formatID = format._formatID;
      //
      //         final int entrySize = sizeOf(_lodLevel) + //
      //                  sizeOf(_pointsCount) + //
      //                  sizeOf(formatID) + //
      //                  sizeOf(format, _points);
      //
      //         final ByteBuffer byteBuffer = ByteBuffer.allocate(entrySize);
      //         byteBuffer.putInt(_lodLevel);
      //         byteBuffer.putInt(_pointsCount * -1); // negative pointsCount means the points are store in the header
      //         byteBuffer.put(formatID);
      //         put(byteBuffer, format, _points);
      //
      //         return new DatabaseEntry(byteBuffer.array());
      //      }


      private DatabaseEntry createNodeLevelDataEntry(final Format format) {
         final int entrySize = sizeOf(format, _points);
         final ByteBuffer byteBuffer = ByteBuffer.allocate(entrySize);
         put(byteBuffer, format, _points);
         return new DatabaseEntry(byteBuffer.array());
      }


      //      private DatabaseEntry createNodeLevelEntry(final Format format) {
      //         final byte formatID = format._formatID;
      //
      //         final int entrySize = sizeOf(_lodLevel) + //
      //                  sizeOf(_pointsCount) + //
      //                  sizeOf(formatID);
      //
      //         final ByteBuffer byteBuffer = ByteBuffer.allocate(entrySize);
      //         byteBuffer.putInt(_lodLevel);
      //         byteBuffer.putInt(_pointsCount);
      //         byteBuffer.put(formatID);
      //
      //         return new DatabaseEntry(byteBuffer.array());
      //      }


      private void save(final com.sleepycat.je.Transaction txn,
                        final Format format) {
         final Database nodeLevelDataDB = _db.getNodeLevelData();

         nodeLevelDataDB.put(txn, createNodeLevelDataKey(_id, _lodLevel), createNodeLevelDataEntry(format));
      }


   }


   static BerkeleyDBLODNode create(final BerkeleyDBLOD db,
                                   final byte[] id,
                                   final List<List<Geodetic3D>> levelsPoints) {
      final int levelsCount = levelsPoints.size();
      final List<BerkeleyDBLODNodeLevel> levels = new ArrayList<BerkeleyDBLODNodeLevel>(levelsCount);
      for (int i = 0; i < levelsCount; i++) {
         final List<Geodetic3D> levelPoints = levelsPoints.get(i);
         levels.add(new BerkeleyDBLODNodeLevel(db, id, i, levelPoints));
      }
      return new BerkeleyDBLODNode(db, id, levels);
   }


   //   static BerkeleyDBLODNode create(final BerkeleyDBLOD db,
   //                                   final byte[] id,
   //                                   final int level,
   //                                   final List<Geodetic3D> points) {
   //      return new BerkeleyDBLODNode(db, id, level, points);
   //   }


   //   static BerkeleyDBLODNode fromDB(final com.sleepycat.je.Transaction txn,
   //                                   final BerkeleyDBLOD db,
   //                                   final byte[] id,
   //                                   final boolean loadLevels,
   //                                   final boolean loadPoints) {
   //      final DatabaseEntry keyEntry = new DatabaseEntry(id);
   //      final DatabaseEntry dataEntry = new DatabaseEntry();
   //
   //      final OperationStatus status = db.getNodeDB().get(txn, keyEntry, dataEntry, LockMode.DEFAULT);
   //      if (status == OperationStatus.SUCCESS) {
   //         return BerkeleyDBLODNode.fromDBData(txn, db, id, dataEntry.getData(), loadLevels, loadPoints);
   //      }
   //      return null;
   //   }


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

      return new BerkeleyDBLODNode(txn, db, id, pointsCount, levelsPointsCount, format, loadPoints);
   }

   private final BerkeleyDBLOD                _db;
   private final byte[]                       _id;
   private final int                          _levelsCount;
   private final List<BerkeleyDBLODNodeLevel> _levels;
   private final int[]                        _levelsPointsCount;
   private final int                          _pointsCount;
   private Sector                             _sector = null;


   //   private BerkeleyDBLODNode(final BerkeleyDBLOD db,
   //                             final byte[] id,
   //                             final int level,
   //                             final List<Geodetic3D> points) {
   //      _db = db;
   //      _id = id;
   //      _lodLevel = level;
   //      _pointsCount = points.size();
   //      _points = new ArrayList<Geodetic3D>(points);
   //      _format = null;
   //   }
   //
   //
   //   private BerkeleyDBLODNode(final BerkeleyDBLOD db,
   //                             final byte[] id,
   //                             final int level,
   //                             final int pointsCount,
   //                             final Format format) {
   //      _db = db;
   //      _id = id;
   //      _lodLevel = level;
   //      _pointsCount = pointsCount;
   //      _points = null;
   //      _format = format;
   //   }


   private BerkeleyDBLODNode(final BerkeleyDBLOD db,
                             final byte[] id,
                             final List<BerkeleyDBLODNodeLevel> levels) {
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
   }


   private BerkeleyDBLODNode(final com.sleepycat.je.Transaction txn,
                             final BerkeleyDBLOD db,
                             final byte[] id,
                             final int pointsCount,
                             final int[] levelsPointsCount,
                             final Format format,
                             final boolean loadPoints) {
      _db = db;
      _id = id;
      _pointsCount = pointsCount;

      _levelsCount = levelsPointsCount.length;
      _levelsPointsCount = levelsPointsCount;

      _levels = new ArrayList<BerkeleyDBLODNode.BerkeleyDBLODNodeLevel>(_levelsCount);
      for (int i = 0; i < _levelsCount; i++) {
         final BerkeleyDBLODNodeLevel level = BerkeleyDBLODNodeLevel.fromDB(txn, db, format, id, i, loadPoints,
                  _levelsPointsCount[i]);
         _levels.add(level);
      }
   }


   //   private List<Geodetic3D> loadPoints(final com.sleepycat.je.Transaction txn) {
   //      final Database nodeDataDB = _db.getNodeDataDB();
   //
   //      final DatabaseEntry dataEntry = new DatabaseEntry();
   //      final OperationStatus status = nodeDataDB.get(txn, createNodeDataKey(), dataEntry, LockMode.DEFAULT);
   //      if (status != OperationStatus.SUCCESS) {
   //         throw new RuntimeException("Unsupported status=" + status);
   //      }
   //
   //      final ByteBuffer byteBuffer = ByteBuffer.wrap(dataEntry.getData());
   //
   //
   //      final List<Geodetic3D> points = getPoints(byteBuffer, _format, _pointsCount);
   //
   //      if (_pointsCount != points.size()) {
   //         throw new RuntimeException("Inconsistency in pointsCount");
   //      }
   //
   //      return Collections.unmodifiableList(points);
   //   }


   //   private DatabaseEntry createNodeDataKey() {
   //      final int size = sizeOf(_id) + //
   //               sizeOf((byte) 255) + //
   //               sizeOf(_lodLevel);
   //      final ByteBuffer byteBuffer = ByteBuffer.allocate(size);
   //      byteBuffer.put(_id);
   //      byteBuffer.put((byte) 255);
   //      byteBuffer.putInt(_lodLevel);
   //      return new DatabaseEntry(byteBuffer.array());
   //   }


   private DatabaseEntry createNodeEntry(final Format format) {
      final byte version = 1;
      final byte subversion = 0;
      final byte formatID = format._formatID;

      final int levelsCount = _levels.size();
      final int entrySize = sizeOf(version) + //
                            sizeOf(subversion) + //
                            sizeOf(_pointsCount) + //
                            sizeOf(formatID) + //
                            sizeOf(levelsCount) + //
                            (sizeOfInt() * levelsCount);

      final ByteBuffer byteBuffer = ByteBuffer.allocate(entrySize);
      byteBuffer.put(version);
      byteBuffer.put(subversion);
      byteBuffer.putInt(_pointsCount);
      byteBuffer.put(formatID);
      byteBuffer.putInt(levelsCount);
      for (final BerkeleyDBLODNodeLevel level : _levels) {
         byteBuffer.putInt(level.getPointsCount());
      }

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


   //   private List<Geodetic3D> pvtGetPoints(final com.sleepycat.je.Transaction txn) {
   //      if (_points == null) {
   //         _points = loadPoints(txn);
   //      }
   //      return _points;
   //   }


   //   void addPoints(final com.sleepycat.je.Transaction txn,
   //                  final List<Geodetic3D> newPoints) {
   //      final List<Geodetic3D> mergedPoints = new ArrayList<Geodetic3D>(newPoints.size() + _pointsCount);
   //      mergedPoints.addAll(pvtGetPoints(txn));
   //      mergedPoints.addAll(newPoints);
   //      _points = mergedPoints;
   //      _pointsCount = mergedPoints.size();
   //   }


   @Override
   public String getID() {
      return Utils.toIDString(_id);
   }


   @Override
   public int getPointsCount() {
      return _pointsCount;
   }


   //   @Override
   //   public List<Geodetic3D> getPoints() {
   //      return getPoints(null);
   //   }
   //
   //
   //   @Override
   //   public List<Geodetic3D> getPoints(final PersistentLOD.Transaction transaction) {
   //            final BerkeleyDBTransaction berkeleyDBTransaction = (BerkeleyDBLOD.BerkeleyDBTransaction) transaction;
   //            final com.sleepycat.je.Transaction txn = (berkeleyDBTransaction == null) ? null : berkeleyDBTransaction._txn;
   //      return pvtGetPoints(txn);
   //   }


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
