

package com.glob3mobile.tools.extruder;

import java.util.List;

import org.glob3.mobile.generated.GEOFeature;
import org.glob3.mobile.generated.Geodetic3D;

import com.glob3mobile.tools.mesh.G3MeshMaterial;


public class Extruder3DPolygon
         extends
            ExtruderPolygon {

   final List<Geodetic3D>       _coordinates;
   final List<List<Geodetic3D>> _holesCoordinatesArray;


   Extruder3DPolygon(final GEOFeature geoFeature,
                     final List<Geodetic3D> coordinates,
                     final List<List<Geodetic3D>> holesCoordinatesArray,
                     final double lowerHeight,
                     final double upperHeight,
                     final G3MeshMaterial material,
                     final boolean depthTest) {
      super(geoFeature, lowerHeight, upperHeight, material, depthTest);
      _coordinates = coordinates;
      _holesCoordinatesArray = holesCoordinatesArray;
   }


}
