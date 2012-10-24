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

#include "IMathUtils.hpp"
#include "IFactory.hpp"

#include <vector>

class EffectTarget {
public:  
  
  // added this method only to force the java-translator to consider EffectTarget as an interface (dgd)
  virtual void unusedMethod() const = 0;
  
#ifdef C_CODE
  virtual ~EffectTarget(){}
#endif
  
};


class Effect {
protected:
  
  double pace(const double f) const {
    if (f < 0) return 0;
    if (f > 1) return 1;
    
//    return sigmoid(f);
//    return gently(f, 0.6, 0.85);
    return gently(f, 0.25, 0.75);
  }
  
  double sigmoid(double x) const {
    x = 12.0*x - 6.0;
    return (1.0 / (1.0 + GMath.exp(-1.0 * x)));
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
  
  virtual void cancel(const TimeInterval& now) = 0;

  virtual ~Effect() { }
};




class EffectWithDuration : public Effect {
private:
  long long _started;
  const long long _duration;
  
protected:
  
  EffectWithDuration(const TimeInterval& duration) :
  _started(0),
  _duration(duration.milliseconds())
  {
    
  }
  
  double percentDone(const TimeInterval& now) const {
    const long long elapsed = now.milliseconds() - _started;
    
    const double percent = (double) elapsed / _duration;
    if (percent > 1) return 1;
    if (percent < 0) return 0;
    return percent;
  }
  
  
public:
  virtual void stop(const RenderContext *rc,
                    const TimeInterval& now) {
    
  }
  
  virtual void start(const RenderContext *rc,
                     const TimeInterval& now) {
    _started = now.milliseconds();
  }
  
  virtual bool isDone(const RenderContext *rc,
                      const TimeInterval& now) {
    const double percent = percentDone(now);
    return percent >= 1;
  }
  
};




class EffectWithForce : public Effect {
private:
  double       _force;
  const double _friction;
  
protected:
  EffectWithForce(double force,
                  double friction):
  _force(force),
  _friction(friction)
  {}
  
  double getForce() const {
    return _force;
  }
  
public:
  virtual void doStep(const RenderContext *rc,
                      const TimeInterval& now) {
    _force *= _friction;
  };
  
  virtual bool isDone(const RenderContext *rc,
                      const TimeInterval& now) {
    return (GMath.abs(_force) < 1e-6);
  }
  
};




class EffectsScheduler {
private:
  
  class EffectRun {
  public:
    Effect*       _effect;
    EffectTarget* _target;
    
    bool         _started;
    
    EffectRun(Effect* effect,
              EffectTarget* target) :
    _effect(effect),
    _target(target),
    _started(false)
    {
    }
    
    ~EffectRun() {
      delete _effect;
    }
  };
  
  
  std::vector<EffectRun*> _effectsRuns;
  ITimer*                 _timer;
  const IFactory*         _factory;
  
  
  void processFinishedEffects(const RenderContext *rc,
                              const TimeInterval& now);
  
public:
  EffectsScheduler(): _effectsRuns(std::vector<EffectRun*>()) {
    
  };
  
  void doOneCyle(const RenderContext *rc);

  void initialize(const InitializationContext* ic);
  
  virtual ~EffectsScheduler() {
    _factory->deleteTimer(_timer);
    
    for (unsigned int i = 0; i < _effectsRuns.size(); i++) {
      EffectRun* effectRun = _effectsRuns[i];
      delete effectRun;
    }
  };
  
  void startEffect(Effect* effect,
                   EffectTarget* target);
  
  void cancellAllEffectsFor(EffectTarget* target);
  
  void onResume(const InitializationContext* ic) {
    
  }
  
  void onPause(const InitializationContext* ic) {
    
  }

};



//class SampleEffect : public EffectWithDuration {
//public:
//  
//  SampleEffect(TimeInterval duration) : EffectWithDuration(duration) {
//  }
//  
//  virtual void start(const RenderContext *rc,
//                     const TimeInterval& now) {
//    EffectWithDuration::start(rc, now);
//    _lastPercent = 0;
//  }
//  
//  virtual void doStep(const RenderContext *rc,
//                      const TimeInterval& now) {
//    const double percent = pace( percentDone(now) );
//    rc->getNextCamera()->moveForward((percent-_lastPercent)*1e7);
//    _lastPercent = percent;
//  }
//  
//  virtual void stop(const RenderContext *rc,
//                    const TimeInterval& now) {
//    EffectWithDuration::stop(rc, now);
//  }
//  
//  virtual void cancel(const TimeInterval& now) {
//    // do nothing, just leave the effect in the intermediate state
//  }
//
//private:
//  double _lastPercent;
//};


#endif
