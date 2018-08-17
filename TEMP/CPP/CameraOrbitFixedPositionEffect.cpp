//
//  CameraOrbitFixedPositionEffect.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel S N on 27/07/2018.
//

#include "CameraOrbitFixedPositionEffect.hpp"


#include "Shape.hpp"
#include "Camera.hpp"

void CameraOrbitFixedPositionEffect::doStep(const G3MRenderContext* rc,
                                    const TimeInterval& when) {
    const double alpha = getAlpha(when);
    
    const IMathUtils* mu = IMathUtils::instance();
    const double distance          = mu->linearInterpolation(_fromDistance,          _toDistance,          alpha);
    const double azimuthInRadians  = mu->linearInterpolation(_fromAzimuthInRadians,  _toAzimuthInRadians,  alpha);
    const double altitudeInRadians = mu->linearInterpolation(_fromAltitudeInRadians, _toAltitudeInRadians, alpha);
    
    rc->getNextCamera()->setPointOfView(_pivot,
                                        distance,
                                        Angle::fromRadians(azimuthInRadians),
                                        Angle::fromRadians(altitudeInRadians));
}

void CameraOrbitFixedPositionEffect::cancel(const TimeInterval& when) {
    // do nothing
}

void CameraOrbitFixedPositionEffect::stop(const G3MRenderContext* rc,
                                  const TimeInterval& when) {
    rc->getNextCamera()->setPointOfView(_pivot,
                                        _toDistance,
                                        Angle::fromRadians(_toAzimuthInRadians),
                                        Angle::fromRadians(_toAltitudeInRadians));
    
}
