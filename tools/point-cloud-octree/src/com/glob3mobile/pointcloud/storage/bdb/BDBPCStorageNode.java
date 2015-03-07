

package com.glob3mobile.pointcloud.storage.bdb;


//
//public class BDBPCStorageNode
//implements
//PCStorage.Node {
//
//   static void insertPoints(final Transaction txn,
//                            final BDBPCStorage storage,
//                            final TileHeader header,
//                            final PointsSet pointsSet) {
//      final byte[] id = header._id;
//
//      final BDBPCStorageNode ancestor = getAncestorOrSameLevel(txn, storage, id);
//      if (ancestor != null) {
//         // System.out.println("==> found ancestor (" + ancestor.getID() + ") for tile " + toString(id));
//
//         ancestor.mergePoints(txn, pointsSet);
//         return;
//      }
//
//      final List<BDBPCStorageNode> descendants = getDescendants(txn, storage, id, false);
//      if ((descendants != null) && !descendants.isEmpty()) {
//         splitPointsIntoDescendants(txn, storage, header, pointsSet, descendants);
//         return;
//      }
//
//      final BDBPCStorageNode tile = new BDBPCStorageNode(storage, id, header._sector, pointsSet);
//      tile.rawSave(txn);
//   }
//
//
//   private static BDBPCStorageNode getAncestorOrSameLevel(final Transaction txn,
//                                                          final BDBPCStorage storage,
//                                                          final byte[] id) {
//      byte[] ancestorId = id;
//      while (ancestorId != null) {
//         final BDBPCStorageNode ancestor = storage.readNode(txn, ancestorId, true);
//         if (ancestor != null) {
//            return ancestor;
//         }
//         ancestorId = Utils.removeTrailing(ancestorId);
//      }
//      return null;
//   }
//
//
//   private static List<BDBPCStorageNode> getDescendants(final Transaction txn,
//                                                        final BDBPCStorage storage,
//                                                        final byte[] id,
//                                                        final boolean loadPoints) {
//      final List<BDBPCStorageNode> result = new ArrayList<>();
//
//      final Database nodeDB = storage.getNodeDB();
//
//      final CursorConfig cursorConfig = new CursorConfig();
//
//      try (final Cursor cursor = nodeDB.openCursor(txn, cursorConfig)) {
//         final DatabaseEntry keyEntry = new DatabaseEntry(id);
//         final DatabaseEntry dataEntry = new DatabaseEntry();
//         final OperationStatus status = cursor.getSearchKeyRange(keyEntry, dataEntry, LockMode.DEFAULT);
//         if (status == OperationStatus.SUCCESS) {
//            byte[] key = keyEntry.getData();
//
//            if (!Utils.hasSamePrefix(key, id)) {
//               return result;
//            }
//            if (Utils.isGreaterThan(key, id)) {
//               result.add(fromDB(txn, storage, key, dataEntry.getData(), loadPoints));
//            }
//
//            while (cursor.getNext(keyEntry, dataEntry, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
//               key = keyEntry.getData();
//               if (!Utils.hasSamePrefix(key, id)) {
//                  return result;
//               }
//               if (Utils.isGreaterThan(key, id)) {
//                  result.add(fromDB(txn, storage, keyEntry.getData(), dataEntry.getData(), loadPoints));
//               }
//            }
//         }
//      }
//
//      //      final int REMOVE_DEBUG_CODE;
//      //      for (final BerkeleyDBMercatorTile descendant : result) {
//      //         if (!Utils.hasSamePrefix(id, descendant._id)) {
//      //            throw new RuntimeException("Logic error");
//      //         }
//      //         if (!Utils.isGreaterThan(descendant._id, id)) {
//      //            throw new RuntimeException("Logic error");
//      //         }
//      //      }
//
//      return result;
//   }
//
//
//   private static void splitPointsIntoDescendants(final Transaction txn,
//                                                  final BDBPCStorage storage,
//                                                  final TileHeader header,
//                                                  final PointsSet pointsSet,
//                                                  final List<BDBPCStorageNode> descendants) {
//      final List<PointData> points = new ArrayList<>(pointsSet._points);
//      for (final BDBPCStorageNode descendant : descendants) {
//         final PointsSet descendantPointsSet = extractPoints(descendant._sector, points);
//         if (descendantPointsSet != null) {
//            // System.out.println(">>> tile " + toString(header._id) + " split " + descendantPointsSet.size()
//            // + " points into descendant " + toString(descendant._id) + " (" + points.size()
//            // + " points not yet distributed)");
//
//            descendant.getPoints(txn); // force points load
//            descendant.mergePoints(txn, descendantPointsSet);
//            descendant._points = null; // release points' memory
//         }
//      }
//
//      if (!points.isEmpty()) {
//         final List<TileHeader> descendantsHeaders = descendantsHeadersOfLevel(header, header.getLevel() + 1);
//         for (final TileHeader descendantHeader : descendantsHeaders) {
//            final PointsSet descendantPointsSet = extractPoints(descendantHeader._sector, points);
//            if (descendantPointsSet != null) {
//               // System.out.println(">>> 2ND tile " + toString(header._id) + " split " + descendantPointsSet.size()
//               // + " points into descendant " + toString(descendantHeader._id) + " (" + points.size()
//               // + " points not yet distributed)");
//
//               insertPoints(txn, storage, descendantHeader, descendantPointsSet);
//            }
//         }
//
//         if (!points.isEmpty()) {
//            throw new RuntimeException("Logic error!");
//         }
//      }
//   }
//
//
//   static BDBPCStorageNode fromDB(final Transaction txn,
//                                  final BDBPCStorage storage,
//                                  final byte[] id,
//                                  final byte[] data,
//                                  final boolean loadPoints) {
//      final ByteBuffer byteBuffer = ByteBuffer.wrap(data);
//
//      final byte version = byteBuffer.get();
//      if (version != 1) {
//         throw new RuntimeException("Invalid version=" + version);
//      }
//      final byte subversion = byteBuffer.get();
//      if (subversion != 0) {
//         throw new RuntimeException("Invalid subversion=" + subversion);
//      }
//
//      final Sector sector = ByteBufferUtils.getSector(byteBuffer);
//
//      final int pointsCount = byteBuffer.getInt();
//      final Geodetic3D averagePoint = ByteBufferUtils.getGeodetic3D(byteBuffer);
//
//      final byte formatID = byteBuffer.get();
//      final Format format = Format.getFromID(formatID);
//
//      final BDBPCStorageNode tile = new BDBPCStorageNode(storage, id, sector, pointsCount, averagePoint, format);
//      if (loadPoints) {
//         tile._points = tile.loadPoints(txn);
//      }
//      return tile;
//   }
//
//
//   private final BDBPCStorage    _storage;
//   private final byte[]          _id;
//   private final Sector          _sector;
//   private final int             _pointsCount;
//   private final List<PointData> _points;
//   private final Geodetic3D      _averagePoint;
//   private final Format          _format;
//
//
//   private BDBPCStorageNode(final BDBPCStorage storage,
//                            final byte[] id,
//                            final Sector sector,
//                            final PointsSet pointsSet) {
//      _storage = storage;
//      _id = id;
//      _sector = sector;
//      _pointsCount = pointsSet.size();
//      _points = pointsSet._points;
//      _averagePoint = pointsSet._averagePoint;
//      _format = null;
//
//      //      final int remove_debug_code;
//      //      checkConsistency();
//   }
//
//
//   private void mergePoints(final Transaction txn,
//                            final PointsSet newPointsSet) {
//      final int mergedPointsLength = getPointsCount() + newPointsSet.size();
//      if (mergedPointsLength > _storage.getMaxPointsPerTile()) {
//         split(txn, newPointsSet);
//      }
//      else {
//         updateFromPoints(txn, newPointsSet);
//      }
//   }
//
//
//   @Override
//   public int getPointsCount() {
//      return _pointsCount;
//   }
//
//
//   @Override
//   public String getID() {
//      return Utils.toIDString(_id);
//   }
//
//
//   @Override
//   public Sector getSector() {
//      return _sector;
//   }
//
//
//   @Override
//   public int getDepth() {
//      return _id.length;
//   }
//
//
//   @Override
//   public Geodetic3D getAveragePoint() {
//      return _averagePoint;
//   }
//
//
//   public List<PointData> getPoints() {
//      return getPoints(null);
//   }
//
//
//   private List<PointData> getPoints(final Transaction txn) {
//      if (_points == null) {
//         _points = loadPoints(txn);
//      }
//      return _points;
//   }
//
//
//   private List<PointData> loadPoints(final Transaction txn) {
//      final Database nodeDataDB = _storage.getNodeDataDB();
//
//      final DatabaseEntry dataEntry = new DatabaseEntry();
//      final OperationStatus status = nodeDataDB.get(txn, new DatabaseEntry(_id), dataEntry, LockMode.DEFAULT);
//      if (status != OperationStatus.SUCCESS) {
//         throw new RuntimeException("Unsupported status=" + status);
//      }
//
//      final ByteBuffer byteBuffer = ByteBuffer.wrap(dataEntry.getData());
//
//
//      final List<PointData> points = ByteBufferUtils.getPoints(byteBuffer, _format, _pointsCount);
//
//      if (_pointsCount != points.size()) {
//         throw new RuntimeException("Inconsistency in pointsCount");
//      }
//
//      return Collections.unmodifiableList(points);
//   }
//
//
//   private void rawSave(final Transaction txn) {
//      //      for (final Geodetic3D p : _points) {
//      //         if (!_sector.contains(p._latitude, p._longitude)) {
//      //            throw new RuntimeException("LOGIC ERROR!!");
//      //         }
//      //      }
//
//      if (getPoints().size() >= _storage.getMaxPointsPerTile()) {
//         split(txn);
//         return;
//      }
//
//      if (getPoints().size() >= _storage.getMaxPointsPerTile()) {
//         System.out.println("***** logic error, tile " + getID() + " has more points than threshold (" + getPoints().size() + ">"
//                            + _storage.getMaxPointsPerTile() + ")");
//      }
//
//
//      final Database nodeDB = _storage.getNodeDB();
//      final Database nodeDataDB = _storage.getNodeDataDB();
//
//      final Format format = Format.LatLonHeight;
//
//      final byte[] id = getBerkeleyDBKey();
//
//      final DatabaseEntry key = new DatabaseEntry(id);
//
//      OperationStatus status = nodeDB.put(txn, key, new DatabaseEntry(createNodeEntry(format)));
//      if (status != OperationStatus.SUCCESS) {
//         throw new RuntimeException("Status not supported: " + status);
//      }
//      status = nodeDataDB.put(txn, key, new DatabaseEntry(createNodeDataEntry(format)));
//      if (status != OperationStatus.SUCCESS) {
//         throw new RuntimeException("Status not supported: " + status);
//      }
//
//      final boolean checkInvariants = false;
//      if (checkInvariants) {
//         checkInvariants(txn);
//      }
//   }
//
//
//   private static PointsSet extractPoints(final Sector sector,
//                                          final List<PointData> points) {
//      double sumLatitudeInRadians = 0;
//      double sumLongitudeInRadians = 0;
//      double sumHeight = 0;
//
//      final List<PointData> extracted = new ArrayList<>();
//
//      final Iterator<PointData> iterator = points.iterator();
//      while (iterator.hasNext()) {
//         final PointData point = iterator.next();
//         final Geodetic3D position = point._position;
//         if (sector.contains(position._latitude, position._longitude)) {
//            extracted.add(point);
//
//            sumLatitudeInRadians += position._latitude._radians;
//            sumLongitudeInRadians += position._longitude._radians;
//            sumHeight += position._height;
//
//            iterator.remove();
//         }
//      }
//
//
//      final int extractedSize = extracted.size();
//      if (extractedSize == 0) {
//         return null;
//      }
//
//      final double averageLatitudeInRadians = sumLatitudeInRadians / extractedSize;
//      final double averageLongitudeInRadians = sumLongitudeInRadians / extractedSize;
//      final double averageHeight = sumHeight / extractedSize;
//
//      final Geodetic3D averagePoint = Geodetic3D.fromRadians(averageLatitudeInRadians, averageLongitudeInRadians, averageHeight);
//
//      return new PointsSet(extracted, averagePoint);
//   }
//
//
//}
