//
//  CameraDoubleTapHandler.hpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 07/08/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

#ifndef G3MiOSSDK_CameraDoubleTapHandler_hpp
#define G3MiOSSDK_CameraDoubleTapHandler_hpp

#include "CameraEventHandler.hpp"
#include "Camera.hpp"
#include "Effects.hpp"


//***************************************************************

class DoubleTapEffect : public EffectWithDuration {
public:
  
  DoubleTapEffect(const TimeInterval& duration,
                  const Vector3D& axis,
                  const Angle& angle,
                  double distance,
                  const bool linearTiming=false):
  EffectWithDuration(duration, linearTiming),
  _axis(axis),
  _angle(angle),
  _distance(distance)
  {}
  
  virtual void start(const G3MRenderContext *rc,
                     const TimeInterval& when) {
    EffectWithDuration::start(rc, when);
    _lastAlpha = 0;
  }
  
  virtual void doStep(const G3MRenderContext *rc,
                      const TimeInterval& when) {
    const double alpha = getAlpha(when);
    Camera *camera = rc->getNextCamera();
    const double step = alpha - _lastAlpha;
    camera->rotateWithAxis(_axis, _angle.times(step));
    camera->moveForward(_distance * step);
    _lastAlpha = alpha;
  }
  
  virtual void stop(const G3MRenderContext *rc,
                    const TimeInterval& when) {
    Camera *camera = rc->getNextCamera();

    const double step = 1.0 - _lastAlpha;
    camera->rotateWithAxis(_axis, _angle.times(step));
    camera->moveForward(_distance * step);
  }
  
  virtual void cancel(const TimeInterval& when) {
    // do nothing, just leave the effect in the intermediate state
  }
  
private:
  Vector3D _axis;
  Angle    _angle;
  double   _distance;
  double   _lastAlpha;
};

//***************************************************************


class CameraDoubleTapHandler: public CameraEventHandler {
  
public:
  
  ~CameraDoubleTapHandler() {}
  
  bool onTouchEvent(const G3MEventContext *eventContext,
                    const TouchEvent* touchEvent,
                    CameraContext *cameraContext);
  
  void render(const G3MRenderContext* rc,
              CameraContext *cameraContext) {
    
  }
  
  void onDown(const G3MEventContext *eventContext,
              const TouchEvent& touchEvent,
              CameraContext *cameraContext);
  void onMove(const G3MEventContext *eventContext,
              const TouchEvent& touchEvent,
              CameraContext *cameraContext) {}
  void onUp(const G3MEventContext *eventContext,
            const TouchEvent& touchEvent,
            CameraContext *cameraContext) {}
  
};


#endif
