
package com.glob3mobile.tools.extruder;

import java.util.*;

import com.glob3mobile.tools.mesh.*;

public interface ExtrusionHandler<T1, T2> {

   //   boolean extrudes(T1 source1,
   //                    T2 source2);

   G3MeshMaterial getMaterialFor(T1 source1, T2 source2);

   boolean getDepthTestFor(T1 source1, T2 source2);

   Heigths getHeightsFor(T1 source1, T2 source2);

   void onBuildings(List<Building> buildings);

   void onPolygons(List<ExtruderPolygon> polygons);

   void onMeshCollection(G3MeshCollection meshes);

}
