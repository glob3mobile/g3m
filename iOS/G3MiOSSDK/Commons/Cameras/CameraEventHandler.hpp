//
//  CameraEventHandler.hpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 07/08/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

#ifndef G3MiOSSDK_CameraEventHandler
#define G3MiOSSDK_CameraEventHandler

#include "CameraRenderer.hpp"

class TouchEvent;
class G3MRenderContext;
class CameraContext;

class CameraEventHandler {
  
public:
  virtual bool onTouchEvent(const G3MEventContext *eventContext,
                            const TouchEvent* touchEvent,
                            CameraContext *cameraContext) = 0;
  
  virtual void render(const G3MRenderContext* rc,
                      CameraContext *cameraContext) = 0;
  
  virtual ~CameraEventHandler() {

  }
  
  virtual void onDown(const G3MEventContext *eventContext,
                      const TouchEvent& touchEvent,
                      CameraContext *cameraContext) = 0;
  
  virtual void onMove(const G3MEventContext *eventContext,
                      const TouchEvent& touchEvent,
                      CameraContext *cameraContext) = 0;
  
  virtual void onUp(const G3MEventContext *eventContext,
                    const TouchEvent& touchEvent,
                    CameraContext *cameraContext) = 0;
  
};

#endif
