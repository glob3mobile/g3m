//
//  CameraRotationHandler.h
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 28/07/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

#ifndef G3MiOSSDK_CameraRotationHandler_h
#define G3MiOSSDK_CameraRotationHandler_h

#include "CameraHandler.h"


class CameraRotationHandler: public CameraHandler {
  
public:
  bool onTouchEvent(const TouchEvent* touchEvent);
  int render(const RenderContext* rc);
  
private:
  void onDown(const TouchEvent& touchEvent);
  void onMove(const TouchEvent& touchEvent);
  void onUp(const TouchEvent& touchEvent);
  
  double lastYValid;
    
};



#endif
