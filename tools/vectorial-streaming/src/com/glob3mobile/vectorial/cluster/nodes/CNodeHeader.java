

package com.glob3mobile.vectorial.cluster.nodes;

import com.glob3mobile.geo.Geodetic2D;
import com.glob3mobile.geo.Sector;


public abstract class CNodeHeader {

   private final Sector     _nodeSector;
   private final Sector     _minimumSector;
   private final Geodetic2D _averagePosition;


   protected CNodeHeader(final Sector nodeSector,
                         final Sector minimumSector,
                         final Geodetic2D averagePosition) {
      _nodeSector = nodeSector;
      _minimumSector = minimumSector;
      _averagePosition = averagePosition;
   }


   public Sector getNodeSector() {
      return _nodeSector;
   }


   public Sector getMinimumSector() {
      return _minimumSector;
   }


   public Geodetic2D getAveragePosition() {
      return _averagePosition;
   }


}
