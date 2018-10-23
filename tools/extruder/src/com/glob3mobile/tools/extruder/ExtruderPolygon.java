

package com.glob3mobile.tools.extruder;

import org.glob3.mobile.generated.GEOFeature;

import com.glob3mobile.tools.mesh.G3MeshMaterial;


public class ExtruderPolygon {

   final GEOFeature     _geoFeature;
   final double         _lowerHeight;
   final double         _upperHeight;
   final G3MeshMaterial _material;
   final boolean        _depthTest;


   protected ExtruderPolygon(final GEOFeature geoFeature,
                             final double lowerHeight,
                             final double upperHeight,
                             final G3MeshMaterial material,
                             final boolean depthTest) {
      _geoFeature = geoFeature;
      _lowerHeight = lowerHeight;
      _upperHeight = upperHeight;
      _material = material;
      _depthTest = depthTest;
   }
}
