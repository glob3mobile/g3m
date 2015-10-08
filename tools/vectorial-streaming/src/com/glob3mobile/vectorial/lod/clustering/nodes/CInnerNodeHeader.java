

package com.glob3mobile.vectorial.lod.clustering.nodes;

import com.glob3mobile.geo.Sector;


public class CInnerNodeHeader
   extends
      CNodeHeader {


   private final int _clustersCount;


   public CInnerNodeHeader(final Sector nodeSector,
                           final Sector minimumSector,
                           final int clustersCount) {
      super(nodeSector, minimumSector);
      _clustersCount = clustersCount;
   }


   public int getClustersCount() {
      return _clustersCount;
   }


}
