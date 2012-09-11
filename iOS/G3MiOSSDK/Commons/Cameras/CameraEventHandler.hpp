//
//  CameraEventHandler.hpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 07/08/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

#ifndef G3MiOSSDK_CameraEventHandler_hpp
#define G3MiOSSDK_CameraEventHandler_hpp

#include "CameraRenderer.hpp"

class TouchEvent;
class RenderContext;
class CameraContext;

class CameraEventHandler {
  
public:
  virtual bool onTouchEvent(const EventContext *eventContext,
                            const TouchEvent* touchEvent,
                            CameraContext *cameraContext) = 0;
  
  virtual void render(const RenderContext* rc,
                      CameraContext *cameraContext) = 0;
  
  virtual ~CameraEventHandler() {}
  
  virtual void onDown(const EventContext *eventContext,
                      const TouchEvent& touchEvent,
                      CameraContext *cameraContext) = 0;
  
  virtual void onMove(const EventContext *eventContext,
                      const TouchEvent& touchEvent,
                      CameraContext *cameraContext) = 0;
  
  virtual void onUp(const EventContext *eventContext,
                    const TouchEvent& touchEvent,
                    CameraContext *cameraContext) = 0;
  
};

#endif
