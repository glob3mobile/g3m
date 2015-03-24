

package com.glob3mobile.pointcloud.kdtree;

import java.util.LinkedList;

import com.glob3mobile.pointcloud.PositionsSet;


public class KDMultiLeafNode
extends
KDLeafNode {

   private final int[] _vertexIndexes;


   KDMultiLeafNode(final KDNode parent,
            final PositionsSet positions,
            final int[] vertexIndexes) {
      super(parent, positions);
      _vertexIndexes = vertexIndexes;
   }


   @Override
   void breadthFirstAcceptVisitor(final KDTreeVisitor visitor,
                                  final LinkedList<KDNode> queue) throws KDTreeVisitor.AbortVisiting {
      visitor.visitLeafNode(this);
   }


   @Override
   public int[] getVertexIndexes() {
      return _vertexIndexes;
   }

}
