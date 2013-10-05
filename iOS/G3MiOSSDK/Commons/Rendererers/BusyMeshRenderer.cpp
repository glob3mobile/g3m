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

void BusyMeshRenderer::initialize(const G3MContext* context)
{
//  unsigned int numStrides = 60;
  unsigned int numStrides = 5;

  FloatBufferBuilderFromCartesian3D vertices = FloatBufferBuilderFromCartesian3D::builderWithoutCenter();
  
  FloatBufferBuilderFromColor colors;
  ShortBufferBuilder indices;


  int indicesCounter=0;
//  const float r1=12;
//  const float r2=18;
  const float r1=0;
//  const float r2=120;
  const float r2=50;
  
  for (int step=0; step<=numStrides; step++) {
    const double angle = (double) step * 2 * PI / numStrides;
    const double c = IMathUtils::instance()->cos(angle);
    const double s = IMathUtils::instance()->sin(angle);
    
    vertices.add( (r1 * c), (r1 * s), 0);
    vertices.add( (r2 * c), (r2 * s), 0);
    
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

    colors.add(1,1,1,1);
    colors.add(1,1,1,0);
  }
  
  // the two last indices
  indices.add((short) 0);
  indices.add((short) 1);
  
  // create mesh
  _mesh = new IndexedMesh(GLPrimitive::triangleStrip(),
                          true,
                          vertices.getCenter(),
                          vertices.create(),
                          indices.create(),
                          1,
                          1,
                          NULL,
                          colors.create());
}

void BusyMeshRenderer::start(const G3MRenderContext* rc) {
  Effect* effect = new BusyMeshEffect(this);
  rc->getEffectsScheduler()->startEffect(effect, this);
}

void BusyMeshRenderer::stop(const G3MRenderContext* rc) {
  rc->getEffectsScheduler()->cancelAllEffectsFor(this);
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

void BusyMeshRenderer::render(const G3MRenderContext* rc, GLState* glState)
{
  GL* gl = rc->getGL();

  createGLState();
  
  gl->clearScreen(*_backgroundColor);
  
  _mesh->render(rc, _glState);
}
