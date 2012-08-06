//
//  CameraSimpleDragRenderer.h
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 28/07/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

#ifndef G3MiOSSDK_CameraSimpleDragRenderer_h
#define G3MiOSSDK_CameraSimpleDragRenderer_h


#include "CameraRenderer.h"


class CameraSimpleDragRenderer: public CameraRenderer {
  
public:
  CameraSimpleDragRenderer():
  _camera0(Camera(NULL, 0, 0)) 
  {}

  bool onTouchEvent(const TouchEvent* touchEvent);
  int render(const RenderContext* rc);
  void initialize(const InitializationContext* ic) {}  
  void onResizeViewportEvent(int width, int height) {}
  
  
private:
  void onDown(const TouchEvent& touchEvent);
  void onMove(const TouchEvent& touchEvent);
  void onUp(const TouchEvent& touchEvent);
  
  const Planet* _planet;
  GL *_gl;
  Camera _camera0;         //Initial Camera saved on Down event
  Camera* _camera;         // Camera used at current frame
  
};


#endif
