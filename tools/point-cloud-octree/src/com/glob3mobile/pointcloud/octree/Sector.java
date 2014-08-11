

package com.glob3mobile.pointcloud.octree;


public class Sector {

   public static final Sector FULL_SPHERE = Sector.fromDegrees(-90, -180, 90, 180);


   public static Sector fromDegrees(final double minLat,
                                    final double minLon,
                                    final double maxLat,
                                    final double maxLon) {
      final Geodetic2D lower = new Geodetic2D(Angle.fromDegrees(minLat), Angle.fromDegrees(minLon));
      final Geodetic2D upper = new Geodetic2D(Angle.fromDegrees(maxLat), Angle.fromDegrees(maxLon));

      return new Sector(lower, upper);
   }


   public final Geodetic2D _lower;
   public final Geodetic2D _upper;


   public Sector(final Geodetic2D lower,
                 final Geodetic2D upper) {
      _lower = lower;
      _upper = upper;
   }


   public final boolean contains(final Angle latitude,
                                 final Angle longitude) {
      return (latitude.isBetween(_lower._latitude, _upper._latitude) && longitude.isBetween(_lower._longitude, _upper._longitude));
   }


   public final boolean fullContains(final Sector that) {
      return (contains(that._upper._latitude, that._upper._longitude) && contains(that._lower._latitude, that._lower._longitude));
   }


}
