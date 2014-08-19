

package com.glob3mobile.pointcloud.server;

import java.io.PrintWriter;

import com.glob3mobile.pointcloud.octree.Angle;
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


   public static void sendJSONKey(final PrintWriter writer,
                                  final String key) {
      writer.print('"');
      writer.print(key);
      writer.print("\":");
   }

}
