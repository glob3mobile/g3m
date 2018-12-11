

package com.glob3mobile.tools.extruder;

import java.util.List;

import com.glob3mobile.tools.mesh.G3MeshCollection;
import com.glob3mobile.tools.mesh.G3MeshMaterial;


public interface ExtrusionHandler<T> {


   boolean extrudes(T source);


   G3MeshMaterial getMaterialFor(T source);


   boolean getDepthTestFor(T source);


   Heigths getHeightsFor(T source);


   void processTriangulationError(T source);


   void onBuildings(List<Building<T>> buildings);


   void onPolygons(List<ExtruderPolygon<T>> polygons);


   void onMeshCollection(G3MeshCollection meshes);


}
