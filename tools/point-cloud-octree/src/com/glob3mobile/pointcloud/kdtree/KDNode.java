

package com.glob3mobile.pointcloud.kdtree;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.glob3mobile.pointcloud.PositionsSet;

import es.igosoftware.euclid.bounding.GAxisAlignedBox;
import es.igosoftware.euclid.vector.GVector3D;
import es.igosoftware.util.GCollections;
import es.igosoftware.util.IComparatorInt;


public abstract class KDNode {


   private static int compareZXY(final GVector3D point1,
                                 final GVector3D point2) {
      final int compareZ = Double.compare(point1._z, point2._z);
      if (compareZ != 0) {
         return compareZ;
      }
      final int compareX = Double.compare(point1._x, point2._x);
      if (compareX != 0) {
         return compareX;
      }
      return Double.compare(point1._y, point2._y);
   }


   private static int compareXYZ(final GVector3D point1,
                                 final GVector3D point2) {
      final int compareX = Double.compare(point1._x, point2._x);
      if (compareX != 0) {
         return compareX;
      }
      final int compareY = Double.compare(point1._y, point2._y);
      if (compareY != 0) {
         return compareY;
      }
      return Double.compare(point1._z, point2._z);
   }


   private static int compareYZX(final GVector3D point1,
                                 final GVector3D point2) {
      final int compareY = Double.compare(point1._y, point2._y);
      if (compareY != 0) {
         return compareY;
      }
      final int compareZ = Double.compare(point1._z, point2._z);
      if (compareZ != 0) {
         return compareZ;
      }
      return Double.compare(point1._x, point2._x);
   }


   static KDNode create(final KDNode parent,
                        final PositionsSet positions,
                        final int[] indexes,
                        final int arity) {

      final int indexesSize = indexes.length;

      if (indexesSize == 0) {
         return null;
      }

      if (indexesSize == 1) {
         return new KDMonoLeafNode(parent, positions, indexes[0]);
      }

      if (indexesSize <= arity) {
         return new KDMultiLeafNode(parent, positions, indexes);
      }

      final GAxisAlignedBox cartesianBounds = getBounds(positions._cartesianPoints, indexes);
      final Axis splitAxis = Axis.largestAxis(cartesianBounds);
      // System.out.println("==> max axis=" + splitAxis + "  " + cartesianBounds._extent);


      final IComparatorInt comparator = new IComparatorInt() {
         @Override
         public int compare(final int index1,
                            final int index2) {
            final GVector3D point1 = positions._cartesianPoints.get(index1);
            final GVector3D point2 = positions._cartesianPoints.get(index2);
            switch (splitAxis) {
               case X:
                  return compareXYZ(point1, point2);
               case Y:
                  return compareYZX(point1, point2);
               case Z:
                  return compareZXY(point1, point2);
            }
            throw new RuntimeException("Axis type not known: " + splitAxis);
         }
      };
      GCollections.quickSort(indexes, 0, indexesSize - 1, comparator);


      //      final int medianI = (indexesSize / 2);
      //
      //      //      final GVector3D average = average(positions._cartesianPoints, indexes);
      //      //      final int medianI = findNearest(average, positions._cartesianPoints, indexes);
      //
      //      final int[] leftVerticesIndexes = Arrays.copyOfRange(indexes, 0, medianI);
      //      final int[] rightVerticesIndexes = Arrays.copyOfRange(indexes, medianI + 1, indexesSize);
      //
      //      final int medianVertexIndex = indexes[medianI];

      //      return new KDInnerNode(parent, positions, axis, medianVertexIndex, leftVerticesIndexes, rightVerticesIndexes);

      final int[] mediansVertexIndexes = new int[arity - 1];
      final int[] mediansIs = new int[arity - 1];
      for (int i = 1; i < arity; i++) {
         final int eachMedianI = (indexesSize / arity) * i;
         mediansIs[i - 1] = eachMedianI;
         mediansVertexIndexes[i - 1] = indexes[eachMedianI];
      }
      final int[][] childrenVerticesIndexes = new int[arity][];
      int from = 0;
      for (int i = 0; i < arity; i++) {
         final int to = (i == (arity - 1)) ? indexesSize : mediansIs[i];
         childrenVerticesIndexes[i] = Arrays.copyOfRange(indexes, from, to);
         from = to + 1;
      }


      //      final boolean ok1 = Arrays.equals(leftVerticesIndexes, childrenVerticesIndexes[0]);
      //      final boolean ok2 = Arrays.equals(rightVerticesIndexes, childrenVerticesIndexes[1]);

      return new KDInnerNode(parent, positions, splitAxis, mediansVertexIndexes, childrenVerticesIndexes, arity);
   }


