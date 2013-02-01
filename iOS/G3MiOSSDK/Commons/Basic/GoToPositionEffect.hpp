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
                     const Geodetic3D& initialPos,
                     const Geodetic3D& finalPos):
  EffectWithDuration(duration),
  _initialPos(initialPos),
  _finalPos(finalPos)
  {
  }

//  virtual void start(const G3MRenderContext *rc,
//                     const TimeInterval& when) {
//    EffectWithDuration::start(rc, when);
//  }

  virtual void doStep(const G3MRenderContext *rc,
                      const TimeInterval& when) {
    //const double percent = pace( percentDone(when) );
    const double percent = percentDone(when);
    Camera *camera = rc->getNextCamera();

    const Geodetic3D g = Geodetic3D::interpolation(_initialPos, _finalPos, percent);

    //camera->setPosition(g);
    camera->orbitTo(g);
  }

  virtual void stop(const G3MRenderContext *rc,
                    const TimeInterval& when) {
    rc->getNextCamera()->orbitTo(_finalPos);
    EffectWithDuration::stop(rc, when);
  }

  virtual void cancel(const TimeInterval& when) {
    // do nothing, just leave the effect in the intermediate state
  }

private:
  const Geodetic3D _initialPos;
  const Geodetic3D _finalPos;
};


#endif
