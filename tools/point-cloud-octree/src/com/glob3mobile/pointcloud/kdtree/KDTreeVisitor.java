

package com.glob3mobile.pointcloud.kdtree;


public interface KDTreeVisitor {

   public class AbortVisiting
   extends
   Exception {
      private static final long serialVersionUID = 1L;

   }


   void startVisiting(KDTree tree);


   void visitInnerNode(KDInnerNode innerNode) throws KDTreeVisitor.AbortVisiting;


   void visitLeafNode(KDLeafNode leafNode) throws KDTreeVisitor.AbortVisiting;


   void endVisiting(KDTree tree);


}
