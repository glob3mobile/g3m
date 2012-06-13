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
  
  virtual void start(double now);
  
  virtual void doStep(double now);
  
  virtual bool isDone(double now);

  virtual void stop(double now);

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
  
};


class EffectsScheduler : public Renderer {
private:
  std::vector<EffectRun> _effects;
  ITimer*                _timer;
  const IFactory*        _factory;
  
  void doOneCyle();
  
  void processFinishedEffects(const double now);

public:
  EffectsScheduler(): _effects(std::vector<EffectRun>()) {
    
  };
  
  virtual void initialize(const InitializationContext* ic);
  
  virtual int render(const RenderContext* rc);
  
  virtual bool onTouchEvent(const TouchEvent* touchEvent);
  
  virtual bool onResizeViewportEvent(int width, int height);
  
  virtual ~EffectsScheduler() {
    _factory->deleteTimer(_timer);
    
    for (int i = 0; i < _effects.size(); i++) {
      EffectRun effectRun = _effects[i];
      delete effectRun._effect;
    }
  };
  
  void startEffect(Effect* effect);
};


#endif
