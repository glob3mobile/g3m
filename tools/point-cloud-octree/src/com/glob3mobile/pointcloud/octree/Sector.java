

package com.glob3mobile.pointcloud.octree;


public class Sector {

   public static final Sector FULL_SPHERE = Sector.fromDegrees(-90, -180, 90, 180);


   public static Sector fromDegrees(final double minLatitudeInDegrees,
                                    final double minLongitudeInDegrees,
                                    final double maxLatitudeInDegrees,
                                    final double maxLongitudeInDegrees) {
      final Geodetic2D lower = Geodetic2D.fromDegrees(minLatitudeInDegrees, minLongitudeInDegrees);
      final Geodetic2D upper = Geodetic2D.fromDegrees(maxLatitudeInDegrees, maxLongitudeInDegrees);
      return new Sector(lower, upper);
   }


   public static Sector fromRadians(final double minLatitudeInRadians,
                                    final double minLongitudeInRadians,
                                    final double maxLatitudeInRadians,
                                    final double maxLongitudeInRadians) {
      final Geodetic2D lower = Geodetic2D.fromRadians(minLatitudeInRadians, minLongitudeInRadians);
      final Geodetic2D upper = Geodetic2D.fromRadians(maxLatitudeInRadians, maxLongitudeInRadians);
      return new Sector(lower, upper);
   }


   public final Geodetic2D _lower;
   public final Geodetic2D _upper;


   public Sector(final Geodetic2D lower,
                 final Geodetic2D upper) {
      _lower = lower;
      _upper = upper;
   }


   public Sector(final Angle lowerLatitude,
                 final Angle lowerLongitude,
                 final Angle upperLatitude,
                 final Angle upperLongitude) {
      _lower = new Geodetic2D(lowerLatitude, lowerLongitude);
      _upper = new Geodetic2D(upperLatitude, upperLongitude);
   }


   public final boolean contains(final Angle latitude,
                                 final Angle longitude) {
      return latitude.isBetween(_lower._latitude, _upper._latitude) && //
               longitude.isBetween(_lower._longitude, _upper._longitude);
   }


   public final boolean fullContains(final Sector that) {
      return contains(that._lower._latitude, that._lower._longitude) && //
             contains(that._upper._latitude, that._upper._longitude);
   }


   @Override
   public String toString() {
      return "[Sector lower=" + _lower + ", upper=" + _upper + "]";
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = (prime * result) + ((_lower == null) ? 0 : _lower.hashCode());
      result = (prime * result) + ((_upper == null) ? 0 : _upper.hashCode());
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
      final Sector other = (Sector) obj;
      if (_lower == null) {
         if (other._lower != null) {
            return false;
         }
      }
      else if (!_lower.equals(other._lower)) {
         return false;
      }
      if (_upper == null) {
         if (other._upper != null) {
            return false;
         }
      }
      else if (!_upper.equals(other._upper)) {
         return false;
      }
      return true;
   }


}
