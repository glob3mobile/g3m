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
//  const double alpha = pace( percentDone(when) );
  const double alpha = percentDone(when);

  const Geodetic3D center = _shape->getPosition();

  const double distance          = GMath.lerp(_fromDistance, _toDistance,  alpha);
  const double azimuthInRadians  = GMath.lerp(_fromAzimuthInRadians,  _toAzimuthInRadians,  alpha);
  const double altitudeInRadians = GMath.lerp(_fromAltitudeInRadians, _toAltitudeInRadians, alpha);

  rc->getNextCamera()->setPointOfView(center,
                                      distance,
                                      Angle::fromRadians(azimuthInRadians),
                                      Angle::fromRadians(altitudeInRadians));
}

void ShapeOrbitCameraEffect::cancel(const TimeInterval& when) {
  // do nothing
}
