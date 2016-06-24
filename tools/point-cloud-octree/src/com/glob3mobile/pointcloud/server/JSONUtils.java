

package com.glob3mobile.pointcloud.server;

import java.io.PrintWriter;
import java.util.List;

import com.glob3mobile.pointcloud.octree.PersistentLOD;
import com.glob3mobile.pointcloud.octree.PersistentLOD.Node;
import com.glob3mobile.utils.Angle;
import com.glob3mobile.utils.Geodetic3D;
import com.glob3mobile.utils.Planet;
import com.glob3mobile.utils.Sector;

import es.igosoftware.euclid.bounding.GAxisAlignedBox;
import es.igosoftware.euclid.vector.GVector3D;
import es.igosoftware.euclid.vector.IVector3;
import es.igosoftware.util.GTriplet;


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


   public static void sendJSONKey(final PrintWriter writer,
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


   //   public static void sendNodeLayoutJSON(final PrintWriter writer,
   //                                         final PersistentLOD.NodeLayout layout) {
   //
   //      writer.print('[');
   //      boolean first = true;
   //      // for (final PersistentLOD.Node node : layout.getNodes()) {
   //      for (final String nodeID : layout.getNodesIDs()) {
   //         if (first) {
   //            first = false;
   //         }
   //         else {
   //            writer.print(',');
   //         }
   //         writer.print('"');
   //         writer.print(nodeID);
   //         writer.print('"');
   //      }
   //      writer.print(']');
   //   }


   public static void sendNodeMetadataJSON(final PrintWriter writer,
                                           final PersistentLOD.Node node) {
      //      final GPair<GAxisAlignedBox, GVector3D> boundsAndAverage = calculateBoundsAndAverage(planet, node);

      final GTriplet<Double, Double, Geodetic3D> minAndMaxHeightsAndAverage = calculateMinAndMaxHeights(node);

      writer.print('{');

      sendJSON(writer, "sector", node.getSector());
      writer.print(',');

      sendJSON(writer, "minHeight", minAndMaxHeightsAndAverage._first.doubleValue());
      writer.print(',');
      sendJSON(writer, "maxHeight", minAndMaxHeightsAndAverage._second.doubleValue());

      //      writer.print(',');
      //      sendJSON(writer, "cartesianEllipsoidalBounds", boundsAndAverage._first);

      writer.print(',');
      sendJSON(writer, "lodLevels", node.getLevelsPointsCount());

      final Geodetic3D average = minAndMaxHeightsAndAverage._third;
      writer.print(',');
      sendJSON(writer, "averagePositionInRadians", average);

      //      writer.print(',');
      //      final GVector3D average = boundsAndAverage._second;
      //      sendJSON(writer, "cartesianEllipsoidalAveragePoint", average);

      writer.print(',');
      final List<PersistentLOD.NodeLevel> levels = node.getLevels();
      sendJSON(writer, "lodLevelsositionsInRadians", levels.subList(0, Math.min(4, levels.size())), average);

      writer.println('}');
   }


   public static void sendJSON(final PrintWriter writer,
                               final String key,
                               final Geodetic3D value) {
      sendJSONKey(writer, key);
      writer.print('[');
      writer.print(Double.toString(value._latitude._radians));
      writer.print(',');
      writer.print(Double.toString(value._longitude._radians));
      writer.print(',');
      writer.print(Double.toString(value._height));
      writer.print(']');
   }


   public static void sendJSON(final PrintWriter writer,
                               final String key,
                               final IVector3 value) {
      sendJSONKey(writer, key);
      writer.print('[');
      writer.print(Double.toString(value.x()));
      writer.print(',');
      writer.print(Double.toString(value.y()));
      writer.print(',');
      writer.print(Double.toString(value.z()));
      writer.print(']');
   }


   private static GTriplet<Double, Double, Geodetic3D> calculateMinAndMaxHeights(final Node node) {
      double minHeight = Double.POSITIVE_INFINITY;
      double maxHeight = Double.NEGATIVE_INFINITY;
      double sumLatitude = 0;
      double sumLongitude = 0;
      double sumHeight = 0;
      long pointsCount = 0;
      for (final PersistentLOD.NodeLevel level : node.getLevels()) {
         for (final Geodetic3D pos : level.getPoints(null)) {
            final double height = pos._height;
            minHeight = Math.min(minHeight, height);
            maxHeight = Math.max(maxHeight, height);

            sumLatitude += pos._latitude._radians;
            sumLongitude += pos._longitude._radians;
            sumHeight += height;
            pointsCount++;
         }
      }

      final Geodetic3D average = Geodetic3D.fromRadians( //
               sumLatitude / pointsCount, //
               sumLongitude / pointsCount, //
               sumHeight / pointsCount);

      return new GTriplet<Double, Double, Geodetic3D>(minHeight, maxHeight, average);
   }


   public static void sendJSON(final PrintWriter writer,
                               final String key,
                               final List<PersistentLOD.NodeLevel> value,
                               final Geodetic3D average) {
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
         sendJSON(writer, each, average);
      }
      writer.print(']');
   }


   public static void sendJSON(final PrintWriter writer,
                               final String key,
                               final List<Geodetic3D> value,
                               final Planet planet,
                               final IVector3 average,
                               final float verticalExaggeration) {
      sendJSONKey(writer, key);
      writer.print('[');
      boolean first = true;
      for (final Geodetic3D each : value) {
         if (first) {
            first = false;
         }
         else {
            writer.print(',');
         }
         sendJSON(writer, each, planet, average, verticalExaggeration);
      }
      writer.print(']');
   }


   public static void sendHeightsJSON(final PrintWriter writer,
                                      final String key,
                                      final List<Geodetic3D> points) {
      sendJSONKey(writer, key);
      writer.print('[');
      boolean first = true;
      for (final Geodetic3D point : points) {
         if (first) {
            first = false;
         }
         else {
            writer.print(',');
         }
         writer.print(Float.toString((float) point._height));
      }
      writer.print(']');
   }


   public static void sendJSON(final PrintWriter writer,
                               final List<Geodetic3D> value,
                               final Planet planet,
                               final IVector3 average,
                               final float verticalExaggeration) {
      writer.print('[');
      boolean first = true;
      for (final Geodetic3D point : value) {
         if (first) {
            first = false;
         }
         else {
            writer.print(',');
         }
         final GVector3D cartesian = planet.toCartesian(point, verticalExaggeration);
         writer.print(Float.toString((float) (cartesian._x - average.x())));
         writer.print(',');
         writer.print(Float.toString((float) (cartesian._y - average.y())));
         writer.print(',');
         writer.print(Float.toString((float) (cartesian._z - average.z())));
      }
      writer.print(']');
   }


   private static void sendJSON(final PrintWriter writer,
                                final Geodetic3D value,
                                final Planet planet,
                                final IVector3 average,
                                final float verticalExaggeration) {
      writer.print('[');
      final GVector3D cartesian = planet.toCartesian(value, verticalExaggeration);
      writer.print(Float.toString((float) (cartesian._x - average.x())));
      writer.print(',');
      writer.print(Float.toString((float) (cartesian._y - average.y())));
      writer.print(',');
      writer.print(Float.toString((float) (cartesian._z - average.z())));
      writer.print(']');
   }


   //      public static void sendJSON(final PrintWriter writer,
   //                                  final String key,
   //                                  final List<PersistentLOD.NodeLevel> value,
   //                                  final Planet planet,
   //                                  final IVector3 average) {
   //         sendJSONKey(writer, key);
   //         writer.print('[');
   //         boolean first = true;
   //         for (final PersistentLOD.NodeLevel each : value) {
   //            if (first) {
   //               first = false;
   //            }
   //            else {
   //               writer.print(',');
   //            }
   //            sendJSON(writer, each, planet, average);
   //         }
   //         writer.print(']');
   //      }


   //   private static void sendJSON(final PrintWriter writer,
   //                                final PersistentLOD.NodeLevel level,
   //                                final Planet planet,
   //                                final IVector3 average,
   //                                final float verticalExaggeration) {
   //      writer.print('[');
   //
   //      boolean first = true;
   //      for (final Geodetic3D pos : level.getPoints(null)) {
   //         if (first) {
   //            first = false;
   //         }
   //         else {
   //            writer.print(',');
   //         }
   //
   //         final GVector3D cartesian = planet.toCartesian(pos, verticalExaggeration);
   //
   //         writer.print(Float.toString((float) (cartesian._x - average.x())));
   //         writer.print(',');
   //         writer.print(Float.toString((float) (cartesian._y - average.y())));
   //         writer.print(',');
   //         writer.print(Float.toString((float) (cartesian._z - average.z())));
   //      }
   //
   //      writer.print(']');
   //   }


   //   private static void sendJSON(final PrintWriter writer,
   //                                final Planet planet,
   //                                final String key,
   //                                final List<PersistentLOD.NodeLevel> value,
   //                                final GVector3D average) {
   //      sendJSONKey(writer, key);
   //      writer.print('[');
   //      boolean first = true;
   //      for (final PersistentLOD.NodeLevel each : value) {
   //         if (first) {
   //            first = false;
   //         }
   //         else {
   //            writer.print(',');
   //         }
   //         sendJSON(writer, planet, each, average);
   //      }
   //      writer.print(']');
   //   }


   //   private static void sendJSON(final PrintWriter writer,
   //                                final Planet planet,
   //                                final PersistentLOD.NodeLevel level,
   //                                final GVector3D average) {
   //      writer.print('[');
   //
   //      boolean first = true;
   //      for (final Geodetic3D pos : level.getPoints(null)) {
   //         if (first) {
   //            first = false;
   //         }
   //         else {
   //            writer.print(',');
   //         }
   //         final GVector3D point = planet.toCartesian(pos);
   //
   //         // System.out.println("geodetic: " + pos + " to cartesian: " + point);
   //
   //         writer.print(Float.toString((float) (point.x() - average._x)));
   //         writer.print(',');
   //         writer.print(Float.toString((float) (point.y() - average._y)));
   //         writer.print(',');
   //         writer.print(Float.toString((float) (point.z() - average._z)));
   //      }
   //
   //      writer.print(']');
   //   }

   private static void sendJSON(final PrintWriter writer,
                                final PersistentLOD.NodeLevel level,
                                final Geodetic3D average) {
      writer.print('[');

      boolean first = true;
      for (final Geodetic3D pos : level.getPoints(null)) {
         if (first) {
            first = false;
         }
         else {
            writer.print(',');
         }

         writer.print(Float.toString((float) (pos._latitude._radians - average._latitude._radians)));
         writer.print(',');
         writer.print(Float.toString((float) (pos._longitude._radians - average._longitude._radians)));
         writer.print(',');
         writer.print(Float.toString((float) (pos._height - average._height)));
      }

      writer.print(']');
   }


   public static void sendJSON(final PrintWriter writer,
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


   public static void sendJSON(final PrintWriter writer,
                               final String key,
                               final GAxisAlignedBox value,
                               final IVector3 average) {

      sendJSONKey(writer, key);
      writer.print('[');
      writer.print(Float.toString((float) (value._lower.x() - average.x())));
      writer.print(',');
      writer.print(Float.toString((float) (value._lower.y() - average.y())));
      writer.print(',');
      writer.print(Float.toString((float) (value._lower.z() - average.z())));
      writer.print(',');
      writer.print(Float.toString((float) (value._upper.x() - average.x())));
      writer.print(',');
      writer.print(Float.toString((float) (value._upper.y() - average.y())));
      writer.print(',');
      writer.print(Float.toString((float) (value._upper.z() - average.z())));
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


   //   private static void sendJSON(final PrintWriter writer,
   //                                final IVector3 value) {
   //      writer.print('[');
   //      writer.print(Double.toString(value.x()));
   //      writer.print(',');
   //      writer.print(Double.toString(value.y()));
   //      writer.print(',');
   //      writer.print(Double.toString(value.z()));
   //      writer.print(']');
   //   }


   //   private static void sendJSON(final PrintWriter writer,
   //                                final String key,
   //                                final IVector3 value) {
   //      sendJSONKey(writer, key);
   //      sendJSON(writer, value);
   //   }


   //   private static GPair<GAxisAlignedBox, GVector3D> calculateBoundsAndAverage(final Planet planet,
   //            final PersistentLOD.Node node) {
   //      double lowerX = Double.POSITIVE_INFINITY;
   //      double lowerY = Double.POSITIVE_INFINITY;
   //      double lowerZ = Double.POSITIVE_INFINITY;
   //
   //      double upperX = Double.NEGATIVE_INFINITY;
   //      double upperY = Double.NEGATIVE_INFINITY;
   //      double upperZ = Double.NEGATIVE_INFINITY;
   //
   //
   //      double sumX = 0;
   //      double sumY = 0;
   //      double sumZ = 0;
   //      long totalPoints = 0;
   //
   //
   //      for (final PersistentLOD.NodeLevel level : node.getLevels()) {
   //         for (final Geodetic3D pos : level.getPoints(null)) {
   //
   //            final GVector3D point = planet.toCartesian(pos);
   //            final double x = point._x;
   //            final double y = point._y;
   //            final double z = point._z;
   //
   //            lowerX = Math.min(lowerX, x);
   //            lowerY = Math.min(lowerY, y);
   //            lowerZ = Math.min(lowerZ, z);
   //
   //            upperX = Math.max(upperX, x);
   //            upperY = Math.max(upperY, y);
   //            upperZ = Math.max(upperZ, z);
   //
   //            sumX += x;
   //            sumY += y;
   //            sumZ += z;
   //            totalPoints++;
   //         }
   //      }
   //
   //
   //      final GVector3D average = new GVector3D( //
   //               sumX / totalPoints, //
   //               sumY / totalPoints, //
   //               sumZ / totalPoints);
   //
   //      final GAxisAlignedBox bounds = new GAxisAlignedBox( //
   //               new GVector3D(lowerX, lowerY, lowerZ), //
   //               new GVector3D(upperX, upperY, upperZ));
   //
   //      return new GPair<GAxisAlignedBox, GVector3D>(bounds, average);
   //   }


   public static void sendJSON(final PrintWriter writer,
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


   public static void sendJSON(final PrintWriter writer,
                               final float[] value) {
      writer.print('[');
      boolean first = true;
      for (final float each : value) {
         if (first) {
            first = false;
         }
         else {
            writer.print(',');
         }
         writer.write(Float.toString(each));
      }
      writer.print(']');
   }

}
