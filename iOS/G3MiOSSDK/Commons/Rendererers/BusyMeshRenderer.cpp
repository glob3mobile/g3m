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
#include "IntBufferBuilder.hpp"

void BusyMeshRenderer::initialize(const InitializationContext* ic)
{
  unsigned int numStrides = 60;
  
#ifdef C_CODE
  FloatBufferBuilderFromCartesian3D vertices(NoCenter, Vector3D::zero());
#else
  FloatBufferBuilderFromCartesian3D vertices(CenterStrategy.NoCenter, Vector3D::zero());
#endif
  FloatBufferBuilderFromColor colors;
  IntBufferBuilder indices;
  
  int indicesCounter=0;
  const float r1=12;
  const float r2=18;
  for (int step=0; step<=numStrides; step++) {
    const double angle = (double) step * 2 * GMath.pi() / numStrides;
    const double c = GMath.cos(angle);
    const double s = GMath.sin(angle);
    
    vertices.add( (r1 * c), (r1 * s), 0);
    vertices.add( (r2 * c), (r2 * s), 0);
    
    indices.add(indicesCounter++);
    indices.add(indicesCounter++);
    
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
  indices.add(0);
  indices.add(1);
  
  // create mesh
#ifdef C_CODE
  _mesh = new IndexedMesh(TriangleStrip,
                          true,
                          vertices.getCenter(),
                          vertices.create(),
                          indices.create(),
                          NULL,
                          colors.create());
#endif
#ifdef JAVA_CODE
  _mesh = new IndexedMesh(GLPrimitive.TriangleStrip,
                      true,
                      vertices.getCenter(),
                      vertices.create(),
                      indices.create(),
                      null,
                      colors.create());
#endif
}

void BusyMeshRenderer::start() {
  //int _TODO_start_effects;
}

void BusyMeshRenderer::stop() {
  //int _TODO_stop_effects;
}

void BusyMeshRenderer::render(const RenderContext* rc)
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
  MutableMatrix44D R2 = MutableMatrix44D::createRotationMatrix(Angle::fromDegrees(_degrees), Vector3D(0, 0, -1));
  gl->multMatrixf(R1.multiply(R2));
  
  // draw mesh
  _mesh->render(rc);
  
  gl->popMatrix();
  
  gl->disableBlend();
}
