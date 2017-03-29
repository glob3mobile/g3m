//
//  Effects.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/06/12.
//


#include "Renderer.hpp"

#include "Effects.hpp"

#include "IMathUtils.hpp"
#include "TimeInterval.hpp"
#include "G3MContext.hpp"
#include "IFactory.hpp"
#include "ITimer.hpp"
#include "ErrorHandling.hpp"


double Effect::sigmoid(double x) {
  const double xx = 12.0*x - 6.0;
  return (1.0 / (1.0 + IMathUtils::instance()->exp(-1.0 * xx)));
}

EffectWithDuration::EffectWithDuration(const TimeInterval& duration,
                                       const bool linearTiming) :
_durationMS(duration._milliseconds),
_linearTiming(linearTiming),
_started(0)
{
  if (_durationMS <= 0) {
    THROW_EXCEPTION("Invalid duration");
  }
}


double EffectWithDuration::percentDone(const TimeInterval& when) const {
  const long long elapsed = when._milliseconds - _started;

  const double percent = (double) elapsed / _durationMS;
  if (percent > 1) return 1;
  if (percent < 0) return 0;
  return percent;
}

void EffectWithDuration::start(const G3MRenderContext* rc,
                               const TimeInterval& when) {
  _started = when._milliseconds;
}


bool EffectWithForce::isDone(const G3MRenderContext* rc,
                             const TimeInterval& when) {
  return (IMathUtils::instance()->abs(_force) < 0.005);
}



void EffectsScheduler::initialize(const G3MContext* context) {
  _timer = context->getFactory()->createTimer();
}

EffectsScheduler::~EffectsScheduler() {
  delete _timer;

  for (unsigned int i = 0; i < _effectsRuns.size(); i++) {
    EffectRun* effectRun = _effectsRuns[i];
    delete effectRun;
  }
}


void EffectsScheduler::cancelAllEffects() {
  const TimeInterval now = _timer->now();
#ifdef C_CODE
  std::vector<size_t> indicesToRemove;

  const size_t size = _effectsRuns.size();
  for (size_t i = 0; i < size; i++) {
    EffectRun* effectRun = _effectsRuns[i];

    if (effectRun->_started) {
      effectRun->_effect->cancel(now);
    }
    indicesToRemove.push_back(i);
  }

  // backward iteration, to remove from bottom to top
  for (int i = indicesToRemove.size() - 1; i >= 0; i--) {
    const size_t indexToRemove = indicesToRemove[i];
    EffectRun* effectRun = _effectsRuns[indexToRemove];
    delete effectRun;

    _effectsRuns.erase(_effectsRuns.begin() + indexToRemove);
  }
#endif

#ifdef JAVA_CODE
  _effectsToCancel.clear();

  final java.util.Iterator<EffectRun> iterator = _effectsRuns.iterator();
  while (iterator.hasNext()) {
    final EffectRun effectRun = iterator.next();
    if (effectRun._started) {
      _effectsToCancel.add(effectRun);
    }
    else {
      effectRun.dispose();
    }
    iterator.remove();
  }

  final int effectsToCancelSize = _effectsToCancel.size();
  for (int i = 0; i < effectsToCancelSize; i++) {
    final EffectRun effectRun = _effectsToCancel.get(i);
    effectRun._effect.cancel(now);
    effectRun.dispose();
  }
#endif
}

void EffectsScheduler::cancelAllEffectsFor(EffectTarget* target) {
  const TimeInterval now = _timer->now();
#ifdef C_CODE
  std::vector<size_t> indicesToRemove;

  const size_t size = _effectsRuns.size();
  for (size_t i = 0; i < size; i++) {
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
    const size_t indexToRemove = indicesToRemove[i];
    EffectRun* effectRun = _effectsRuns[indexToRemove];
    delete effectRun;

    _effectsRuns.erase(_effectsRuns.begin() + indexToRemove);
  }
#endif

#ifdef JAVA_CODE
  _effectsToCancel.clear();

  final java.util.Iterator<EffectRun> iterator = _effectsRuns.iterator();
  while (iterator.hasNext()) {
    final EffectRun effectRun = iterator.next();
    if (effectRun._target == target) {
      if (effectRun._started) {
        _effectsToCancel.add(effectRun);
      }
      else {
        effectRun.dispose();
      }
      iterator.remove();
    }
  }

  final int effectsToCancelSize = _effectsToCancel.size();
  for (int i = 0; i < effectsToCancelSize; i++) {
    final EffectRun effectRun = _effectsToCancel.get(i);
    effectRun._effect.cancel(now);
    effectRun.dispose();
  }
#endif
}

void EffectsScheduler::processFinishedEffects(const G3MRenderContext* rc,
                                              const TimeInterval& when) {
#ifdef C_CODE
  std::vector<EffectRun*> effectsToStop;
  std::vector<EffectRun*>::iterator it = _effectsRuns.begin();
  while (it != _effectsRuns.end()) {
    EffectRun* effectRun = *it;

    bool removed = false;
    if (effectRun->_started) {
      Effect* effect = effectRun->_effect;
      if (effect->isDone(rc, when)) {
        it = _effectsRuns.erase(it);
        removed = true;
        effectsToStop.push_back( effectRun );
      }
    }
    if (!removed) {
      it++;
    }
  }

  const size_t removedSize = effectsToStop.size();
  for (size_t i = 0; i < removedSize; i++) {
    EffectRun* effectRun = effectsToStop[i];
    effectRun->_effect->stop(rc, when);

    delete effectRun;
  }
#endif

#ifdef JAVA_CODE
  _effectsToStop.clear();
  final java.util.Iterator<EffectRun> iterator = _effectsRuns.iterator();
  while (iterator.hasNext()) {
    final EffectRun effectRun = iterator.next();
    if (effectRun._started) {
      if (effectRun._effect.isDone(rc, when)) {
        iterator.remove();
        _effectsToStop.add(effectRun);
      }
    }
  }

  final int effectsToStopSize = _effectsToStop.size();
  for (int i = 0; i < effectsToStopSize; i++) {
    final EffectRun effectRun = _effectsToStop.get(i);
    effectRun._effect.stop(rc, when);
    effectRun.dispose();
  }
#endif
}

void EffectsScheduler::doOneCyle(const G3MRenderContext* rc) {
  if (!_effectsRuns.empty()) {
    const TimeInterval now = _timer->now();

    processFinishedEffects(rc, now);

    // ask for _effectsRuns.size() here, as processFinishedEffects can modify the size
    const size_t effectsRunsSize = _effectsRuns.size();
    for (size_t i = 0; i < effectsRunsSize; i++) {
      EffectRun* effectRun = _effectsRuns[i];
      Effect* effect = effectRun->_effect;
      if (!effectRun->_started) {
        effect->start(rc, now);
        effectRun->_started = true;
      }
      effect->doStep(rc, now);
    }
  }
}

void EffectsScheduler::startEffect(Effect* effect,
                                   EffectTarget* target) {
  _effectsRuns.push_back(new EffectRun(effect, target));
}
