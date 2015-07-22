

package com.glob3mobile.pointcloud.kdtree;

import java.util.LinkedList;

import com.glob3mobile.pointcloud.PositionsSet;


public class KDMonoLeafNode
extends
KDLeafNode {

   private final int _vertexIndex;


   KDMonoLeafNode(final KDNode parent,
                  final PositionsSet positions,
                  final int vertexIndex) {
      super(parent, positions);
      _vertexIndex = vertexIndex;
   }


   @Override
   void breadthFirstAcceptVisitor(final KDTreeVisitor visitor,
                                  final LinkedList<KDNode> queue) throws KDTreeVisitor.AbortVisiting {
      visitor.visitLeafNode(this);
   }


   public int getVertexIndex() {
      return _vertexIndex;
   }


   @Override
   public int[] getVertexIndexes() {
      return new int[] { _vertexIndex };
   }


}
