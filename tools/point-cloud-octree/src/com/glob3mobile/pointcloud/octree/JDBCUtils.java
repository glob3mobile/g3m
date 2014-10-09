

package com.glob3mobile.pointcloud.octree;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;


public class JDBCUtils {
   private JDBCUtils() {
   }


   public static void close(final Connection connection) {
      if (connection != null) {
         try {
            connection.close();
         }
         catch (final SQLException e) {
            throw new RuntimeException("Can't close connection", e);
         }
      }
   }


   public static void close(final ResultSet rs) {
      if (rs != null) {
         try {
            rs.close();
         }
         catch (final SQLException e) {
            throw new RuntimeException("Can't close resultset", e);
         }
      }
   }

}
