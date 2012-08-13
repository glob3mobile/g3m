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
  // create quad
  unsigned int numVertices = 4;
  unsigned int numIndices = 4;
  float *quadVertices = new float [numVertices*3];
  int *quadIndices = new int [numIndices];
  float *texC = new float [numVertices*2];
  
  unsigned int nv = 0;
  float halfSize = 100;
  quadVertices[nv++] = -halfSize;    quadVertices[nv++] = halfSize;   quadVertices[nv++] = 0;
  quadVertices[nv++] = -halfSize;    quadVertices[nv++] = -halfSize;  quadVertices[nv++] = 0;
  quadVertices[nv++] = halfSize;     quadVertices[nv++] = halfSize;   quadVertices[nv++] = 0;
  quadVertices[nv++] = halfSize;     quadVertices[nv++] = -halfSize;  quadVertices[nv++] = 0;
  
  for (unsigned int n=0; n<numIndices; n++) quadIndices[n] = n;
  
  unsigned int nc = 0;
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
  
  _quadMesh = new TexturedMesh(im, true, texMap, true);

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
  MutableMatrix44D R1 = MutableMatrix44D::createRotationMatrix(Angle::fromDegrees(0), Vector3D(-1, 0, 0));
  MutableMatrix44D R2 = MutableMatrix44D::createRotationMatrix(Angle::fromDegrees(_degrees), Vector3D(0, 0, 1));
  gl->multMatrixf(R1.multiply(R2));
  
  // draw mesh  
  _quadMesh->render(rc);
  
  gl->popMatrix();
  
  glDisable(GL_BLEND);
  
  return MAX_TIME_TO_RENDER;
}

