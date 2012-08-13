//
//  BusyQuadRenderer.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 13/08/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

#include <OpenGLES/ES2/gl.h>


#include "BusyQuadRenderer.hpp"

#include "Context.hpp"
#include "GL.hpp"
#include "MutableMatrix44D.hpp"
#include "TexturesHandler.hpp"
#include "TextureMapping.hpp"
#include "TexturedMesh.hpp"


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


bool BusyQuadRenderer::initMesh(const RenderContext* rc)
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
  float r1=200, r2=230;
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
    float col       = 1.1f * step / numStrides;
    if (col>1) {
      colors[nc++]    = 128;
      colors[nc++]    = 128;
      colors[nc++]    = 128;
      colors[nc++]    = 0;
      colors[nc++]    = 128;
      colors[nc++]    = 128;
      colors[nc++]    = 128;
      colors[nc++]    = 0;      
    } else {
      colors[nc++]    = 128;
      colors[nc++]    = 128;
      colors[nc++]    = 128;
      colors[nc++]    = 1-col;
      colors[nc++]    = 128;
      colors[nc++]    = 128;
      colors[nc++]    = 128;
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
  
  // create quad
  numVertices = 4;
  numIndices = 4;
  float *quadVertices = new float [numVertices*3];
  int *quadIndices = new int [numIndices];
  float *texC = new float [numVertices*2];
  
  nv = 0;
  quadVertices[nv++] = -200;    quadVertices[nv++] = 200;   quadVertices[nv++] = 0;
  quadVertices[nv++] = -200;    quadVertices[nv++] = -200;  quadVertices[nv++] = 0;
  quadVertices[nv++] = 200;     quadVertices[nv++] = 200;   quadVertices[nv++] = 0;
  quadVertices[nv++] = 200;     quadVertices[nv++] = -200;  quadVertices[nv++] = 0;
  
  for (unsigned int n=0; n<numIndices; n++) quadIndices[n] = n;
  
  nc = 0;
  texC[nc++] = 0;    texC[nc++] = 0.0;
  texC[nc++] = 0;    texC[nc++] = 1.0;
  texC[nc++] = 1;    texC[nc++] = 0.0;
  texC[nc++] = 1;    texC[nc++] = 1.0;
  
  
  //TEXTURED
  GLTextureID texID = GLTextureID::invalid();
  if (true){
    texID = rc->getTexturesHandler()->getGLTextureIdFromFileName(_textureFilename, 2048, 1024);
    if (!texID.isValid()) {
      rc->getLogger()->logError("Can't load file %s", _textureFilename.c_str());
      return false;
    }
  }


  IndexedMesh *im = IndexedMesh::CreateFromVector3D(true, TriangleStrip, NoCenter, Vector3D(0,0,0), 
                                                    numVertices, quadVertices, quadIndices, numIndices, NULL);
  
  TextureMapping* texMap = new SimpleTextureMapping(texID, texC, true);
  
  _mesh = new TexturedMesh(im, true, texMap, true);

  return true;
}  


int BusyQuadRenderer::render(const RenderContext* rc) 
{  
  GL* gl = rc->getGL();
  
  if (_quadMesh == NULL){
    if (!initMesh(rc)) {
      return MAX_TIME_TO_RENDER;
    }
  }

  
  // init effect in the first render
  static bool firstTime = true;
  if (firstTime) {
    firstTime = false;
    Effect *effect = new BusyEffect(this);
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
  gl->clearScreen(0.0f, 0.2f, 0.4f, 1.0f);
  
  glEnable(GL_BLEND);
  glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
  
  gl->pushMatrix();
  MutableMatrix44D R1 = MutableMatrix44D::createRotationMatrix(Angle::fromDegrees(60), Vector3D(-1, 0, 0));
  MutableMatrix44D R2 = MutableMatrix44D::createRotationMatrix(Angle::fromDegrees(_degrees), Vector3D(0, 0, -1));
  gl->multMatrixf(R1.multiply(R2));
  
  // draw mesh
  _mesh->render(rc);
  
  _quadMesh->render(rc);
  
  
  
  gl->popMatrix();
  
  glDisable(GL_BLEND);
  
  return MAX_TIME_TO_RENDER;
}

