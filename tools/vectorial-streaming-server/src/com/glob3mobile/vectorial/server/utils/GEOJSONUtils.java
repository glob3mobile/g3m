

package com.glob3mobile.vectorial.server.utils;

import com.glob3mobile.geo.Geodetic2D;
import com.glob3mobile.geo.Sector;


public class GEOJSONUtils {


   private GEOJSONUtils() {
   }


   public static Double[] toJSON(final Sector sector) {
      return new Double[] {
               //
               sector._lower._latitude._degrees, //
               sector._lower._longitude._degrees, //
               sector._upper._latitude._degrees, //
               sector._upper._longitude._degrees //
      };
   }


   public static Double[] toJSON(final Geodetic2D position) {
      return new Double[] {
               //
               position._latitude._degrees, //
               position._longitude._degrees //
      };
   }


}
