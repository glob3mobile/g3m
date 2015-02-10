

package com.glob3mobile.utils;

import com.glob3mobile.utils.Angle;
import com.glob3mobile.utils.Geodetic3D;
import com.glob3mobile.utils.Planet;

import es.igosoftware.euclid.vector.GVector2D;
import es.igosoftware.euclid.vector.GVector3D;


public class FlatPlanet
         implements
            Planet {
   public static final Planet EARTH = new FlatPlanet(new GVector2D(4 * 6378137.0, 2 * 6378137.0));

   private final GVector2D    _size;


   public FlatPlanet(final GVector2D size) {
      _size = size;
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
      final double x = (longitude._degrees * _size._x) / 360.0;
      final double y = (latitude._degrees * _size._y) / 180.0;
      return new GVector3D(x, y, height * verticalExaggeration);
   }


}
