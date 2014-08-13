

package com.glob3mobile.pointcloud.kdtree;

import java.util.LinkedList;


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


   @Override
   protected void breadthFirstAcceptVisitor(final KDTreeVisitor visitor,
                                            final LinkedList<KDNode> queue) throws KDTreeVisitor.AbortVisiting {
      visitor.visitLeafNode(this);
   }


}
