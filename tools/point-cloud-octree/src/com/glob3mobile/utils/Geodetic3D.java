

package com.glob3mobile.utils;


public class Geodetic3D {


   public static Geodetic3D fromRadians(final double latitudeInRadians,
                                        final double longitudeInRadians,
                                        final double height) {
      return new Geodetic3D( //
               Angle.fromRadians(latitudeInRadians), //
               Angle.fromRadians(longitudeInRadians), //
               height);
   }


   public final Angle  _latitude;
   public final Angle  _longitude;
   public final double _height;


   public Geodetic3D(final Angle latitude,
                     final Angle longitude,
                     final double height) {
      _latitude = latitude;
      _longitude = longitude;
      _height = height;
   }


   public final Geodetic2D asGeodetic2D() {
      return new Geodetic2D(_latitude, _longitude);
   }


   @Override
   public String toString() {
      return "[lat=" + _latitude + ", lon=" + _longitude + ", height=" + _height + "]";
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      long temp;
      temp = Double.doubleToLongBits(_height);
      result = (prime * result) + (int) (temp ^ (temp >>> 32));
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
      final Geodetic3D other = (Geodetic3D) obj;
      if (Double.doubleToLongBits(_height) != Double.doubleToLongBits(other._height)) {
         return false;
      }
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

}
