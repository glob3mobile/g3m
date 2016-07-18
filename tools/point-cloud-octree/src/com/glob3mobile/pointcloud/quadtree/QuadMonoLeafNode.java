

package com.glob3mobile.pointcloud.quadtree;

import java.util.LinkedList;


public class QuadMonoLeafNode
   extends
      QuadLeafNode {

   private final int _vertexIndex;


   QuadMonoLeafNode(final QuadNode parent,
                    final int vertexIndex) {
      super(parent);
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
