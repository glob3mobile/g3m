

package com.glob3mobile.vectorial.storage.mapdb;

import com.glob3mobile.geo.Geodetic2D;
import com.glob3mobile.geo.Sector;


public class NodeHeader {

   public final Sector     _nodeSector;
   public final Sector     _minimumSector;
   public final Geodetic2D _averagePosition;
   public final int        _featuresCount;


   public NodeHeader(final Sector nodeSector,
                     final Sector minimumSector,
                     final Geodetic2D averagePosition,
                     final int featuresCount) {
      _nodeSector = nodeSector;
      _minimumSector = minimumSector;
      _averagePosition = averagePosition;
      _featuresCount = featuresCount;
   }


}
