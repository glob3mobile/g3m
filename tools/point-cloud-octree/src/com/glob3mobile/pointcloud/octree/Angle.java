

package com.glob3mobile.pointcloud.octree;


public class Angle {


   public static Angle fromRadians(final double radians) {
      return new Angle(((radians) * (180.0 / 3.14159265358979323846264338327950288)), radians);
   }


   public static Angle fromDegrees(final double degrees) {
      return new Angle(degrees, (((degrees) / 180.0) * 3.14159265358979323846264338327950288));
   }


   public static Angle midAngle(final Angle angle1,
                                final Angle angle2) {
      return Angle.fromRadians((angle1._radians + angle2._radians) / 2);
   }


   public final double _degrees;
   public final double _radians;


   private Angle(final double degrees,
                 final double radians) {
      _degrees = degrees;
      _radians = radians;
   }


   public final boolean isBetween(final Angle min,
                                  final Angle max) {
      return (_radians >= min._radians) && (_radians <= max._radians);
   }


}
