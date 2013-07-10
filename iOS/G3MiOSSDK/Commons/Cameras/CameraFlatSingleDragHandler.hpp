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
#include "SingleDragEffect.hpp"


class CameraFlatSingleDragHandler: public CameraEventHandler {
  
public:
  CameraFlatSingleDragHandler(bool useInertia):
  _camera0(Camera(0, 0)),
  _initialPoint(0,0,0),
  //  _initialPixel(0,0),
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
  //MutableVector2I _initialPixel;  //Initial pixel at start of gesture
  
  MutableVector3D _axis;
  double          _lastRadians;
  double          _radiansStep;
  
};



#endif
