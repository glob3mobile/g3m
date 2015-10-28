

package com.glob3mobile.vectorial.lod.clustering.nodes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.glob3mobile.geo.Geodetic2D;
import com.glob3mobile.vectorial.storage.PointFeatureCluster;


public class CInnerNodeData
   extends
      CNodeData {


   private final List<PointFeatureCluster> _clusters;


   public CInnerNodeData(final List<PointFeatureCluster> clusters) {
      _clusters = Collections.unmodifiableList(new ArrayList<>(clusters));
   }


   public List<PointFeatureCluster> getClusters() {
      return _clusters;
   }


   @Override
   public PointFeatureCluster createCluster() {
      double sumLatRad = 0;
      double sumLonRad = 0;
      long sumSize = 0;
      for (final PointFeatureCluster cluster : _clusters) {
         final Geodetic2D position = cluster._position;
         final long clusterSize = cluster._size;
         sumLatRad += position._latitude._radians * clusterSize;
         sumLonRad += position._longitude._radians * clusterSize;
         sumSize += clusterSize;
      }

      final Geodetic2D averagePosition = Geodetic2D.fromRadians(sumLatRad / sumSize, sumLonRad / sumSize);
      return new PointFeatureCluster(averagePosition, sumSize);
   }


}
