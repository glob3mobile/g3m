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
  //renderer->initialize(_ic);
  
  // maybe this part is conflicted with diego -> in that case, remove it
  if (_ic != NULL) {
    renderer->initialize(_ic);
  }
}

int CompositeRenderer::render(const RenderContext* rc) {
  //rc->getLogger()->logInfo("CompositeRenderer::render()");
  
  int min = 9999;
  for (int i = 0; i < _renderers.size(); i++) {
    int x = _renderers[i]->render(rc);
    if (x < min) min = x; 
  }
  return min;
}

bool CompositeRenderer::onTouchEvent(const TouchEvent* touchEvent) {
  for (int i = 0; i < _renderers.size(); i++) {
    //THE EVENT IS PROCESSED ONLY BY THE FIRST RENDERER
    if (_renderers[i]->onTouchEvent(touchEvent)) {
      return true;
    }
  }
  return false;
}

bool CompositeRenderer::onResizeViewportEvent(int width, int height)
{
  bool res = false;
  for (int i = 0; i < _renderers.size(); i++) {
    //THE EVENT IS PROCESSED ONLY BY ALL RENDERERS
    res = res | _renderers[i]->onResizeViewportEvent(width, height);
  }
  return res;
}
