

package com.glob3mobile.vectorial.lod.mapdb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.glob3mobile.geo.Geodetic2D;
import com.glob3mobile.vectorial.storage.PointFeature;
import com.glob3mobile.vectorial.storage.PointFeatureCluster;


public class LODNodeData {


   private final List<PointFeatureCluster> _clusters;
   private final List<PointFeature>        _features;


   public LODNodeData(final List<PointFeatureCluster> clusters,
                   final List<PointFeature> features) {
      _clusters = Collections.unmodifiableList(new ArrayList<>(clusters));
      _features = Collections.unmodifiableList(new ArrayList<>(features));
   }


   public List<PointFeatureCluster> getClusters() {
      return _clusters;
   }


   public List<PointFeature> getFeatures() {
      return _features;
   }


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

      sumSize += _features.size();
      for (final PointFeature feature : _features) {
         final Geodetic2D position = feature._position;
         sumLatRad += position._latitude._radians;
         sumLonRad += position._longitude._radians;
      }

      final Geodetic2D averagePosition = Geodetic2D.fromRadians(sumLatRad / sumSize, sumLonRad / sumSize);
      return new PointFeatureCluster(averagePosition, sumSize);
   }


}
