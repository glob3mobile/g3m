

package com.glob3mobile.pointcloud.quadtree;

import java.util.LinkedList;

import com.glob3mobile.pointcloud.PositionsSet;


public class QuadMonoLeafNode
extends
QuadLeafNode {

   private final int _vertexIndex;


   QuadMonoLeafNode(final QuadNode parent,
            final PositionsSet positionsSet,
                    final int vertexIndex) {
      super(parent, positionsSet);
      _vertexIndex = vertexIndex;
   }


   @Override
   void breadthFirstAcceptVisitor(final QuadTreeVisitor visitor,
                                  final LinkedList<QuadNode> queue) throws QuadTreeVisitor.AbortVisiting {
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
