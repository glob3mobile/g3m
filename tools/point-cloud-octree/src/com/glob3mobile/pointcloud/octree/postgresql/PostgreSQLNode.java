

package com.glob3mobile.pointcloud.octree.postgresql;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.Sector;

import com.glob3mobile.pointcloud.octree.MercatorTile;


public class PostgreSQLNode {

   private static enum Format {
      LatLonHeight(1);

      private int _id;


      private Format(final int id) {
         _id = id;
      }
   }


   static List<PostgreSQLNode> getAll(final Connection connection,
            final String quotedNodeTableName) throws SQLException {
      final ArrayList<PostgreSQLNode> result = new ArrayList<PostgreSQLNode>();

      try (Statement st = connection.createStatement()) {
         try (ResultSet rs = st.executeQuery("SELECT * FROM " + quotedNodeTableName)) {
            while (rs.next()) {
               result.add(new PostgreSQLNode(rs));
            }
         }
      }

      result.trimToSize();

      return Collections.unmodifiableList(result);
   }


   private PostgreSQLNode(final ResultSet rs) throws SQLException {
      final Array id = rs.getArray("id");
      System.out.println(id);
   }


   public static void save(final Connection connection,
                           final String quotedNodeTableName,
                           final MercatorTile tile,
                           final Geodetic3D averagePoint,
                           final Float[] points) throws SQLException {

      final long start = System.currentTimeMillis();

      final int pointsCount = points.length / 3;


      final String sql = //
      "INSERT INTO " + quotedNodeTableName + //
               " (" + //
               "id, " + //
               "lower_latitude, lower_longitude, " + //
               "upper_latitude, upper_longitude, " + //
               "points_count, " + //
               "average_latitude, average_longitude, average_height, " + //
               "format, " + //
               "points" + //
               ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

      //      "format smallint NOT NULL," + //
      //      "points float4[] NOT NULL," + //


      try (final PreparedStatement st = connection.prepareStatement(sql)) {
         final Array id = connection.createArrayOf("smallint", tile.getID());
         st.setArray(1, id);

         final Sector sector = tile.getSector();
         st.setDouble(2, sector._lower._latitude._degrees);
         st.setDouble(3, sector._lower._longitude._degrees);
         st.setDouble(4, sector._upper._latitude._degrees);
         st.setDouble(5, sector._upper._longitude._degrees);

         st.setInt(6, pointsCount);

         st.setDouble(7, averagePoint._latitude._degrees);
         st.setDouble(8, averagePoint._longitude._degrees);
         st.setDouble(9, averagePoint._height);

         final int format = Format.LatLonHeight._id;
         st.setInt(10, format);

         final Array pointsArray = connection.createArrayOf("float", points);
         st.setArray(11, pointsArray);

         final boolean ok = st.execute();
      }

      final long elapsed = System.currentTimeMillis() - start;
      System.out.println("Saved tile" + tile.getIDString() + " in " + elapsed + "ms");
   }


   private static Byte[] boxIt(final byte[] id) {
      if (id == null) {
         return null;
      }

      final int length = id.length;
      final Byte[] result = new Byte[length];
      for (int i = 0; i < length; i++) {
         result[i] = id[i];
      }
      return result;
   }

}
