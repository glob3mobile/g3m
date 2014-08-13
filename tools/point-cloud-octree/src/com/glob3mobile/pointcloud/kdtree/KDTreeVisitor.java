

package com.glob3mobile.pointcloud.kdtree;


public interface KDTreeVisitor {

   public class AbortVisiting
   extends
   Exception {
      private static final long serialVersionUID = 1L;

   }


   void startVisiting(KDTree kdTree);


   void visitInnerNode(KDInnerNode kdInnerNode) throws KDTreeVisitor.AbortVisiting;


   void visitLeafNode(KDMonoLeafNode leafNode) throws KDTreeVisitor.AbortVisiting;


   void endVisiting(KDTree kdTree);


}
