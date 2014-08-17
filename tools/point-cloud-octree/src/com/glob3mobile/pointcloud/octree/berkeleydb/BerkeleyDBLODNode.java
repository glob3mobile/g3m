

package com.glob3mobile.pointcloud.octree.berkeleydb;

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


   private static final int MAX_POINTS_IN_HEADER = 512;


   static BerkeleyDBLODNode create(final BerkeleyDBLOD db,
                                   final byte[] id,
                                   final int level,
                                   final List<Geodetic3D> points) {
      return new BerkeleyDBLODNode(db, id, level, points);
   }


   //   static BerkeleyDBLODNode fromDB(final com.sleepycat.je.Transaction txn,
   //                                   final BerkeleyDBLOD db,
   //                                   final byte[] id,
   //                                   final boolean loadPoints) {
   //      final DatabaseEntry keyEntry = new DatabaseEntry(id);
   //      final DatabaseEntry dataEntry = new DatabaseEntry();
   //
   //      final OperationStatus status = db.getNodeDB().get(txn, keyEntry, dataEntry, LockMode.DEFAULT);
   //      if (status == OperationStatus.SUCCESS) {
   //         return BerkeleyDBLODNode.fromDBData(txn, db, id, dataEntry.getData(), loadPoints);
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

      final int level = byteBuffer.getInt();

      final int pointsCount = byteBuffer.getInt();

      final byte formatID = byteBuffer.get();
      final Format format = Format.getFromID(formatID);

      final int absPointsCount = Math.abs(pointsCount);
      final BerkeleyDBLODNode tile = new BerkeleyDBLODNode(db, id, level, absPointsCount, format);
      if (pointsCount < 0) {
         tile._points = ByteBufferUtils.getPoints(byteBuffer, format, absPointsCount);
      }
      else {
         if (loadPoints) {
            tile._points = tile.loadPoints(txn);
         }
      }
      return tile;
   }

   private final BerkeleyDBLOD _db;
   private final byte[]        _id;
   private final int           _lodLevel;
   private final int           _pointsCount;
   private List<Geodetic3D>    _points;
   private final Format        _format;
   private Sector              _sector = null;


   private BerkeleyDBLODNode(final BerkeleyDBLOD db,
                             final byte[] id,
                             final int level,
                             final List<Geodetic3D> points) {
      _db = db;
      _id = id;
      _lodLevel = level;
      _pointsCount = points.size();
      _points = new ArrayList<Geodetic3D>(points);
      _format = null;
   }


   private BerkeleyDBLODNode(final BerkeleyDBLOD db,
                             final byte[] id,
                             final int level,
                             final int pointsCount,
                             final Format format) {
      _db = db;
      _id = id;
      _lodLevel = level;
      _pointsCount = pointsCount;
      _points = null;
      _format = format;
   }


   private List<Geodetic3D> loadPoints(final com.sleepycat.je.Transaction txn) {
      final Database nodeDataDB = _db.getNodeDataDB();

      final DatabaseEntry dataEntry = new DatabaseEntry();
      final OperationStatus status = nodeDataDB.get(txn, createNodeDataKey(), dataEntry, LockMode.DEFAULT);
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


   private DatabaseEntry createNodeDataKey() {
      final int size = ByteBufferUtils.sizeOf(_id) + //
                       ByteBufferUtils.sizeOf((byte) 255) + //
                       ByteBufferUtils.sizeOf(_lodLevel);
      final ByteBuffer byteBuffer = ByteBuffer.allocate(size);
      byteBuffer.put(_id);
      byteBuffer.put((byte) 255);
      byteBuffer.putInt(_lodLevel);
      return new DatabaseEntry(byteBuffer.array());
   }


   private DatabaseEntry createNodeEntry(final Format format) {
      final byte version = 1;
      final byte subversion = 0;
      final byte formatID = format._formatID;

      final int entrySize = ByteBufferUtils.sizeOf(version) + //
               ByteBufferUtils.sizeOf(subversion) + //
               ByteBufferUtils.sizeOf(_lodLevel) + //
                            ByteBufferUtils.sizeOf(_pointsCount) + //
                            ByteBufferUtils.sizeOf(formatID);

      final ByteBuffer byteBuffer = ByteBuffer.allocate(entrySize);
      byteBuffer.put(version);
      byteBuffer.put(subversion);
      byteBuffer.putInt(_lodLevel);
      byteBuffer.putInt(_pointsCount);
      byteBuffer.put(formatID);

      return new DatabaseEntry(byteBuffer.array());
   }


   private DatabaseEntry createNodeWithPointsEntry(final Format format) {
      final byte version = 1;
      final byte subversion = 0;
      final byte formatID = format._formatID;

      final int entrySize = ByteBufferUtils.sizeOf(version) + //
                            ByteBufferUtils.sizeOf(subversion) + //
                            ByteBufferUtils.sizeOf(_lodLevel) + //
                            ByteBufferUtils.sizeOf(_pointsCount) + //
                            ByteBufferUtils.sizeOf(formatID) + //
                            ByteBufferUtils.sizeOf(format, _points);

      final ByteBuffer byteBuffer = ByteBuffer.allocate(entrySize);
      byteBuffer.put(version);
      byteBuffer.put(subversion);
      byteBuffer.putInt(_lodLevel);
      byteBuffer.putInt(_pointsCount * -1); // negative pointsCount means the points are store in the header
      byteBuffer.put(formatID);
      ByteBufferUtils.put(byteBuffer, format, _points);

      return new DatabaseEntry(byteBuffer.array());
   }


   private DatabaseEntry createNodeDataEntry(final Format format) {
      final int entrySize = ByteBufferUtils.sizeOf(format, _points);
      final ByteBuffer byteBuffer = ByteBuffer.allocate(entrySize);
      ByteBufferUtils.put(byteBuffer, format, _points);
      return new DatabaseEntry(byteBuffer.array());
   }


   void save(final com.sleepycat.je.Transaction txn) {
      final Database nodeDB = _db.getNodeDB();
      final Database nodeDataDB = _db.getNodeDataDB();

      final Format format = Format.LatLonHeight;

      final DatabaseEntry idEntry = new DatabaseEntry(_id);
      if (getPointsCount() <= MAX_POINTS_IN_HEADER) {
         nodeDB.put(txn, idEntry, createNodeWithPointsEntry(format));
      }
      else {
         nodeDB.put(txn, idEntry, createNodeEntry(format));
         nodeDataDB.put(txn, createNodeDataKey(), createNodeDataEntry(format));
      }
   }


   private List<Geodetic3D> pvtGetPoints(final com.sleepycat.je.Transaction txn) {
      if (_points == null) {
         _points = loadPoints(txn);
      }
      return _points;
   }


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


   @Override
   public List<Geodetic3D> getPoints() {
      return getPoints(null);
   }


   @Override
   public List<Geodetic3D> getPoints(final PersistentLOD.Transaction transaction) {
      final BerkeleyDBTransaction berkeleyDBTransaction = (BerkeleyDBLOD.BerkeleyDBTransaction) transaction;
      final com.sleepycat.je.Transaction txn = (berkeleyDBTransaction == null) ? null : berkeleyDBTransaction._txn;
      return pvtGetPoints(txn);
   }


   @Override
   public Sector getSector() {
      if (_sector == null) {
         _sector = TileHeader.sectorFor(_id);
      }
      return _sector;
   }


   public int getLODLevel() {
      return _lodLevel;
   }


   @Override
   public String toString() {
      return "[BerkeleyDBLODNode id=" + Utils.toIDString(_id) + ", lodLevel=" + _lodLevel + ", points=" + _pointsCount + "]";
   }


   @Override
   public int getDepth() {
      return _id.length;
   }

}
