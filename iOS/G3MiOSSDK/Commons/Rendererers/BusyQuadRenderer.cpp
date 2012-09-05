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


void BusyQuadRenderer::start() {
  //int _TODO_start_effects;
}

void BusyQuadRenderer::stop() {
  //int _TODO_stop_effects;
}


bool BusyQuadRenderer::initMesh(const RenderContext* rc)
{  
  // create quad
  unsigned int numVertices = 4;
  unsigned int numIndices = 4;
  float* quadVertices = new float[numVertices*3];
  int*   quadIndices  = new int[numIndices];
  float* texC         = new float[numVertices*2];
  
  unsigned int nv = 0;
  float halfSize = 16;
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
  GLTextureId texId = GLTextureId::invalid();
  if (true){
    texId = rc->getTexturesHandler()->getGLTextureIdFromFileName(_textureFilename, 256, 256, false);
    if (!texId.isValid()) {
      rc->getLogger()->logError("Can't load file %s", _textureFilename.c_str());
      return false;
    }
  }

#ifdef C_CODE
  IndexedMesh *im = IndexedMesh::createFromVector3D(true, TriangleStrip, NoCenter, Vector3D(0,0,0), 
                                                    numVertices, quadVertices, quadIndices, numIndices, NULL);
#else
  IndexedMesh *im = IndexedMesh::createFromVector3D(true, GLPrimitive.TriangleStrip, NoCenter, Vector3D(0,0,0), 
                                                    numVertices, quadVertices, quadIndices, numIndices, NULL);
#endif
  
  TextureMapping* texMap = new SimpleTextureMapping(texId, texC, true);
  
  _quadMesh = new TexturedMesh(im, true, texMap, true);

  return true;
}  


void BusyQuadRenderer::render(const RenderContext* rc) {
  GL* gl = rc->getGL();
  
  if (_quadMesh == NULL){
    if (!initMesh(rc)) {
      return;
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
  int currentViewport[4];
  gl->getViewport(currentViewport);
  int halfWidth = currentViewport[2] / 2;
  int halfHeight = currentViewport[3] / 2;
  MutableMatrix44D M = MutableMatrix44D::createOrthographicProjectionMatrix(-halfWidth, halfWidth,
                                                                            -halfHeight, halfHeight,
                                                                            -halfWidth, halfWidth);
  gl->setProjection(M);
  gl->loadMatrixf(MutableMatrix44D::identity());
  
  // clear screen
  //gl->clearScreen(0.0f, 0.2f, 0.4f, 1.0f);
  gl->clearScreen(0.0f, 0.0f, 0.0f, 1.0f);

  gl->enableBlend();
  gl->setBlendFuncSrcAlpha();
  
  gl->pushMatrix();
  MutableMatrix44D R1 = MutableMatrix44D::createRotationMatrix(Angle::fromDegrees(0), Vector3D(-1, 0, 0));
  MutableMatrix44D R2 = MutableMatrix44D::createRotationMatrix(Angle::fromDegrees(_degrees), Vector3D(0, 0, 1));
  gl->multMatrixf(R1.multiply(R2));
  
  // draw mesh  
  _quadMesh->render(rc);
  
  gl->popMatrix();
  
  gl->disableBlend();
  
}
