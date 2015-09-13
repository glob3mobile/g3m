

package com.glob3mobile.vectorial.lod.mapdb;

import com.glob3mobile.geo.Sector;


public class LODNodeHeader {

   private final Sector _nodeSector;
   private final Sector _minimumSector;
   private final int    _clustersCount;
   private final int    _featuresCount;


   LODNodeHeader(final Sector nodeSector,
              final Sector minimumSector,
              final int clustersCount,
              final int featuresCount) {
      _nodeSector = nodeSector;
      _minimumSector = minimumSector;
      _clustersCount = clustersCount;
      _featuresCount = featuresCount;
   }


   public Sector getNodeSector() {
      return _nodeSector;
   }


   public Sector getMinimumSector() {
      return _minimumSector;
   }


   public int getClustersCount() {
      return _clustersCount;
   }


   public int getFeaturesCount() {
      return _featuresCount;
   }

}
