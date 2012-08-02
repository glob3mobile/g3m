//
//  LatLonMesh.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 02/08/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

#include <iostream>

#include "LatLonMesh.h"
#include "Planet.hpp"


LatLonMesh::LatLonMesh(const InitializationContext *ic,
           bool owner,
           const GLPrimitive primitive,
           CenterStrategy strategy,
           Vector3D center,
           const unsigned int numVertices,
           float* vertices,
           const unsigned int* indexes,
           const int numIndex, 
           const Color* flatColor,
           const float * colors,
           const float colorsIntensity,
           const float* normals)
{
  // convert vertices to latlon coordinates
  const Planet *planet = ic->getPlanet();
  for (unsigned int n=0; n<numVertices*3; n+=3) {
    Geodetic3D g(Angle::fromDegrees(vertices[n]), Angle::fromDegrees(vertices[n+1]), vertices[n+2]);
    Vector3D v = planet->toVector3D(g);
    vertices[n]   = (float) v.x();
    vertices[n+1] = (float) v.y();
    vertices[n+2] = (float) v.z();
  }
  
  // create indexed mesh
  mesh = new IndexedMesh(owner, primitive, strategy, center,
                         numVertices, vertices, indexes, numIndex,
                         flatColor, colors, colorsIntensity, normals);
}
