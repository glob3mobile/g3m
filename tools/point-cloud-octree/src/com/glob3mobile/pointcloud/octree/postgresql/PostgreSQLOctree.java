

package com.glob3mobile.pointcloud.octree.postgresql;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.glob3mobile.pointcloud.octree.JDBCUtils;
import com.glob3mobile.pointcloud.octree.PersistentOctree;


public class PostgreSQLOctree
implements
PersistentOctree {


   public static PostgreSQLOctree get(final String server,
                                      final String db,
                                      final String user,
                                      final String password,
                                      final String cloudName,
                                      final boolean createIfNotExists) {
      return new PostgreSQLOctree(server, db, user, password, cloudName, createIfNotExists);
   }


   private final Connection _connection;
   private final String     _cloudName;


   private PostgreSQLOctree(final String server,
                            final String db,
                            final String user,
                            final String password,
                            final String cloudName,
                            final boolean createIfNotExists) {
      final String url = "jdbc:postgresql://" + server + "/" + db;

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
      log("reading nodes");
      final int TODO;
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
      createNodeTableIndex("lower_height");

      createNodeTableIndex("upper_latitude");
      createNodeTableIndex("upper_longitude");
      createNodeTableIndex("upper_height");
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
               "lower_height float8 NOT NULL," + //
               "upper_latitude float8 NOT NULL," + //
               "upper_longitude float8 NOT NULL," + //
               "upper_height float8 NOT NULL," + //
               "points_count bigint NOT NULL," + //
               "average_latitude float8 NOT NULL," + //
               "average_longitude float8 NOT NULL," + //
               "average_height float8 NOT NULL," + //
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
}
