//
//  CompositeRenderer.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "CompositeRenderer.hpp"

#include "ILogger.hpp"

void CompositeRenderer::initialize(const G3MContext* context) {
  _context = context;

  for (int i = 0; i < _renderersSize; i++) {
    _renderers[i]->initialize(context);
  }
}

void CompositeRenderer::addRenderer(Renderer *renderer) {
  _renderers.push_back(renderer);
  _renderersSize = _renderers.size();

  if (_context != NULL) {
    renderer->initialize(_context);
  }
}

void CompositeRenderer::render(const G3MRenderContext* rc, GLState* glState) {
  //rc->getLogger()->logInfo("CompositeRenderer::render()");

  for (int i = 0; i < _renderersSize; i++) {
    Renderer* renderer = _renderers[i];
    if (renderer->isEnable()) {
      renderer->render(rc, glState);
    }
  }
}

bool CompositeRenderer::onTouchEvent(const G3MEventContext* ec,
                                     const TouchEvent* touchEvent) {
  // the events are processed bottom to top
  for (int i = _renderersSize - 1; i >= 0; i--) {
    Renderer* renderer = _renderers[i];
    if (renderer->isEnable()) {
      if (renderer->onTouchEvent(ec, touchEvent)) {
        return true;
      }
    }
  }
  return false;
}

void CompositeRenderer::onResizeViewportEvent(const G3MEventContext* ec,
                                              int width, int height)
{
  // the events are processed bottom to top
  for (int i = _renderersSize - 1; i >= 0; i--) {
    _renderers[i]->onResizeViewportEvent(ec, width, height);
  }
}

RenderState CompositeRenderer::getRenderState(const G3MRenderContext* rc) {
  _errors.clear();
  bool busyFlag  = false;
  bool errorFlag = false;

  for (int i = 0; i < _renderersSize; i++) {
    Renderer* child = _renderers[i];
    if (child->isEnable()) {
      const RenderState childRenderState = child->getRenderState(rc);

      const RenderState_Type childRenderStateType = childRenderState._type;

      if (childRenderStateType == RENDER_ERROR) {
        errorFlag = true;

        const std::vector<std::string> childErrors = childRenderState.getErrors();
#ifdef C_CODE
        _errors.insert(_errors.end(),
                       childErrors.begin(),
                       childErrors.end());
#endif
#ifdef JAVA_CODE
        _errors.addAll(childErrors);
#endif
      }
      else if (childRenderStateType == RENDER_BUSY) {
        busyFlag = true;
      }
    }
  }

  if (errorFlag) {
    return RenderState::error(_errors);
  }
  else if (busyFlag) {
    return RenderState::busy();
  }
  else {
    return RenderState::ready();
  }
}

void CompositeRenderer::start(const G3MRenderContext* rc) {
  for (int i = 0; i < _renderersSize; i++) {
    _renderers[i]->start(rc);
  }
}

void CompositeRenderer::stop(const G3MRenderContext* rc) {
  for (int i = 0; i < _renderersSize; i++) {
    _renderers[i]->stop(rc);
  }
}

void CompositeRenderer::onResume(const G3MContext* context) {
  for (int i = 0; i < _renderersSize; i++) {
    _renderers[i]->onResume(context);
  }
}

void CompositeRenderer::onPause(const G3MContext* context) {
  for (int i = 0; i < _renderersSize; i++) {
    _renderers[i]->onPause(context);
  }
}

void CompositeRenderer::onDestroy(const G3MContext* context) {
  for (int i = 0; i < _renderersSize; i++) {
    _renderers[i]->onDestroy(context);
  }
}

bool CompositeRenderer::isEnable() const {
  if (!_enable) {
    return false;
  }

  for (int i = 0; i < _renderersSize; i++) {
    if (_renderers[i]->isEnable()) {
      return true;
    }
  }
  return false;
}

void CompositeRenderer::setEnable(bool enable) {
  _enable = enable;
}

SurfaceElevationProvider* CompositeRenderer::getSurfaceElevationProvider() {
  SurfaceElevationProvider* result = NULL;

  for (int i = 0; i < _renderersSize; i++) {
    Renderer* renderer = _renderers[i];
    SurfaceElevationProvider* childSurfaceElevationProvider = renderer->getSurfaceElevationProvider();
    if (childSurfaceElevationProvider != NULL) {
      if (result == NULL) {
        result = childSurfaceElevationProvider;
      }
      else {
        ILogger::instance()->logError("Inconsistency in Renderers: more than one SurfaceElevationProvider");
      }
    }
  }

  return result;
}

PlanetRenderer* CompositeRenderer::getPlanetRenderer() {
  PlanetRenderer* result = NULL;

  for (int i = 0; i < _renderersSize; i++) {
    Renderer* renderer = _renderers[i];
    PlanetRenderer* planetRenderer = renderer->getPlanetRenderer();
    if (planetRenderer != NULL) {
      if (result == NULL) {
        result = planetRenderer;
      }
      else {
        ILogger::instance()->logError("Inconsistency in Renderers: more than one PlanetRenderer");
      }
    }
  }

  return result;
}
