//
//  CameraGoToPositionEffect.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 24/10/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_CameraGoToPositionEffect
#define G3MiOSSDK_CameraGoToPositionEffect

#include "Geodetic3D.hpp"


class CameraGoToPositionEffect : public EffectWithDuration {
private:
  const Geodetic3D _fromPosition;
  const Geodetic3D _toPosition;

  const Angle _fromHeading;
  const Angle _toHeading;

  const Angle _fromPitch;
  const Angle _toPitch;

  const bool       _linearHeight;
  double           _middleHeight;


  double calculateMaxHeight(const Planet* planet) {
    // curve parameters
    const double distanceInDegreesMaxHeight = 180;
    const double maxHeight = planet->getRadii().axisAverage() * 5;


    // rough estimation of distance using lat/lon degrees
    const double deltaLatInDeg = _fromPosition.latitude()._degrees  - _toPosition.latitude()._degrees;
    const double deltaLonInDeg = _fromPosition.longitude()._degrees - _toPosition.longitude()._degrees;
    const double distanceInDeg = IMathUtils::instance()->sqrt((deltaLatInDeg * deltaLatInDeg) +
                                                              (deltaLonInDeg * deltaLonInDeg));

    if (distanceInDeg >= distanceInDegreesMaxHeight) {
      return maxHeight;
    }

    const double middleHeight = (distanceInDeg / distanceInDegreesMaxHeight) * maxHeight;

    const double averageHeight = (_fromPosition.height() + _toPosition.height()) / 2;
    if (middleHeight < averageHeight) {
      return averageHeight;
    }
    /*
    const double averageHeight = (_fromPosition.height() + _toPosition.height()) / 2;
    if (middleHeight < averageHeight) {
      return (averageHeight + middleHeight) / 2.0;
    }
     */

    return middleHeight;
  }


public:

  CameraGoToPositionEffect(const TimeInterval& duration,
                           const Geodetic3D& fromPosition,
                           const Geodetic3D& toPosition,
                           const Angle& fromHeading,
                           const Angle& toHeading,
                           const Angle& fromPitch,
                           const Angle& toPitch,
                           const bool linearTiming,
                           const bool linearHeight):
  EffectWithDuration(duration, linearTiming),
  _fromPosition(fromPosition),
  _toPosition(toPosition),
  _fromHeading(fromHeading),
  _toHeading(toHeading),
  _fromPitch(fromPitch),
  _toPitch(toPitch),
  _linearHeight(linearHeight)
  {
  }

  virtual void doStep(const G3MRenderContext *rc,
                      const TimeInterval& when) {
    const double alpha = getAlpha(when);

    double height;
    if (_linearHeight) {
      height = IMathUtils::instance()->linearInterpolation(_fromPosition.height(),
                                                           _toPosition.height(),
                                                           alpha);
    }
    else {
      height = IMathUtils::instance()->quadraticBezierInterpolation(_fromPosition.height(),
                                                                    _middleHeight,
                                                                    _toPosition.height(),
                                                                    alpha);
    }

    Camera *camera = rc->getNextCamera();
    camera->orbitTo(Angle::linearInterpolation(_fromPosition.latitude(),  _toPosition.latitude(),  alpha),
                    Angle::linearInterpolation(_fromPosition.longitude(), _toPosition.longitude(), alpha),
                    height);


    const Angle heading = Angle::linearInterpolation(_fromHeading, _toHeading, alpha);
    camera->setHeading(heading);

    const Angle middlePitch = Angle::fromDegrees(0);
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

  virtual void stop(const G3MRenderContext *rc,
                    const TimeInterval& when) {
    rc->getNextCamera()->orbitTo(_toPosition);
  }

  virtual void cancel(const TimeInterval& when) {
    // do nothing, just leave the effect in the intermediate state
  }

  virtual void start(const G3MRenderContext *rc,
                     const TimeInterval& when) {
    EffectWithDuration::start(rc, when);

    _middleHeight = calculateMaxHeight(rc->getPlanet());
  }
};

#endif
