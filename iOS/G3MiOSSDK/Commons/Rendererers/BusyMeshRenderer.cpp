//
//  BusyMeshRenderer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 20/07/12.
//



#include "BusyMeshRenderer.hpp"

#include "G3MContext.hpp"
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
#include "G3MEventContext.hpp"


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
  int viewPortWidth  = camera->getViewPortWidth();
  if (rc->getViewMode() == STEREO) {
    viewPortWidth /= 2;
  }
  const int viewPortHeight = camera->getViewPortHeight();
  const int minSize = (viewPortWidth < viewPortHeight) ? viewPortWidth : viewPortHeight;
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
                                 vertices->getCenter(),
                                 vertices->create(),
                                 true,
                                 indices.create(),
                                 true,
                                 1,
                                 1,
                                 NULL,
                                 colors.create());
  delete vertices;
  
  return result;
}

void BusyMeshRenderer::onResizeViewportEvent(const G3MEventContext* ec,
                                             int width, int height) {
  int logicWidth = width;
  if (ec->getViewMode() == STEREO) {
    logicWidth /= 2;
  }
  const int halfWidth  = logicWidth / 2;
  const int halfHeight = height / 2;
  _projectionMatrix.copyValue(MutableMatrix44D::createOrthographicProjectionMatrix(-halfWidth,  halfWidth,
                                                                                   -halfHeight, halfHeight,
                                                                                   -halfWidth,  halfWidth));

  delete _mesh;
  _mesh = NULL;
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
