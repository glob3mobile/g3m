//
//  BusyRenderer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 20/07/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//


#include <OpenGLES/ES2/gl.h>


#include "BusyRenderer.hpp"

#include "Context.hpp"
#include "GL.hpp"
#include "MutableMatrix44D.hpp"

int __agustin_note; // this function is already implemented in shaders branch
MutableMatrix44D createOrthographicProjectionMatrix(double left, double right,
                                                    double bottom, double top,
                                                    double znear, double zfar) 
{
  // set frustum matrix in double
  const double rl = right - left;
  const double tb = top - bottom;
  const double fn = zfar - znear;
  
  double P[16];
  P[0] = 2 / rl;
  P[1] = P[2] = P[3] = P[4] = 0;
  P[5] = 2 / tb;
  P[6] = P[7] = P[8] = P[9] = 0;
  P[10] = -2 / fn;
  P[11] = 0;
  P[12] = -(right+left) / rl;
  P[13] = -(top+bottom) / tb;
  P[14] = -(zfar+znear) / fn;
  P[15] = 1;
  
  return MutableMatrix44D(P);
}


void BusyRenderer::initialize(const InitializationContext* ic)
{
  unsigned int numVertices = 8;
  int numIndices = 4;
  float x=100, y=150;
  
  float v[] = {
    -x,   y,  0,
    -x,   -y, 0,
    x,    y,  0,
    x,    -y, 0
  };
  
  int i[] = { 0, 1, 2, 3};
  
  // create vertices and indices in dinamic memory
  float *vertices = new float [numVertices*3];
  memcpy(vertices, v, numVertices*3*sizeof(float));
  int *indices = new int [numIndices];
  memcpy(indices, i, numIndices*sizeof(int));  
  
  // create mesh
  Color *flatColor = new Color(Color::fromRGBA(1.0, 1.0, 0.0, 1.0));
  _mesh = IndexedMesh::CreateFromVector3D(true, TriangleStrip, NoCenter, Vector3D(0,0,0), 
                                           4, vertices, indices, 4, flatColor);
}  


int BusyRenderer::render(const RenderContext* rc) 
{  
  GL* gl = rc->getGL();
  
  // init modelview matrix
  GLint currentViewport[4];
  glGetIntegerv(GL_VIEWPORT, currentViewport);
  int halfWidth = currentViewport[2];
  int halfHeight = currentViewport[3];
  MutableMatrix44D M = createOrthographicProjectionMatrix(-halfWidth, halfWidth, -halfHeight, halfHeight, -1, 1);
  gl->setProjection(M);
  gl->loadMatrixf(MutableMatrix44D::identity());
  
  // clear screen
  gl->clearScreen(0.0f, 0.2f, 0.4f, 1.0f);
  
  // draw mesh
  _mesh->render(rc);

  return MAX_TIME_TO_RENDER;
}
