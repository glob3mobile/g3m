//
//  CameraZoomAndRotateHandler.hpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo on 26/06/13.
//
//

#ifndef __G3MiOSSDK__CameraZoomAndRotateHandler__
#define __G3MiOSSDK__CameraZoomAndRotateHandler__


#include "CameraEventHandler.hpp"
#include "Camera.hpp"
#include "MutableVector2F.hpp"

class CameraZoomAndRotateHandler: public CameraEventHandler {
private:  
  double _fingerSep0;
  double _lastAngle;
  double _angle0;
  
  MutableVector3D _centralGlobePoint;
  MutableVector3D _centralGlobeNormal;
  
  void zoom(Camera* camera, const Vector2F& difCurrentPixels);
  void rotate();
  
  
public:
  CameraZoomAndRotateHandler()
  {}
  
  ~CameraZoomAndRotateHandler() {
#ifdef JAVA_CODE
  super.dispose();
#endif

  }
  
  
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
  
  //MutableVector3D _initialPoint;  //Initial point at dragging
  MutableVector2F _initialPixel0, _initialPixel1;  //Initial pixels at start of gesture
  //MutableVector3D _initialPoint0, _initialPoint1;
  double _initialFingerSeparation;
  double _initialFingerInclination;
  
  MutableVector3D _cameraPosition;
  MutableVector3D _cameraCenter;
  MutableVector3D _cameraUp;

};


#endif
