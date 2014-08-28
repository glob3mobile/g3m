

package com.glob3mobile.pointcloud.kdtree;

import java.util.ArrayList;
import java.util.List;

import com.glob3mobile.pointcloud.octree.Geodetic3D;

import es.igosoftware.euclid.vector.GVector3D;


class PositionsSet {
   final List<Geodetic3D> _positions;
   final List<GVector3D>  _cartesianPoints;


   PositionsSet(final List<Geodetic3D> positions) {
      _positions = positions;

      _cartesianPoints = new ArrayList<GVector3D>(positions.size());
      final Planet planet = FlatPlanet.EARTH;
      for (final Geodetic3D position : positions) {
         final GVector3D point = planet.toCartesian(position);
         _cartesianPoints.add(point);
      }
   }


   @Override
   public String toString() {
      return "[PositionsSet positions=" + _positions.size() + "]";
   }

}
