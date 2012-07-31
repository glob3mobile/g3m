//
//  CameraDoubleDragHandler.h
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 28/07/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

#ifndef G3MiOSSDK_CameraDoubleDragHandler_h
#define G3MiOSSDK_CameraDoubleDragHandler_h


#include "CameraHandler.h"


class CameraDoubleDragHandler: public CameraHandler {
  
public:
  bool onTouchEvent(const TouchEvent* touchEvent);
  int render(const RenderContext* rc);
  void initialize(const InitializationContext* ic) {}  
  void onResizeViewportEvent(int width, int height) {}
  
private:
  void onDown(const TouchEvent& touchEvent);
  void onMove(const TouchEvent& touchEvent);
  void onUp(const TouchEvent& touchEvent);
  
  MutableVector3D initialPoint0, initialPoint1;
        
};

#endif
