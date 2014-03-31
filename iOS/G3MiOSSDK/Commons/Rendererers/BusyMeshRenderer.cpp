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
#include "IMathUtils.hpp"
#include "FloatBufferBuilderFromColor.hpp"
#include "FloatBufferBuilderFromCartesian3D.hpp"
#include "ShortBufferBuilder.hpp"
#include "GLConstants.hpp"
#include "GPUProgram.hpp"
#include "GPUProgramManager.hpp"
#include "GPUUniform.hpp"
#include "Camera.hpp"


void BusyMeshRenderer::start(const G3MRenderContext* rc) {
  Effect* effect = new BusyMeshEffect(this);
  rc->getEffectsScheduler()->startEffect(effect, this);
}

void BusyMeshRenderer::stop(const G3MRenderContext* rc) {
  rc->getEffectsScheduler()->cancelAllEffectsFor(this);

  delete _mesh;
  _mesh = NULL;
}

void BusyMeshRenderer::createGLState() {
  if (_projectionFeature == NULL) {
    _projectionFeature = new ProjectionGLFeature(_projectionMatrix.asMatrix44D());
    _glState->addGLFeature(_projectionFeature, false);
  }
  else {
    _projectionFeature->setMatrix(_projectionMatrix.asMatrix44D());
  }

  if (_modelFeature == NULL) {
    _modelFeature = new ModelGLFeature(_modelviewMatrix.asMatrix44D());
    _glState->addGLFeature(_modelFeature, false);
  }
  else {
    _modelFeature->setMatrix(_modelviewMatrix.asMatrix44D());
  }
}

Mesh* BusyMeshRenderer::createMesh(const G3MRenderContext* rc) {
  const int numStrides = 5;

  FloatBufferBuilderFromCartesian3D* vertices = FloatBufferBuilderFromCartesian3D::builderWithoutCenter();

  FloatBufferBuilderFromColor colors;
  ShortBufferBuilder indices;

  int indicesCounter = 0;

  const float innerRadius = 0;

//  const float r2=50;
  const Camera* camera = rc->getCurrentCamera();
  const int width  = camera->getWidth();
  const int height = camera->getHeight();
  const int minSize = (width < height) ? width : height;
  const float outerRadius = minSize / 15.0f;

  const IMathUtils* mu = IMathUtils::instance();

  for (int step = 0; step <= numStrides; step++) {
    const double angle = (double) step * 2 * PI / numStrides;
    const double c = mu->cos(angle);
    const double s = mu->sin(angle);

    vertices->add( (innerRadius * c), (innerRadius * s), 0);
    vertices->add( (outerRadius * c), (outerRadius * s), 0);

    indices.add((short) (indicesCounter++));
    indices.add((short) (indicesCounter++));

    //    float col = (float) (1.0 * step / numStrides);
    //    if (col>1) {
    //      colors.add(1, 1, 1, 0);
    //      colors.add(1, 1, 1, 0);
    //    }
    //    else {
    //      colors.add(1, 1, 1, 1 - col);
    //      colors.add(1, 1, 1, 1 - col);
    //    }

    //    colors.add(Color::red().wheelStep(numStrides, step));
    //    colors.add(Color::red().wheelStep(numStrides, step));

    colors.add(1, 1, 1, 1);
    colors.add(1, 1, 1, 0);
  }

  // the two last indices
  indices.add((short) 0);
  indices.add((short) 1);


  Mesh* result = new IndexedMesh(GLPrimitive::triangleStrip(),
                                true,
                                vertices->getCenter(),
                                vertices->create(),
                                indices.create(),
                                1,
                                1,
                                NULL,
                                colors.create());
  delete vertices;

  return result;
}

Mesh* BusyMeshRenderer::getMesh(const G3MRenderContext* rc) {
  if (_mesh == NULL) {
    _mesh = createMesh(rc);
  }
  return _mesh;
}

void BusyMeshRenderer::render(const G3MRenderContext* rc,
                              GLState* glState)
{
  GL* gl = rc->getGL();
  createGLState();
  
  gl->clearScreen(*_backgroundColor);

  Mesh* mesh = getMesh(rc);
  if (mesh != NULL) {
    mesh->render(rc, _glState);
  }
}
