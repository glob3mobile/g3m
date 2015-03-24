

package com.glob3mobile.pointcloud.quadtree;

import java.util.LinkedList;
import java.util.List;

import com.glob3mobile.pointcloud.PositionsSet;

import es.igosoftware.euclid.vector.GVector2D;
import es.igosoftware.euclid.vector.GVector3D;


public abstract class QuadNode {


   static QuadNode create(final QuadNode parent,
                          final PositionsSet positionsSet,
                          final int[] indexes,
                          final int maxPointsPerLeaf) {

      final int indexesSize = indexes.length;

      if (indexesSize == 0) {
         return null;
      }

      if (indexesSize == 1) {
         return new QuadMonoLeafNode(parent, positionsSet, indexes[0]);
      }

      if (indexesSize <= maxPointsPerLeaf) {
         return new QuadMultiLeafNode(parent, positionsSet, indexes);
      }

      final int pivotIndex = getPivotIndex(positionsSet, indexes);
      final GVector3D pivot = positionsSet._cartesianPoints.get(pivotIndex);

      throw new RuntimeException("Not yet implemented");

      //      final GAxisAlignedBox cartesianBounds = getBounds(positions._cartesianPoints, indexes);
      //      final Axis splitAxis = Axis.largestAxis(cartesianBounds);
      //      // System.out.println("==> max axis=" + splitAxis + "  " + cartesianBounds._extent);
      //
      //
      //      final IComparatorInt comparator = new IComparatorInt() {
      //         @Override
      //         public int compare(final int index1,
      //                            final int index2) {
      //            final GVector3D point1 = positions._cartesianPoints.get(index1);
      //            final GVector3D point2 = positions._cartesianPoints.get(index2);
      //            switch (splitAxis) {
      //               case X:
      //                  return compareXYZ(point1, point2);
      //               case Y:
      //                  return compareYZX(point1, point2);
      //               case Z:
      //                  return compareZXY(point1, point2);
      //            }
      //            throw new RuntimeException("Axis type not known: " + splitAxis);
      //         }
      //      };
      //      GCollections.quickSort(indexes, 0, indexesSize - 1, comparator);
      //
      //
      //      final int[] mediansVertexIndexes = new int[arity - 1];
      //      final int[] mediansIs = new int[arity - 1];
      //      for (int i = 1; i < arity; i++) {
      //         final int eachMedianI = (indexesSize / arity) * i;
      //         mediansIs[i - 1] = eachMedianI;
      //         mediansVertexIndexes[i - 1] = indexes[eachMedianI];
      //      }
      //      final int[][] childrenVerticesIndexes = new int[arity][];
      //      int from = 0;
      //      for (int i = 0; i < arity; i++) {
      //         final int to = (i == (arity - 1)) ? indexesSize : mediansIs[i];
      //         childrenVerticesIndexes[i] = Arrays.copyOfRange(indexes, from, to);
      //         from = to + 1;
      //      }
      //
      //
      //      //      final boolean ok1 = Arrays.equals(leftVerticesIndexes, childrenVerticesIndexes[0]);
      //      //      final boolean ok2 = Arrays.equals(rightVerticesIndexes, childrenVerticesIndexes[1]);
      //
      //      return new QuadInnerNode(parent, positions, splitAxis, mediansVertexIndexes, childrenVerticesIndexes, arity);
   }


   //   private static GVector3D getAverage3D(final List<GVector3D> positions,
   //                                         final int[] indexes) {
   //      double sumX = 0;
   //      double sumY = 0;
   //      double sumZ = 0;
   //
   //      for (final int index : indexes) {
   //         final GVector3D position = positions.get(index);
   //         sumX += position._x;
   //         sumY += position._y;
   //         sumZ += position._z;
   //      }
   //
   //      final int size = positions.size();
   //      return new GVector3D(sumX / size, sumY / size, sumZ / size);
   //   }


   private static GVector2D getAverage2D(final List<GVector3D> positions,
                                         final int[] indexes) {
      double sumX = 0;
      double sumY = 0;

      for (final int index : indexes) {
         final GVector3D position = positions.get(index);
         sumX += position._x;
         sumY += position._y;
      }

      final int size = positions.size();
      return new GVector2D(sumX / size, sumY / size);
   }


   private static int getPivotIndex(final PositionsSet positionsSet,
                                    final int[] indexes) {
      final GVector2D average = getAverage2D(positionsSet._cartesianPoints, indexes);
      double nearestDistance = Double.POSITIVE_INFINITY;
      int nearestIndex = 0;

      for (final int index : indexes) {
         final GVector3D point = positionsSet._cartesianPoints.get(index);
         final double distance = point.asVector2().squaredDistance(average);
         if (distance < nearestDistance) {
            nearestDistance = distance;
            nearestIndex = index;
         }
      }

      return nearestIndex;
   }


   private final QuadNode     _parent;
   private final PositionsSet _positionsSet;


   protected QuadNode(final QuadNode parent,
                      final PositionsSet positionsSet) {
      _parent = parent;
      _positionsSet = positionsSet;
   }


   public final int getDepth() {
      return (_parent == null) ? 0 : _parent.getDepth() + 1;
   }


   public abstract int[] getVertexIndexes();


   void breadthFirstAcceptVisitor(final QuadTreeVisitor visitor) throws QuadTreeVisitor.AbortVisiting {
      final LinkedList<QuadNode> queue = new LinkedList<QuadNode>();
      queue.addLast(this);

      while (!queue.isEmpty()) {
         final QuadNode current = queue.removeFirst();
         current.breadthFirstAcceptVisitor(visitor, queue);
      }
   }


   abstract void breadthFirstAcceptVisitor(QuadTreeVisitor visitor,
                                           LinkedList<QuadNode> queue) throws QuadTreeVisitor.AbortVisiting;

}
