//
//  BusyRenderer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 20/07/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#include "BusyRenderer.hpp"

#include "Context.hpp"
#include "GL.hpp"


void BusyRenderer::initialize(const InitializationContext* ic)
{
  unsigned int numVertices = 8;
  int numIndices = 4;
  float x=7e6, y=1e6;
  
  float v[] = {
    x, -y, y,
    x, -y, -y,
    x, y, y,
    x, y, -y
  };
  
  int i[] = { 0, 1, 2, 3};
  
  // create vertices and indices in dinamic memory
  _vertices = new float [numVertices*3];
  memcpy(_vertices, v, numVertices*3*sizeof(float));
  _indices = new int [numIndices];
  memcpy(_indices, i, numIndices*sizeof(unsigned int));  
}  


int BusyRenderer::render(const RenderContext* rc) 
{  
  GL* gl = rc->getGL();
  gl->clearScreen(0.0f, 0.2f, 0.4f, 1.0f);
  gl->disableTexture2D();
  gl->enableVerticesPosition();
    
  gl->vertexPointer(3, 0, _vertices);
  gl->drawTriangleStrip(4, _indices);

  return MAX_TIME_TO_RENDER;
}
