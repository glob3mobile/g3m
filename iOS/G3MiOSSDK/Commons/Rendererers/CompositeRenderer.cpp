//
//  CompositeRenderer.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "CompositeRenderer.hpp"

void CompositeRenderer::initialize(const InitializationContext* ic) {
  _ic = ic;
  
  for (int i = 0; i < _renderers.size(); i++) {
    _renderers[i]->initialize(ic);
  }
}

void CompositeRenderer::addRenderer(Renderer *renderer) {
  _renderers.push_back(renderer);
  if (_ic != NULL) {
    renderer->initialize(_ic);
  }
}

int CompositeRenderer::render(const RenderContext* rc) {
  //rc->getLogger()->logInfo("CompositeRenderer::render()");
  
  int min = MAX_TIME_TO_RENDER;
  for (int i = 0; i < _renderers.size(); i++) {
    int x = _renderers[i]->render(rc);
    if (x < min) min = x; 
  }
  return min;
}

bool CompositeRenderer::onTouchEvent(const EventContext* ec,
                                     const TouchEvent* touchEvent) {
  // the events are processed bottom to top
  for (int i = _renderers.size() - 1; i >= 0; i--) {
    if (_renderers[i]->onTouchEvent(ec, touchEvent)) {
      return true;
    }
  }
  return false;
}

void CompositeRenderer::onResizeViewportEvent(const EventContext* ec,
                                              int width, int height)
{
  // the events are processed bottom to top
  for (int i = _renderers.size() - 1; i >= 0; i--) {
    _renderers[i]->onResizeViewportEvent(ec, width, height);
  }
}

bool CompositeRenderer::isReadyToRender(const RenderContext *rc) {
  for (int i = 0; i < _renderers.size(); i++) {
    if (!_renderers[i]->isReadyToRender(rc)) {
      return false;
    }
  }
  
  return true;
}

void CompositeRenderer::start() {
  for (int i = 0; i < _renderers.size(); i++) {
    _renderers[i]->start();
  }
}

void CompositeRenderer::stop() {
  for (int i = 0; i < _renderers.size(); i++) {
    _renderers[i]->stop();
  }
}
