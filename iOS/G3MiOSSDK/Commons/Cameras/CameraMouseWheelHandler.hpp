//
//  CameraMouseWheelHandler.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 10/10/14.
//
//

#ifndef __G3MiOSSDK__CameraMouseWheelHandler__
#define __G3MiOSSDK__CameraMouseWheelHandler__

#include "CameraEventHandler.hpp"

class CameraMouseWheelHandler : public CameraEventHandler {
  
public:
  CameraMouseWheelHandler()
  {}
  
  ~CameraMouseWheelHandler() {
#ifdef JAVA_CODE
    super.dispose();
#endif
    
  }
  
  bool onTouchEvent(const G3MEventContext *eventContext,
                    const TouchEvent* touchEvent,
                    CameraContext *cameraContext);
  
  void render(const G3MRenderContext* rc,
              CameraContext *cameraContext){}
  
  void onDown(const G3MEventContext *eventContext,
              const TouchEvent& touchEvent,
              CameraContext *cameraContext){}
  
  void onMove(const G3MEventContext *eventContext,
              const TouchEvent& touchEvent,
              CameraContext *cameraContext){}
  
  void onUp(const G3MEventContext *eventContext,
            const TouchEvent& touchEvent,
            CameraContext *cameraContext){}
  
  void onMouseWheel(const G3MEventContext *eventContext,
                    const TouchEvent& touchEvent,
                    CameraContext *cameraContext);
  
  
  
};

#endif /* defined(__G3MiOSSDK__CameraMouseWheelHandler__) */
