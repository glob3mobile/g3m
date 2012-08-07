//
//  CameraRenderer.hpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 30/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_CameraRenderer_h
#define G3MiOSSDK_CameraRenderer_h

#include <vector>

#include "Renderer.hpp"
//#include "MutableVector3D.hpp"

class CameraEventHandler;


class ILogger;
class Camera;
class Vector3D;
class TouchEvent;


enum Gesture {
  None,                  
  Drag,
  Zoom,
  Rotate,
  DoubleDrag
};


class CameraRenderer: public Renderer
{
  
private:    
  Gesture _gesture;        
  
  const ILogger * _logger;  
  Camera* _camera;         // Camera used at current frame
  
  std::vector<CameraEventHandler *> _handlers;
    
  
public:
  
  CameraRenderer():_gesture(None) {}
  
  void addHandler(CameraEventHandler *handler) { _handlers.push_back(handler); }
  
  bool onTouchEvent(const TouchEvent* touchEvent);
  int render(const RenderContext* rc);
  void initialize(const InitializationContext* ic);
  void onResizeViewportEvent(int width, int height);
  
  bool isReadyToRender(const RenderContext* rc) {
    return true;
  }
  
  
};


#endif
