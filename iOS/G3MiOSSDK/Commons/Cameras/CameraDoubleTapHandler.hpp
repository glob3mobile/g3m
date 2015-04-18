//
//  CameraDoubleTapHandler.hpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 07/08/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

#ifndef G3MiOSSDK_CameraDoubleTapHandler
#define G3MiOSSDK_CameraDoubleTapHandler

#include "CameraEventHandler.hpp"
#include "Camera.hpp"
#include "Effects.hpp"


class CameraDoubleTapHandler: public CameraEventHandler {
  
public:
  
  ~CameraDoubleTapHandler() {
#ifdef JAVA_CODE
  super.dispose();
#endif

  }
  
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
