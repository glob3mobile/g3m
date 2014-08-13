

package com.glob3mobile.pointcloud.kdtree;

import es.igosoftware.euclid.bounding.GAxisAlignedBox;
import es.igosoftware.euclid.vector.IVector3;


public enum Axis {
   X,
   Y,
   Z;


   static Axis largestAxis(final GAxisAlignedBox bounds) {
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
