//
//  GoToPositionEffect.h
//  G3MiOSSDK
//
//  Created by José Miguel S N on 24/10/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_GoToPositionEffect_h
#define G3MiOSSDK_GoToPositionEffect_h

#include "Geodetic3D.hpp"


class GoToPositionEffect : public EffectWithDuration {
public:
  
  GoToPositionEffect(const TimeInterval& duration,
                     Geodetic3D initialPos,
                     Geodetic3D finalPos):
  EffectWithDuration(duration),
  _initialPos(initialPos),
  _finalPos(finalPos)
  {}
  
  virtual void start(const RenderContext *rc,
                     const TimeInterval& now) {
    EffectWithDuration::start(rc, now);
  }
  
  virtual void doStep(const RenderContext *rc,
                      const TimeInterval& now) {
    //const double percent = gently(percentDone(now), 0.2, 0.9);
    //const double percent = pace( percentDone(now) );
    const double percent = percentDone(now);
    Camera *camera = rc->getNextCamera();
    
    Geodetic3D g = Geodetic3D::interpolation(_initialPos, _finalPos, percent);
    
    //printf("EFFECT %f - %f, %f, %f\n", percent, g.latitude()._degrees, g.longitude()._degrees, g.height());
    //camera->setPosition(g);
    camera->orbitTo(g);
  }
  
  virtual void stop(const RenderContext *rc,
                    const TimeInterval& now) {
    EffectWithDuration::stop(rc, now);
  }
  
  virtual void cancel(const TimeInterval& now) {
    // do nothing, just leave the effect in the intermediate state
  }
  
private:
  Geodetic3D  _initialPos;
  Geodetic3D  _finalPos;
};


#endif
