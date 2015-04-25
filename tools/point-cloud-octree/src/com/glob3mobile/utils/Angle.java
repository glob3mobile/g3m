

package com.glob3mobile.utils;

import java.io.Serializable;


public class Angle
implements
Serializable {


   private static final long serialVersionUID = 1L;


   public static Angle fromRadians(final double radians) {
      // return new Angle((radians * (180.0 / 3.14159265358979323846264338327950288)), radians);
      return new Angle(Math.toDegrees(radians), radians);
   }


   public static Angle fromDegrees(final double degrees) {
      // return new Angle(degrees, ((degrees / 180.0) * 3.14159265358979323846264338327950288));
      return new Angle(degrees, Math.toRadians(degrees));
   }


   public static Angle midAngle(final Angle angle1,
                                final Angle angle2) {
      // return Angle.fromRadians((angle1._radians + angle2._radians) / 2);
      return Angle.fromRadians((angle1._radians / 2.0) + (angle2._radians / 2.0));
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


   @Override
   public String toString() {
      //      return _degrees + "d/" + _radians + "r";
      return _degrees + "d";
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      long temp;
      temp = Double.doubleToLongBits(_degrees);
      result = (prime * result) + (int) (temp ^ (temp >>> 32));
      temp = Double.doubleToLongBits(_radians);
      result = (prime * result) + (int) (temp ^ (temp >>> 32));
      return result;
   }


   @Override
   public boolean equals(final Object obj) {
      if (this == obj) {
         return true;
      }
      if (obj == null) {
         return false;
      }
      if (getClass() != obj.getClass()) {
         return false;
      }
      final Angle other = (Angle) obj;
      if (Double.doubleToLongBits(_degrees) != Double.doubleToLongBits(other._degrees)) {
         return false;
      }
      if (Double.doubleToLongBits(_radians) != Double.doubleToLongBits(other._radians)) {
         return false;
      }
      return true;
   }


   public final Angle sub(final Angle that) {
      return Angle.fromRadians(_radians - that._radians);
   }


   public static Angle min(final Angle a1,
                           final Angle a2) {
      return (a1._radians < a2._radians) ? a1 : a2;
   }


   public static Angle max(final Angle a1,
                           final Angle a2) {
      return (a1._radians > a2._radians) ? a1 : a2;
   }


   public final boolean lowerThan(final Angle that) {
      return (_radians < that._radians);
   }


}
