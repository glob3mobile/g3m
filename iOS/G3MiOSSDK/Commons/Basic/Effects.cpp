//
//  Effects.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/06/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//


#include "Renderer.hpp"

#include "Effects.hpp"

#include "Context.hpp"

void EffectsScheduler::initialize(const G3MContext* context) {
  _factory = context->getFactory();
  _timer = _factory->createTimer();
}

void EffectsScheduler::cancelAllEffectsFor(EffectTarget* target) {
  std::vector<int> indicesToRemove;
  const TimeInterval now = _timer->now();

  const int size = _effectsRuns.size();
  for (int i = 0; i < size; i++) {
    EffectRun* effectRun = _effectsRuns[i];

    if (effectRun->_target == target) {
      if (effectRun->_started) {
        effectRun->_effect->cancel(now);
      }
      indicesToRemove.push_back(i);
    }
  }

  // backward iteration, to remove from bottom to top
  for (int i = indicesToRemove.size() - 1; i >= 0; i--) {
    const int indexToRemove = indicesToRemove[i];
    EffectRun* effectRun = _effectsRuns[indexToRemove];
    delete effectRun;

#ifdef C_CODE
    _effectsRuns.erase(_effectsRuns.begin() + indexToRemove);
#endif
#ifdef JAVA_CODE
    _effectsRuns.remove(indexToRemove);
#endif
  }

}

void EffectsScheduler::processFinishedEffects(const G3MRenderContext* rc,
                                              const TimeInterval& when) {
  std::vector<int> indicesToRemove;
  const int size = _effectsRuns.size();
  for (int i = 0; i < size; i++) {
    EffectRun* effectRun = _effectsRuns[i];

    if (effectRun->_started) {
      Effect* effect = effectRun->_effect;
      if (effect->isDone(rc, when)) {
        effect->stop(rc, when);

        indicesToRemove.push_back(i);
      }
    }
  }

  // backward iteration, to remove from bottom to top
  for (int i = indicesToRemove.size() - 1; i >= 0; i--) {
    const int indexToRemove = indicesToRemove[i];
    EffectRun* effectRun = _effectsRuns[indexToRemove];
    delete effectRun;

#ifdef C_CODE
    _effectsRuns.erase(_effectsRuns.begin() + indexToRemove);
#endif
#ifdef JAVA_CODE
    _effectsRuns.remove(indexToRemove);
#endif
  }
}

void EffectsScheduler::doOneCyle(const G3MRenderContext* rc) {
  const TimeInterval now = _timer->now();

  processFinishedEffects(rc, now);

  const int size = _effectsRuns.size();
  for (int i = 0; i < size; i++) {
    EffectRun* effectRun = _effectsRuns[i];
    Effect* effect = effectRun->_effect;

    if (!effectRun->_started) {
      effect->start(rc, now);
      effectRun->_started = true;
    }

    effect->doStep(rc, now);
  }
}

void EffectsScheduler::startEffect(Effect* effect,
                                   EffectTarget* target) {
  _effectsRuns.push_back(new EffectRun(effect, target));
}
