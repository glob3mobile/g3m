//
//  BusyMeshRenderer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 20/07/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//


#include <OpenGLES/ES2/gl.h>


#include "BusyMeshRenderer.hpp"

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


void BusyMeshRenderer::initialize(const InitializationContext* ic)
{  
  // compute number of vertex for the ring
  unsigned int numStrides = 60;
  unsigned int numVertices = numStrides * 2 + 2;
  int numIndices = numVertices;
  
  // add number of vertex for the square
  
  // create vertices and indices in dinamic memory
  float *vertices = new float [numVertices*3];
  int *indices = new int [numIndices];
  float *colors = new float [numVertices*4];
  
  // create vertices
  unsigned int nv=0, ni=0, nc=0;
//  float r1=200, r2=230;
  float r1=12, r2=18;
  for (unsigned int step=0; step<=numStrides; step++) {
    double angle = (double) step * 2 * M_PI / numStrides;
    double c = cos(angle);
    double s = sin(angle);
    vertices[nv++]  = (float) (r1 * c);
    vertices[nv++]  = (float) (r1 * s);
    vertices[nv++]  = 0.0;
    vertices[nv++]  = (float) (r2 * c);
    vertices[nv++]  = (float) (r2 * s);
    vertices[nv++]  = 0.0;
    indices[ni]     = ni;
    indices[ni+1]   = ni+1;
    ni+=2;    
    float col       = 1.1 * step / numStrides;
    if (col>1) {
      colors[nc++]    = 255;
      colors[nc++]    = 255;
      colors[nc++]    = 255;
      colors[nc++]    = 0;
      colors[nc++]    = 255;
      colors[nc++]    = 255;
      colors[nc++]    = 255;
      colors[nc++]    = 0;      
    } else {
      colors[nc++]    = 255;
      colors[nc++]    = 255;
      colors[nc++]    = 255;
      colors[nc++]    = 1-col;
      colors[nc++]    = 255;
      colors[nc++]    = 255;
      colors[nc++]    = 255;
      colors[nc++]    = 1-col;
    }
  }

  // the two last indices
  indices[ni++]     = 0;
  indices[ni++]     = 1;

  
  // create mesh
  //Color *flatColor = new Color(Color::fromRGBA(1.0, 1.0, 0.0, 1.0));

  _mesh = IndexedMesh::CreateFromVector3D(true, TriangleStrip, NoCenter, Vector3D(0,0,0), 
                                           numVertices, vertices, indices, numIndices, NULL, colors);
}  

void BusyMeshRenderer::start() {
  int _TODO_start_effects;
}

void BusyMeshRenderer::stop() {
  int _TODO_stop_effects;
}

int BusyMeshRenderer::render(const RenderContext* rc) 
{  
  GL* gl = rc->getGL();
  
  // init effect in the first render
  static bool firstTime = true;
  if (firstTime) {
    firstTime = false;
    Effect *effect = new BusyMeshEffect(this);
    rc->getEffectsScheduler()->startEffect(effect, this);
  }

  // init modelview matrix
  GLint currentViewport[4];
  glGetIntegerv(GL_VIEWPORT, currentViewport);
  int halfWidth = currentViewport[2] / 2;
  int halfHeight = currentViewport[3] / 2;
  MutableMatrix44D M = createOrthographicProjectionMatrix(-halfWidth, halfWidth, -halfHeight, halfHeight, -halfWidth, halfWidth);
  gl->setProjection(M);
  gl->loadMatrixf(MutableMatrix44D::identity());
  
  // clear screen
//  gl->clearScreen(0.0f, 0.2f, 0.4f, 1.0f);
  
  glEnable(GL_BLEND);
  glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
  
  gl->pushMatrix();
  MutableMatrix44D R1 = MutableMatrix44D::createRotationMatrix(Angle::fromDegrees(0), Vector3D(-1, 0, 0));
  MutableMatrix44D R2 = MutableMatrix44D::createRotationMatrix(Angle::fromDegrees(_degrees), Vector3D(0, 0, -1));
  gl->multMatrixf(R1.multiply(R2));
  
  // draw mesh
  _mesh->render(rc);
  
  

  gl->popMatrix();
  
  glDisable(GL_BLEND);
  
  return MAX_TIME_TO_RENDER;
}
