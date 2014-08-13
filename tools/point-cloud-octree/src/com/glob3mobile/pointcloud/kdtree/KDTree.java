

package com.glob3mobile.pointcloud.kdtree;

import java.util.List;

import com.glob3mobile.pointcloud.octree.Geodetic3D;


public class KDTree {

   private final KDNode _root;


   public KDTree(final List<Geodetic3D> positions,
                 final int arity) {
      if (arity < 2) {
         throw new RuntimeException("Invalid arity: " + arity);
      }

      final PositionsSet positionsSet = new PositionsSet(positions);
      final int indexesSize = positions.size();
      final int[] indexes = new int[indexesSize];
      for (int i = 0; i < indexesSize; i++) {
         indexes[i] = i;
      }

      _root = KDNode.create(null, positionsSet, indexes, arity);
   }


   public void breadthFirstAcceptVisitor(final KDTreeVisitor visitor) {
      visitor.startVisiting(this);

      try {
         _root.breadthFirstAcceptVisitor(visitor);
      }
      catch (final KDTreeVisitor.AbortVisiting e) {
      }

      visitor.endVisiting(this);
   }


}
