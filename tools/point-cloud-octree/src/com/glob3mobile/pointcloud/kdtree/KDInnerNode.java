
package com.glob3mobile.pointcloud.kdtree;

import java.util.LinkedList;

import com.glob3mobile.pointcloud.PositionsSet;

public class KDInnerNode extends KDNode {

   private final int[]    _mediansVertexIndexes;
   private final KDNode[] _children;

   KDInnerNode(final KDNode parent,
               final PositionsSet positions,
               final int[] mediansVertexIndexes,
               final int[][] childrenVerticesIndexes,
               final int arity) {
      super(parent);
      _mediansVertexIndexes = mediansVertexIndexes;
      _children             = new KDNode[arity];
      for (int i = 0; i < arity; i++) {
         final int[] childVerticesIndexes = childrenVerticesIndexes[i];
         _children[i] = KDNode.create(this, positions, childVerticesIndexes, arity);
      }
   }

   @Override
   protected void breadthFirstAcceptVisitor(final KDTreeVisitor visitor, final LinkedList<KDNode> queue) throws KDTreeVisitor.AbortVisiting {
      visitor.visitInnerNode(this);

      for (final KDNode child : _children) {
         if (child != null) {
            queue.addLast(child);
         }
      }
   }

   @Override
   public int[] getVertexIndexes() {
      return _mediansVertexIndexes;
   }

}
