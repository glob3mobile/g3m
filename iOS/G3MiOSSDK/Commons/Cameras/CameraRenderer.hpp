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
#include "Effects.hpp"


#include "Effects.hpp"

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

class CameraContext: public EffectTarget {
private:
  Gesture _currentGesture;
  Camera* _nextCamera;         

public:
  CameraContext(Gesture gesture, Camera* nextCamera): 
  _currentGesture(gesture),
  _nextCamera(nextCamera)
  {}
  
  ~CameraContext(){}
  
  const Gesture getCurrentGesture() const { return _currentGesture; }
  void setCurrentGesture(const Gesture& gesture) { _currentGesture = gesture; }
  Camera* getNextCamera() { return _nextCamera; }
  
  bool isEffectable() const{
    return true;
  }
};



class CameraRenderer: public Renderer
{
  
private:      
  
  std::vector<CameraEventHandler *> _handlers;
  
  CameraContext *_cameraContext;    
  
public:
  CameraRenderer(): _cameraContext(NULL) {}
  ~CameraRenderer() { if (_cameraContext!=NULL) delete _cameraContext; }
    
  void addHandler(CameraEventHandler *handler) { _handlers.push_back(handler); }
  
  void render(const RenderContext* rc);
  void initialize(const InitializationContext* ic);
  
  bool onTouchEvent(const EventContext* ec,
                    const TouchEvent* touchEvent);

  void onResizeViewportEvent(const EventContext* ec,
                             int width, int height);
  
  bool isReadyToRender(const RenderContext* rc) {
    return true;
  }
  
  void start() {
    
  }
  
  void stop() {
    
  }


};


#endif
