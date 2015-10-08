

package com.glob3mobile.vectorial.lod.clustering.nodes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.glob3mobile.geo.Geodetic2D;
import com.glob3mobile.vectorial.storage.PointFeature;
import com.glob3mobile.vectorial.storage.PointFeatureCluster;


public class CLeafNodeData
   extends
      CNodeData {

   private final List<PointFeature> _features;


   public CLeafNodeData(final List<PointFeature> features) {
      _features = Collections.unmodifiableList(new ArrayList<>(features));
   }


   public List<PointFeature> getFeatures() {
      return _features;
   }


   @Override
   public PointFeatureCluster createCluster() {
      final int size = _features.size();

      double sumLatRad = 0;
      double sumLonRad = 0;
      for (final PointFeature feature : _features) {
         final Geodetic2D position = feature._position;
         sumLatRad += position._latitude._radians;
         sumLonRad += position._longitude._radians;
      }

      final Geodetic2D averagePosition = Geodetic2D.fromRadians(sumLatRad / size, sumLonRad / size);
      return new PointFeatureCluster(averagePosition, size);
   }


}
