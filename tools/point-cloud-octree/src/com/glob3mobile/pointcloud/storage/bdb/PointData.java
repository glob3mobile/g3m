

package com.glob3mobile.pointcloud.storage.bdb;

import java.awt.Color;

import com.glob3mobile.pointcloud.Classification;
import com.glob3mobile.utils.Geodetic3D;


class PointData {
   final Geodetic3D             _position;
   private final float          _intensity;
   private final Classification _classification;
   private final Color          _color;


   PointData(final Geodetic3D position,
             final float intensity,
             final Classification classification,
             final Color color) {
      _position = position;
      _intensity = intensity;
      _classification = classification;
      _color = color;
   }


}
