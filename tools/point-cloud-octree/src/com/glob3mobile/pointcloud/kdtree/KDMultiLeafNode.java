

package com.glob3mobile.pointcloud.kdtree;



public class KDMultiLeafNode
   extends
      KDLeafNode {

   private final int[] _vertexIndexes;


   KDMultiLeafNode(final KDNode parent,
                   final int[] vertexIndexes) {
      super(parent);
      _vertexIndexes = vertexIndexes;
   }


   @Override
   public int[] getVertexIndexes() {
      return _vertexIndexes;
   }

}
