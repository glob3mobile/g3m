

package com.glob3mobile.pointcloud.octree;



public class Geodetic3D {
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

}
