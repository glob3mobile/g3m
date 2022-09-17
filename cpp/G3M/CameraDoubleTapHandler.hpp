//
//  CameraDoubleTapHandler.hpp
//  G3M
//
//  Created by Agustin Trujillo Pino on 07/08/12.
//

#ifndef G3M_CameraDoubleTapHandler
#define G3M_CameraDoubleTapHandler

#include "CameraEventHandler.hpp"


class CameraDoubleTapHandler: public CameraEventHandler {
private:
  void onDown(const G3MEventContext *eventContext,
              const TouchEvent& touchEvent,
              CameraContext *cameraContext);
  
public:
  
  ~CameraDoubleTapHandler() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }
  
  RenderState getRenderState(const G3MRenderContext* rc);
  
  bool onTouchEvent(const G3MEventContext *eventContext,
                    const TouchEvent* touchEvent,
                    CameraContext *cameraContext);
  
  void render(const G3MRenderContext* rc,
              CameraContext *cameraContext) {
    
  }
  
};

#endif
