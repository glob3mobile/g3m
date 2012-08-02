//
//  LatLonMeshRenderer.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 02/08/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

#include <iostream>

#include "LatLonMeshRenderer.h"


LatLonMeshRenderer::~LatLonMeshRenderer()
{
  delete mesh;
}


void LatLonMeshRenderer::initialize(const InitializationContext* ic)
{
  unsigned int numVertices = 4;
  unsigned int numIndices = 4;
  
  float v[] = {
    3e6,  -7e6, 7e6,
    3e6,  -7e6, -7e6,
    3e6,  7e6,  7e6,
    3e6,  7e6,  -7e6 
  };
  
  unsigned int i[] = { 0, 1, 2, 3};
  
  // create vertices and indices in dinamic memory
  float *vertices = new float [numVertices*3];
  memcpy(vertices, v, numVertices*3*sizeof(float));
  unsigned int *indices = new unsigned int [numIndices];
  memcpy(indices, i, numIndices*sizeof(unsigned int));

  // create mesh
  mesh = new LatLonMesh(true, TriangleStrip, NoCenter, Vector3D(0,0,0), 
                        4, vertices, indices, 4);
}  


int LatLonMeshRenderer::render(const RenderContext* rc)
{  
  mesh->render(rc);

  return MAX_TIME_TO_RENDER;
}
