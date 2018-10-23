

package com.glob3mobile.tools.extruder;

import java.util.List;

import org.glob3.mobile.generated.GEOFeature;
import org.glob3.mobile.generated.Geodetic2D;

import com.glob3mobile.tools.mesh.G3MeshMaterial;


public class Extruder2DPolygon
         extends
            ExtruderPolygon {

   final List<Geodetic2D>       _coordinates;
   final List<List<Geodetic2D>> _holesCoordinatesArray;


   Extruder2DPolygon(final GEOFeature geoFeature,
                     final List<Geodetic2D> coordinates,
                     final List<List<Geodetic2D>> holesCoordinatesArray,
                     final double lowerHeight,
                     final double upperHeight,
                     final G3MeshMaterial material,
                     final boolean depthTest) {
      super(geoFeature, lowerHeight, upperHeight, material, depthTest);
      _coordinates = coordinates;
      _holesCoordinatesArray = holesCoordinatesArray;
   }


}
