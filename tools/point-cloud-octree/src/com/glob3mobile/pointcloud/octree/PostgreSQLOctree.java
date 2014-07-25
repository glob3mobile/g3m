

package com.glob3mobile.pointcloud.octree;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


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
         try (final Statement st = _connection.createStatement()) {
            log("creating metadata table");
            st.executeUpdate(getCreateMetadataTableSQL());
         }

         try (final Statement st = _connection.createStatement()) {
            st.executeUpdate("INSERT INTO " + getQuotedMetadataTableName() + " (name) VALUES ('" + _cloudName + "');");
         }


         try (final Statement st = _connection.createStatement()) {
            log("creating node table");
            st.executeUpdate(getCreateNodeTableSQL());
         }

         log("creating node table indexes");
         createNodeTableIndex("lowerLatitude");
         createNodeTableIndex("lowerLongitude");
         createNodeTableIndex("lowerHeight");

         createNodeTableIndex("upperLatitude");
         createNodeTableIndex("upperLongitude");
         createNodeTableIndex("upperHeight");
      }
      catch (final SQLException e) {
         throw new RuntimeException("Can't create", e);
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
             "lowerLatitude float8 NOT NULL," + //
             "lowerLongitude float8 NOT NULL," + //
             "lowerHeight float8 NOT NULL," + //
             "upperLatitude float8 NOT NULL," + //
             "upperLongitude float8 NOT NULL," + //
             "upperHeight float8 NOT NULL," + //
             " CONSTRAINT \"" + getNodeTableName() + "_id_primary_key\" PRIMARY KEY (id))";
   }


   private String getMetadataTableName() {
      return "OT_metadata_" + _cloudName;
   }


   private String getNodeTableName() {
      return "OT_node_" + _cloudName;
   }


   private String getQuotedMetadataTableName() {
      return "\"" + getMetadataTableName() + "\"";
   }


   private String getQuotedNodeTableName() {
      return "\"" + getNodeTableName() + "\"";
   }


   private boolean exists() {
      boolean nodeExists = false;
      boolean metadataExists = false;
      try {
         final DatabaseMetaData md = _connection.getMetaData();

         try (final ResultSet rs = md.getTables(null, null, "%", null)) {
            final String metadataTableName = getMetadataTableName();
            final String nodeTableName = getNodeTableName();

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
               }
            }
         }
      }
      catch (final SQLException e) {
         throw new RuntimeException("Can't connect", e);
      }
      return nodeExists & metadataExists;
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
      }
      catch (final SQLException e) {
         throw new RuntimeException("Can't connect", e);
      }
   }
}
