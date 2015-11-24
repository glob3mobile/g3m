

package com.glob3mobile.geo;

import java.io.Serializable;


public class Geodetic2D
   implements
      Serializable {


   private static final long serialVersionUID = 1L;


   public static Geodetic2D fromRadians(final double lat,
                                        final double lon) {
      return new Geodetic2D(Angle.fromRadians(lat), Angle.fromRadians(lon));
   }


   public static Geodetic2D fromDegrees(final double lat,
                                        final double lon) {
      return new Geodetic2D(Angle.fromDegrees(lat), Angle.fromDegrees(lon));
   }


   public final Angle _latitude;
   public final Angle _longitude;


   public Geodetic2D(final Angle latitude,
                     final Angle longitude) {
      _latitude = latitude;
      _longitude = longitude;
   }


   @Override
   public String toString() {
      return "[" + _latitude + ", " + _longitude + "]";
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = (prime * result) + ((_latitude == null) ? 0 : _latitude.hashCode());
      result = (prime * result) + ((_longitude == null) ? 0 : _longitude.hashCode());
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
      final Geodetic2D other = (Geodetic2D) obj;
      if (_latitude == null) {
         if (other._latitude != null) {
            return false;
         }
      }
      else if (!_latitude.equals(other._latitude)) {
         return false;
      }
      if (_longitude == null) {
         if (other._longitude != null) {
            return false;
         }
      }
      else if (!_longitude.equals(other._longitude)) {
         return false;
      }
      return true;
   }


   public boolean closeTo(final Geodetic2D that) {
      return _latitude.closeTo(that._latitude) && _longitude.closeTo(that._longitude);
   }


   public String toName() {
      return _latitude.toName() + "_" + _longitude.toName();
   }


}
