//
//  TrailsRenderer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/23/12.
//
//

#include "TrailsRenderer.hpp"

void Trail::render(const RenderContext* rc) {
  if (_visible) {
    
  }
}

TrailsRenderer::~TrailsRenderer() {
  const int trailsCount = _trails.size();
  for (int i = 0; i < trailsCount; i++) {
    Trail* trail = _trails[i];
    delete trail;
  }
  _trails.clear();
}

void TrailsRenderer::addTrail(Trail* trail) {
  _trails.push_back(trail);
}

void TrailsRenderer::render(const RenderContext* rc) {
  const int trailsCount = _trails.size();
  for (int i = 0; i < trailsCount; i++) {
    Trail* trail = _trails[i];
    trail->render(rc);
  }
}
