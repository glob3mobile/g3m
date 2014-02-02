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

void EffectsScheduler::cancelAllEffects() {
  const TimeInterval now = _timer->now();
#ifdef C_CODE
  std::vector<int> indicesToRemove;

  const int size = _effectsRuns.size();
  for (int i = 0; i < size; i++) {
    EffectRun* effectRun = _effectsRuns[i];

    if (effectRun->_started) {
      effectRun->_effect->cancel(now);
    }
    indicesToRemove.push_back(i);
  }

  // backward iteration, to remove from bottom to top
  for (int i = indicesToRemove.size() - 1; i >= 0; i--) {
    const int indexToRemove = indicesToRemove[i];
    EffectRun* effectRun = _effectsRuns[indexToRemove];
    delete effectRun;

    _effectsRuns.erase(_effectsRuns.begin() + indexToRemove);
  }
#endif

#ifdef JAVA_CODE
  final java.util.ArrayList<EffectRun> effectsToCancel = new java.util.ArrayList<EffectRun>();

  final java.util.Iterator<EffectRun> iterator = _effectsRuns.iterator();
  while (iterator.hasNext()) {
    final EffectRun effectRun = iterator.next();
    if (effectRun._started) {
      effectsToCancel.add(effectRun);
    }
    else {
      effectRun.dispose();
    }
    iterator.remove();
  }

  for (final EffectRun effectRun : effectsToCancel) {
    effectRun._effect.cancel(now);
    effectRun.dispose();
  }
#endif
}

void EffectsScheduler::cancelAllEffectsFor(EffectTarget* target) {
  const TimeInterval now = _timer->now();
#ifdef C_CODE
  std::vector<int> indicesToRemove;

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

    _effectsRuns.erase(_effectsRuns.begin() + indexToRemove);
  }
#endif

#ifdef JAVA_CODE
  final java.util.ArrayList<EffectRun> effectsToCancel = new java.util.ArrayList<EffectRun>();

  final java.util.Iterator<EffectRun> iterator = _effectsRuns.iterator();
  while (iterator.hasNext()) {
    final EffectRun effectRun = iterator.next();
    if (effectRun._target == target) {
      if (effectRun._started) {
        effectsToCancel.add(effectRun);
      }
      else {
        effectRun.dispose();
      }
      iterator.remove();
    }
  }

  for (final EffectRun effectRun : effectsToCancel) {
    effectRun._effect.cancel(now);
    effectRun.dispose();
  }
#endif
}

void EffectsScheduler::processFinishedEffects(const G3MRenderContext* rc,
                                              const TimeInterval& when) {
#ifdef C_CODE
  std::vector<EffectRun*> effectsToStop;
  std::vector<EffectRun*>::iterator i = _effectsRuns.begin();
  while (i != _effectsRuns.end()) {
    EffectRun* effectRun = *i;

    bool removed = false;
    if (effectRun->_started) {
      Effect* effect = effectRun->_effect;
      if (effect->isDone(rc, when)) {
        i = _effectsRuns.erase(i);
        removed = true;
        effectsToStop.push_back( effectRun );
      }
    }
    if (!removed) {
      i++;
    }
  }

  const int removedSize = effectsToStop.size();
  for (int i = 0; i < removedSize; i++) {
    EffectRun* effectRun = effectsToStop[i];
    effectRun->_effect->stop(rc, when);

    delete effectRun;
  }
#endif

#ifdef JAVA_CODE
  final java.util.ArrayList<EffectRun> effectsToStop = new java.util.ArrayList<EffectRun>();
  final java.util.Iterator<EffectRun> iterator = _effectsRuns.iterator();
  while (iterator.hasNext()) {
    final EffectRun effectRun = iterator.next();
    if (effectRun._started) {
      if (effectRun._effect.isDone(rc, when)) {
        iterator.remove();
        effectsToStop.add(effectRun);
      }
    }
  }

  for (final EffectRun effectRun : effectsToStop) {
    effectRun._effect.stop(rc, when);
    effectRun.dispose();
  }
#endif
}

void EffectsScheduler::doOneCyle(const G3MRenderContext* rc) {
  const TimeInterval now = _timer->now();

  processFinishedEffects(rc, now);

#ifdef C_CODE
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
#endif
#ifdef JAVA_CODE
  final java.util.Iterator<EffectRun> iterator = _effectsRuns.iterator();
  while (iterator.hasNext()) {
    final EffectRun effectRun = iterator.next();
    final Effect effect = effectRun._effect;
    if (!effectRun._started) {
      effect.start(rc, now);
      effectRun._started = true;
    }
    effect.doStep(rc, now);
  }
#endif
}

void EffectsScheduler::startEffect(Effect* effect,
                                   EffectTarget* target) {
  _effectsRuns.push_back(new EffectRun(effect, target));
}
