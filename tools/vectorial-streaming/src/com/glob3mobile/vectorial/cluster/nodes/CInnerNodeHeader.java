

package com.glob3mobile.vectorial.cluster.nodes;

import com.glob3mobile.geo.Geodetic2D;
import com.glob3mobile.geo.Sector;


public class CInnerNodeHeader
   extends
      CNodeHeader {


   private final int _clustersCount;


   public CInnerNodeHeader(final Sector nodeSector,
                           final Sector minimumSector,
                           final Geodetic2D averagePosition,
                           final int clustersCount) {
      super(nodeSector, minimumSector, averagePosition);
      _clustersCount = clustersCount;
   }


   public int getClustersCount() {
      return _clustersCount;
   }


}
