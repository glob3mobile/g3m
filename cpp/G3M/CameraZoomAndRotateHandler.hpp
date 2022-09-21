//
//  CameraZoomAndRotateHandler.hpp
//  G3M
//
//  Created by Agustin Trujillo on 26/06/13.
//
//

#ifndef __G3M__CameraZoomAndRotateHandler__
#define __G3M__CameraZoomAndRotateHandler__


#include "CameraEventHandler.hpp"

#include "MutableVector3D.hpp"
#include "MutableVector2F.hpp"

class Camera;


class CameraZoomAndRotateHandler: public CameraEventHandler {
private:
  double _fingerSep0;
  double _lastAngle;
  double _angle0;

  MutableVector3D _centralGlobePoint;
  MutableVector3D _centralGlobeNormal;

  MutableVector2F _initialPixel0, _initialPixel1;  //Initial pixels at start of gesture

  MutableVector3D _cameraPosition;
  MutableVector3D _cameraCenter;
  MutableVector3D _cameraUp;

  void zoom(Camera* camera, const Vector2F& difCurrentPixels);

  void rotate();

  void onDown(const G3MEventContext *eventContext,
              const TouchEvent& touchEvent,
              CameraContext *cameraContext);
  void onMove(const G3MEventContext *eventContext,
              const TouchEvent& touchEvent,
              CameraContext *cameraContext);
  void onUp(const G3MEventContext *eventContext,
            const TouchEvent& touchEvent,
            CameraContext *cameraContext);

public:
  CameraZoomAndRotateHandler() :
  CameraEventHandler("ZoomAndRotate")
  {
  }

  ~CameraZoomAndRotateHandler() {
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
