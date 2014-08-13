

package com.glob3mobile.pointcloud.kdtree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.glob3mobile.pointcloud.octree.Geodetic3D;

import es.igosoftware.euclid.bounding.GAxisAlignedBox;
import es.igosoftware.euclid.vector.GVector3D;
import es.igosoftware.euclid.vector.IVector3;
import es.igosoftware.util.GCollections;
import es.igosoftware.util.IComparatorInt;


public class KDTree {

   private static enum Axis {
      X,
      Y,
      Z;


      private static Axis largestAxis(final GAxisAlignedBox bounds) {
         final IVector3 extent = bounds._extent;
         final double x = extent.x();
         final double y = extent.y();
         final double z = extent.z();
         if ((x > y) && (x > z)) {
            return X;
         }
         if ((y > x) && (y > z)) {
            return Y;
         }
         return Z;
      }
   }


   private static class PositionsSet {
      private final List<Geodetic3D> _positions;
      private final List<GVector3D>  _cartesianPoints;
      private final GAxisAlignedBox  _cartesianBounds;


      private PositionsSet(final List<Geodetic3D> positions) {
         _positions = positions;

         _cartesianPoints = new ArrayList<GVector3D>(positions.size());

         double minX = Double.POSITIVE_INFINITY;
         double minY = Double.POSITIVE_INFINITY;
         double minZ = Double.POSITIVE_INFINITY;

         double maxX = Double.NEGATIVE_INFINITY;
         double maxY = Double.NEGATIVE_INFINITY;
         double maxZ = Double.NEGATIVE_INFINITY;

         for (final Geodetic3D position : positions) {
            final GVector3D point = EllipsoidalPlanet.EARTH.toCartesian(position);
            _cartesianPoints.add(point);

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
         _cartesianBounds = new GAxisAlignedBox(lower, upper);
      }


      @Override
      public String toString() {
         return "[PositionsSet positions=" + _positions.size() + ", bounds=" + _cartesianBounds + "]";
      }
   }


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


   private static int compareYXZ(final GVector3D point1,
                                 final GVector3D point2) {
      final int compareY = Double.compare(point1._y, point2._y);
      if (compareY != 0) {
         return compareY;
      }
      final int compareX = Double.compare(point1._x, point2._x);
      if (compareX != 0) {
         return compareX;
      }
      return Double.compare(point1._z, point2._z);
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


   public static abstract class Node {

      private final Node         _parent;
      private final PositionsSet _positions;


      protected Node(final Node parent,
                     final PositionsSet positions) {
         _parent = parent;
         _positions = positions;
      }


      private static Node create(final Node parent,
                                 final PositionsSet positions,
                                 final int[] indexes) {

         final int indexesSize = indexes.length;

         if (indexesSize == 0) {
            return null;
         }

         if (indexesSize == 1) {
            final int vertexIndex = indexes[0];
            return createMonoLeafNode(parent, positions, vertexIndex);
         }


         final GAxisAlignedBox cartesianBounds = getBounds(positions._cartesianPoints, indexes);
         final Axis axis = Axis.largestAxis(cartesianBounds);
         //System.out.println("==> max axis=" + axis + "  " + cartesianBounds._extent);


         final IComparatorInt comparator = new IComparatorInt() {
            @Override
            public int compare(final int index1,
                               final int index2) {
               final GVector3D point1 = positions._cartesianPoints.get(index1);
               final GVector3D point2 = positions._cartesianPoints.get(index2);
               switch (axis) {
                  case X:
                     return compareXYZ(point1, point2);
                  case Y:
                     return compareYXZ(point1, point2);
                  case Z:
                     return compareZXY(point1, point2);
               }
               throw new RuntimeException("Axis type not known: " + axis);
            }
         };
         GCollections.quickSort(indexes, 0, indexesSize - 1, comparator);


         final int medianI = (indexesSize / 2);

         final int[] leftVerticesIndexes = Arrays.copyOfRange(indexes, 0, medianI);
         final int[] rightVerticesIndexes = Arrays.copyOfRange(indexes, medianI + 1, indexesSize);

         final int medianVertexIndex = indexes[medianI];

         //         System.out.println(" ==> medianVertexIndex=" + medianVertexIndex + //
         //                            ", median=" + positions._positions.get(medianVertexIndex) + //
         //                            ", cartesian=" + positions._cartesianPoints.get(medianVertexIndex) + //
         //                            ", left side=" + leftVerticesIndexes.length + //
         //                            ", right side=" + rightVerticesIndexes.length);

         return new InnerNode(parent, positions, axis, medianVertexIndex, leftVerticesIndexes, rightVerticesIndexes);
      }


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


      private static Node createMonoLeafNode(final Node parent,
                                             final PositionsSet positions,
                                             final int vertexIndex) {
         return new MonoLeafNode(parent, positions, vertexIndex);
      }

   }

   private static class MonoLeafNode
            extends
               Node {

      private final int _vertexIndex;


      private MonoLeafNode(final Node parent,
                           final PositionsSet positions,
                           final int vertexIndex) {
         super(parent, positions);
         _vertexIndex = vertexIndex;
      }
   }

   private static class InnerNode
   extends
   Node {
      private final Axis _splitAxis;
      private final int  _medianVertexIndex;
      private final Node _leftNode;
      private final Node _rightNode;


      private InnerNode(final Node parent,
                        final PositionsSet positions,
                        final Axis splitAxis,
                        final int medianVertexIndex,
                        final int[] leftVerticesIndexes,
                        final int[] rightVerticesIndexes) {
         super(parent, positions);
         _splitAxis = splitAxis;
         _medianVertexIndex = medianVertexIndex;
         _leftNode = Node.create(this, positions, leftVerticesIndexes);
         _rightNode = Node.create(this, positions, rightVerticesIndexes);
      }

   }


   private final Node _root;


   public KDTree(final List<Geodetic3D> positions) {
      //      final long start = System.currentTimeMillis();

      final PositionsSet positionsSet = new PositionsSet(positions);
      final int indexesSize = positions.size();
      final int[] indexes = new int[indexesSize];
      for (int i = 0; i < indexesSize; i++) {
         indexes[i] = i;
      }

      //log(positionsSet.toString());
      _root = Node.create(null, positionsSet, indexes);

      //      final long elapsed = System.currentTimeMillis() - start;
      //      System.out.println("==> created KDTree in " + elapsed + "ms");
   }


}
