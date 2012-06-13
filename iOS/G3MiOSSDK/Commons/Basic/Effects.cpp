//
//  Effects.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/06/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#include "Effects.hpp"


void EffectsScheduler::initialize(const InitializationContext* ic) {
  _factory = ic->getFactory();
  _timer = ic->getFactory()->createTimer();
}

void EffectsScheduler::processFinishedEffects(const double now) {
  std::vector<int> indicesToRemove;
  for (int i = 0; i < _effects.size(); i++) {
    EffectRun effectRun = _effects[i];
    
    if (effectRun._started == true) {
      if (effectRun._effect->isDone(now)) {
        effectRun._effect->stop(now);
        
        indicesToRemove.push_back(i);
      }
    }
  }
  
  // backward iteration, to remove from bottom to top
  for (int i = indicesToRemove.size() - 1; i >= 0; i--) {
    const int indexToRemove = indicesToRemove[i];
    _effects.erase(_effects.begin() + indexToRemove);
  }
}

void EffectsScheduler::doOneCyle() {
  const double now = _timer->now();
  
  
  processFinishedEffects(now);
  
  
  for (int i = 0; i < _effects.size(); i++) {
    EffectRun effectRun = _effects[i];
    
    if (effectRun._started == false) {
      effectRun._effect->start(now);
      effectRun._started = true;
    }
    
    effectRun._effect->doStep(now);
  }
}

int EffectsScheduler::render(const RenderContext *rc) {
  doOneCyle();
  
  return 99999;
}

bool EffectsScheduler::onTouchEvent(const TouchEvent* touchEvent) {
  return false;
}

bool EffectsScheduler::onResizeViewportEvent(int width, int height) {
  return false;
}

void EffectsScheduler::startEffect(Effect* effect) {
  _effects.push_back(EffectRun(effect));
}
