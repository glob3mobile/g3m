

package com.glob3mobile.vectorial.storage.mapdb;

import com.glob3mobile.geo.Geodetic2D;


class MapDBFeature {
   public final Geodetic2D _position;
   public final long       _propertiesID;


   MapDBFeature(final Geodetic2D position,
                final long propertiesID) {
      super();
      _position = position;
      _propertiesID = propertiesID;
   }


}
