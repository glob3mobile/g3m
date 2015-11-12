

package com.glob3mobile.vectorial.lod.clustering.nodes;

import com.glob3mobile.geo.Sector;


public class CLeafNodeHeader
   extends
      CNodeHeader {


   private final int _featuresCount;


   public CLeafNodeHeader(final Sector nodeSector,
                          final Sector minimumSector,
                          final int featuresCount) {
      super(nodeSector, minimumSector);
      _featuresCount = featuresCount;
   }


   public int getFeaturesCount() {
      return _featuresCount;
   }


}
