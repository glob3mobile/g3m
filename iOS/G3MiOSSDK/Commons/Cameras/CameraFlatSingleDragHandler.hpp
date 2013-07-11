//
//  CameraFlatSingleDragHandler.hpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo on 10/07/13.
//
//

#ifndef G3MiOSSDK_CameraFlatSingleDragHandler_hpp
#define G3MiOSSDK_CameraFlatSingleDragHandler_hpp

#include "CameraEventHandler.hpp"
#include "Camera.hpp"
#include "Effects.hpp"
#include "MutableVector2I.hpp"


class SingleTranslationEffect : public EffectWithForce {
public:
  
  SingleTranslationEffect(const Vector3D& desp):
  EffectWithForce(1, 0.92),
  _direction(desp){
  }
  
  virtual void start(const G3MRenderContext *rc,
                     const TimeInterval& when) {
  }
  
  virtual void doStep(const G3MRenderContext *rc,
                      const TimeInterval& when) {
    EffectWithForce::doStep(rc, when);
    rc->getNextCamera()->translateCamera(_direction.times(getForce()));
  }
  
  virtual void stop(const G3MRenderContext *rc,
                    const TimeInterval& when) {
    rc->getNextCamera()->translateCamera(_direction.times(getForce()));
  }
  
  virtual void cancel(const TimeInterval& when) {
    // do nothing, just leave the effect in the intermediate state
  }
  
private:
  Vector3D _direction;
};



class CameraFlatSingleDragHandler: public CameraEventHandler {
  
public:
  CameraFlatSingleDragHandler(bool useInertia):
  _camera0(Camera(0, 0)),
  _initialPoint(0,0,0),
  _useInertia(useInertia)
  {}
  
  ~CameraFlatSingleDragHandler() {}
  
  
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
  
  double          _lastDesp;
  double          _despStep;
  MutableVector3D _lastDirection;
  MutableVector3D _lastFinalPoint;
  
};



#endif
