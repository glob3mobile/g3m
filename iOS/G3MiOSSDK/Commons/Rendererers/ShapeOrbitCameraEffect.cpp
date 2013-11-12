//
//  ShapeOrbitCameraEffect.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/31/13.
//
//

#include "ShapeOrbitCameraEffect.hpp"

#include "Shape.hpp"
#include "Camera.hpp"

void ShapeOrbitCameraEffect::doStep(const G3MRenderContext* rc,
                                    const TimeInterval& when) {
  const double alpha = getAlpha(when);

  IMathUtils* mu = IMathUtils::instance();
  const double distance          = mu->linearInterpolation(_fromDistance,          _toDistance,          alpha);
  const double azimuthInRadians  = mu->linearInterpolation(_fromAzimuthInRadians,  _toAzimuthInRadians,  alpha);
  const double altitudeInRadians = mu->linearInterpolation(_fromAltitudeInRadians, _toAltitudeInRadians, alpha);

  rc->getNextCamera()->setPointOfView(_shape->getPosition(),
                                      distance,
                                      Angle::fromRadians(azimuthInRadians),
                                      Angle::fromRadians(altitudeInRadians));
}

void ShapeOrbitCameraEffect::cancel(const TimeInterval& when) {
  // do nothing
}

void ShapeOrbitCameraEffect::stop(const G3MRenderContext* rc,
                                  const TimeInterval& when) {
  rc->getNextCamera()->setPointOfView(_shape->getPosition(),
                                      _toDistance,
                                      Angle::fromRadians(_toAzimuthInRadians),
                                      Angle::fromRadians(_toAltitudeInRadians));

}
