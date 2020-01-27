//
//  Effects.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 12/06/12.
//

#ifndef G3M_Effects
#define G3M_Effects

#include <vector>

class G3MRenderContext;
class TimeInterval;
class ITimer;
class IFactory;
class G3MContext;


class EffectTarget {
public:
#ifdef C_CODE
  virtual ~EffectTarget() { }
#else
  // useless, it's here only to make the C++ => Java translator creates an interface intead of an empty class
  virtual void unusedMethod() const = 0;
#endif

#ifdef JAVA_CODE
  void dispose();
#endif
};


class Effect {
protected:

  static double sigmoid(double x);

  static double gently(const double x,
                       const double lower,
                       const double upper) {
    const double uperSquared  = upper * upper;
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
  static double pace(const double f) {
    if (f <= 0) return 0;
    if (f >= 1) return 1;
    const double result = gently(f, 0.25, 0.75);
    if (result <= 0) return 0;
    if (result >= 1) return 1;
    return result;
  }

  virtual void start(const G3MRenderContext* rc,
                     const TimeInterval& when) = 0;

  virtual void doStep(const G3MRenderContext* rc,
                      const TimeInterval& when) = 0;

  virtual bool isDone(const G3MRenderContext* rc,
                      const TimeInterval& when) = 0;

  virtual void stop(const G3MRenderContext* rc,
                    const TimeInterval& when) = 0;

  virtual void cancel(const TimeInterval& when) = 0;

  virtual ~Effect() {

  }
};


class EffectWithDuration : public Effect {
private:
  long long       _started;
  const long long _durationMS;
  const bool      _linearTiming;

protected:

  EffectWithDuration(const TimeInterval& duration,
                     const bool linearTiming);

  double percentDone(const TimeInterval& when) const;

  double getAlpha(const TimeInterval& when) const {
    const double percent = percentDone(when);
    return _linearTiming ? percent : pace(percent);
  }


public:
  virtual void start(const G3MRenderContext* rc,
                     const TimeInterval& when);

  virtual bool isDone(const G3MRenderContext* rc,
                      const TimeInterval& when) {
    const double percent = percentDone(when);
    return (percent >= 1);
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
  virtual void doStep(const G3MRenderContext* rc,
                      const TimeInterval& when) {
    _force *= _friction;
  };

  virtual bool isDone(const G3MRenderContext* rc,
                      const TimeInterval& when);

};


class EffectNeverEnding : public Effect {
private:

protected:
  EffectNeverEnding() {}

public:
  virtual void doStep(const G3MRenderContext* rc,
                      const TimeInterval& when) {
  };

  virtual bool isDone(const G3MRenderContext* rc,
                      const TimeInterval& when) {
    return false;
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

  void processFinishedEffects(const G3MRenderContext* rc,
                              const TimeInterval& when);

#ifdef JAVA_CODE
  final java.util.ArrayList<EffectRun> _effectsToStop   = new java.util.ArrayList<EffectRun>();
  final java.util.ArrayList<EffectRun> _effectsToCancel = new java.util.ArrayList<EffectRun>();
#endif

public:
  EffectsScheduler() :
  _effectsRuns(std::vector<EffectRun*>())
  {
  }

  void doOneCyle(const G3MRenderContext* rc);

  void initialize(const G3MContext* context);

  virtual ~EffectsScheduler();

  void startEffect(Effect* effect,
                   EffectTarget* target);

  void cancelAllEffects();

  void cancelAllEffectsFor(EffectTarget* target);

  void onResume(const G3MContext* context) {

  }

  void onPause(const G3MContext* context) {
    
  }
  
  void onDestroy(const G3MContext* context) {
    
  }
  
};

#endif
