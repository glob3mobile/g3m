

package com.glob3mobile.pointcloud.octree.berkeleydb;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import com.glob3mobile.utils.Geodetic3D;
import com.glob3mobile.utils.Planet;
import com.glob3mobile.utils.Sector;

import es.igosoftware.euclid.bounding.GAxisAlignedBox;
import es.igosoftware.euclid.vector.GVector3D;
import es.igosoftware.euclid.vector.GVector3F;
import es.igosoftware.euclid.vector.IVector3;


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
   public static int sizeOf(final GVector3F any) {
      return 3 * 4;
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


   @SuppressWarnings("unused")
   public static int sizeOf(final Format format,
                            final Geodetic3D any,
                            final Geodetic3D averagePoint) {
      switch (format) {
         case LatLonHeight:
            return 3 * 4; // 3 floats

         default:
            throw new RuntimeException("Unsupported format: " + format);
      }
   }


   public static int sizeOf(final byte[] any) {
      return any.length;
   }


   public static void put(final ByteBuffer buffer,
                          final Sector sector) {
      buffer.putDouble(sector._lower._latitude._radians);
      buffer.putDouble(sector._lower._longitude._radians);
      buffer.putDouble(sector._upper._latitude._radians);
      buffer.putDouble(sector._upper._longitude._radians);
   }


   public static void put(final ByteBuffer buffer,
                          final Format format,
                          final Geodetic3D point) {
      switch (format) {
         case LatLonHeight:
            buffer.putDouble(point._latitude._radians);
            buffer.putDouble(point._longitude._radians);
            buffer.putDouble(point._height);
            break;

         default:
            throw new RuntimeException("Unsupported format: " + format);
      }
   }


   public static void put(final ByteBuffer buffer,
                          final Format format,
                          final Geodetic3D point,
                          final Geodetic3D averagePoint) {
      switch (format) {
         case LatLonHeight:
            buffer.putFloat((float) (point._latitude._radians - averagePoint._latitude._radians));
            buffer.putFloat((float) (point._longitude._radians - averagePoint._longitude._radians));
            buffer.putFloat((float) (point._height - averagePoint._height));
            break;

         default:
            throw new RuntimeException("Unsupported format: " + format);
      }
   }


   public static int sizeOf(final Format format,
                            final List<Geodetic3D> points) {
      return points.size() * sizeOf(format, points.get(0));
   }


   public static int sizeOf(final Format format,
                            final List<Geodetic3D> points,
                            final Geodetic3D averagePoint) {
      return points.size() * sizeOf(format, points.get(0), averagePoint);
   }


   public static Sector getSector(final ByteBuffer buffer) {
      final double lowerLatitude = buffer.getDouble();
      final double lowerLongitude = buffer.getDouble();
      final double upperLatitude = buffer.getDouble();
      final double upperLongitude = buffer.getDouble();

      return Sector.fromRadians(//
               lowerLatitude, lowerLongitude, //
               upperLatitude, upperLongitude);
   }


   public static Geodetic3D getGeodetic3D(final ByteBuffer buffer) {
      final double latitude = buffer.getDouble();
      final double longitude = buffer.getDouble();
      final double height = buffer.getDouble();

      return Geodetic3D.fromRadians(latitude, longitude, height);
   }


   public static Geodetic3D getGeodetic3D(final Format format,
                                          final ByteBuffer buffer) {
      switch (format) {
         case LatLonHeight:
            final double latitude = buffer.getDouble();
            final double longitude = buffer.getDouble();
            final double height = buffer.getDouble();

            return Geodetic3D.fromRadians(latitude, longitude, height);

         default:
            throw new RuntimeException("Unsupported format: " + format);
      }
   }


   public static Geodetic3D getGeodetic3D(final Format format,
                                          final ByteBuffer buffer,
                                          final Geodetic3D averagePoint) {
      switch (format) {
         case LatLonHeight:
            final double latitude = buffer.getFloat() + averagePoint._latitude._radians;
            final double longitude = buffer.getFloat() + averagePoint._longitude._radians;
            final double height = buffer.getFloat() + averagePoint._height;

            return Geodetic3D.fromRadians(latitude, longitude, height);

         default:
            throw new RuntimeException("Unsupported format: " + format);
      }
   }


   public static List<Geodetic3D> getPoints(final ByteBuffer buffer,
            final Format format,
            final int pointsCount) {
      switch (format) {
         case LatLonHeight:
            final List<Geodetic3D> points = new ArrayList<Geodetic3D>(pointsCount);
            for (int i = 0; i < pointsCount; i++) {
               final Geodetic3D point = getGeodetic3D(format, buffer);
               points.add(point);
            }
            return points;

         default:
            throw new RuntimeException("Unsupported format: " + format);
      }
   }


   public static void put(final ByteBuffer buffer,
                          final Format format,
                          final List<Geodetic3D> points) {
      switch (format) {
         case LatLonHeight:
            for (final Geodetic3D point : points) {
               put(buffer, format, point);
            }
            break;

         default:
            throw new RuntimeException("Unsupported format: " + format);
      }
   }


   public static List<Geodetic3D> getPoints(final ByteBuffer buffer,
                                            final Format format,
                                            final int pointsCount,
                                            final Geodetic3D averagePoint) {
      switch (format) {
         case LatLonHeight:
            final List<Geodetic3D> points = new ArrayList<Geodetic3D>(pointsCount);
            for (int i = 0; i < pointsCount; i++) {
               final Geodetic3D point = getGeodetic3D(format, buffer, averagePoint);
               points.add(point);
            }
            return points;

         default:
            throw new RuntimeException("Unsupported format: " + format);
      }
   }


   public static void put(final ByteBuffer buffer,
                          final Format format,
                          final List<Geodetic3D> points,
                          final Geodetic3D averagePoint) {
      switch (format) {
         case LatLonHeight:
            for (final Geodetic3D point : points) {
               put(buffer, format, point, averagePoint);
            }
            break;

         default:
            throw new RuntimeException("Unsupported format: " + format);
      }
   }


   //   public static void put(final ByteBuffer buffer,
   //                          final GVector3D value) {
   //      buffer.putDouble(value.x());
   //      buffer.putDouble(value.y());
   //      buffer.putDouble(value.z());
   //   }

   public static void put(final ByteBuffer buffer,
                          final GVector3F value) {
      buffer.putFloat(value._x);
      buffer.putFloat(value._y);
      buffer.putFloat(value._z);
   }


   public static void put(final ByteBuffer buffer,
                          final GAxisAlignedBox box,
                          final GVector3F average) {
      put(buffer, box._lower, average);
      put(buffer, box._upper, average);
   }


   private static void put(final ByteBuffer buffer,
                           final IVector3 value,
                           final GVector3F average) {
      buffer.putFloat((float) (value.x() - average._x));
      buffer.putFloat((float) (value.y() - average._y));
      buffer.putFloat((float) (value.z() - average._z));
   }


   public static void put(final ByteBuffer buffer,
                          final Planet planet,
                          final List<Geodetic3D> points,
                          final GVector3F average,
                          final float verticalExaggeration,
                          final double deltaHeight) {
      buffer.putInt(points.size());
      for (final Geodetic3D point : points) {
         final GVector3D cartesian = planet.toCartesian(point._latitude, point._longitude, point._height + deltaHeight,
                  verticalExaggeration);
         put(buffer, cartesian, average);
      }
   }


   @SuppressWarnings("unused")
   public static int sizeOf(final GAxisAlignedBox any,
                            final GVector3F average) {
      return 4 * 6;
   }


   @SuppressWarnings("unused")
   public static int sizeOf(final Planet planet,
                            final List<Geodetic3D> points,
                            final GVector3F average) {
      return 4 /* points size */+ (points.size() * 3 * 4);
   }
}
