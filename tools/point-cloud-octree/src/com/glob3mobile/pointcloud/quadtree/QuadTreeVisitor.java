

package com.glob3mobile.pointcloud.quadtree;

public interface QuadTreeVisitor {

   public class AbortVisiting
   extends
   Exception {
      private static final long serialVersionUID = 1L;

   }


   void startVisiting(QuadTree tree);


   void visitInnerNode(QuadInnerNode innerNode) throws QuadTreeVisitor.AbortVisiting;


   void visitLeafNode(QuadLeafNode leafNode) throws QuadTreeVisitor.AbortVisiting;


   void endVisiting(QuadTree tree);

}
