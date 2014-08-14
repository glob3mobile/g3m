

package com.glob3mobile.pointcloud.octree.berkeleydb;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import com.glob3mobile.pointcloud.octree.Geodetic3D;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.Transaction;


public class BerkeleyDBLODNode {


   static BerkeleyDBLODNode create(final BerkeleyDBLOD db,
                                   final byte[] id,
                                   final List<Geodetic3D> points,
                                   final boolean dirty) {
      return new BerkeleyDBLODNode(db, id, points, dirty);
   }


   private final BerkeleyDBLOD    _db;
   private final byte[]           _id;
   private final int              _pointsCount;
   private final List<Geodetic3D> _points;
   private final boolean          _dirty;


   private BerkeleyDBLODNode(final BerkeleyDBLOD db,
                             final byte[] id,
                             final List<Geodetic3D> points,
                             final boolean dirty) {
      _db = db;
      _id = id;
      _pointsCount = points.size();
      _points = new ArrayList<Geodetic3D>(points);
      _dirty = dirty;
   }


   private byte[] createNodeEntry(final Format format) {
      final byte version = 1;
      final byte subversion = 0;
      final byte formatID = format._formatID;

      final int entrySize = ByteBufferUtils.sizeOf(version) + //
                            ByteBufferUtils.sizeOf(subversion) + //
                            //ByteBufferUtils.sizeOf(_sector) + //
                            ByteBufferUtils.sizeOf(_pointsCount) + //
                            ByteBufferUtils.sizeOf(formatID);

      final ByteBuffer byteBuffer = ByteBuffer.allocate(entrySize);
      byteBuffer.put(version);
      byteBuffer.put(subversion);
      final byte dirtyByte = _dirty ? (byte) 1 : (byte) 0;
      byteBuffer.put(dirtyByte);
      //ByteBufferUtils.put(byteBuffer, _sector);
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


}
