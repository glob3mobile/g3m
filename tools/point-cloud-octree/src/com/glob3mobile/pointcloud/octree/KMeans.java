

package com.glob3mobile.pointcloud.octree;

import java.util.ArrayList;
import java.util.List;

import com.glob3mobile.utils.FlatPlanet;
import com.glob3mobile.utils.Geodetic3D;
import com.glob3mobile.utils.Utils;

import es.igosoftware.euclid.bounding.GAxisAlignedBox;
import es.igosoftware.euclid.vector.GVector3D;
import es.igosoftware.euclid.vector.IVector3;
import es.igosoftware.util.GCollections;
import es.igosoftware.util.IComparatorInt;


public class KMeans {


   public static class Cluster {

      public final Geodetic3D _centroid;


      private Cluster(final Geodetic3D centroid) {
         _centroid = centroid;
      }
   }


   private KMeans() {
   }


   public static List<Geodetic3D> cluster(final List<Geodetic3D> positions,
                                          final int k,
                                          final float verticalExaggeration) {
      final int positionsSize = positions.size();
      if (k > positionsSize) {
         throw new RuntimeException("Invalid K");
      }

      final int[] indexes = new int[positionsSize];
      for (int i = 0; i < positionsSize; i++) {
         indexes[i] = i;
      }

      final List<GVector3D> cartesianPoints = Utils.toCartesian(FlatPlanet.EARTH, positions, verticalExaggeration);

      final GAxisAlignedBox bounds = GAxisAlignedBox.minimumBoundingBox(cartesianPoints);
      //      final IVector3 center = bounds._center;
      final IVector3 lower = bounds._lower;

      final double[] distances = new double[positionsSize];
      for (int i = 0; i < positionsSize; i++) {
         distances[i] = cartesianPoints.get(i).squaredDistance(lower);
      }

      final IComparatorInt comparator = new IComparatorInt() {
         @Override
         public int compare(final int index1,
                            final int index2) {
            //            final GVector3D point1 = cartesianPoints.get(index1);
            //            final GVector3D point2 = cartesianPoints.get(index2);
            //            return compareXYZ(point1, point2);

            final double distance1 = distances[index1];
            final double distance2 = distances[index2];
            return Double.compare(distance1, distance2);

            //            final double mag1 = magnitude(cartesianPoints.get(index1));
            //            final double mag2 = magnitude(cartesianPoints.get(index2));
            //            return Double.compare(mag1, mag2);
         }


         //         private double magnitude(final GVector3D point) {
         //            final double boundsHeight = bounds._extent.y();
         //            final double boundsDepth = bounds._extent.z();
         //            return (point._x * boundsHeight * boundsDepth) + (point._y * boundsDepth) + point._z;
         //         }

      };

      GCollections.quickSort(indexes, comparator);

      final int[] centroidIndexes = new int[k];
      for (int i = 0; i < k; i++) {
         centroidIndexes[i] = -1;
      }
      final float centroidInterval = (float) positionsSize / k;
      final float centroidDelta = centroidInterval / 2;
      for (int i = 0; i < k; i++) {
         final int index = Math.round(centroidDelta + (centroidInterval * i));
         centroidIndexes[i] = index;
      }

      final List<Geodetic3D> result = new ArrayList<Geodetic3D>(k);
      for (final int centroidIndex : centroidIndexes) {
         result.add(positions.get(centroidIndex));
      }
      return result;
   }


   //   private static int compareXYZ(final GVector3D point1,
   //                                 final GVector3D point2) {
   //      final int compareX = Double.compare(point1._x, point2._x);
   //      if (compareX != 0) {
   //         return compareX;
   //      }
   //      final int compareY = Double.compare(point1._y, point2._y);
   //      if (compareY != 0) {
   //         return compareY;
   //      }
   //      return Double.compare(point1._z, point2._z);
   //   }


}
