

package com.glob3mobile.pointcloud.quadtree;

import com.glob3mobile.pointcloud.PositionsSet;


public abstract class QuadLeafNode
         extends
            QuadNode {

   protected QuadLeafNode(final QuadNode parent,
                          final PositionsSet positionsSet) {
      super(parent, positionsSet);
   }

}
