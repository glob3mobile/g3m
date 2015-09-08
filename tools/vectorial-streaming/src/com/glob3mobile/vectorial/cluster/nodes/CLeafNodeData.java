

package com.glob3mobile.vectorial.cluster.nodes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.glob3mobile.vectorial.storage.PointFeature;


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


}
