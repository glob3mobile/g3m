//
//  CameraEventHandler.hpp
//  G3M
//
//  Created by Agustin Trujillo Pino on 07/08/12.
//

#ifndef G3M_CameraEventHandler
#define G3M_CameraEventHandler

class RenderState;
class G3MRenderContext;
class CameraContext;
class G3MEventContext;
class TouchEvent;


class CameraEventHandler {
protected:
  CameraEventHandler() {

  }

public:

  virtual ~CameraEventHandler() {

  }

  virtual RenderState getRenderState(const G3MRenderContext* rc) = 0;

  virtual void render(const G3MRenderContext* rc,
                      CameraContext *cameraContext) = 0;

  virtual bool onTouchEvent(const G3MEventContext *eventContext,
                            const TouchEvent* touchEvent,
                            CameraContext *cameraContext) = 0;

};

#endif
