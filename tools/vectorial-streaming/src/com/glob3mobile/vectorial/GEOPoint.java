

package com.glob3mobile.vectorial;

import com.glob3mobile.geo.Geodetic2D;


public class GEOPoint
   extends
      GEOGeometry {


   public final Geodetic2D _position;


   public GEOPoint(final Geodetic2D position) {
      _position = position;
   }


   @Override
   public String toString() {
      return "[GEOPoint " + _position + "]";
   }


}
