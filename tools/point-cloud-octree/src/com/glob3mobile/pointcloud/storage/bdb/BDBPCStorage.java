

package com.glob3mobile.pointcloud.storage.bdb;


//
//public class BDBPCStorage
//implements
//PCStorage {
//
//
//   private static final float          NO_Intensity            = Float.NaN;
//   private static final Classification NO_Classification       = null;
//   private static final Color          NO_Color                = null;
//
//
//   private static final String         NODE_DATABASE_NAME      = "Node";
//   private static final String         NODE_DATA_DATABASE_NAME = "NodeData";
//
//
//   private final String                _cloudName;
//   private final int                   _maxPointsPerTitle;
//
//
//   private final boolean               _hasIntensity;
//   private final boolean               _hasClassification;
//   private final boolean               _hasColor;
//
//   private final int                   _maxBufferSize;
//   private final List<PointData>       _buffer;
//
//   private double                      _minLatitudeInRadians;
//   private double                      _minLongitudeInRadians;
//   private double                      _minHeight;
//   private double                      _maxLatitudeInRadians;
//   private double                      _maxLongitudeInRadians;
//   private double                      _maxHeight;
//   private double                      _sumLatitudeInRadians;
//   private double                      _sumLongitudeInRadians;
//   private double                      _sumHeight;
//
//
//   private final Environment           _env;
//   private final Database              _nodeDB;
//   private final Database              _nodeDataDB;
//   private final boolean               _readOnly;
//   private final File                  _cachedStatisticsFile;
//
//
//   private BDBPCStorage(final String cloudDirectory,
//                        final String cloudName,
//                        final boolean createIfNotExists,
//                        final int maxBufferSize,
//                        final int maxPointsPerTitle,
//                        final boolean readOnly,
//                        final long cacheSizeInBytes,
//                        final boolean hasIntensity,
//                        final boolean hasClassification,
//                        final boolean hasColor) {
//      _hasIntensity = hasIntensity;
//      _hasClassification = hasClassification;
//      _hasColor = hasColor;
//
//
//      final File envHome = new File(cloudDirectory, cloudName);
//      if (createIfNotExists) {
//         if (!envHome.exists()) {
//            envHome.mkdirs();
//         }
//      }
//
//      final String readOnlyMsg = readOnly ? " read-only" : " read-write";
//      System.out.println("- opening" + readOnlyMsg + " cloud name \"" + cloudName + "\" (" + envHome.getAbsoluteFile() + ")");
//
//      _readOnly = readOnly;
//      _cloudName = cloudName;
//
//      _maxBufferSize = maxBufferSize;
//      _buffer = new ArrayList<>(maxBufferSize);
//      resetBufferBounds();
//
//      _maxPointsPerTitle = maxPointsPerTitle;
//
//
//      final EnvironmentConfig envConfig = new EnvironmentConfig();
//      envConfig.setAllowCreate(createIfNotExists);
//      envConfig.setTransactional(true);
//      envConfig.setReadOnly(readOnly);
//      if (cacheSizeInBytes > 0) {
//         envConfig.setCacheSize(cacheSizeInBytes);
//      }
//      envConfig.setSharedCache(true);
//      _env = new Environment(envHome, envConfig);
//
//      final DatabaseConfig dbConfig = new DatabaseConfig();
//      dbConfig.setAllowCreate(createIfNotExists);
//      dbConfig.setTransactionalVoid(true);
//      dbConfig.setKeyPrefixing(true);
//      dbConfig.setReadOnly(readOnly);
//
//
//      _nodeDB = _env.openDatabase(null, NODE_DATABASE_NAME, dbConfig);
//      _nodeDataDB = _env.openDatabase(null, NODE_DATA_DATABASE_NAME, dbConfig);
//
//      _cachedStatisticsFile = new File(cloudDirectory, "_stats_" + cloudName + ".ser");
//   }
//
//
//   @Override
//   public boolean hasIntensity() {
//      return _hasIntensity;
//   }
//
//
//   @Override
//   public boolean hasClassification() {
//      return _hasClassification;
//   }
//
//
//   @Override
//   public boolean hasColor() {
//      return _hasColor;
//   }
//
//
//   private String getShapeDescription() {
//      final StringBuilder sb = new StringBuilder();
//      sb.append("Color");
//
//      if (_hasIntensity) {
//         sb.append("/Intensity");
//      }
//      if (_hasClassification) {
//         sb.append("/Classification");
//      }
//      if (_hasColor) {
//         sb.append("/Color");
//      }
//      return sb.toString();
//   }
//
//
//   @Override
//   public void addPoint(final Geodetic3D position) {
//      if (_hasIntensity || _hasClassification || _hasColor) {
//         throw new RuntimeException("Can't add Position, This Storage needs: " + getShapeDescription());
//      }
//      rawAddPoint(position, NO_Intensity, NO_Classification, NO_Color);
//   }
//
//
//   @Override
//   public void addPoint(final Geodetic3D position,
//                        final float intensity) {
//      if (!_hasIntensity || _hasClassification || _hasColor) {
//         throw new RuntimeException("Can't add Position/Intensity, This Storage needs: " + getShapeDescription());
//      }
//      rawAddPoint(position, intensity, NO_Classification, NO_Color);
//   }
//
//
//   @Override
//   public void addPoint(final Geodetic3D position,
//                        final Classification classification) {
//      if (_hasIntensity || !_hasClassification || _hasColor) {
//         throw new RuntimeException("Can't add only a Position/Classification, This Storage needs: " + getShapeDescription());
//      }
//      rawAddPoint(position, NO_Intensity, classification, NO_Color);
//   }
//
//
//   @Override
//   public void addPoint(final Geodetic3D position,
//                        final Color color) {
//      if (_hasIntensity || _hasClassification || !_hasColor) {
//         throw new RuntimeException("Can't add Position/Color, This Storage needs: " + getShapeDescription());
//      }
//      rawAddPoint(position, NO_Intensity, NO_Classification, color);
//   }
//
//
//   @Override
//   public void addPoint(final Geodetic3D position,
//                        final float intensity,
//                        final Classification classification) {
//      if (!_hasIntensity || !_hasClassification || _hasColor) {
//         throw new RuntimeException("Can't add Position/Intensity/Classification, This Storage needs: " + getShapeDescription());
//      }
//      rawAddPoint(position, intensity, classification, NO_Color);
//   }
//
//
//   @Override
//   public void addPoint(final Geodetic3D position,
//                        final float intensity,
//                        final Color color) {
//      if (!_hasIntensity || _hasClassification || !_hasColor) {
//         throw new RuntimeException("Can't add Position/Intensity/Color, This Storage needs: " + getShapeDescription());
//      }
//      rawAddPoint(position, intensity, NO_Classification, color);
//   }
//
//
//   @Override
//   public void addPoint(final Geodetic3D position,
//                        final Classification classification,
//                        final Color color) {
//      if (_hasIntensity || !_hasClassification || !_hasColor) {
//         throw new RuntimeException("Can't add Position/Classification/Color, This Storage needs: " + getShapeDescription());
//      }
//      rawAddPoint(position, NO_Intensity, classification, color);
//   }
//
//
//   @Override
//   public void addPoint(final Geodetic3D position,
//                        final float intensity,
//                        final Classification classification,
//                        final Color color) {
//      if (!_hasIntensity || !_hasClassification || !_hasColor) {
//         throw new RuntimeException("Can't add Position/Intensity/Classification/Color, This Storage needs: "
//                  + getShapeDescription());
//      }
//      rawAddPoint(position, intensity, classification, color);
//   }
//
//
//   private void rawAddPoint(final Geodetic3D position,
//                            final float intensity,
//                            final Classification classification,
//                            final Color color) {
//      if (_readOnly) {
//         throw new RuntimeException("Can't add points to readonly Storage");
//      }
//
//      _buffer.add(new PointData(position, intensity, classification, color));
//
//      final double latitudeInRadians = position._latitude._radians;
//      final double longitudeInRadians = position._longitude._radians;
//      final double height = position._height;
//
//      _sumLatitudeInRadians += latitudeInRadians;
//      _sumLongitudeInRadians += longitudeInRadians;
//      _sumHeight += height;
//
//
//      if (latitudeInRadians < _minLatitudeInRadians) {
//         _minLatitudeInRadians = latitudeInRadians;
//      }
//      if (latitudeInRadians > _maxLatitudeInRadians) {
//         _maxLatitudeInRadians = latitudeInRadians;
//      }
//
//      if (longitudeInRadians < _minLongitudeInRadians) {
//         _minLongitudeInRadians = longitudeInRadians;
//      }
//      if (longitudeInRadians > _maxLongitudeInRadians) {
//         _maxLongitudeInRadians = longitudeInRadians;
//      }
//
//      if (height < _minHeight) {
//         _minHeight = height;
//      }
//      if (height > _maxHeight) {
//         _maxHeight = height;
//      }
//
//
//      if (_buffer.size() == _maxBufferSize) {
//         flush();
//      }
//   }
//
//
//   @Override
//   synchronized public void close() {
//      flush();
//      _nodeDataDB.close();
//      _nodeDB.close();
//      _env.close();
//   }
//
//
//   private void deleteCachedStatistics() {
//      if (_cachedStatisticsFile.exists()) {
//         _cachedStatisticsFile.delete();
//      }
//   }
//
//
//   @Override
//   synchronized public void flush() {
//      final int bufferSize = _buffer.size();
//      if (bufferSize > 0) {
//         deleteCachedStatistics();
//
//         final Sector targetSector = Sector.fromRadians( //
//                  _minLatitudeInRadians, _minLongitudeInRadians, //
//                  _maxLatitudeInRadians, _maxLongitudeInRadians);
//
//         //         final Sector boundsSector = Sector.getBounds(_buffer);
//         //         if (!targetSector.equals(boundsSector)) {
//         //            throw new RuntimeException("LOGIC ERROR");
//         //         }
//
//
//         final TileHeader header = TileHeader.deepestEnclosingTileHeader(targetSector);
//
//
//         final double averageLatitudeInRadians = _sumLatitudeInRadians / bufferSize;
//         final double averageLongitudeInRadians = _sumLongitudeInRadians / bufferSize;
//         final double averageHeight = _sumHeight / bufferSize;
//
//         final Geodetic3D averagePoint = Geodetic3D.fromRadians(averageLatitudeInRadians, averageLongitudeInRadians,
//                  averageHeight);
//
//
//         final TransactionConfig txnConfig = new TransactionConfig();
//         final Transaction txn = _env.beginTransaction(null, txnConfig);
//
//         final PointsSet pointsSet = new PointsSet(new ArrayList<>(_buffer), averagePoint);
//         BDBPCStorageNode.insertPoints(txn, this, header, pointsSet);
//
//         txn.commit();
//
//         _buffer.clear();
//         resetBufferBounds();
//      }
//   }
//
//
//   private void resetBufferBounds() {
//      _minLatitudeInRadians = Double.POSITIVE_INFINITY;
//      _minLongitudeInRadians = Double.POSITIVE_INFINITY;
//      _minHeight = Double.POSITIVE_INFINITY;
//
//      _maxLatitudeInRadians = Double.NEGATIVE_INFINITY;
//      _maxLongitudeInRadians = Double.NEGATIVE_INFINITY;
//      _maxHeight = Double.NEGATIVE_INFINITY;
//
//      _sumLatitudeInRadians = 0.0;
//      _sumLongitudeInRadians = 0.0;
//      _sumHeight = 0.0;
//   }
//
//
//   BDBPCStorageNode readNode(final Transaction txn,
//                             final byte[] id,
//                             final boolean loadPoints) {
//      final DatabaseEntry keyEntry = new DatabaseEntry(id);
//      final DatabaseEntry dataEntry = new DatabaseEntry();
//
//      final OperationStatus status = _nodeDB.get(txn, keyEntry, dataEntry, LockMode.DEFAULT);
//      switch (status) {
//         case SUCCESS:
//            return BDBPCStorageNode.fromDB(txn, this, id, dataEntry.getData(), loadPoints);
//         case NOTFOUND:
//            return null;
//         default:
//            throw new RuntimeException("Status not supported: " + status);
//      }
//   }
//
//
//   Database getNodeDB() {
//      return _nodeDB;
//   }
//
//
//   Database getNodeDataDB() {
//      return _nodeDataDB;
//   }
//
//
//   int getMaxPointsPerTile() {
//      return _maxPointsPerTitle;
//   }
//
//
//}

