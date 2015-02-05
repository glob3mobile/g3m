//
//  CameraSingleDragHandler.hpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 28/07/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

#ifndef G3MiOSSDK_CameraSingleDragHandler
#define G3MiOSSDK_CameraSingleDragHandler


#include "CameraEventHandler.hpp"
#include "Camera.hpp"
#include "Effects.hpp"
#include "MutableVector2I.hpp"



class CameraSingleDragHandler: public CameraEventHandler {
  
public:
  CameraSingleDragHandler(bool useInertia):
  _camera0(Camera()),
//  _initialPoint(0,0,0),
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
};


#endif
