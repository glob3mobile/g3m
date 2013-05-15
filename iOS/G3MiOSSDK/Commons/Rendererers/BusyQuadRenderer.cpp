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
#include "ShortBufferBuilder.hpp"

#include "GLConstants.hpp"
#include "GPUProgram.hpp"

void BusyQuadRenderer::start(const G3MRenderContext* rc) {
  if (_animated) {
    Effect *effect = new BusyEffect(this);
    rc->getEffectsScheduler()->startEffect(effect, this);
  }
}

void BusyQuadRenderer::stop(const G3MRenderContext* rc) {
  if (_animated) {
    rc->getEffectsScheduler()->cancelAllEffectsFor(this);
  }
}


bool BusyQuadRenderer::initMesh(const G3MRenderContext* rc) {
  //TEXTURED
#ifdef C_CODE
  const IGLTextureId* texId = NULL;
#endif
#ifdef JAVA_CODE
  IGLTextureId texId = null;
#endif
//  IImage* image = rc->getFactory()->createImageFromFileName(_textureFilename);

  texId = rc->getTexturesHandler()->getGLTextureId(_image,
                                                   GLFormat::rgba(),
                                                   "BusyQuadRenderer-Texture",
                                                   false);

  rc->getFactory()->deleteImage(_image);
  _image = NULL;

  if (texId == NULL) {
    rc->getLogger()->logError("Can't upload texture to GPU");
    return false;
  }

  const double halfWidth = _size._x / 2;
  const double hadfHeight = _size._y / 2;
  FloatBufferBuilderFromCartesian3D vertices(CenterStrategy::noCenter(), Vector3D::zero());
  vertices.add(-halfWidth, +hadfHeight, 0);
  vertices.add(-halfWidth, -hadfHeight, 0);
  vertices.add(+halfWidth, +hadfHeight, 0);
  vertices.add(+halfWidth, -hadfHeight, 0);

  FloatBufferBuilderFromCartesian2D texCoords;
  texCoords.add(0, 0);
  texCoords.add(0, 1);
  texCoords.add(1, 0);
  texCoords.add(1, 1);

  DirectMesh *im = new DirectMesh(GLPrimitive::triangleStrip(),
                                  true,
                                  vertices.getCenter(),
                                  vertices.create(),
                                  1,
                                  1);

  TextureMapping* texMap = new SimpleTextureMapping(texId,
                                                    texCoords.create(),
                                                    true,
                                                    false);

  _quadMesh = new TexturedMesh(im, true, texMap, true, true);

  return true;
}


void BusyQuadRenderer::render(const G3MRenderContext* rc,
                              const GLState& parentState) {
  GL* gl = rc->getGL();

  GLState state(parentState);
  state.enableBlend();

  if (_quadMesh == NULL){
    if (!initMesh(rc)) {
      return;
    }
  }

  // init modelview matrix
  int currentViewport[4];
  gl->getViewport(currentViewport);
  const int halfWidth = currentViewport[2] / 2;
  const int halfHeight = currentViewport[3] / 2;
  MutableMatrix44D M = MutableMatrix44D::createOrthographicProjectionMatrix(-halfWidth, halfWidth,
                                                                            -halfHeight, halfHeight,
                                                                            -halfWidth, halfWidth);
  
  if (!_projectionMatrix.isValid()){
    // init modelview matrix
    int currentViewport[4];
    gl->getViewport(currentViewport);
    const int halfWidth = currentViewport[2] / 2;
    const int halfHeight = currentViewport[3] / 2;
    _projectionMatrix = MutableMatrix44D::createOrthographicProjectionMatrix(-halfWidth, halfWidth,
                                                                             -halfHeight, halfHeight,
                                                                             -halfWidth, halfWidth);
  }
  
  // clear screen
  state.setClearColor(*_backgroundColor);
  gl->clearScreen(state);

  state.setBlendFactors(GLBlendFactor::srcAlpha(), GLBlendFactor::oneMinusSrcAlpha());

//  MutableMatrix44D R2 = MutableMatrix44D::createRotationMatrix(Angle::fromDegrees(_degrees), Vector3D(0, 0, 1));
//  _programState.setUniformValue("Modelview", _modelviewMatrix);

  // draw mesh
  _quadMesh->render(rc);
}

void BusyQuadRenderer::modifyGLState(GLState& glState) const{
  glState.enableBlend();
  glState.setBlendFactors(GLBlendFactor::srcAlpha(), GLBlendFactor::oneMinusSrcAlpha());
  glState.setClearColor(*_backgroundColor);
}

void BusyQuadRenderer::modifyGPUProgramState(GPUProgramState& progState) const{
  progState.setUniformValue("EnableTexture", false);
  progState.setUniformValue("PointSize", (float)1.0);
  progState.setUniformValue("ScaleTexCoord", Vector2D(1.0,1.0));
  progState.setUniformValue("TranslationTexCoord", Vector2D(0.0,0.0));
  
  progState.setUniformValue("ColorPerVertexIntensity", (float)0.0);
  progState.setUniformValue("EnableFlatColor", false);
  progState.setUniformValue("FlatColor", (float)0.0, (float)0.0, (float)0.0, (float)0.0);
  progState.setUniformValue("FlatColorIntensity", (float)0.0);
  
  progState.setAttributeEnabled("TextureCoord", false);
  progState.setAttributeEnabled("Color", false);
  
  //Modelview and projection
  _modelviewMatrix = MutableMatrix44D::createRotationMatrix(Angle::fromDegrees(_degrees), Vector3D(0, 0, 1));
  progState.setUniformValue("Modelview", &_modelviewMatrix); //Program state will store a pointer
  
  progState.setUniformValue("Projection", &_projectionMatrix);
}
