//
//  CameraMouseWheelHandler.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 16/2/21.
//

#ifndef CameraMouseWheelHandler_hpp
#define CameraMouseWheelHandler_hpp

#include <stdio.h>

#include "CameraEventHandler.hpp"
#include "RenderState.hpp"

class CameraMouseWheelHandler: public CameraEventHandler {
private:
  double _zoomSpeed;
  
public:
  CameraMouseWheelHandler(double zoomSpeed = 0.05): _zoomSpeed(zoomSpeed){}
  
  ~CameraMouseWheelHandler(){}
  
  RenderState getRenderState(const G3MRenderContext* rc){
    return RenderState::ready();
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
};


#endif /* CameraMouseWheelHandler_hpp */
