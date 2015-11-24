

package com.glob3mobile.geo;

import java.io.Serializable;


public class Sector
   implements
      Serializable {

   private static final long  serialVersionUID = 1L;

   public static final Sector FULL_SPHERE      = Sector.fromDegrees(-90, -180, 90, 180);


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


   //   public static Sector getBounds(final List<Geodetic3D> points) {
   //      if ((points == null) || points.isEmpty()) {
   //         return null;
   //      }
   //
   //      final Geodetic3D firstPoint = points.get(0);
   //      double minLatitudeInRadians = firstPoint._latitude._radians;
   //      double minLongitudeInRadians = firstPoint._longitude._radians;
   //
   //      double maxLatitudeInRadians = firstPoint._latitude._radians;
   //      double maxLongitudeInRadians = firstPoint._longitude._radians;
   //
   //      for (int i = 1; i < points.size(); i++) {
   //         final Geodetic3D point = points.get(i);
   //         final double latitudeInRadians = point._latitude._radians;
   //         final double longitudeInRadians = point._longitude._radians;
   //
   //         if (latitudeInRadians < minLatitudeInRadians) {
   //            minLatitudeInRadians = latitudeInRadians;
   //         }
   //         if (latitudeInRadians > maxLatitudeInRadians) {
   //            maxLatitudeInRadians = latitudeInRadians;
   //         }
   //
   //         if (longitudeInRadians < minLongitudeInRadians) {
   //            minLongitudeInRadians = longitudeInRadians;
   //         }
   //         if (longitudeInRadians > maxLongitudeInRadians) {
   //            maxLongitudeInRadians = longitudeInRadians;
   //         }
   //      }
   //
   //      return Sector.fromRadians( //
   //               minLatitudeInRadians, minLongitudeInRadians, //
   //               maxLatitudeInRadians, maxLongitudeInRadians);
   //   }


   public final Geodetic2D _lower;
   public final Geodetic2D _upper;
   public final Angle      _deltaLatitude;
   public final Angle      _deltaLongitude;


   public Sector(final Geodetic2D lower,
                 final Geodetic2D upper) {
      _lower = lower;
      _upper = upper;
      _deltaLatitude = _upper._latitude.sub(_lower._latitude);
      _deltaLongitude = _upper._longitude.sub(_lower._longitude);
   }


   public Sector(final Angle lowerLatitude,
                 final Angle lowerLongitude,
                 final Angle upperLatitude,
                 final Angle upperLongitude) {
      _lower = new Geodetic2D(lowerLatitude, lowerLongitude);
      _upper = new Geodetic2D(upperLatitude, upperLongitude);
      _deltaLatitude = _upper._latitude.sub(_lower._latitude);
      _deltaLongitude = _upper._longitude.sub(_lower._longitude);
   }


   public final boolean contains(final Geodetic2D position) {
      return contains(position._latitude, position._longitude);
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
      return "[Sector " + _lower + " / " + _upper + "]";
   }


   public final double getUCoordinate(final Angle longitude) {
      return (longitude._radians - _lower._longitude._radians) / _deltaLongitude._radians;
   }


   public final double getVCoordinate(final Angle latitude) {
      return (_upper._latitude._radians - latitude._radians) / _deltaLatitude._radians;
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


   public final Sector mergedWith(final Sector that) {
      if (that == null) {
         return this;
      }

      final Angle lowLat = Angle.min(_lower._latitude, that._lower._latitude);
      final Angle lowLon = Angle.min(_lower._longitude, that._lower._longitude);
      final Geodetic2D low = new Geodetic2D(lowLat, lowLon);

      final Angle upLat = Angle.max(_upper._latitude, that._upper._latitude);
      final Angle upLon = Angle.max(_upper._longitude, that._upper._longitude);
      final Geodetic2D up = new Geodetic2D(upLat, upLon);

      return new Sector(low, up);
   }


   public final boolean touchesWith(final Sector that) {
      // from Real-Time Collision Detection - Christer Ericson
      //   page 79

      if ((_upper._latitude._radians < that._lower._latitude._radians) || //
          (_lower._latitude._radians > that._upper._latitude._radians)) {
         return false;
      }
      if ((_upper._longitude._radians < that._lower._longitude._radians) || //
          (_lower._longitude._radians > that._upper._longitude._radians)) {
         return false;
      }

      // Overlapping on all axes means Sectors are intersecting
      return true;
   }


   public final Sector intersection(final Sector that) {
      final Angle lowLat = Angle.max(_lower._latitude, that._lower._latitude);
      final Angle lowLon = Angle.max(_lower._longitude, that._lower._longitude);

      final Angle upLat = Angle.min(_upper._latitude, that._upper._latitude);
      final Angle upLon = Angle.min(_upper._longitude, that._upper._longitude);

      if (lowLat.lowerThan(upLat) && lowLon.lowerThan(upLon)) {
         final Geodetic2D low = new Geodetic2D(lowLat, lowLon);
         final Geodetic2D up = new Geodetic2D(upLat, upLon);

         return new Sector(low, up);
      }

      return null;
   }


   public String toName() {
      return "s_" + _lower.toName() + "_" + _upper.toName();
   }

}
