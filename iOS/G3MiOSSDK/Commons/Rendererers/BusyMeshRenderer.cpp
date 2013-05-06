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
  unsigned int numStrides = 60;

  FloatBufferBuilderFromCartesian3D vertices(CenterStrategy::noCenter(), Vector3D::zero());
  FloatBufferBuilderFromColor colors;
  ShortBufferBuilder indices;

  int indicesCounter=0;
  const float r1=12;
  const float r2=18;
  for (int step=0; step<=numStrides; step++) {
    const double angle = (double) step * 2 * IMathUtils::instance()->pi() / numStrides;
    const double c = IMathUtils::instance()->cos(angle);
    const double s = IMathUtils::instance()->sin(angle);

    vertices.add( (r1 * c), (r1 * s), 0);
    vertices.add( (r2 * c), (r2 * s), 0);

    indices.add((short) (indicesCounter++));
    indices.add((short) (indicesCounter++));

    float col = (float) (1.1 * step / numStrides);
    if (col>1) {
      colors.add(255, 255, 255, 0);
      colors.add(255, 255, 255, 0);
    } else {
      colors.add(255, 255, 255, 1 - col);
      colors.add(255, 255, 255, 1 - col);
    }
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

void BusyMeshRenderer::render(const G3MRenderContext* rc,
                              const GLState& parentState)
{
  GL* gl = rc->getGL();

  // set mesh glstate
  GLState state(parentState);
  state.enableBlend();
  
  state.setBlendFactors(GLBlendFactor::srcAlpha(), GLBlendFactor::oneMinusSrcAlpha());

  // init modelview matrix
  int currentViewport[4];
  gl->getViewport(currentViewport);
  const int halfWidth = currentViewport[2] / 2;
  const int halfHeight = currentViewport[3] / 2;
  MutableMatrix44D M = MutableMatrix44D::createOrthographicProjectionMatrix(-halfWidth, halfWidth,
                                                                            -halfHeight, halfHeight,
                                                                            -halfWidth, halfWidth);
  
  //state.getProgram()->setUniform(rc->getGL(), "Projection", M);
  
  //GPUProgram* prog = rc->getGPUProgramManager()->getProgram("DefaultProgram");
  int _WORKING_JM;
//  UniformMatrix4Float* projection = prog->getUniformMatrix4Float("Projection");
//  UniformMatrix4Float* modelview = prog->getUniformMatrix4Float("Modelview");
  //state.setProgram(prog);
//  projection->set(M);
  //modelview->set(MutableMatrix44D::identity());
  
  
  _programState.setUniformValue("Projection", M);
  //_programState.setValueToUniform("Modelview", MutableMatrix44D::identity());
  
  //_programState.setValueToUniform("Modelview", MutableMatrix44D::identity());
  
  //state.setProjectionMatrix(M);
  //state.setModelViewMatrix(MutableMatrix44D::identity());

  // clear screen
  state.setClearColor(*_backgroundColor);
  gl->clearScreen(state);

  MutableMatrix44D R1 = MutableMatrix44D::createRotationMatrix(Angle::fromDegrees(_degrees), Vector3D(0, 0, -1));
  //state.multiplyModelViewMatrix(R1);
  
  _programState.setUniformValue("Modelview", R1);

  // draw mesh
  _mesh->render(rc, state, &_programState);
}
