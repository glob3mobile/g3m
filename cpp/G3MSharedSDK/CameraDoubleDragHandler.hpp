//
//  CameraDoubleDragHandler.hpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 28/07/12.
//

#ifndef G3MiOSSDK_CameraDoubleDragHandler
#define G3MiOSSDK_CameraDoubleDragHandler


#include "CameraEventHandler.hpp"

#include "MutableVector3D.hpp"
#include "MutableVector2I.hpp"
#include "MutableMatrix44D.hpp"


class CameraDoubleDragHandler: public CameraEventHandler {
private:
  MutableVector3D  _cameraPosition;
  MutableVector3D  _cameraCenter;
  MutableVector3D  _cameraUp;
  MutableVector2I  _cameraViewPort;
  MutableMatrix44D _cameraModelViewMatrix;


public:
  CameraDoubleDragHandler() {
  }

  ~CameraDoubleDragHandler() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

  RenderState getRenderState(const G3MRenderContext* rc);

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
  
};

#endif