   //   private static int findNearest(final GVector3D average,
   //                                  final List<GVector3D> cartesianPoints,
   //                                  final int[] indexes) {
   //      double shortestDistance = cartesianPoints.get(indexes[0]).squaredDistance(average);
   //      int shortestI = 0;
   //      for (int i = 1; i < indexes.length; i++) {
   //         final GVector3D point = cartesianPoints.get(indexes[i]);
   //         final double distance = point.squaredDistance(average);
   //         if (distance < shortestDistance) {
   //            shortestDistance = distance;
   //            shortestI = i;
   //         }
   //      }
   //      return shortestI;
   //   }


   private static GAxisAlignedBox getBounds(final List<GVector3D> cartesianPoints,
                                            final int[] indexes) {
      double minX = Double.POSITIVE_INFINITY;
      double minY = Double.POSITIVE_INFINITY;
      double minZ = Double.POSITIVE_INFINITY;

      double maxX = Double.NEGATIVE_INFINITY;
      double maxY = Double.NEGATIVE_INFINITY;
      double maxZ = Double.NEGATIVE_INFINITY;

      for (final int index : indexes) {
         final GVector3D point = cartesianPoints.get(index);

         final double x = point.x();
         final double y = point.y();
         final double z = point.z();

         minX = Math.min(minX, x);
         minY = Math.min(minY, y);
         minZ = Math.min(minZ, z);

         maxX = Math.max(maxX, x);
         maxY = Math.max(maxY, y);
         maxZ = Math.max(maxZ, z);
      }

      final GVector3D lower = new GVector3D(minX, minY, minZ);
      final GVector3D upper = new GVector3D(maxX, maxY, maxZ);
      return new GAxisAlignedBox(lower, upper);
   }


   //   private static GVector3D average(final List<GVector3D> cartesianPoints,
   //                                    final int[] indexes) {
   //      double sumX = 0;
   //      double sumY = 0;
   //      double sumZ = 0;
   //      for (final int index : indexes) {
   //         final GVector3D point = cartesianPoints.get(index);
   //         sumX += point._x;
   //         sumY += point._y;
   //         sumZ += point._z;
   //      }
   //      final int indexesSize = indexes.length;
   //      return new GVector3D( //
   //               sumX / indexesSize, //
   //               sumY / indexesSize, //
   //               sumZ / indexesSize);
   //   }


   private final KDNode       _parent;
   private final PositionsSet _positions;


   protected KDNode(final KDNode parent,
                    final PositionsSet positions) {
      _parent = parent;
      _positions = positions;
   }


   void breadthFirstAcceptVisitor(final KDTreeVisitor visitor) throws KDTreeVisitor.AbortVisiting {
      final LinkedList<KDNode> queue = new LinkedList<KDNode>();
      queue.addLast(this);

      while (!queue.isEmpty()) {
         final KDNode current = queue.removeFirst();
         current.breadthFirstAcceptVisitor(visitor, queue);
      }
   }


   abstract void breadthFirstAcceptVisitor(KDTreeVisitor visitor,
                                           LinkedList<KDNode> queue) throws KDTreeVisitor.AbortVisiting;


   public final int getDepth() {
      return (_parent == null) ? 0 : _parent.getDepth() + 1;
   }


   public abstract int[] getVertexIndexes();

}
