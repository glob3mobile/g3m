//
//  EffectWithListener.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 10/9/26.
//

#include "EffectWithListener.hpp"

#include "EffectListener.hpp"


EffectWithListener::~EffectWithListener() {
  delete _effect;
  if (_deleteListener) {
    delete _listener;
  }
#ifdef JAVA_CODE
  super.dispose();
#endif
}

void EffectWithListener::start(const G3MRenderContext* rc,
                               const TimeInterval& when) {
  _effect->start(rc, when);
}

void EffectWithListener::doStep(const G3MRenderContext* rc,
                                const TimeInterval& when) {
  _effect->doStep(rc, when);
}

bool EffectWithListener::isDone(const G3MRenderContext* rc,
                                const TimeInterval& when) {
  return _effect->isDone(rc, when);
}

void EffectWithListener::stop(const G3MRenderContext* rc,
                              const TimeInterval& when) {
  _effect->stop(rc, when);
  _listener->onStop(rc, when);
}

void EffectWithListener::cancel(const TimeInterval& when) {
  _effect->cancel(when);
  _listener->onCancel(when);
}
