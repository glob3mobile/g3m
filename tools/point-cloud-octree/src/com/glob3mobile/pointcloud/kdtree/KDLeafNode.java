

package com.glob3mobile.pointcloud.kdtree;


public abstract class KDLeafNode
         extends
            KDNode {

   protected KDLeafNode(final KDNode parent,
                        final PositionsSet positions) {
      super(parent, positions);
   }


}
