//
//  CameraDoubleDragRenderer.h
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 28/07/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

#ifndef G3MiOSSDK_CameraDoubleDragRenderer_h
#define G3MiOSSDK_CameraDoubleDragRenderer_h


#include "CameraRenderer.h"


class CameraDoubleDragRenderer: public CameraRenderer {
  
public:
  CameraDoubleDragRenderer():
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
  
  MutableVector3D initialPoint0, initialPoint1;
  const Planet* _planet;
  IGL *_gl;
  Camera _camera0;         //Initial Camera saved on Down event
  Camera* _camera;         // Camera used at current frame
        
};

#endif
