//
//  CameraRenderer.h
//  G3MiOSSDK
//
//  Created by José Miguel S N on 04/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_CameraRenderer_h
#define G3MiOSSDK_CameraRenderer_h

#include "CameraHandler.h"


class CameraRenderer: public CameraHandler {
  
public:
  bool onTouchEvent(const TouchEvent* touchEvent) { return false; }
  int render(const RenderContext* rc);
  void initialize(const InitializationContext* ic);
  void onResizeViewportEvent(int width, int height);
    
};



/*
#include "Renderer.hpp"
#include "Camera.hpp"

class ILogger;
class Planet;
class IGL;


class CameraRenderer: public Renderer
{
private:
  
  const ILogger * _logger;
  
  const Planet* _planet;
  IGL *gl;
    
  Camera _camera0;                //Initial Camera saved on Down event
  Camera* _camera;         // Camera used at current frame
  
  MutableVector3D _initialPoint;  //Initial point at dragging
  MutableVector3D _initialPixel;  //Initial pixel at start of gesture
    
  double _initialFingerSeparation;
  double _initialFingerInclination;
    
  
  
public:
  
  CameraHandler();
  
  void initialize(const InitializationContext* ic);  
  
  virtual int render(const RenderContext* rc) = 0;
  
  virtual bool onTouchEvent(const TouchEvent* touchEvent) = 0;
  
  void onResizeViewportEvent(int width, int height);
  
  bool isReadyToRender(const RenderContext* rc) {
    return true;
  }
  
  
};
*/

#endif
