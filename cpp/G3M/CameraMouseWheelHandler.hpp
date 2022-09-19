//
//  CameraMouseWheelHandler.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 16/2/21.
//

#ifndef CameraMouseWheelHandler_hpp
#define CameraMouseWheelHandler_hpp

#include "CameraEventHandler.hpp"
#include "RenderState.hpp"

class CameraMouseWheelHandler: public CameraEventHandler {
private:
  double _zoomSpeed;

public:
  CameraMouseWheelHandler(double zoomSpeed = 0.05) :
  CameraEventHandler("MouseWheel"),
  _zoomSpeed(zoomSpeed)
  {
  }

  ~CameraMouseWheelHandler(){}
  
  RenderState getRenderState(const G3MRenderContext* rc){
    return RenderState::ready();
  }

  void render(const G3MRenderContext* rc,
              CameraContext *cameraContext){}

  bool onTouchEvent(const G3MEventContext *eventContext,
                    const TouchEvent* touchEvent,
                    CameraContext *cameraContext);
  
};

#endif
