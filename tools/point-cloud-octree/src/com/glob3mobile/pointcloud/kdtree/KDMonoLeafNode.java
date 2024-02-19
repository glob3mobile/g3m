
package com.glob3mobile.pointcloud.kdtree;

public class KDMonoLeafNode extends KDLeafNode {

   private final int _vertexIndex;

   KDMonoLeafNode(final KDNode parent,
                  final int vertexIndex) {
      super(parent);
      _vertexIndex = vertexIndex;
   }

   public int getVertexIndex() {
      return _vertexIndex;
   }

   @Override
   public int[] getVertexIndexes() {
      return new int[] { _vertexIndex };
   }

}
