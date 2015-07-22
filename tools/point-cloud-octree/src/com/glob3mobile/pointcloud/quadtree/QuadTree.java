

package com.glob3mobile.pointcloud.quadtree;

import java.util.Arrays;
import java.util.List;

import com.glob3mobile.pointcloud.PositionsSet;
import com.glob3mobile.utils.FlatPlanet;
import com.glob3mobile.utils.Geodetic3D;


public class QuadTree {

   private final QuadNode _root;


   public QuadTree(final List<Geodetic3D> positions,
                   final int maxPointsPerLeaf) {
      final PositionsSet positionsSet = new PositionsSet(FlatPlanet.EARTH, positions);

      final int[] indexes = createIndexes(positions);
      final int duplicatesCount = positions.size() - indexes.length;
      if (duplicatesCount != 0) {
         System.out.println("QuadTree: removed " + duplicatesCount + " of " + positions.size() + " points");
      }

      _root = QuadNode.create(null, positionsSet, indexes, maxPointsPerLeaf);
   }


   //   private static int[] createIndexes(final List<Geodetic3D> positions) {
   //      final int indexesSize = positions.size();
   //      final int[] indexes = new int[indexesSize];
   //      for (int i = 0; i < indexesSize; i++) {
   //         indexes[i] = i;
   //      }
   //      return indexes;
   //   }


   private static int[] createIndexes(final List<Geodetic3D> positions) {
      final int positionsSize = positions.size();
      final int[] indexes = new int[positionsSize];
      int indexCounter = 0;
      for (int index = 0; index < positionsSize; index++) {
         final Geodetic3D position = positions.get(index);
         final boolean isDuplicated = isDuplicated(positions, indexes, indexCounter, position);
         if (!isDuplicated) {
            indexes[indexCounter++] = index;
         }
      }
      return (indexCounter == indexes.length) ? indexes : Arrays.copyOf(indexes, indexCounter);
   }


   private static boolean isDuplicated(final List<Geodetic3D> positions,
                                       final int[] indexes,
                                       final int indexesLength,
                                       final Geodetic3D position) {
      for (int i = 0; i < indexesLength; i++) {
         final int index = indexes[i];
         if (position.equals(positions.get(index))) {
            return true;
         }
      }
      return false;
   }


   public void breadthFirstAcceptVisitor(final QuadTreeVisitor visitor) {
      visitor.startVisiting(this);

      if (_root != null) {
         try {
            _root.breadthFirstAcceptVisitor(visitor);
         }
         catch (final QuadTreeVisitor.AbortVisiting e) {
         }
      }

      visitor.endVisiting(this);
   }


   //   public static void main(final String[] args) {
   //      final List<Geodetic3D> positions = Arrays.asList( //
   //               Geodetic3D.fromRadians(0, 0, 0), //
   //               Geodetic3D.fromRadians(0, 1, 0), //
   //               Geodetic3D.fromRadians(0, 1, 1), //
   //               Geodetic3D.fromRadians(0, 0, 1) //
   //      );
   //
   //      final int[] indexes = createIndexes(positions);
   //      System.out.println(Arrays.toString(indexes));
   //   }

}
