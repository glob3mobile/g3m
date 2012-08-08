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
#include "Context.hpp"

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


class CameraContext {
private:
  Gesture _currentGesture;
public:
  Gesture getCurrentGesture() const { return _currentGesture; }
  void setCurrentGesture(Gesture gesture) { _currentGesture = gesture; }
};



class CameraRenderer: public Renderer
{
  
private:      
  const ILogger * _logger;  
  Camera* _camera;         // Camera used at current frame
  
  std::vector<CameraEventHandler *> _handlers;
  
  CameraContext _cameraContext;
    
  
public:
    
  void addHandler(CameraEventHandler *handler) { _handlers.push_back(handler); }
  
  int render(const RenderContext* rc);
  void initialize(const InitializationContext* ic);
  
  bool onTouchEvent(const EventContext* ec,
                    const TouchEvent* touchEvent);

  void onResizeViewportEvent(const EventContext* ec,
                             int width, int height);
  
  bool isReadyToRender(const RenderContext* rc) {
    return true;
  }
  

};


#endif
