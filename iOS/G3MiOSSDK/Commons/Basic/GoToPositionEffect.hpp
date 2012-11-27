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
  
  virtual void start(const G3MRenderContext *rc,
                     const TimeInterval& when) {
    EffectWithDuration::start(rc, when);
  }
  
  virtual void doStep(const G3MRenderContext *rc,
                      const TimeInterval& when) {
    //const double percent = gently(percentDone(when), 0.2, 0.9);
    //const double percent = pace( percentDone(when) );
    const double percent = percentDone(when);
    Camera *camera = rc->getNextCamera();
    
    Geodetic3D g = Geodetic3D::interpolation(_initialPos, _finalPos, percent);
    
    //printf("EFFECT %f - %f, %f, %f\n", percent, g.latitude()._degrees, g.longitude()._degrees, g.height());
    //camera->setPosition(g);
    camera->orbitTo(g);
  }
  
  virtual void stop(const G3MRenderContext *rc,
                    const TimeInterval& when) {
    EffectWithDuration::stop(rc, when);
  }
  
  virtual void cancel(const TimeInterval& when) {
    // do nothing, just leave the effect in the intermediate state
  }
  
private:
  Geodetic3D  _initialPos;
  Geodetic3D  _finalPos;
};


#endif
