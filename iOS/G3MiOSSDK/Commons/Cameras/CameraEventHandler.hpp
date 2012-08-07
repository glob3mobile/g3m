//
//  CameraEventHandler.hpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 07/08/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

#ifndef G3MiOSSDK_CameraEventHandler_hpp
#define G3MiOSSDK_CameraEventHandler_hpp

#include "CameraRenderer.hpp"

class TouchEvent;
class RenderContext;


class CameraEventHandler {
  
public:  
  virtual bool onTouchEvent(const TouchEvent* touchEvent, Gesture &gesture) = 0;
  virtual int render(const RenderContext* rc, Gesture &gesture) = 0;
  virtual ~CameraEventHandler() {}
  
private:
  virtual void onDown(const TouchEvent& touchEvent, Gesture &gesture) = 0;
  virtual void onMove(const TouchEvent& touchEvent, Gesture &gesture) = 0;
  virtual void onUp(const TouchEvent& touchEvent, Gesture &gesture) = 0;

};

#endif
