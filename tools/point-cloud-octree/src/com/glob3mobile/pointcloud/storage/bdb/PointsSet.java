

package com.glob3mobile.pointcloud.storage.bdb;

import java.util.List;

import com.glob3mobile.utils.Geodetic3D;


class PointsSet {

   private final List<PointData> _points;
   private final Geodetic3D      _averagePoint;


   PointsSet(final List<PointData> points,
            final Geodetic3D averagePoint) {
      _points = points;
      _averagePoint = averagePoint;
   }

}
