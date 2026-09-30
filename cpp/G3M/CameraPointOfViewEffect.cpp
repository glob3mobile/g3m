//
//  CameraPointOfViewEffect.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 9/30/26.
//

#include "CameraPointOfViewEffect.hpp"

#include "IMathUtils.hpp"
#include "G3MRenderContext.hpp"
#include "Camera.hpp"
#include "CameraFlightArc.hpp"


CameraPointOfViewEffect::~CameraPointOfViewEffect() {
  delete _arc;
}

Geodetic3D CameraPointOfViewEffect::targetAt(const double pan) const {
  return Geodetic3D(Angle::linearInterpolation(_fromTarget._latitude,  _toTarget._latitude,  pan),
                    Angle::linearInterpolation(_fromTarget._longitude, _toTarget._longitude, pan),
                    IMathUtils::instance()->linearInterpolation(_fromTarget._height, _toTarget._height, pan));
}

double CameraPointOfViewEffect::distanceAt(const double alpha) const {
  if (_linearDistance) {
    return IMathUtils::instance()->linearInterpolation(_fromDistance, _toDistance, alpha);
  }
  return _arc->valueAt(alpha);
}

void CameraPointOfViewEffect::start(const G3MRenderContext* rc,
                                    const TimeInterval& when) {
  EffectWithDuration::start(rc, when);

  delete _arc;
  _arc = new CameraFlightArc(rc->getPlanet(), _fromTarget, _toTarget, _fromDistance, _toDistance);
}

void CameraPointOfViewEffect::doStep(const G3MRenderContext* rc,
                                     const TimeInterval& when) {
  const double alpha = getAlpha(when);
  const double pan   = _linearDistance ? alpha : _arc->panAt(alpha);

  rc->getNextCamera()->setPointOfView(targetAt(pan),
                                      distanceAt(alpha),
                                      Angle::linearInterpolation(_fromAzimuth,  _toAzimuth,  alpha),
                                      Angle::linearInterpolation(_fromAltitude, _toAltitude, alpha));
}

void CameraPointOfViewEffect::stop(const G3MRenderContext* rc,
                                   const TimeInterval& when) {
  rc->getNextCamera()->setPointOfView(_toTarget,
                                      _toDistance,
                                      _toAzimuth,
                                      _toAltitude);
}
