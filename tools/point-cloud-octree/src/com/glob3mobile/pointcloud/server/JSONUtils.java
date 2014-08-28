

package com.glob3mobile.pointcloud.server;

import java.io.PrintWriter;
import java.util.List;

import com.glob3mobile.pointcloud.kdtree.Planet;
import com.glob3mobile.pointcloud.octree.Angle;
import com.glob3mobile.pointcloud.octree.Geodetic3D;
import com.glob3mobile.pointcloud.octree.PersistentLOD;
import com.glob3mobile.pointcloud.octree.Sector;

import es.igosoftware.euclid.bounding.GAxisAlignedBox;
import es.igosoftware.euclid.vector.GVector3D;
import es.igosoftware.euclid.vector.IVector3;
import es.igosoftware.util.GPair;


public class JSONUtils {

   private JSONUtils() {
   }


   public static void sendJSON(final PrintWriter writer,
                               final String key,
                               final Sector value) {
      sendJSONKey(writer, key);
      writer.print('{');
      sendJSON(writer, "lowerLatitude", value._lower._latitude);
      writer.print(",");
      sendJSON(writer, "lowerLongitude", value._lower._longitude);
      writer.print(",");
      sendJSON(writer, "upperLatitude", value._upper._latitude);
      writer.print(",");
      sendJSON(writer, "upperLongitude", value._upper._longitude);
      writer.print('}');
   }


   public static void sendJSON(final PrintWriter writer,
                               final String key,
                               final Angle value) {
      sendJSON(writer, key, value._degrees);
   }


   public static void sendJSON(final PrintWriter writer,
                               final String key,
                               final double value) {
      sendJSONKey(writer, key);
      writer.print(value);
   }


   public static void sendJSON(final PrintWriter writer,
                               final String key,
                               final long value) {
      sendJSONKey(writer, key);
      writer.print(value);
   }


   private static void sendJSONKey(final PrintWriter writer,
                                   final String key) {
      writer.print('"');
      writer.print(key);
      writer.print("\":");
   }


   public static void sendJSON(final PrintWriter writer,
                               final String key,
                               final String value) {
      sendJSONKey(writer, key);
      writer.print('"');
      writer.print(value);
      writer.print('"');
   }


   public static void sendJSON(final PrintWriter writer,
                               final PersistentLOD.Statistics statistics) {

      writer.print('{');

      sendJSON(writer, "name", statistics.getPointCloudName());

      writer.print(',');
      sendJSON(writer, "pointsCount", statistics.getPointsCount());

      writer.print(',');
      sendJSON(writer, "sector", statistics.getSector());

      writer.print(',');
      sendJSON(writer, "minHeight", statistics.getMinHeight());

      writer.print(',');
      sendJSON(writer, "maxHeight", statistics.getMaxHeight());

      writer.println('}');

   }


   public static void sendNodeLayoutJSON(final PrintWriter writer,
                                         final PersistentLOD.NodeLayout layout) {

      writer.print('[');
      boolean first = true;
      // for (final PersistentLOD.Node node : layout.getNodes()) {
      for (final String nodeID : layout.getNodesIDs()) {
         if (first) {
            first = false;
         }
         else {
            writer.print(',');
         }
         writer.print('"');
         writer.print(nodeID);
         writer.print('"');
      }
      writer.print(']');
   }


   public static void sendNodeMetadataJSON(final PrintWriter writer,
                                           final Planet planet,
                                           final PersistentLOD.Node node) {
      final GPair<GAxisAlignedBox, GVector3D> boundsAndAverage = calculateBoundsAndAverage(planet, node);

      writer.print('{');
      //      sendJSON(writer, "sector", node.getSector());
      //      writer.print(',');

      sendJSON(writer, "cartesianEllipsoidalBounds", boundsAndAverage._first);

      writer.print(',');
      sendJSON(writer, "lodLevels", node.getLevelsPointsCount());

      writer.print(',');
      final GVector3D average = boundsAndAverage._second;
      sendJSON(writer, "cartesianEllipsoidalAveragePoint", average);

      writer.print(',');
      final List<PersistentLOD.NodeLevel> levels = node.getLevels();
      sendJSON(writer, planet, "lodLevelsCartesianEllipsoidalDeltaPoints", levels.subList(0, Math.min(5, levels.size())), average);

      writer.println('}');
   }


   private static void sendJSON(final PrintWriter writer,
                                final Planet planet,
                                final String key,
                                final List<PersistentLOD.NodeLevel> value,
                                final GVector3D average) {
      sendJSONKey(writer, key);
      writer.print('[');
      boolean first = true;
      for (final PersistentLOD.NodeLevel each : value) {
         if (first) {
            first = false;
         }
         else {
            writer.print(',');
         }
         sendJSON(writer, planet, each, average);
      }
      writer.print(']');
   }


