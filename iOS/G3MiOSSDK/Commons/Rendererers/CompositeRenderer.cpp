//
//  CompositeRenderer.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "CompositeRenderer.hpp"

void CompositeRenderer::initialize(const G3MContext* context) {
  _context = context;
  
  const int rendersSize = _renderers.size();
  for (int i = 0; i < rendersSize; i++) {
    _renderers[i]->initialize(context);
  }
}

void CompositeRenderer::addRenderer(Renderer *renderer) {
  _renderers.push_back(renderer);
  if (_context != NULL) {
    renderer->initialize(_context);
  }
}

void CompositeRenderer::render(const G3MRenderContext* rc,
                               const GLState& parentState) {
  //rc->getLogger()->logInfo("CompositeRenderer::render()");
  
  const int rendersSize = _renderers.size();
  for (int i = 0; i < rendersSize; i++) {
    Renderer* renderer = _renderers[i];
    if (renderer->isEnable()) {
      renderer->render(rc, parentState);
    }
  }
}

bool CompositeRenderer::onTouchEvent(const G3MEventContext* ec,
                                     const TouchEvent* touchEvent) {
  // the events are processed bottom to top
  const int rendersSize = _renderers.size();
  for (int i = rendersSize - 1; i >= 0; i--) {
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
  const int rendersSize = _renderers.size();
  for (int i = rendersSize - 1; i >= 0; i--) {
    _renderers[i]->onResizeViewportEvent(ec, width, height);
  }
}

bool CompositeRenderer::isReadyToRender(const G3MRenderContext *rc) {
  const int rendersSize = _renderers.size();
  for (int i = 0; i < rendersSize; i++) {
    Renderer* renderer = _renderers[i];
    if (renderer->isEnable()) {
      if (!renderer->isReadyToRender(rc)) {
        return false;
      }
    }
  }
  
  return true;
}

void CompositeRenderer::start(const G3MRenderContext* rc) {
  const int rendersSize = _renderers.size();
  for (int i = 0; i < rendersSize; i++) {
    _renderers[i]->start(rc);
  }
}

void CompositeRenderer::stop(const G3MRenderContext* rc) {
  const int rendersSize = _renderers.size();
  for (int i = 0; i < rendersSize; i++) {
    _renderers[i]->stop(rc);
  }
}

void CompositeRenderer::onResume(const G3MContext* context) {
  const int rendersSize = _renderers.size();
  for (int i = 0; i < rendersSize; i++) {
    _renderers[i]->onResume(context);
  }
}

void CompositeRenderer::onPause(const G3MContext* context) {
  const int rendersSize = _renderers.size();
  for (int i = 0; i < rendersSize; i++) {
    _renderers[i]->onPause(context);
  }
}

void CompositeRenderer::onDestroy(const G3MContext* context) {
  const int rendersSize = _renderers.size();
  for (int i = 0; i < rendersSize; i++) {
    _renderers[i]->onDestroy(context);
  }
}

bool CompositeRenderer::isEnable() const {
  if (!_enable) {
    return false;
  }
  
  const int rendersSize = _renderers.size();
  for (int i = 0; i < rendersSize; i++) {
    if (_renderers[i]->isEnable()) {
      return true;
    }
  }
  return false;
}

void CompositeRenderer::setEnable(bool enable) {
  _enable = enable;
}
