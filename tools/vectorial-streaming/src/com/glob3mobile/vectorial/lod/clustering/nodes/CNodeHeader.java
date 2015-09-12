

package com.glob3mobile.vectorial.lod.clustering.nodes;

import com.glob3mobile.geo.Sector;


public abstract class CNodeHeader {

   private final Sector _nodeSector;
   private final Sector _minimumSector;


   protected CNodeHeader(final Sector nodeSector,
                         final Sector minimumSector) {
      _nodeSector = nodeSector;
      _minimumSector = minimumSector;
   }


   public Sector getNodeSector() {
      return _nodeSector;
   }


   public Sector getMinimumSector() {
      return _minimumSector;
   }


}