   private static void sendJSON(final PrintWriter writer,
                                final Planet planet,
                                final PersistentLOD.NodeLevel level,
                                final GVector3D average) {
      writer.print('[');

      boolean first = true;
      for (final Geodetic3D pos : level.getPoints(null)) {
         if (first) {
            first = false;
         }
         else {
            writer.print(',');
         }
         final GVector3D point = planet.toCartesian(pos);

         // System.out.println("geodetic: " + pos + " to cartesian: " + point);

         writer.print(Float.toString((float) (point.x() - average._x)));
         writer.print(',');
         writer.print(Float.toString((float) (point.y() - average._y)));
         writer.print(',');
         writer.print(Float.toString((float) (point.z() - average._z)));
      }

      writer.print(']');
   }


   private static void sendJSON(final PrintWriter writer,
                                final String key,
                                final GAxisAlignedBox value) {

      sendJSONKey(writer, key);
      writer.print('[');
      writer.print(Double.toString(value._lower.x()));
      writer.print(',');
      writer.print(Double.toString(value._lower.y()));
      writer.print(',');
      writer.print(Double.toString(value._lower.z()));
      writer.print(',');
      writer.print(Double.toString(value._upper.x()));
      writer.print(',');
      writer.print(Double.toString(value._upper.y()));
      writer.print(',');
      writer.print(Double.toString(value._upper.z()));
      writer.print(']');
   }


   //   private static void sendJSON(final PrintWriter writer,
   //                                final IVector3 value,
   //                                final GVector3D average) {
   //      writer.print('[');
   //      writer.print(Float.toString((float) (value.x() - average._x)));
   //      writer.print(',');
   //      writer.print(Float.toString((float) (value.y() - average._y)));
   //      writer.print(',');
   //      writer.print(Float.toString((float) (value.z() - average._z)));
   //      writer.print(']');
   //   }


   //   private static void sendJSON(final PrintWriter writer,
   //                                final String key,
   //                                final IVector3 value,
   //                                final GVector3D average) {
   //      sendJSONKey(writer, key);
   //      sendJSON(writer, value, average);
   //   }


   private static void sendJSON(final PrintWriter writer,
                                final IVector3 value) {
      writer.print('[');
      writer.print(Double.toString(value.x()));
      writer.print(',');
      writer.print(Double.toString(value.y()));
      writer.print(',');
      writer.print(Double.toString(value.z()));
      writer.print(']');
   }


   private static void sendJSON(final PrintWriter writer,
                                final String key,
                                final IVector3 value) {
      sendJSONKey(writer, key);
      sendJSON(writer, value);
   }


   private static GPair<GAxisAlignedBox, GVector3D> calculateBoundsAndAverage(final Planet planet,
                                                                              final PersistentLOD.Node node) {
      double lowerX = Double.POSITIVE_INFINITY;
      double lowerY = Double.POSITIVE_INFINITY;
      double lowerZ = Double.POSITIVE_INFINITY;

      double upperX = Double.NEGATIVE_INFINITY;
      double upperY = Double.NEGATIVE_INFINITY;
      double upperZ = Double.NEGATIVE_INFINITY;


      double sumX = 0;
      double sumY = 0;
      double sumZ = 0;
      long totalPoints = 0;


      for (final PersistentLOD.NodeLevel level : node.getLevels()) {
         for (final Geodetic3D pos : level.getPoints(null)) {

            final GVector3D point = planet.toCartesian(pos);
            final double x = point._x;
            final double y = point._y;
            final double z = point._z;

            lowerX = Math.min(lowerX, x);
            lowerY = Math.min(lowerY, y);
            lowerZ = Math.min(lowerZ, z);

            upperX = Math.max(upperX, x);
            upperY = Math.max(upperY, y);
            upperZ = Math.max(upperZ, z);

            sumX += x;
            sumY += y;
            sumZ += z;
            totalPoints++;
         }
      }


      final GVector3D average = new GVector3D( //
               sumX / totalPoints, //
               sumY / totalPoints, //
               sumZ / totalPoints);

      final GAxisAlignedBox bounds = new GAxisAlignedBox( //
               new GVector3D(lowerX, lowerY, lowerZ), //
               new GVector3D(upperX, upperY, upperZ));

      return new GPair<GAxisAlignedBox, GVector3D>(bounds, average);
   }


   private static void sendJSON(final PrintWriter writer,
                                final String key,
                                final int[] value) {
      sendJSONKey(writer, key);
      writer.print('[');
      boolean first = true;
      for (final int each : value) {
         if (first) {
            first = false;
         }
         else {
            writer.print(',');
         }
         writer.write(Integer.toString(each));
      }
      writer.print(']');
   }


}
