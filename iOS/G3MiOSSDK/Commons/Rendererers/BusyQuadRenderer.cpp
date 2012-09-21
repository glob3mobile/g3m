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
#include "TextureBuilder.hpp"

#include "FloatBufferBuilderFromCartesian3D.hpp"
#include "FloatBufferBuilderFromCartesian2D.hpp"
#include "IntBufferBuilder.hpp"

#include "GLConstants.hpp"

void BusyQuadRenderer::start() {
  //int _TODO_start_effects;
}

void BusyQuadRenderer::stop() {
  //int _TODO_stop_effects;
}


bool BusyQuadRenderer::initMesh(const RenderContext* rc) {
  //TEXTURED
#ifdef C_CODE
  const IGLTextureId* texId = NULL;
#endif
#ifdef JAVA_CODE
  IGLTextureId texId = null;
#endif
  if (true){
    IImage* image = rc->getFactory()->createImageFromFileName(_textureFilename);
    
    texId = rc->getTexturesHandler()->getGLTextureId(image, GLFormat::rgba(),
                                                     _textureFilename, false);
    
    rc->getFactory()->deleteImage(image);
    
    if (texId == NULL) {
      rc->getLogger()->logError("Can't load file %s", _textureFilename.c_str());
      return false;
    }
  }
  
  const float halfSize = 16;
  FloatBufferBuilderFromCartesian3D vertices(CenterStrategy::noCenter(), Vector3D::zero());
  vertices.add(-halfSize, +halfSize, 0);
  vertices.add(-halfSize, -halfSize, 0);
  vertices.add(+halfSize, +halfSize, 0);
  vertices.add(+halfSize, -halfSize, 0);
  
  IntBufferBuilder indices;
  indices.add(0);
  indices.add(1);
  indices.add(2);
  indices.add(3);
  
  FloatBufferBuilderFromCartesian2D texCoords;
  texCoords.add(0, 0);
  texCoords.add(0, 1);
  texCoords.add(1, 0);
  texCoords.add(1, 1);
  
  IndexedMesh *im = new IndexedMesh(GLPrimitive::triangleStrip(),
                                    true,
                                    Vector3D::zero(),
                                    vertices.create(),
                                    indices.create());
  
  TextureMapping* texMap = new SimpleTextureMapping(texId,
                                                    texCoords.create(),
                                                    true);
  
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
