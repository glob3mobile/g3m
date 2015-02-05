//
//  CameraEffects.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo on 17/07/13.
//
//

#include "CameraEffects.hpp"
#include "Context.hpp"
#include "Camera.hpp"



RotateWithAxisEffect::RotateWithAxisEffect(const Vector3D& axis, const Angle& angle):
EffectWithForce(angle._degrees, 0.975),
_axis(axis)
{
}


void RotateWithAxisEffect::doStep(const G3MRenderContext* rc,
                                  const TimeInterval& when) {
  EffectWithForce::doStep(rc, when);
  rc->getNextCamera()->rotateWithAxis(_axis, Angle::fromDegrees(getForce()));
}


void RotateWithAxisEffect::stop(const G3MRenderContext* rc,
                                const TimeInterval& when) {
  rc->getNextCamera()->rotateWithAxis(_axis, Angle::fromDegrees(getForce()));
}


SingleTranslationEffect::SingleTranslationEffect(const Vector3D& desp):
EffectWithForce(1, 0.92),
_direction(desp)
{
}


void SingleTranslationEffect::doStep(const G3MRenderContext* rc,
                                     const TimeInterval& when) {
  EffectWithForce::doStep(rc, when);
  rc->getNextCamera()->translateCamera(_direction.times(getForce()));
}


void SingleTranslationEffect::stop(const G3MRenderContext* rc,
                                   const TimeInterval& when) {
  rc->getNextCamera()->translateCamera(_direction.times(getForce()));
}


DoubleTapRotationEffect::DoubleTapRotationEffect(const TimeInterval& duration,
                                                 const Vector3D& axis,
                                                 const Angle& angle,
                                                 double distance,
                                                 const bool linearTiming):
EffectWithDuration(duration, linearTiming),
_axis(axis),
_angle(angle),
_distance(distance)
{}


void DoubleTapRotationEffect::start(const G3MRenderContext* rc,
                                    const TimeInterval& when) {
  EffectWithDuration::start(rc, when);
  _lastAlpha = 0;
}


void DoubleTapRotationEffect::doStep(const G3MRenderContext* rc,
                                     const TimeInterval& when) {
  const double alpha = getAlpha(when);
  Camera *camera = rc->getNextCamera();
  const double step = alpha - _lastAlpha;
  camera->rotateWithAxis(_axis, _angle.times(step));
  camera->moveForward(_distance * step);
  _lastAlpha = alpha;
}


void DoubleTapRotationEffect::stop(const G3MRenderContext* rc,
                                   const TimeInterval& when) {
  Camera *camera = rc->getNextCamera();
  const double step = 1.0 - _lastAlpha;
  camera->rotateWithAxis(_axis, _angle.times(step));
  camera->moveForward(_distance * step);
}


DoubleTapTranslationEffect::DoubleTapTranslationEffect(const TimeInterval& duration,
                                                       const Vector3D& translation,
                                                       double distance,
                                                       const bool linearTiming):
EffectWithDuration(duration, linearTiming),
_translation(translation),
_distance(distance)
{}


void DoubleTapTranslationEffect::start(const G3MRenderContext* rc,
                                       const TimeInterval& when) {
  EffectWithDuration::start(rc, when);
  _lastAlpha = 0;
}


void DoubleTapTranslationEffect::doStep(const G3MRenderContext* rc,
                                        const TimeInterval& when) {
  const double alpha = getAlpha(when);
  Camera *camera = rc->getNextCamera();
  const double step = alpha - _lastAlpha;
  camera->translateCamera(_translation.times(step));
  camera->moveForward(_distance * step);
  _lastAlpha = alpha;
}


void DoubleTapTranslationEffect::stop(const G3MRenderContext* rc,
                                      const TimeInterval& when) {
  Camera *camera = rc->getNextCamera();
  const double step = 1.0 - _lastAlpha;
  camera->translateCamera(_translation.times(step));
  camera->moveForward(_distance * step);
}


