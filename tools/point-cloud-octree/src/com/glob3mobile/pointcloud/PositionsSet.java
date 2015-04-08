

package com.glob3mobile.pointcloud;

import java.util.ArrayList;
import java.util.List;

import com.glob3mobile.utils.Geodetic3D;
import com.glob3mobile.utils.Planet;

import es.igosoftware.euclid.vector.GVector3D;


public class PositionsSet {
   final List<Geodetic3D>       _positions;
   public final List<GVector3D> _cartesianPoints;


   public PositionsSet(final Planet planet,
                       final List<Geodetic3D> positions) {
      _positions = positions;

      _cartesianPoints = new ArrayList<GVector3D>(positions.size());
      for (final Geodetic3D position : positions) {
         final GVector3D point = planet.toCartesian(position, 1);
         _cartesianPoints.add(point);
      }
   }


   @Override
   public String toString() {
      return "[PositionsSet positions=" + _positions.size() + "]";
   }

}
