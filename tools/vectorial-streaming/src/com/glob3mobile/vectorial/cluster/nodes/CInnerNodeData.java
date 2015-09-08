

package com.glob3mobile.vectorial.cluster.nodes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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


}
