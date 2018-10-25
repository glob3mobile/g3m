

package com.glob3mobile.tools.extruder;

import org.glob3.mobile.generated.GEOFeature;
import org.glob3.mobile.generated.GEOObject;

import com.glob3mobile.tools.mesh.G3MeshMaterial;


public interface ExtrusionHandler {


   void onRootGEOObject(GEOObject geoObject);


   /**
    * @param geoFeature
    * @return if the geoFeature's geometry has to be extruded or not
    */
   boolean extrudes(GEOFeature geoFeature);


   /**
    * @param geoFeature
    * @return if the extrusion mesh generated for the given geoFeature need to depthTest (in OpenGL meaning) while rendering
    */
   boolean getDepthTestFor(GEOFeature geoFeature);


   G3MeshMaterial getMaterialFor(GEOFeature geoFeature);


   Heigths getHeightsFor(GEOFeature geoFeature);


   void processTriangulationError(GEOFeature geoFeature);


   void onFinish();


}
