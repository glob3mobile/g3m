

package com.glob3mobile.pointcloud.octree;

import org.glob3.mobile.generated.Angle;
import org.glob3.mobile.generated.Geodetic2D;
import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.Sector;


public class Utils {

   private Utils() {
   }


   public static String toString(final Angle angle) {
      return angle._degrees + "d";
   }


   public static String toString(final Geodetic3D point) {
      return "(lat=" + toString(point._latitude) + ", lon=" + toString(point._longitude) + ", height=" + point._height + ")";
   }


   public static String toString(final Geodetic2D point) {
      return "(lat=" + toString(point._latitude) + ", lon=" + toString(point._longitude) + ")";
   }


   public static String toString(final Sector sector) {
      return "(lower=" + toString(sector._lower) + ", upper=" + toString(sector._upper) + ")";

   }


   public static Geodetic3D fromRadians(final double latitudeInRadians,
                                        final double longitudeInRadians,
                                        final double height) {
      return new Geodetic3D( //
               Angle.fromRadians(latitudeInRadians), //
               Angle.fromRadians(longitudeInRadians), //
               height);
   }


   public static int compare(final byte[] left,
                             final byte[] right) {
      final int leftLength = left.length;
      final int rightLength = right.length;
      final int compareLenght = Math.min(leftLength, rightLength);
      for (int i = 0; i < compareLenght; i++) {
         final int leftValue = (left[i] & 0xff);
         final int rightValue = (right[i] & 0xff);
         if (leftValue != rightValue) {
            return leftValue - rightValue;
         }
      }
      return leftLength - rightLength;
   }


   public static boolean isGreaterThan(final byte[] id,
                                       final byte[] thatId) {
      return compare(id, thatId) > 0;
   }

}
