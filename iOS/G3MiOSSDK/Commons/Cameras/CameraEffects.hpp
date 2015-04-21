//
//  CameraEffects.hpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo on 17/07/13.
//
//

#ifndef G3MiOSSDK_CameraEffects
#define G3MiOSSDK_CameraEffects


#include "Effects.hpp"
#include "Vector3D.hpp"

class Angle;



class RotateWithAxisEffect : public EffectWithForce {
private:
  const Vector3D _axis;

public:
  RotateWithAxisEffect(const Vector3D& axis,
                       const Angle& angle);

  virtual ~RotateWithAxisEffect() {
  }

  void start(const G3MRenderContext* rc,
             const TimeInterval& when) {
  }

  void doStep(const G3MRenderContext* rc,
              const TimeInterval& when);

  void stop(const G3MRenderContext* rc,
            const TimeInterval& when);

  void cancel(const TimeInterval& when) {
  }
};


class SingleTranslationEffect : public EffectWithForce {
private:
  const Vector3D _direction;

public:

  SingleTranslationEffect(const Vector3D& desp);

  void start(const G3MRenderContext* rc,
             const TimeInterval& when) {
  }

  void doStep(const G3MRenderContext* rc,
              const TimeInterval& when);

  void stop(const G3MRenderContext* rc,
            const TimeInterval& when);

  void cancel(const TimeInterval& when) {
  }

};


class DoubleTapRotationEffect : public EffectWithDuration {
private:
  const Vector3D _axis;
  const Angle    _angle;
  const double   _distance;
  double         _lastAlpha;

public:
  DoubleTapRotationEffect(const TimeInterval& duration,
                          const Vector3D& axis,
                          const Angle& angle,
                          double distance,
                          const bool linearTiming=false);

  void start(const G3MRenderContext* rc,
             const TimeInterval& when);

  void doStep(const G3MRenderContext* rc,
              const TimeInterval& when);

  void stop(const G3MRenderContext* rc,
            const TimeInterval& when);

  void cancel(const TimeInterval& when) {
  }

};


class DoubleTapTranslationEffect : public EffectWithDuration {
private:
  const Vector3D _translation;
  const double   _distance;
  double         _lastAlpha;

public:

  DoubleTapTranslationEffect(const TimeInterval& duration,
                             const Vector3D& translation,
                             double distance,
                             const bool linearTiming=false);

  void start(const G3MRenderContext* rc,
             const TimeInterval& when);

  void doStep(const G3MRenderContext* rc,
              const TimeInterval& when);

  void stop(const G3MRenderContext* rc,
            const TimeInterval& when);

  void cancel(const TimeInterval& when) {}

};


#endif
