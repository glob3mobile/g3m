package com.glob3mobile.pointcloud.kdtree;


class KDMonoLeafNode
         extends
            KDNode {

   private final int _vertexIndex;


   KDMonoLeafNode(final KDNode parent,
                          final PositionsSet positions,
                          final int vertexIndex) {
      super(parent, positions);
      _vertexIndex = vertexIndex;
   }
}