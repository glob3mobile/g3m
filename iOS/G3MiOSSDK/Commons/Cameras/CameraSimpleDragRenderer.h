//
//  CameraSimpleDragRenderer.h
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 28/07/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

#ifndef G3MiOSSDK_CameraSimpleDragRenderer_h
#define G3MiOSSDK_CameraSimpleDragRenderer_h


#include "CameraRenderer.hpp"


class CameraSimpleDragRenderer: public CameraRenderer {
  
public:
  bool onTouchEvent(const TouchEvent* touchEvent);
  
private:
  void onDown(const TouchEvent& touchEvent);
  void onMove(const TouchEvent& touchEvent);
  void onUp(const TouchEvent& touchEvent);
  
};


#endif
