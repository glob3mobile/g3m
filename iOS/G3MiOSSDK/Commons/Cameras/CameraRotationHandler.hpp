//
//  CameraRotationHandler.hpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 28/07/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

#ifndef G3MiOSSDK_CameraRotationHandlerr_h
#define G3MiOSSDK_CameraRotationHandler_h

#include "CameraEventHandler.hpp"
#include "Camera.hpp"


class CameraRotationHandler: public CameraEventHandler {
  
public:
  CameraRotationHandler():
  _camera0(Camera(NULL, 0, 0)),
  _initialPoint(0,0,0),
  _initialPixel(0,0,0) 
  {}
  
  ~CameraRotationHandler() {}

  bool onTouchEvent(const TouchEvent* touchEvent, Gesture &gesture);
  int render(const RenderContext* rc, Gesture &gesture);
  
private:
  void onDown(const TouchEvent& touchEvent, Gesture &gesture);
  void onMove(const TouchEvent& touchEvent, Gesture &gesture);
  void onUp(const TouchEvent& touchEvent, Gesture &gesture);
  
  double lastYValid;
  const Planet* _planet;
  GL *_gl;
  Camera _camera0;         //Initial Camera saved on Down event
  Camera* _camera;         // Camera used at current frame
  
  MutableVector3D _initialPoint;  //Initial point at dragging
  MutableVector3D _initialPixel;  //Initial pixel at start of gesture
    
};



#endif
