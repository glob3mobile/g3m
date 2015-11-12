

package com.glob3mobile.geo;


public class Mercator {


   private Mercator() {
   }


   private static final double UPPER_LIMIT_DEGREES = 85.0511287798;
   private static final double LOWER_LIMIT_DEGREES = -85.0511287798;


   private static double getMercatorV(final Angle latitude) {
      if (latitude._degrees >= UPPER_LIMIT_DEGREES) {
         return 0;
      }
      if (latitude._degrees <= LOWER_LIMIT_DEGREES) {
         return 1;
      }

      final double pi4 = Math.PI * 4;

      final double latSin = Math.sin(latitude._radians);
      return 1.0 - ((Math.log((1.0 + latSin) / (1.0 - latSin)) / pi4) + 0.5);
   }


   private static Angle toLatitude(final double v) {
      final double exp = Math.exp(-2 * Math.PI * (1.0 - v - 0.5));
      final double atan = Math.atan(exp);
      return Angle.fromRadians((Math.PI / 2) - (2 * atan));
   }


   public static Angle calculateSplitLatitude(final Angle lowerLatitude,
                                              final Angle upperLatitude) {
      final double middleV = (getMercatorV(lowerLatitude) / 2.0) + (getMercatorV(upperLatitude) / 2.0);
      return toLatitude(middleV);
   }


}
