

package com.glob3mobile.pointcloud.server;

import java.io.PrintWriter;

import com.glob3mobile.pointcloud.octree.Angle;
import com.glob3mobile.pointcloud.octree.PersistentLOD;
import com.glob3mobile.pointcloud.octree.Sector;


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

      //      writer.print('{');
      //
      //      sendJSON(writer, "id", layout.getID());
      //
      //      writer.print(',');
      //      sendJSONKey(writer, "nodes");
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
      writer.println(']');

      //      writer.println('}');
   }


   public static void sendNodeMetadataJSON(final PrintWriter writer,
                                           final PersistentLOD.Node node) {

      //         int getPointsCount();
      //         int getLevelsCount();
      //         List<PersistentLOD.NodeLevel> getLevels();
      //         int[] getLevelsPointsCount();
      //         Sector getSector();
      //         int getDepth();


      writer.print('{');
      sendJSON(writer, "pointsCount", node.getPointsCount());
      writer.print(',');
      sendJSON(writer, "lodLevels", node.getLevelsPointsCount());
      // bounds

      writer.println('}');
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


   //   private static void sendNodeLayoutJSON(final PrintWriter writer,
   //                                final PersistentLOD.Node node) {
   //      writer.print('{');
   //
   //      sendJSON(writer, "id", node.getID());
   //
   //      writer.print(',');
   //      sendJSONKey(writer, "levels");
   //      writer.print('[');
   //         boolean first = true;
   //         for (final PersistentLOD.NodeLevel level : node.getLevels()) {
   //            if (first) {
   //               first = false;
   //            }
   //            else {
   //               writer.print(',');
   //            }
   //            sendJSON(writer, level);
   //         }
   //      writer.print(']');
   //
   //      writer.print('}');
   //   }


   //   private static void sendNodeLayoutJSON(final PrintWriter writer,
   //                                final PersistentLOD.NodeLevel level) {
   //      writer.print('{');
   //      final int pointsCount = level.getPointsCount();
   //      sendJSON(writer, "pointsCount", pointsCount);
   //
   //      writer.print('}');
   //   }


}
