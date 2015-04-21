

package com.glob3mobile.pointcloud.quadtree;

import java.util.LinkedList;

import com.glob3mobile.pointcloud.PositionsSet;


public class QuadMultiLeafNode
extends
QuadLeafNode {

   private final int[] _vertexIndexes;


   QuadMultiLeafNode(final QuadNode parent,
                     final PositionsSet positionsSet,
                     final int[] vertexIndexes) {
      super(parent, positionsSet);
      _vertexIndexes = vertexIndexes;
   }


   @Override
   void breadthFirstAcceptVisitor(final QuadTreeVisitor visitor,
                                  final LinkedList<QuadNode> queue) throws QuadTreeVisitor.AbortVisiting {
      visitor.visitLeafNode(this);
   }


   @Override
   public int[] getVertexIndexes() {
      return _vertexIndexes;
   }

}
