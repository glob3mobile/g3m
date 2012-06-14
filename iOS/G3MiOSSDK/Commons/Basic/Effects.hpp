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
#include "TimeInterval.hpp"
#include "ITimer.hpp"
#include "Camera.hpp"

class Effect {
protected:
  
  double pace(const double f) const {
    if (f < 0) return 0;
    if (f > 1) return 1;
    
    //return sigmoid(f);
    return gently(f, 0.6, 0.85);
  }
  
  double sigmoid(double x) const {
    x = 12.0*x - 6.0;
    return (1.0 / (1.0 + exp(-1.0 * x)));
  }
  
  double gently(const double x,
                const double lower,
                const double upper) const {
    const double uperSquared = upper * upper;
    const double lowerPerUper = lower * upper;
    const double tmp = uperSquared - lowerPerUper + lower - 1;
    
    if (x < lower) {
      return ((upper - 1) / (lower *  tmp)) * x * x;
    }
    
    if (x > upper) {
      const double a3 = 1 / tmp;
      const double b3 = -2 * a3;
      const double c3 = 1 + a3;
      return (a3 * x * x) + (b3 * x) + c3;
    }
    
    const double m = 2 * (upper - 1) / tmp;
    const double b2 = (0 - m) * lower / 2;
    return m * x + b2;
  }
  
public:
  virtual void start(const RenderContext *rc,
                     const TimeInterval& now) = 0;
  
  virtual void doStep(const RenderContext *rc,
                      const TimeInterval& now) = 0;
  
  virtual bool isDone(const RenderContext *rc,
                      const TimeInterval& now) = 0;
  
  virtual void stop(const RenderContext *rc,
                    const TimeInterval& now) = 0;
  
  virtual ~Effect() { }
};


class DummyEffect : public Effect {
private:
  long _started;
  const long _duration;
  
  double percentDone(const TimeInterval& now) const {
    const long elapsed = now.milliseconds() - _started;
    
    const double percent = (double) elapsed / _duration;
    if (percent > 1) return 1;
    if (percent < 0) return 0;
    return percent;
  }
  
public:
  
  DummyEffect(TimeInterval duration) :
  _started(0),
  _duration(duration.milliseconds())
  {
    
  }
  
  virtual void start(const RenderContext *rc,
                     const TimeInterval& now) {
//    rc->getLogger()->logInfo("start %i", now.milliseconds());
    
    _started = now.milliseconds();
  }
  
  virtual void doStep(const RenderContext *rc,
                      const TimeInterval& now) {
//    rc->getLogger()->logInfo("doStep %i", now.milliseconds());
    const double percent = pace( percentDone(now) );
    
    rc->getCamera()->zoom(1 - (percent / 25));
  }
  
  virtual bool isDone(const RenderContext *rc,
                      const TimeInterval& now) {
    const double percent = percentDone(now);
    
//    rc->getLogger()->logInfo("isDone %i, %f", now.milliseconds(), percent);
    
    return percent >= 1;
  }
  
  virtual void stop(const RenderContext *rc,
                    const TimeInterval& now) {
//    rc->getLogger()->logInfo("stop %i", now.milliseconds());
  }
};


class EffectsScheduler : public Renderer {
private:
  
  class EffectRun {
  public:
    Effect* _effect;
    bool    _started;
    
    EffectRun(Effect* effect) : _effect(effect), _started(false) {
    }
    
    ~EffectRun() {
      delete _effect;
    }
  };
  
  
  std::vector<EffectRun*> _effectsRuns;
  ITimer*                 _timer;
  const IFactory*         _factory;
  
  void doOneCyle(const RenderContext *rc);
  
  void processFinishedEffects(const RenderContext *rc,
                              const TimeInterval& now);
  
public:
  EffectsScheduler(): _effectsRuns(std::vector<EffectRun*>()) {
    
  };
  
  virtual void initialize(const InitializationContext* ic);
  
  virtual int render(const RenderContext* rc);
  
  virtual bool onTouchEvent(const TouchEvent* touchEvent);
  
  virtual bool onResizeViewportEvent(int width, int height);
  
  virtual ~EffectsScheduler() {
    _factory->deleteTimer(_timer);
    
    for (int i = 0; i < _effectsRuns.size(); i++) {
      EffectRun* effectRun = _effectsRuns[i];
      delete effectRun;
    }
  };
  
  void startEffect(Effect* effect);
};


#endif
