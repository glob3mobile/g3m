

package com.glob3mobile.pointcloud.octree.berkeleydb;

import java.util.List;

import com.glob3mobile.utils.Geodetic3D;


class PointsSet {
   final List<Geodetic3D> _points;
   final Geodetic3D       _averagePoint;


   PointsSet(final List<Geodetic3D> points,
             final Geodetic3D averagePoint) {
      _points = points;
      _averagePoint = averagePoint;
   }


   //   boolean isEmpty() {
   //      return _points.isEmpty();
   //   }


   int size() {
      return _points.size();
   }

}
