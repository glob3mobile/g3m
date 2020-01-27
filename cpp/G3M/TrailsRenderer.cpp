//
//  TrailsRenderer.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 10/23/12.
//
//

#include "TrailsRenderer.hpp"

#include "GLState.hpp"
#include "Camera.hpp"
#include "Trail.hpp"
#include "G3MRenderContext.hpp"


TrailsRenderer::TrailsRenderer():
_projection(NULL),
_model(NULL),
_glState(new GLState())
{
}

void TrailsRenderer::updateGLState(const Camera* camera) {
  if (_projection == NULL) {
    _projection = new ProjectionGLFeature(camera->getProjectionMatrix44D());
    _glState->addGLFeature(_projection, true);
  }
  else {
    _projection->setMatrix(camera->getProjectionMatrix44D());
  }

  if (_model == NULL) {
    _model = new ModelGLFeature(camera->getModelMatrix44D());
    _glState->addGLFeature(_model, true);
  }
  else {
    _model->setMatrix(camera->getModelMatrix44D());
  }
}

TrailsRenderer::~TrailsRenderer() {
  const size_t trailsCount = _trails.size();
  for (size_t i = 0; i < trailsCount; i++) {
    Trail* trail = _trails[i];
    delete trail;
  }
  _trails.clear();

  _glState->_release();
#ifdef JAVA_CODE
  super.dispose();
#endif
}

void TrailsRenderer::addTrail(Trail* trail) {
  if (trail != NULL) {
    _trails.push_back(trail);
  }
}

void TrailsRenderer::render(const G3MRenderContext* rc,
                            GLState* glState) {
  const size_t trailsCount = _trails.size();
  if (trailsCount > 0) {
    const Camera* camera = rc->getCurrentCamera();

    updateGLState(camera);

    const Frustum* frustum = camera->getFrustumInModelCoordinates();
    for (size_t i = 0; i < trailsCount; i++) {
      Trail* trail = _trails[i];
      if (trail != NULL) {
        trail->render(rc, frustum, _glState);
      }
    }
  }
}

void TrailsRenderer::removeTrail(Trail* trail,
                                 bool deleteTrail) {
  const size_t trailsCount = _trails.size();
  for (size_t i = 0; i < trailsCount; i++) {
    Trail* each = _trails[i];
    if (trail == each) {
#ifdef C_CODE
      _trails.erase(_trails.begin() + i);
#endif
#ifdef JAVA_CODE
      _trails.remove(i);
#endif
      if (deleteTrail) {
        delete trail;
      }
      break;
    }
  }
}

void TrailsRenderer::removeAllTrails(bool deleteTrails) {
  if (deleteTrails) {
    const size_t trailsCount = _trails.size();
    for (size_t i = 0; i < trailsCount; i++) {
      Trail* trail = _trails[i];
      delete trail;
    }
  }
  _trails.clear();
}
