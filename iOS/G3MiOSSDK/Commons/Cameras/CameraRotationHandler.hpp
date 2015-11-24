//
//  CameraRotationHandler.hpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 28/07/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

#ifndef G3MiOSSDK_CameraRotationHandler
#define G3MiOSSDK_CameraRotationHandler

#include "CameraEventHandler.hpp"
#include "Camera.hpp"
#include "MutableVector2F.hpp"

class CameraRotationHandler: public CameraEventHandler {
private:
  MutableVector3D _pivotPoint;    //Initial point at dragging
  MutableVector2F _pivotPixel;  //Initial pixel at start of gesture

//  int _lastYValid;
  MutableVector3D _cameraPosition;
  MutableVector3D _cameraCenter;
  MutableVector3D _cameraUp;
  MutableVector3D _tempCameraPosition;
  MutableVector3D _tempCameraCenter;
  MutableVector3D _tempCameraUp;


public:
  CameraRotationHandler():
  _pivotPoint(0, 0, 0),
  _pivotPixel(0, 0)
  {}
  
  ~CameraRotationHandler() {
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
  
  
  
};



#endif
