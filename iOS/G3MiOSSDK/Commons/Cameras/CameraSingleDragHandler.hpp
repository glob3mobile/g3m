//
//  CameraSingleDragHandler.hpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 28/07/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

#ifndef G3MiOSSDK_CameraSingleDragHandler_h
#define G3MiOSSDK_CameraSingleDragHandler_h


#include "CameraEventHandler.hpp"
#include "Camera.hpp"
#include "Effects.hpp"
#include "MutableVector2I.hpp"


class SingleDragEffect : public EffectWithForce {
public:
  
  SingleDragEffect(const Vector3D& axis,
                   const Angle& angle):
  EffectWithForce(angle._degrees, 0.975),
  _axis(axis)
  {
  }
  
  virtual void start(const G3MRenderContext *rc,
                     const TimeInterval& when) {
  }
  
  virtual void doStep(const G3MRenderContext *rc,
                      const TimeInterval& when) {
    EffectWithForce::doStep(rc, when);
    rc->getNextCamera()->rotateWithAxis(_axis, Angle::fromDegrees(getForce()));
  }
  
  virtual void stop(const G3MRenderContext *rc,
                    const TimeInterval& when) {
    rc->getNextCamera()->rotateWithAxis(_axis, Angle::fromDegrees(getForce()));
  }
  
  virtual void cancel(const TimeInterval& when) {
    // do nothing, just leave the effect in the intermediate state
  }
  
private:
  Vector3D _axis;
};


class CameraSingleDragHandler: public CameraEventHandler {
  
public:
  CameraSingleDragHandler(bool useInertia):
  _camera0(Camera(0, 0)),
  _initialPoint(0,0,0),
//  _initialPixel(0,0),
  _useInertia(useInertia)
  {}
  
  ~CameraSingleDragHandler() {
#ifdef JAVA_CODE
  super.dispose();
#endif

  }
  
  
  bool onTouchEvent(const G3MEventContext *eventContext,
                    const TouchEvent* touchEvent,
                    CameraContext *cameraContext);
  
  void render(const G3MRenderContext* rc,
              CameraContext *cameraContext);
  
  const bool _useInertia;
  void onDown(const G3MEventContext *eventContext,
              const TouchEvent& touchEvent,
              CameraContext *cameraContext);
  void onMove(const G3MEventContext *eventContext,
              const TouchEvent& touchEvent,
              CameraContext *cameraContext);
  void onUp(const G3MEventContext *eventContext,
            const TouchEvent& touchEvent,
            CameraContext *cameraContext);
private:
  
  Camera _camera0;         //Initial Camera saved on Down event
  
  MutableVector3D _initialPoint;  //Initial point at dragging
  //MutableVector2I _initialPixel;  //Initial pixel at start of gesture
  
  MutableVector3D _axis;
  double          _lastRadians;
  double          _radiansStep;
  
};


#endif
