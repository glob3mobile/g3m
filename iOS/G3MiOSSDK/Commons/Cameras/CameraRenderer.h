//
//  CameraRenderer.h
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 30/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_CameraRenderer_h
#define G3MiOSSDK_CameraRenderer_h

#include "Renderer.hpp"
#include "Camera.hpp"
#include "MutableVector3D.hpp"
#include "Vector3D.hpp"
#include "TouchEvent.hpp"

class Planet;
class IGL;
class ILogger;


enum Gesture {
  None,                   // used only for animation, not for gesture
  Drag,
  Zoom,
  Rotate,
  DoubleDrag
};


class CameraRenderer: public Renderer
{
  
protected:
  static const Planet* _planet;
  static const ILogger * _logger;
  static IGL *gl;
  
  static Camera _camera0;                //Initial Camera saved on Down event
  static Camera* _camera;         // Camera used at current frame
  
  MutableVector3D _initialPoint;  //Initial point at dragging
  MutableVector3D _initialPixel;  //Initial pixel at start of gesture
  
  static Gesture _currentGesture;        //Gesture the user is making at the moment
  
  double _initialFingerSeparation;
  double _initialFingerInclination;
    
  
public:
  
  CameraRenderer():
  _initialPoint(0,0,0),
  _initialPixel(0,0,0)
  {
  }
  
  virtual void initialize(const InitializationContext* ic) = 0;  
  
  virtual int render(const RenderContext* rc) = 0;
  
  virtual bool onTouchEvent(const TouchEvent* touchEvent) = 0;
  
  virtual void onResizeViewportEvent(int width, int height) = 0;
  
  bool isReadyToRender(const RenderContext* rc) {
    return true;
  }
  
  
};


#endif
