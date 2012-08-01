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
  _timer = _factory->createTimer();
}

void EffectsScheduler::processFinishedEffects(const RenderContext *rc,
                                              const TimeInterval& now) {
  std::vector<int> indicesToRemove;
  for (int i = 0; i < _effectsRuns.size(); i++) {
    EffectRun* effectRun = _effectsRuns[i];
    
    if (effectRun->_started == true) {
      if (effectRun->_effect->isDone(rc, now)) {
        effectRun->_effect->stop(rc, now);
        
        indicesToRemove.push_back(i);
      }
    }
  }
  
  // backward iteration, to remove from bottom to top
  for (int i = indicesToRemove.size() - 1; i >= 0; i--) {
    const int indexToRemove = indicesToRemove[i];
    delete _effectsRuns[indexToRemove];
    
#ifdef C_CODE
    _effectsRuns.erase(_effectsRuns.begin() + indexToRemove);
#endif
#ifdef JAVA_CODE
    _effectsRuns.remove(indexToRemove);
#endif
  }
}

void EffectsScheduler::doOneCyle(const RenderContext *rc) {
  const TimeInterval now = _timer->now();
  
  
  processFinishedEffects(rc, now);
  
  
  for (int i = 0; i < _effectsRuns.size(); i++) {
    EffectRun* effectRun = _effectsRuns[i];
    
    if (effectRun->_started == false) {
      effectRun->_effect->start(rc, now);
      effectRun->_started = true;
    }
    
    effectRun->_effect->doStep(rc, now);
  }
}

int EffectsScheduler::render(const RenderContext *rc) {
  doOneCyle(rc);
  if (_effectsRuns.size() == 0) {
    return Renderer::maxTimeToRender;
  }
  else {
    return 0;
  }
}

bool EffectsScheduler::onTouchEvent(const TouchEvent* touchEvent) {
  return false;
}

void EffectsScheduler::onResizeViewportEvent(int width, int height) {

}

void EffectsScheduler::startEffect(Effect* effect) {
  _effectsRuns.push_back(new EffectRun(effect));
}
