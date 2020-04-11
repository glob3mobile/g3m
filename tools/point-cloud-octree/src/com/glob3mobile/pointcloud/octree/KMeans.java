

package com.glob3mobile.pointcloud.octree;


import java.util.*;

import com.glob3mobile.utils.*;

import es.igosoftware.euclid.bounding.*;
import es.igosoftware.euclid.vector.*;
import es.igosoftware.util.*;


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
      // final IVector3 center = bounds._center;
      final IVector3 lower = bounds._lower;

      final double[] distances = new double[positionsSize];
      for (int i = 0; i < positionsSize; i++) {
         distances[i] = cartesianPoints.get(i).squaredDistance(lower);
      }

      final IComparatorInt comparator = (index1,
                                         index2) -> {
         final double distance1 = distances[index1];
         final double distance2 = distances[index2];
         return Double.compare(distance1, distance2);
      };

      GCollections.quickSort(indexes, comparator);

      final int[] centroidIndexes = new int[k];
      for (int i = 0; i < k; i++) {
         centroidIndexes[i] = -1;
      }
      final float centroidInterval = (float) positionsSize / k;
      final float centroidDelta    = centroidInterval / 2;
      for (int i = 0; i < k; i++) {
         final int index = Math.round(centroidDelta + (centroidInterval * i));
         centroidIndexes[i] = index;
      }

      final List<Geodetic3D> result = new ArrayList<>(k);
      for (final int centroidIndex : centroidIndexes) {
         result.add(positions.get(centroidIndex));
      }
      return result;
   }


}
