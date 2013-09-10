//
//  CameraEffects.h
//  G3MiOSSDK
//
//  Created by Agustín Trujillo on 17/07/13.
//
//

#ifndef G3MiOSSDK_CameraEffects_h
#define G3MiOSSDK_CameraEffects_h


#include "Effects.hpp"
#include "Vector3D.hpp"

class Angle;



class RotateWithAxisEffect : public EffectWithForce {
public:
  
  RotateWithAxisEffect(const Vector3D& axis, const Angle& angle);
  
  virtual ~RotateWithAxisEffect() {};
  
  virtual void start(const G3MRenderContext *rc, const TimeInterval& when) {}
  
  virtual void doStep(const G3MRenderContext *rc, const TimeInterval& when);
  
  virtual void stop(const G3MRenderContext *rc, const TimeInterval& when);
  
  virtual void cancel(const TimeInterval& when) {}
  
private:
  Vector3D _axis;
};

//***************************************************************

class SingleTranslationEffect : public EffectWithForce {
public:
  
  SingleTranslationEffect(const Vector3D& desp);
  
  virtual void start(const G3MRenderContext *rc,
                     const TimeInterval& when) {}
  
  virtual void doStep(const G3MRenderContext *rc,
                      const TimeInterval& when);
  
  virtual void stop(const G3MRenderContext *rc,
                    const TimeInterval& when);
  
  virtual void cancel(const TimeInterval& when) {}
  
private:
  Vector3D _direction;
};


//***************************************************************

class DoubleTapRotationEffect : public EffectWithDuration {
public:
  
  DoubleTapRotationEffect(const TimeInterval& duration,
                          const Vector3D& axis,
                          const Angle& angle,
                          double distance,
                          const bool linearTiming=false);
  
  virtual void start(const G3MRenderContext *rc,
                     const TimeInterval& when);
  
  virtual void doStep(const G3MRenderContext *rc,
                      const TimeInterval& when);
  
  virtual void stop(const G3MRenderContext *rc,
                    const TimeInterval& when);
  
  virtual void cancel(const TimeInterval& when) {}
  
private:
  Vector3D _axis;
  Angle    _angle;
  double   _distance;
  double   _lastAlpha;
};

//***************************************************************

class DoubleTapTranslationEffect : public EffectWithDuration {
public:
  
  DoubleTapTranslationEffect(const TimeInterval& duration,
                             const Vector3D& translation,
                             double distance,
                             const bool linearTiming=false);
  
  virtual void start(const G3MRenderContext *rc,
                     const TimeInterval& when);
  
  virtual void doStep(const G3MRenderContext *rc,
                      const TimeInterval& when);
  
  virtual void stop(const G3MRenderContext *rc,
                    const TimeInterval& when);
  
  virtual void cancel(const TimeInterval& when) {}
  
private:
  Vector3D _translation;
  double   _distance;
  double   _lastAlpha;
};




#endif
