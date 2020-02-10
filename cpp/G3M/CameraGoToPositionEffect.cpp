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


double CameraGoToPositionEffect::calculateMaxHeight(const Planet* planet) {
  // curve parameters
  const double distanceInDegreesMaxHeight = 180;
  const double maxHeight = planet->getRadii().axisAverage() * 5;


  // rough estimation of distance using lat/lon degrees
  const double deltaLatInDeg = _fromPosition._latitude._degrees  - _toPosition._latitude._degrees;
  const double deltaLonInDeg = _fromPosition._longitude._degrees - _toPosition._longitude._degrees;
  const double distanceInDeg = IMathUtils::instance()->sqrt((deltaLatInDeg * deltaLatInDeg) +
                                                            (deltaLonInDeg * deltaLonInDeg));

  if (distanceInDeg >= distanceInDegreesMaxHeight) {
    return maxHeight;
  }

  const double middleHeight = (distanceInDeg / distanceInDegreesMaxHeight) * maxHeight;

  const double averageHeight = (_fromPosition._height + _toPosition._height) / 2;
  if (middleHeight < averageHeight) {
    const double delta = (averageHeight - middleHeight) / 2.0;
    return averageHeight + delta;
  }

  return middleHeight;
}

void CameraGoToPositionEffect::start(const G3MRenderContext* rc,
                                     const TimeInterval& when) {
  EffectWithDuration::start(rc, when);

  _middleHeight = calculateMaxHeight(rc->getPlanet());
}



void CameraGoToPositionEffect::doStep(const G3MRenderContext* rc,
                                      const TimeInterval& when) {
  const double alpha = getAlpha(when);

  double height;
  if (_linearHeight) {
    height = IMathUtils::instance()->linearInterpolation(_fromPosition._height,
                                                         _toPosition._height,
                                                         alpha);
  }
  else {
    height = IMathUtils::instance()->quadraticBezierInterpolation(_fromPosition._height,
                                                                  _middleHeight,
                                                                  _toPosition._height,
                                                                  alpha);
  }

  Camera *camera = rc->getNextCamera();
  camera->setGeodeticPosition(Angle::linearInterpolation(_fromPosition._latitude,  _toPosition._latitude,  alpha),
                              Angle::linearInterpolation(_fromPosition._longitude, _toPosition._longitude, alpha),
                              height);


  const Angle heading = Angle::linearInterpolation(_fromHeading, _toHeading, alpha);
  camera->setHeading(heading);

  const Angle middlePitch = Angle::_MINUS_HALF_PI;
  //    const Angle pitch =  (alpha < 0.5)
  //    ? Angle::linearInterpolation(_fromPitch, middlePitch, alpha*2)
  //    : Angle::linearInterpolation(middlePitch, _toPitch, (alpha-0.5)*2);

  if (alpha <= 0.1) {
    camera->setPitch( Angle::linearInterpolation(_fromPitch, middlePitch, alpha*10) );
  }
  else if (alpha >= 0.9) {
    camera->setPitch( Angle::linearInterpolation(middlePitch, _toPitch, (alpha-0.9)*10) );
  }
  else {
    camera->setPitch(middlePitch);
  }

}


void CameraGoToPositionEffect::stop(const G3MRenderContext* rc,
                                    const TimeInterval& when) {
  Camera* camera = rc->getNextCamera();
  camera->setGeodeticPosition(_toPosition);
  camera->setPitch(_toPitch);
  camera->setHeading(_toHeading);
}
