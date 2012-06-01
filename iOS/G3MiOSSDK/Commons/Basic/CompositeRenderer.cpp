//
//  CompositeRenderer.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "CompositeRenderer.h"

void CompositeRenderer::initialize(InitializationContext& ic) {
  _ic = ic;
  
  for (int i = 0; i < _renderers.size(); i++) {
    _renderers[i]->initialize(ic);
  }
}

void CompositeRenderer::addRenderer(Renderer *renderer) {
  _renderers.push_back(renderer);
  renderer->initialize(_ic);
}

int CompositeRenderer::render(RenderContext &rc) {
  int min = 9999;
  for (int i = 0; i < _renderers.size(); i++) {
    int x = _renderers[i]->render(rc);
    if (x < min) min = x; 
  }
  return min;
}

bool CompositeRenderer::onTapEvent(TapEvent& event) {
  for (int i = 0; i < _renderers.size(); i++) {
    //THE EVENT IS PROCESSED ONLY BY THE FIRST RENDERER
    if (_renderers[i]->onTapEvent(event))
    {
      return true;
    }
  }
  return false;
}

bool CompositeRenderer::onTouchEvent(TouchEvent &event) {
  for (int i = 0; i < _renderers.size(); i++) {
    //THE EVENT IS PROCESSED ONLY BY THE FIRST RENDERER
    if (_renderers[i]->onTouchEvent(event))
    {
      return true;
    }
  }
  return false;
}
