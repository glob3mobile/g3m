

package com.glob3mobile.pointcloud.octree.berkeleydb;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.glob3mobile.pointcloud.octree.Geodetic3D;
import com.glob3mobile.pointcloud.octree.PersistentLOD;
import com.glob3mobile.pointcloud.octree.Sector;
import com.glob3mobile.pointcloud.octree.Utils;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;
import com.sleepycat.je.Transaction;


public class BerkeleyDBLODNode
         implements
            PersistentLOD.Node {


   static BerkeleyDBLODNode create(final BerkeleyDBLOD db,
                                   final byte[] id,
                                   final boolean dirty,
                                   final List<Geodetic3D> points) {
      return new BerkeleyDBLODNode(db, id, dirty, points);
   }


   static BerkeleyDBLODNode fromDB(final com.sleepycat.je.Transaction txn,
                                   final BerkeleyDBLOD db,
                                   final byte[] id,
                                   final boolean loadPoints) {
      final DatabaseEntry keyEntry = new DatabaseEntry(id);
      final DatabaseEntry dataEntry = new DatabaseEntry();

      final OperationStatus status = db.getNodeDB().get(txn, keyEntry, dataEntry, LockMode.DEFAULT);
      if (status == OperationStatus.SUCCESS) {
         return BerkeleyDBLODNode.fromDBData(txn, db, id, dataEntry.getData(), loadPoints);
      }
      return null;
   }


   static BerkeleyDBLODNode fromDB(final com.sleepycat.je.Transaction txn,
                                   final BerkeleyDBLOD db,
                                   final byte[] id,
                                   final byte[] data,
                                   final boolean loadPoints) {
      return BerkeleyDBLODNode.fromDBData(txn, db, id, data, loadPoints);
   }


   private static BerkeleyDBLODNode fromDBData(final Transaction txn,
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

      final boolean dirty = byteBuffer.get() != 0;

      final int pointsCount = byteBuffer.getInt();

      final byte formatID = byteBuffer.get();
      final Format format = Format.getFromID(formatID);

      final BerkeleyDBLODNode tile = new BerkeleyDBLODNode(db, id, dirty, pointsCount, format);

      if (loadPoints) {
         tile._points = tile.loadPoints(txn);
      }
      return tile;
   }


   private final BerkeleyDBLOD _db;
   private final byte[]        _id;
   private boolean             _dirty;
   private int                 _pointsCount;
   private List<Geodetic3D>    _points;
   private final Format        _format;
   private Sector              _sector = null;


   private BerkeleyDBLODNode(final BerkeleyDBLOD db,
                             final byte[] id,
                             final boolean dirty,
                             final List<Geodetic3D> points) {
      _db = db;
      _id = id;
      _dirty = dirty;
      _pointsCount = points.size();
      _points = new ArrayList<Geodetic3D>(points);
      _format = null;
   }


   private BerkeleyDBLODNode(final BerkeleyDBLOD db,
                             final byte[] id,
                             final boolean dirty,
                             final int pointsCount,
                             final Format format) {
      _db = db;
      _id = id;
      _dirty = dirty;
      _pointsCount = pointsCount;
      _points = null;
      _format = format;
   }


   private List<Geodetic3D> loadPoints(final Transaction txn) {
      final Database nodeDataDB = _db.getNodeDataDB();

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


   private byte[] createNodeEntry(final Format format) {
      final byte version = 1;
      final byte subversion = 0;
      final byte formatID = format._formatID;

      final byte dirtyByte = _dirty ? (byte) 1 : (byte) 0;

      final int entrySize = ByteBufferUtils.sizeOf(version) + //
               ByteBufferUtils.sizeOf(subversion) + //
                            ByteBufferUtils.sizeOf(dirtyByte) + //
                            ByteBufferUtils.sizeOf(_pointsCount) + //
                            ByteBufferUtils.sizeOf(formatID);

      final ByteBuffer byteBuffer = ByteBuffer.allocate(entrySize);
      byteBuffer.put(version);
      byteBuffer.put(subversion);
      byteBuffer.put(dirtyByte);
      byteBuffer.putInt(_pointsCount);
      byteBuffer.put(formatID);

      return byteBuffer.array();
   }


   private byte[] createNodeDataEntry(final Format format) {
      final int entrySize = ByteBufferUtils.sizeOf(format, _points);
      final ByteBuffer byteBuffer = ByteBuffer.allocate(entrySize);
      ByteBufferUtils.put(byteBuffer, format, _points);
      return byteBuffer.array();
   }


   void save(final Transaction txn) {
      //      if (getPoints().size() >= _octree.getMaxPointsPerTile()) {
      //         split(txn);
      //         return;
      //      }
      //
      //      if (getPoints().size() >= _octree.getMaxPointsPerTile()) {
      //         System.out.println("***** logic error, tile " + getID() + " has more points than threshold (" + getPoints().size() + ">"
      //                            + _octree.getMaxPointsPerTile() + ")");
      //      }


      final Database nodeDB = _db.getNodeDB();
      final Database nodeDataDB = _db.getNodeDataDB();

      final Format format = Format.LatLonHeight;

      final DatabaseEntry key = new DatabaseEntry(_id);

      nodeDB.put(txn, key, new DatabaseEntry(createNodeEntry(format)));
      nodeDataDB.put(txn, key, new DatabaseEntry(createNodeDataEntry(format)));

      //      final boolean checkInvariants = false;
      //      if (checkInvariants) {
      //         checkInvariants(txn);
      //      }
   }


   private List<Geodetic3D> getPoints(final Transaction txn) {
      if (_points == null) {
         _points = loadPoints(txn);
      }
      return _points;
   }


   void addPoints(final Transaction txn,
                  final List<Geodetic3D> newPoints) {
      final List<Geodetic3D> mergedPoints = new ArrayList<Geodetic3D>(newPoints.size() + _pointsCount);
      mergedPoints.addAll(getPoints(txn));
      mergedPoints.addAll(newPoints);
      _points = mergedPoints;
      _pointsCount = mergedPoints.size();
   }


   void setDirty(final boolean dirty) {
      _dirty = dirty;
   }


   @Override
   public String getID() {
      return Utils.toIDString(_id);
   }


   @Override
   public boolean isDirty() {
      return _dirty;
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
   public Sector getSector() {
      if (_sector == null) {
         _sector = TileHeader.sectorFor(_id);
      }
      return _sector;
   }


   @Override
   public int getLevel() {
      return _id.length;
   }


}
