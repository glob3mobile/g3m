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


void RotateWithAxisEffect::doStep(const G3MRenderContext *rc,
                    const TimeInterval& when) {
  EffectWithForce::doStep(rc, when);
  rc->getNextCamera()->rotateWithAxis(_axis, Angle::fromDegrees(getForce()));
}


void RotateWithAxisEffect::stop(const G3MRenderContext *rc,
                  const TimeInterval& when) {
  rc->getNextCamera()->rotateWithAxis(_axis, Angle::fromDegrees(getForce()));
}


SingleTranslationEffect::SingleTranslationEffect(const Vector3D& desp):
EffectWithForce(1, 0.92),
_direction(desp)
{
}


void SingleTranslationEffect::doStep(const G3MRenderContext *rc,
                    const TimeInterval& when) {
  EffectWithForce::doStep(rc, when);
  rc->getNextCamera()->translateCamera(_direction.times(getForce()));
}


void SingleTranslationEffect::stop(const G3MRenderContext *rc,
                  const TimeInterval& when) {
  rc->getNextCamera()->translateCamera(_direction.times(getForce()));
}

