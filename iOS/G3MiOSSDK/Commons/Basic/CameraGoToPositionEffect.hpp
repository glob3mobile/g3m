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
  const Geodetic3D  _initialPos;
  const Geodetic3D  _finalPos;
  
  const bool        _linearHeight;
  double            _maxHeight;
      
  void calculateMaxHeight() {
    Angle deltaLat = Angle::fromDegrees(_initialPos.latitude()._degrees).sub(Angle::fromDegrees(_finalPos.latitude()._degrees));
    Angle deltaLon = Angle::fromDegrees(_initialPos.longitude()._degrees).sub(Angle::fromDegrees(_finalPos.longitude()._degrees));
    
    const double distance2D = IMathUtils::instance()->sqrt(IMathUtils::instance()->pow(deltaLat._degrees, 2) +
                                                           IMathUtils::instance()->pow(deltaLon._degrees, 2));
    
    _maxHeight = (((_initialPos.height() + _finalPos.height()) / 2) * distance2D);
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
    Camera *camera = rc->getNextCamera();

    double height;
    if (_linearHeight) {
      height = IMathUtils::instance()->linearInterpolation(_initialPos.height(),
                                                           _finalPos.height(),
                                                           alpha);
    }
    else {
      height = IMathUtils::instance()->quadraticBezierInterpolation(_initialPos.height(),
                                                                    _maxHeight,
                                                                    _finalPos.height(),
                                                                    alpha);
    }

    const Geodetic3D g = Geodetic3D(Angle::linearInterpolation(_initialPos.latitude(),
                                                               _finalPos.latitude(),
                                                               alpha),
                                    Angle::linearInterpolation(_initialPos.longitude(),
                                                               _finalPos.longitude(),
                                                               alpha),
                                    height);
    
    camera->orbitTo(g);
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
    
    calculateMaxHeight();
  }
};

#endif
