//
//  LatLonMesh.h
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 02/08/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

#ifndef G3MiOSSDK_LatLonMesh_h
#define G3MiOSSDK_LatLonMesh_h

#include "IndexedMesh.hpp"

class LatLonMesh: public IndexedMesh {
  
public:
  LatLonMesh(bool owner,
             const GLPrimitive primitive,
             CenterStrategy strategy,
             Vector3D center,
             const unsigned int numVertices,
             const float* vertices,
             const unsigned int* indexes,
             const int numIndex, 
             const Color* flatColor = NULL,
             const float * colors = NULL,
             const float colorsIntensity = 0.0,
             const float* normals = NULL) {
    return new IndexedMesh(owner, primitive, strategy, center, 
                           numVertices, vertices, indexes, numIndex,
                           flatColor, colors, colorsIntensity, normals);
    
  }

};


#endif
