//
//  BusyQuadRenderer.cpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 13/08/12.
//



#include "BusyQuadRenderer.hpp"

#include "G3MContext.hpp"
#include "GL.hpp"
#include "MutableMatrix44D.hpp"
#include "TexturesHandler.hpp"
#include "SimpleTextureMapping.hpp"
#include "TexturedMesh.hpp"

#include "FloatBufferBuilderFromCartesian3D.hpp"
#include "FloatBufferBuilderFromCartesian2D.hpp"
#include "ShortBufferBuilder.hpp"

#include "GLConstants.hpp"
#include "GPUProgram.hpp"
#include "Camera.hpp"
#include "G3MEventContext.hpp"
#include "DirectMesh.hpp"



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

  delete _quadMesh;
  _quadMesh = NULL;
}


bool BusyQuadRenderer::initMesh(const G3MRenderContext* rc) {
#ifdef C_CODE
  const TextureIDReference* texId = NULL;
#endif
#ifdef JAVA_CODE
  TextureIDReference texId = null;
#endif

  texId = rc->getTexturesHandler()->getTextureIDReference(_image,
                                                          GLFormat::rgba(),
                                                          "BusyQuadRenderer-Texture",
                                                          false);

  if (texId == NULL) {
    rc->getLogger()->logError("Can't upload texture to GPU");
    return false;
  }

  const double halfWidth = _size._x / 2;
  const double hadfHeight = _size._y / 2;
  FloatBufferBuilderFromCartesian3D* vertices = FloatBufferBuilderFromCartesian3D::builderWithoutCenter();
  vertices->add(-halfWidth, +hadfHeight, 0);
  vertices->add(-halfWidth, -hadfHeight, 0);
  vertices->add(+halfWidth, +hadfHeight, 0);
  vertices->add(+halfWidth, -hadfHeight, 0);

  FloatBufferBuilderFromCartesian2D texCoords;
  texCoords.add(0, 0);
  texCoords.add(0, 1);
  texCoords.add(1, 0);
  texCoords.add(1, 1);

  DirectMesh *im = new DirectMesh(GLPrimitive::triangleStrip(),
                                  true,
                                  vertices->getCenter(),
                                  vertices->create(),
                                  1,
                                  1);

  delete vertices;

  TextureMapping* texMap = new SimpleTextureMapping(texId,
                                                    texCoords.create(),
                                                    true,
                                                    false);

  _quadMesh = new TexturedMesh(im, true, texMap, true, true);

  return true;
}

//TODO: REMOVE???
void BusyQuadRenderer::render(const G3MRenderContext* rc,
                              GLState* glState) {
  GL* gl = rc->getGL();

  if (_quadMesh == NULL) {
    if (!initMesh(rc)) {
      return;
    }
  }

  createGLState();

  // clear screen
  gl->clearScreen(*_backgroundColor);

  // draw mesh
  _quadMesh->render(rc, _glState);
}

void BusyQuadRenderer::createGLState() {
  //Modelview and projection
  _modelviewMatrix.copyValue(MutableMatrix44D::createRotationMatrix(Angle::fromDegrees(_degrees), Vector3D(0, 0, 1)));
  _glState->clearGLFeatureGroup(CAMERA_GROUP);
  _glState->addGLFeature(new ProjectionGLFeature(_projectionMatrix.asMatrix44D()), false);
  _glState->addGLFeature(new ModelGLFeature(_modelviewMatrix.asMatrix44D()), false);
}


void BusyQuadRenderer::onResizeViewportEvent(const G3MEventContext* ec,
                                             int width, int height) {
  int logicWidth = width;
  if (ec->getViewMode() == STEREO) {
    logicWidth /= 2;
  }
  const int halfWidth  = logicWidth / 2;
  const int halfHeight = height / 2;
  _projectionMatrix.copyValue(MutableMatrix44D::createOrthographicProjectionMatrix(-halfWidth, halfWidth,
                                                                                   -halfHeight, halfHeight,
                                                                                   -halfWidth, halfWidth));
}

void BusyQuadRenderer::incDegrees(double value) {
  _degrees += value;
  if (_degrees>360) _degrees -= 360;
  _modelviewMatrix.copyValue(MutableMatrix44D::createRotationMatrix(Angle::fromDegrees(_degrees), Vector3D(0, 0, 1)));
}

BusyQuadRenderer::~BusyQuadRenderer() {
  delete _image;
  delete _quadMesh;
  delete _backgroundColor;

  _glState->_release();
}
