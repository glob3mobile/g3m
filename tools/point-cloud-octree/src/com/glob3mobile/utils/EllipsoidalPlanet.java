

package com.glob3mobile.utils;

import es.igosoftware.euclid.vector.GVector3D;


public class EllipsoidalPlanet
implements
Planet {

   public static final Planet EARTH = new EllipsoidalPlanet(new GVector3D(6378137.0, 6378137.0, 6356752.314245));

   private final GVector3D    _radii;
   private final GVector3D    _radiiSquared;


   //   private final GVector3D               _radiiToTheFourth;
   //   private final GVector3D               _oneOverRadiiSquared;


   public EllipsoidalPlanet(final GVector3D radii) {
      _radii = radii;
      _radiiSquared = new GVector3D( //
               _radii._x * _radii._x, //
               _radii._y * _radii._y, //
               _radii._z * _radii._z);

      //      _radiiToTheFourth = new GVector3D( //
      //               _radiiSquared._x * _radiiSquared._x, //
      //               _radiiSquared._y * _radiiSquared._y, //
      //               _radiiSquared._z * _radiiSquared._z);
      //
      //      _oneOverRadiiSquared = new GVector3D( //
      //               1.0 / (radii._x * radii._x), //
      //               1.0 / (radii._y * radii._y), //
      //               1.0 / (radii._z * radii._z));
   }


   public GVector3D geodeticSurfaceNormal(final Angle latitude,
                                          final Angle longitude) {
      final double cosLatitude = Math.cos(latitude._radians);

      return new GVector3D( //
               cosLatitude * Math.cos(longitude._radians), //
               cosLatitude * Math.sin(longitude._radians), //
               Math.sin(latitude._radians));
   }


   @Override
   public GVector3D toCartesian(final Angle latitude,
                                final Angle longitude,
                                final double height,
                                final float verticalExaggeration) {
      final GVector3D n = geodeticSurfaceNormal(latitude, longitude);

      final GVector3D k = _radiiSquared.scale(n);
      final double gamma = Math.sqrt((k._x * n._x) + (k._y * n._y) + (k._z * n._z));

      final GVector3D rSurface = k.div(gamma);
      return rSurface.add(n.scale(height * verticalExaggeration));
   }


   @Override
   public GVector3D toCartesian(final Geodetic3D geodetic,
                                final float verticalExaggeration) {
      return toCartesian(geodetic._latitude, geodetic._longitude, geodetic._height, verticalExaggeration);
   }


}
