
package com.glob3mobile.pointcloud.kdtree;

import java.util.LinkedList;

public abstract class KDLeafNode extends KDNode {

   protected KDLeafNode(final KDNode parent) {
      super(parent);
   }

   @Override
   protected void breadthFirstAcceptVisitor(final KDTreeVisitor visitor, final LinkedList<KDNode> queue) throws KDTreeVisitor.AbortVisiting {
      visitor.visitLeafNode(this);
   }

}
