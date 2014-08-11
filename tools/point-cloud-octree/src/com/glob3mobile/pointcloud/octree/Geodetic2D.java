

package com.glob3mobile.pointcloud.octree;


public class Geodetic2D {


   public static Geodetic2D fromRadians(final double lat,
                                        final double lon) {
      return new Geodetic2D(Angle.fromRadians(lat), Angle.fromRadians(lon));
   }


   public final Angle _latitude;
   public final Angle _longitude;


   public Geodetic2D(final Angle latitude,
                     final Angle longitude) {
      _latitude = latitude;
      _longitude = longitude;
   }


}
