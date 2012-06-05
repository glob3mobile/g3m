//
//  DummyRenderer.cpp
//  Glob3 Mobile
//
//  Created by Agustín Trujillo Pino on 02/05/11.
//  Copyright 2011 Universidad de Las Palmas. All rights reserved.
//

#include "DummyRenderer.hpp"

void DummyRenderer::initialize(const InitializationContext* ic)
{
}

bool DummyRenderer::onTouchEvent(const TouchEvent* event)
{
  return false;
}

int DummyRenderer::render(const RenderContext* rc)
{
  int res = 12;
  float *vertices = new float[res * res * 3];
  int numIndices = 2 * (res - 1) * (res + 1);
  unsigned char *index = new unsigned char[numIndices];
  
  // create vertices
  float size = 1e7;
  int n = 0;
  for (int j = 0; j < res; j++) {
    for (int i = 0; i < res; i++) {
      vertices[n++] = (float) 0;
      vertices[n++] = (float) (-size + i / (float) (res - 1) * 2*size);
      vertices[n++] = (float) (size - j / (float) (res - 1) * 2*size);
    }
  }
  
  n = 0;
  for (int j = 0; j < res - 1; j++) {
    if (j > 0) index[n++] = (char) (j * res);
    for (int i = 0; i < res; i++) {
      index[n++] = (char) (j * res + i);
      index[n++] = (char) (j * res + i + res);
    }
    index[n++] = (char) (j * res + 2 * res - 1);
  }
  
  
  
  // obtaing gl object reference
  IGL *gl = rc->getGL();
  
  // draw a red square
  gl->Color((float) 1, (float) 0, (float) 0);
  
  // insert pointers
  gl->DisableTextures();
  gl->VertexPointer(3, 0, vertices);
  
  gl->PushMatrix();
  gl->EnablePolygonOffset(1.0f, 5.0f);
  gl->DrawTriangleStrip(n, index);
  gl->DisablePolygonOffset();
  gl->PopMatrix();
  gl->EnableTextures();
  
  
  delete[] index;
  delete[] vertices;
  
  return 9999;
}
