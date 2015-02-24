

package com.glob3mobile.utils;


import es.igosoftware.euclid.vector.GVector3D;


public class SphericalPlanet
implements
Planet {

   public static final Planet EARTH = new SphericalPlanet(6378137.0);


   private final double       _radius;


   public SphericalPlanet(final double radius) {
      _radius = radius;
   }


   @Override
   public final GVector3D toCartesian(final Geodetic3D geodetic,
                                      final float verticalExaggeration) {
      return toCartesian(geodetic._latitude, geodetic._longitude, geodetic._height, verticalExaggeration);
   }


   @Override
   public GVector3D toCartesian(final Angle latitude,
                                final Angle longitude,
                                final double height,
                                final float verticalExaggeration) {
      return geodeticSurfaceNormal(latitude, longitude).scale(_radius + (height * verticalExaggeration));
   }


   public final GVector3D geodeticSurfaceNormal(final Angle latitude,
                                                final Angle longitude) {
      final double cosLatitude = Math.cos(latitude._radians);
      return new GVector3D( //
               cosLatitude * Math.cos(longitude._radians), //
               cosLatitude * Math.sin(longitude._radians), //
               Math.sin(latitude._radians));
   }


}
