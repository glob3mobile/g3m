//
//  CameraDoubleDragHandler.hpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 28/07/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

#ifndef G3MiOSSDK_CameraDoubleDragHandler_h
#define G3MiOSSDK_CameraDoubleDragHandler_h


#include "CameraEventHandler.hpp"
#include "Camera.hpp"


class CameraDoubleDragHandler: public CameraEventHandler {
private:
  const bool _processRotation;
  const bool _processZoom;
  
public:
  CameraDoubleDragHandler(bool processRotation,
                          bool processZoom):
  _camera0(Camera(0, 0)),
  _initialPoint(0,0,0),
  _initialPixel(0,0,0),
  _processRotation(processRotation),
  _processZoom(processZoom)
  {
  }
  
  ~CameraDoubleDragHandler() {
    JAVA_POST_DISPOSE
  }
  
  
  bool onTouchEvent(const G3MEventContext *eventContext,
                    const TouchEvent* touchEvent,
                    CameraContext *cameraContext);
  
  void render(const G3MRenderContext* rc,
              CameraContext *cameraContext);
  
  void onDown(const G3MEventContext *eventContext,
              const TouchEvent& touchEvent,
              CameraContext *cameraContext);
  void onMove(const G3MEventContext *eventContext,
              const TouchEvent& touchEvent,
              CameraContext *cameraContext);
  void onUp(const G3MEventContext *eventContext,
            const TouchEvent& touchEvent,
            CameraContext *cameraContext);
  
  MutableVector3D _initialPoint;  //Initial point at dragging
  MutableVector3D _initialPixel;  //Initial pixel at start of gesture
  MutableVector3D _initialPoint0, _initialPoint1;
  double _initialFingerSeparation;
  double _initialFingerInclination;
  
  Camera _camera0;         //Initial Camera saved on Down event
  
};

#endif
