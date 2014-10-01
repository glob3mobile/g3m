

package com.glob3mobile.pointcloud.kdtree;

import java.util.LinkedList;

import com.glob3mobile.pointcloud.PositionsSet;


public class KDInnerNode
         extends
            KDNode {
   private final Axis     _splitAxis;
   //   private final int    _medianVertexIndex;
   //   private final KDNode _leftNode;
   //   private final KDNode _rightNode;

   private final int[]    _mediansVertexIndexes;
   private final KDNode[] _children;


   KDInnerNode(final KDNode parent,
               final PositionsSet positions,
               final Axis splitAxis,
               final int[] mediansVertexIndexes,
               final int[][] childrenVerticesIndexes,
            final int arity) {
      super(parent, positions);
      _splitAxis = splitAxis;
      //      _medianVertexIndex = medianVertexIndex;
      //      _leftNode = KDNode.create(this, positions, leftVerticesIndexes, arity);
      //      _rightNode = KDNode.create(this, positions, rightVerticesIndexes, arity);
      _mediansVertexIndexes = mediansVertexIndexes;
      _children = new KDNode[arity];
      for (int i = 0; i < arity; i++) {
         final int[] childVerticesIndexes = childrenVerticesIndexes[i];
         _children[i] = KDNode.create(this, positions, childVerticesIndexes, arity);
      }
   }


   @Override
   void breadthFirstAcceptVisitor(final KDTreeVisitor visitor,
                                  final LinkedList<KDNode> queue) throws KDTreeVisitor.AbortVisiting {
      visitor.visitInnerNode(this);

      //      if (_leftNode != null) {
      //         queue.addLast(_leftNode);
      //      }
      //      if (_rightNode != null) {
      //         queue.addLast(_rightNode);
      //      }

      for (final KDNode child : _children) {
         if (child != null) {
            queue.addLast(child);
         }
      }
   }


   //   public int getVertexIndex() {
   //      return _medianVertexIndex;
   //   }

   @Override
   public int[] getVertexIndexes() {
      return _mediansVertexIndexes;
   }

}
