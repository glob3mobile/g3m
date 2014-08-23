

package com.glob3mobile.pointcloud.octree.berkeleydb;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import com.glob3mobile.pointcloud.octree.Geodetic3D;
import com.glob3mobile.pointcloud.octree.Sector;


public class ByteBufferUtils {

   private ByteBufferUtils() {

   }


   @SuppressWarnings("unused")
   public static int sizeOf(final double any) {
      return 8;
   }


   @SuppressWarnings("unused")
   public static int sizeOf(final int any) {
      return sizeOfInt();
   }


   public static int sizeOfInt() {
      return 4;
   }


   @SuppressWarnings("unused")
   public static int sizeOf(final byte any) {
      return 1;
   }


   @SuppressWarnings("unused")
   public static int sizeOf(final Sector any) {
      return 4 * 8; // 4 doubles
   }


   @SuppressWarnings("unused")
   public static int sizeOf(final Format format,
                            final Geodetic3D any) {
      switch (format) {
         case LatLonHeight:
            return 3 * 8; // 3 doubles

         default:
            throw new RuntimeException("Unsupported format: " + format);
      }
   }


   public static int sizeOf(final byte[] any) {
      return any.length;
   }


   public static void put(final ByteBuffer byteBuffer,
                          final Sector sector) {
      byteBuffer.putDouble(sector._lower._latitude._radians);
      byteBuffer.putDouble(sector._lower._longitude._radians);
      byteBuffer.putDouble(sector._upper._latitude._radians);
      byteBuffer.putDouble(sector._upper._longitude._radians);
   }


   /**
    * @param format
    */
   public static void put(final ByteBuffer byteBuffer,
                          final Format format,
                          final Geodetic3D point) {
      switch (format) {
         case LatLonHeight:
            byteBuffer.putDouble(point._latitude._radians);
            byteBuffer.putDouble(point._longitude._radians);
            byteBuffer.putDouble(point._height);
            break;

         default:
            throw new RuntimeException("Unsupported format: " + format);
      }
   }


   public static int sizeOf(final Format format,
                            final List<Geodetic3D> points) {
      return points.size() * sizeOf(format, points.get(0));
   }


   public static void put(final ByteBuffer byteBuffer,
                          final Format format,
                          final List<Geodetic3D> points) {
      switch (format) {
         case LatLonHeight:
            for (final Geodetic3D point : points) {
               put(byteBuffer, format, point);
            }
            break;

         default:
            throw new RuntimeException("Unsupported format: " + format);
      }
   }


   public static Sector getSector(final ByteBuffer byteBuffer) {
      final double lowerLatitude = byteBuffer.getDouble();
      final double lowerLongitude = byteBuffer.getDouble();
      final double upperLatitude = byteBuffer.getDouble();
      final double upperLongitude = byteBuffer.getDouble();

      return Sector.fromRadians(//
               lowerLatitude, lowerLongitude, //
               upperLatitude, upperLongitude);
   }


   public static Geodetic3D getGeodetic3D(final ByteBuffer byteBuffer) {
      final double latitude = byteBuffer.getDouble();
      final double longitude = byteBuffer.getDouble();
      final double height = byteBuffer.getDouble();

      return Geodetic3D.fromRadians(latitude, longitude, height);
   }


   public static Geodetic3D getGeodetic3D(final Format format,
                                          final ByteBuffer byteBuffer) {
      switch (format) {
         case LatLonHeight:
            final double latitude = byteBuffer.getDouble();
            final double longitude = byteBuffer.getDouble();
            final double height = byteBuffer.getDouble();

            return Geodetic3D.fromRadians(latitude, longitude, height);

         default:
            throw new RuntimeException("Unsupported format: " + format);
      }
   }


   public static List<Geodetic3D> getPoints(final ByteBuffer byteBuffer,
                                            final Format format,
                                            final int pointsCount) {
      switch (format) {
         case LatLonHeight:
            final List<Geodetic3D> points = new ArrayList<Geodetic3D>(pointsCount);
            for (int i = 0; i < pointsCount; i++) {
               final Geodetic3D point = getGeodetic3D(format, byteBuffer);
               points.add(point);
            }
            return points;

         default:
            throw new RuntimeException("Unsupported format: " + format);
      }

   }


}
