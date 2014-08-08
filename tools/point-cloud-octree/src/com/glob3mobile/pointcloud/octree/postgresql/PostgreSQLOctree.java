

package com.glob3mobile.pointcloud.octree.postgresql;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.glob3.mobile.generated.Angle;
import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.Sector;

import com.glob3mobile.pointcloud.octree.JDBCUtils;
import com.glob3mobile.pointcloud.octree.PersistentOctree;


public class PostgreSQLOctree
         implements
            PersistentOctree {


   private static final int DEFAULT_BUFFER_SIZE = 1024 * 64;


   //   private static final int DEFAULT_BUFFER_SIZE = 1024 * 16;


   public static PostgreSQLOctree get(final String server,
                                      final String db,
                                      final String user,
                                      final String password,
                                      final String cloudName,
                                      final boolean createIfNotExists) {
      return get(server, db, user, password, cloudName, createIfNotExists, DEFAULT_BUFFER_SIZE);
   }


   public static PostgreSQLOctree get(final String server,
                                      final String db,
                                      final String user,
                                      final String password,
                                      final String cloudName,
                                      final boolean createIfNotExists,
                                      final int bufferSize) {
      return new PostgreSQLOctree(server, db, user, password, cloudName, createIfNotExists, bufferSize);
   }


   private final Connection       _connection;
   private final String           _cloudName;
   private List<PostgreSQLNode>   _nodes;

   private final List<Geodetic3D> _buffer;
   private final int              _bufferSize;
   private double                 _minLatitudeInRadians;
   private double                 _minLongitudeInRadians;
   private double                 _minHeight;
   private double                 _maxLatitudeInRadians;
   private double                 _maxLongitudeInRadians;
   private double                 _maxHeight;
   private double                 _sumLatitudeInRadians;
   private double                 _sumLongitudeInRadians;
   private double                 _sumHeight;


   private PostgreSQLOctree(final String server,
                            final String db,
                            final String user,
                            final String password,
                            final String cloudName,
                            final boolean createIfNotExists,
                            final int bufferSize) {
      final String url = "jdbc:postgresql://" + server + "/" + db;

      _bufferSize = bufferSize;
      _buffer = new ArrayList<>(bufferSize);
      resetBufferBounds();

      try {
         _connection = DriverManager.getConnection(url, user, password);
      }
      catch (final SQLException e) {
         throw new RuntimeException("Can't connect", e);
      }

      _cloudName = cloudName;
      if (!exists()) {
         if (!createIfNotExists) {
            throw new RuntimeException("Octree \"" + _cloudName + "\" doesn't exist");
         }

         create();
      }
      readNodes();
   }


   private void readNodes() {
      log("reading nodes...");
      try {
         _nodes = PostgreSQLNode.getAll(_connection, getQuotedNodeTableName());
      }
      catch (final SQLException e) {
         throw new RuntimeException(e);
      }
      log("read " + _nodes.size() + " nodes");
   }


   private void create() {
      try {
         createMetadataTable();
         createNodeTable();
         createNodeDataTable();
      }
      catch (final SQLException e) {
         throw new RuntimeException("Can't create", e);
      }
   }


   private void createMetadataTable() throws SQLException {
      try (final Statement st = _connection.createStatement()) {
         log("creating metadata table");
         st.executeUpdate(getCreateMetadataTableSQL());
      }

      try (final Statement st = _connection.createStatement()) {
         st.executeUpdate("INSERT INTO " + getQuotedMetadataTableName() + " (name) VALUES ('" + _cloudName + "');");
      }
   }


   private void createNodeTable() throws SQLException {
      try (final Statement st = _connection.createStatement()) {
         log("creating node table");
         st.executeUpdate(getCreateNodeTableSQL());
      }

      log("creating node table indexes");
      createNodeTableIndex("lower_latitude");
      createNodeTableIndex("lower_longitude");

      createNodeTableIndex("upper_latitude");
      createNodeTableIndex("upper_longitude");
   }


   private void createNodeDataTable() throws SQLException {
      try (final Statement st = _connection.createStatement()) {
         log("creating node_data table");
         st.executeUpdate(getCreateNodeDataTableSQL());
      }
   }


   private void createNodeTableIndex(final String columnName) throws SQLException {
      try (final Statement st = _connection.createStatement()) {
         st.executeUpdate("CREATE INDEX \"" + getNodeTableName() + "_" + columnName + "\" ON " + getQuotedNodeTableName() + " ("
                          + columnName + ")");
      }
   }


   private static final DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS: ");


   private static String timestamp() {
      synchronized (format) {
         return format.format(Calendar.getInstance().getTime());
      }
   }


   private void log(final String string) {
      System.out.println("- " + timestamp() + _cloudName + ": " + string);
   }


   private String getCreateMetadataTableSQL() {
      return "CREATE TABLE " + getQuotedMetadataTableName() + " " + //
             "(name TEXT NOT NULL)";
   }


   private String getCreateNodeTableSQL() {
      return "CREATE TABLE " + getQuotedNodeTableName() + " " + //
             "(id smallint[] NOT NULL," + //
             "lower_latitude float8 NOT NULL," + //
             "lower_longitude float8 NOT NULL," + //
             "upper_latitude float8 NOT NULL," + //
             "upper_longitude float8 NOT NULL," + //
             "points_count int NOT NULL," + //
             "average_latitude float8 NOT NULL," + //
             "average_longitude float8 NOT NULL," + //
             "average_height float8 NOT NULL," + //
             "format smallint NOT NULL," + //
             "points float4[] NOT NULL," + //
             " CONSTRAINT \"" + getNodeTableName() + "_id_primary_key\" PRIMARY KEY (id))";
   }


   private String getCreateNodeDataTableSQL() {
      return "CREATE TABLE " + getQuotedNodeDataTableName() + " " + //
             "(id smallint[] NOT NULL," + //
             "format smallint NOT NULL," + //
             "points float4[] NOT NULL," + //
             " CONSTRAINT \"" + getNodeDataTableName() + "_id_primary_key\" PRIMARY KEY (id))";
   }


   private String getMetadataTableName() {
      return "OT_" + _cloudName + "_metadata";
   }


   private String getNodeTableName() {
      return "OT_" + _cloudName + "_node";
   }


   private String getNodeDataTableName() {
      return "OT_" + _cloudName + "_node_data";
   }


   private String getQuotedMetadataTableName() {
      return "\"" + getMetadataTableName() + "\"";
   }


   private String getQuotedNodeTableName() {
      return "\"" + getNodeTableName() + "\"";
   }


   private String getQuotedNodeDataTableName() {
      return "\"" + getNodeDataTableName() + "\"";
   }


   private boolean exists() {
      boolean metadataExists = false;
      boolean nodeExists = false;
      boolean nodeDataExists = false;
      try {
         final DatabaseMetaData md = _connection.getMetaData();

         try (final ResultSet rs = md.getTables(null, null, "%", null)) {
            final String metadataTableName = getMetadataTableName();
            final String nodeTableName = getNodeTableName();
            final String nodeDataTableName = getNodeDataTableName();

            while (rs.next()) {
               final String tableType = rs.getString("TABLE_TYPE");
               if ("TABLE".equals(tableType)) {
                  final String tableName = rs.getString("TABLE_NAME");
                  if (tableName.equals(metadataTableName)) {
                     metadataExists = true;
                  }
                  if (tableName.equals(nodeTableName)) {
                     nodeExists = true;
                  }
                  if (tableName.equals(nodeDataTableName)) {
                     nodeDataExists = true;
                  }
               }
            }
         }
      }
      catch (final SQLException e) {
         throw new RuntimeException("Can't connect", e);
      }
      return metadataExists & nodeExists & nodeDataExists;
   }


   @Override
   public void close() {

      try {
         flush();
      }
      catch (final SQLException e) {
         throw new RuntimeException(e);
      }

      JDBCUtils.close(_connection);
   }


   @Override
   public void remove() {
      try {
         try (final Statement st = _connection.createStatement()) {
            log("removing metadata table");
            st.executeUpdate("DROP TABLE " + getQuotedMetadataTableName() + ";");
         }
         try (final Statement st = _connection.createStatement()) {
            log("removing node table");
            st.executeUpdate("DROP TABLE " + getQuotedNodeTableName() + ";");
         }
         try (final Statement st = _connection.createStatement()) {
            log("removing node_data table");
            st.executeUpdate("DROP TABLE " + getQuotedNodeDataTableName() + ";");
         }
      }
      catch (final SQLException e) {
         throw new RuntimeException("Can't connect", e);
      }
   }


   private static Geodetic3D fromRadians(final double latitudeInRadians,
                                         final double longitudeInRadians,
                                         final double height) {
      return new Geodetic3D( //
               Angle.fromRadians(latitudeInRadians), //
               Angle.fromRadians(longitudeInRadians), //
               height);
   }


   private void flush() throws SQLException {
      final int bufferSize = _buffer.size();
      if (bufferSize > 0) {

         final Geodetic3D lower = fromRadians(_minLatitudeInRadians, _minLongitudeInRadians, _minHeight);
         final Geodetic3D upper = fromRadians(_maxLatitudeInRadians, _maxLongitudeInRadians, _maxHeight);

         final Sector targetSector = new Sector(lower.asGeodetic2D(), upper.asGeodetic2D());

         final MercatorTile tile = MercatorTile.deepestEnclosingTile(targetSector);

         //         final String tileID = tile.getIDString();

         final double averageLatitudeInRadians = _sumLatitudeInRadians / bufferSize;
         final double averageLongitudeInRadians = _sumLongitudeInRadians / bufferSize;
         final double averageHeight = _sumHeight / bufferSize;

         final Geodetic3D averagePoint = fromRadians(averageLatitudeInRadians, averageLongitudeInRadians, averageHeight);

         log("Flushing buffer of " + bufferSize + //
             //             ", average=" + Utils.toString(averagePoint) + //
                  //                  ", bounds=(" + Utils.toString(lowerPoint) + " / " + Utils.toString(upperPoint) + ")" + //
                  " into tile=" + tile.getIDString() + " level=" + tile.getLevel() + "...");


         final Float[] values = new Float[bufferSize * 3];
         int i = 0;
         for (final Geodetic3D point : _buffer) {
            final float deltaLatitudeInRadians = (float) (point._latitude._radians - averageLatitudeInRadians);
            final float deltaLongitudeInRadians = (float) (point._longitude._radians - averageLongitudeInRadians);
            final float deltaHeight = (float) (point._height - averageHeight);

            values[i++] = deltaLatitudeInRadians;
            values[i++] = deltaLongitudeInRadians;
            values[i++] = deltaHeight;
         }

         _buffer.clear();
         resetBufferBounds();

         PostgreSQLNode.save(_connection, getQuotedNodeTableName(), tile, averagePoint, values);
      }
   }


   private void resetBufferBounds() {
      _minLatitudeInRadians = Double.POSITIVE_INFINITY;
      _minLongitudeInRadians = Double.POSITIVE_INFINITY;
      _minHeight = Double.POSITIVE_INFINITY;

      _maxLatitudeInRadians = Double.NEGATIVE_INFINITY;
      _maxLongitudeInRadians = Double.NEGATIVE_INFINITY;
      _maxHeight = Double.NEGATIVE_INFINITY;

      _sumLatitudeInRadians = 0.0;
      _sumLongitudeInRadians = 0.0;
      _sumHeight = 0.0;
   }


   @Override
   public void addPoint(final Geodetic3D point) {
      _buffer.add(point);

      final double latitudeInRadians = point._latitude._radians;
      final double longitudeInRadians = point._longitude._radians;
      final double height = point._height;

      _sumLatitudeInRadians += latitudeInRadians;
      _sumLongitudeInRadians += longitudeInRadians;
      _sumHeight += height;


      if (latitudeInRadians < _minLatitudeInRadians) {
         _minLatitudeInRadians = latitudeInRadians;
      }
      if (latitudeInRadians > _maxLatitudeInRadians) {
         _maxLatitudeInRadians = latitudeInRadians;
      }

      if (longitudeInRadians < _minLongitudeInRadians) {
         _minLongitudeInRadians = longitudeInRadians;
      }
      if (longitudeInRadians > _maxLongitudeInRadians) {
         _maxLongitudeInRadians = longitudeInRadians;
      }

      if (height < _minHeight) {
         _minHeight = height;
      }
      if (height > _maxHeight) {
         _maxHeight = height;
      }


      if (_buffer.size() == _bufferSize) {
         try {
            flush();
         }
         catch (final SQLException e) {
            throw new RuntimeException(e);
         }
      }
   }


}
