

package com.glob3mobile.tools.extruder;

import org.glob3.mobile.generated.GEOFeature;

import com.glob3mobile.tools.mesh.G3MeshMaterial;


public interface ExtrusionHandler {
   /**
    * @param geoFeature
    * @return if the geoFeature's geometry has to be extruded or not
    */
   public boolean extrudes(GEOFeature geoFeature);


   /**
    * @param geoFeature
    * @return if the extrusion mesh generated for the given geoFeature need to depthTest (in OpenGL meaning) while rendering
    */
   public boolean getDepthTestFor(GEOFeature geoFeature);


   public G3MeshMaterial getMaterialFor(GEOFeature geoFeature);


   public Heigths getHeightsFor(GEOFeature geoFeature);


   public void processTriangulationError(GEOFeature geoFeature);

}
