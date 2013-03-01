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
  const Geodetic3D _initialPos;
  const Geodetic3D _finalPos;

  const bool       _linearHeight;
  double           _middleHeight;


  double calculateMaxHeight(const Planet* planet) {
    // curve parameters
    const double distanceInDegreesMaxHeight = 180;
    const double maxHeight = planet->getRadii().axisAverage();


    // rough estimation of distance using lat/lon degrees
    const double deltaLatInDeg = _initialPos.latitude()._degrees  - _finalPos.latitude()._degrees;
    const double deltaLonInDeg = _initialPos.longitude()._degrees - _finalPos.longitude()._degrees;
    const double distanceInDeg = IMathUtils::instance()->sqrt((deltaLatInDeg * deltaLatInDeg) +
                                                              (deltaLonInDeg * deltaLonInDeg)  );

    if (distanceInDeg >= distanceInDegreesMaxHeight) {
      return maxHeight;
    }

    const double middleHeight = (distanceInDeg / distanceInDegreesMaxHeight) * maxHeight;

    /*
    const double averageHeight = (_initialPos.height() + _finalPos.height()) / 2;
    if (middleHeight < averageHeight) {
      return averageHeight;
    }
    */
    const double averageHeight = (_initialPos.height() + _finalPos.height()) / 2;
    if (middleHeight < averageHeight) {
      return (averageHeight + middleHeight) / 2.0;
    }

    return middleHeight;
  }


public:

  CameraGoToPositionEffect(const TimeInterval& duration,
                           const Geodetic3D& initialPos,
                           const Geodetic3D& finalPos,
                           const bool linearTiming=false,
                           const bool linearHeight=false):
  EffectWithDuration(duration, linearTiming),
  _initialPos(initialPos),
  _finalPos(finalPos),
  _linearHeight(linearHeight)
  {
  }

  virtual void doStep(const G3MRenderContext *rc,
                      const TimeInterval& when) {
    const double alpha = getAlpha(when);

    double height;
    if (_linearHeight) {
      height = IMathUtils::instance()->linearInterpolation(_initialPos.height(),
                                                           _finalPos.height(),
                                                           alpha);
    }
    else {
      height = IMathUtils::instance()->quadraticBezierInterpolation(_initialPos.height(),
                                                                    _middleHeight,
                                                                    _finalPos.height(),
                                                                    alpha);
    }

    Camera *camera = rc->getNextCamera();
    camera->orbitTo(Angle::linearInterpolation(_initialPos.latitude(),  _finalPos.latitude(),  alpha),
                    Angle::linearInterpolation(_initialPos.longitude(), _finalPos.longitude(), alpha),
                    height);
  }

  virtual void stop(const G3MRenderContext *rc,
                    const TimeInterval& when) {
    rc->getNextCamera()->orbitTo(_finalPos);
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
