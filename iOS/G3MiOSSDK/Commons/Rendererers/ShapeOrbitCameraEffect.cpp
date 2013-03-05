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

void ShapeOrbitCameraEffect::doStep(const G3MRenderContext *rc,
                                    const TimeInterval& when) {
  const double alpha = getAlpha(when);

  const Geodetic3D center = _shape->getPosition();

  IMathUtils* mu = IMathUtils::instance();
  const double distance          = mu->linearInterpolation(_fromDistance,          _toDistance,          alpha);
  const double azimuthInRadians  = mu->linearInterpolation(_fromAzimuthInRadians,  _toAzimuthInRadians,  alpha);
  const double altitudeInRadians = mu->linearInterpolation(_fromAltitudeInRadians, _toAltitudeInRadians, alpha);

  rc->getNextCamera()->setPointOfView(center,
                                      distance,
                                      Angle::fromRadians(azimuthInRadians),
                                      Angle::fromRadians(altitudeInRadians));
}

void ShapeOrbitCameraEffect::cancel(const TimeInterval& when) {
  // do nothing
}

void ShapeOrbitCameraEffect::stop(const G3MRenderContext *rc,
                                  const TimeInterval& when) {
  const Geodetic3D center = _shape->getPosition();

  rc->getNextCamera()->setPointOfView(center,
                                      _toDistance,
                                      Angle::fromRadians(_toAzimuthInRadians),
                                      Angle::fromRadians(_toAltitudeInRadians));

}
