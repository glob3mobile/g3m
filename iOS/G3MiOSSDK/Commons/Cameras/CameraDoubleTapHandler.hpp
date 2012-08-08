//
//  CameraDoubleTapHandler.hpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 07/08/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

#ifndef G3MiOSSDK_CameraDoubleTapHandler_hpp
#define G3MiOSSDK_CameraDoubleTapHandler_hpp

#include "CameraEventHandler.hpp"
#include "Camera.hpp"


class CameraDoubleTapHandler: public CameraEventHandler {
  
public:
  CameraDoubleTapHandler():
  _camera0(Camera(NULL, 0, 0)),
  _initialPoint(0,0,0),
  _initialPixel(0,0,0)
  {}
  
  ~CameraDoubleTapHandler() {}
  
  
  bool onTouchEvent(const EventContext *eventContext,
                    const TouchEvent* touchEvent, 
                    CameraContext *cameraContext);
  int render(const RenderContext* rc, CameraContext *cameraContext) {
    return MAX_TIME_TO_RENDER;
  }

  
private:
  void onDown(const EventContext *eventContext,
              const TouchEvent& touchEvent, 
              CameraContext *cameraContext);
  void onMove(const EventContext *eventContext,
              const TouchEvent& touchEvent, 
              CameraContext *cameraContext) {}
  void onUp(const EventContext *eventContext,
            const TouchEvent& touchEvent, 
            CameraContext *cameraContext) {}
  
  
  Camera _camera0;         //Initial Camera saved on Down event
  
  MutableVector3D _initialPoint;  //Initial point at dragging
  MutableVector3D _initialPixel;  //Initial pixel at start of gesture
  
};


#endif
