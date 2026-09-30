//
//  CameraGoToPositionEffect.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 11/7/16.
//
//

#include "CameraGoToPositionEffect.hpp"

#include "IMathUtils.hpp"
#include "Planet.hpp"
#include "Vector3D.hpp"
#include "G3MRenderContext.hpp"
#include "Camera.hpp"
#include "CameraFlightArc.hpp"


CameraGoToPositionEffect::~CameraGoToPositionEffect() {
  delete _arc;
}

double CameraGoToPositionEffect::horizonDepressionRadians(const double height) const {
  if ((_planetRadius <= 0) || (height <= 0)) {
    return 0;
  }
  return IMathUtils::instance()->acos(_planetRadius / (_planetRadius + height));
}

double CameraGoToPositionEffect::angleBelowHorizonRadians(const Angle& pitch,
                                                          const double height) const {
  return -pitch._radians - horizonDepressionRadians(height);
}

void CameraGoToPositionEffect::start(const G3MRenderContext* rc,
                                     const TimeInterval& when) {
  EffectWithDuration::start(rc, when);

  const Planet* planet = rc->getPlanet();
  delete _arc;
  _arc = new CameraFlightArc(planet, _fromPosition, _toPosition, _fromPosition._height, _toPosition._height);
  _planetRadius = planet->isFlat() ? 0 : planet->getRadii().axisAverage();

  _fromAngleBelowHorizonRadians = angleBelowHorizonRadians(_fromPitch, _fromPosition._height);
  _toAngleBelowHorizonRadians   = angleBelowHorizonRadians(_toPitch,   _toPosition._height);
}



void CameraGoToPositionEffect::doStep(const G3MRenderContext* rc,
                                      const TimeInterval& when) {
  const double alpha = getAlpha(when);

  double pan;
  double height;
  if (_linearHeight) {
    pan    = alpha;
    height = IMathUtils::instance()->linearInterpolation(_fromPosition._height,
                                                         _toPosition._height,
                                                         alpha);
  }
  else {
    pan    = _arc->panAt(alpha);
    height = _arc->valueAt(alpha);
  }

  Camera *camera = rc->getNextCamera();
  camera->setGeodeticPosition(Angle::linearInterpolation(_fromPosition._latitude,  _toPosition._latitude,  pan),
                              Angle::linearInterpolation(_fromPosition._longitude, _toPosition._longitude, pan),
                              height);


  const Angle heading = Angle::linearInterpolation(_fromHeading, _toHeading, alpha);
  camera->setHeading(heading);

  // the horizon keeps its place on screen while the height changes; beyond the nadir the camera would look backwards
  const double belowHorizonRadians = IMathUtils::instance()->linearInterpolation(_fromAngleBelowHorizonRadians,
                                                                                 _toAngleBelowHorizonRadians,
                                                                                 alpha);
  const double pitchRadians = -(horizonDepressionRadians(height) + belowHorizonRadians);
  camera->setPitch( Angle::fromRadians( IMathUtils::instance()->max(pitchRadians, Angle::_MINUS_HALF_PI._radians) ) );
}


void CameraGoToPositionEffect::stop(const G3MRenderContext* rc,
                                    const TimeInterval& when) {
  Camera* camera = rc->getNextCamera();
  camera->setGeodeticPosition(_toPosition);
  camera->setPitch(_toPitch);
  camera->setHeading(_toHeading);
}
