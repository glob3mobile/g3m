

package com.glob3mobile.pointcloud.kdtree;

import java.util.LinkedList;


public class KDInnerNode
         extends
            KDNode {
   private final Axis   _splitAxis;
   private final int    _medianVertexIndex;
   private final KDNode _leftNode;
   private final KDNode _rightNode;


   KDInnerNode(final KDNode parent,
               final PositionsSet positions,
               final Axis splitAxis,
               final int medianVertexIndex,
               final int[] leftVerticesIndexes,
               final int[] rightVerticesIndexes) {
      super(parent, positions);
      _splitAxis = splitAxis;
      _medianVertexIndex = medianVertexIndex;
      _leftNode = KDNode.create(this, positions, leftVerticesIndexes);
      _rightNode = KDNode.create(this, positions, rightVerticesIndexes);
   }


   @Override
   void breadthFirstAcceptVisitor(final KDTreeVisitor visitor,
                                  final LinkedList<KDNode> queue) throws KDTreeVisitor.AbortVisiting {
      visitor.visitInnerNode(this);

      if (_leftNode != null) {
         queue.addLast(_leftNode);
      }
      if (_rightNode != null) {
         queue.addLast(_rightNode);
      }
   }


   public int getVertexIndex() {
      return _medianVertexIndex;
   }

}
