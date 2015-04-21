

package com.glob3mobile.pointcloud.quadtree;

import java.util.LinkedList;
import java.util.List;

import com.glob3mobile.pointcloud.PositionsSet;


public class QuadInnerNode
         extends
            QuadNode {

   private final List<QuadNode> _children;


   protected QuadInnerNode(final QuadNode parent,
                           final PositionsSet positionsSet) {
      super(parent, positionsSet);
      throw new RuntimeException("Not yet implemented");
   }


   @Override
   void breadthFirstAcceptVisitor(final QuadTreeVisitor visitor,
                                  final LinkedList<QuadNode> queue) throws QuadTreeVisitor.AbortVisiting {
      visitor.visitInnerNode(this);

      for (final QuadNode child : _children) {
         if (child != null) {
            queue.addLast(child);
         }
      }
   }


   @Override
   public int[] getVertexIndexes() {
      throw new RuntimeException("Not yet implemented");
   }


}
