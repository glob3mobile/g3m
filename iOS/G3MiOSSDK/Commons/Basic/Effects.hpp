//
//  Effects.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/06/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#ifndef G3MiOSSDK_Effects_hpp
#define G3MiOSSDK_Effects_hpp

#include "Renderer.hpp"
#include "ITimer.hpp"


class Effect {
public:
  virtual void start(const RenderContext *rc,
                     double now) = 0;
  
  virtual void doStep(const RenderContext *rc,
                      double now) = 0;
  
  virtual bool isDone(const RenderContext *rc,
                      double now) = 0;
  
  virtual void stop(const RenderContext *rc,
                    double now) = 0;
  
  virtual ~Effect() { }
};


class DummyEffect : public Effect {
private:
  unsigned int _counter;
  
public:
  
  virtual void start(const RenderContext *rc,
                     const double now) {
    rc->getLogger()->logInfo("start %f", now);
    
    _counter = 0;
  }
  
  virtual void doStep(const RenderContext *rc,
                      const double now) {
    rc->getLogger()->logInfo("doStep %f", now);
  }
  
  virtual bool isDone(const RenderContext *rc,
                      const double now) {
    rc->getLogger()->logInfo("isDone %f", now);
    
    return ++_counter > 5;
  }
  
  virtual void stop(const RenderContext *rc,
                    const double now) {
    rc->getLogger()->logInfo("stop %f", now);
  }
};


class EffectRun {
public:
  Effect* _effect;
  bool    _started;
  
  EffectRun(Effect* effect) :
  _effect(effect),
  _started(false)
  {
    
  }
  
  ~EffectRun() {
    delete _effect;
  }
  
};


class EffectsScheduler : public Renderer {
private:
  std::vector<EffectRun*> _effects;
  ITimer*                 _timer;
  const IFactory*         _factory;
  
  void doOneCyle(const RenderContext *rc);
  
  void processFinishedEffects(const RenderContext *rc,
                              const double now);
  
public:
  EffectsScheduler(): _effects(std::vector<EffectRun*>()) {
    
  };
  
  virtual void initialize(const InitializationContext* ic);
  
  virtual int render(const RenderContext* rc);
  
  virtual bool onTouchEvent(const TouchEvent* touchEvent);
  
  virtual bool onResizeViewportEvent(int width, int height);
  
  virtual ~EffectsScheduler() {
    _factory->deleteTimer(_timer);
    
    for (int i = 0; i < _effects.size(); i++) {
      EffectRun* effectRun = _effects[i];
      delete effectRun;
    }
  };
  
  void startEffect(Effect* effect);
};


#endif
