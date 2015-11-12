

package com.glob3mobile.vectorial.storage.mapdb;

import com.glob3mobile.geo.Sector;


public class NodeHeader {

   public final Sector _nodeSector;
   public final Sector _minimumSector;
   public final int    _featuresCount;


   public NodeHeader(final Sector nodeSector,
                     final Sector minimumSector,
                     final int featuresCount) {
      _nodeSector = nodeSector;
      _minimumSector = minimumSector;
      _featuresCount = featuresCount;
   }


}
