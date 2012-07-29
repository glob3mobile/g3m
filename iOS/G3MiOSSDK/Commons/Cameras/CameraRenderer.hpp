//
//  CameraRenderer.h
//  G3MiOSSDK
//
//  Created by José Miguel S N on 04/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_CameraRenderer_h
#define G3MiOSSDK_CameraRenderer_h

#include "Renderer.hpp"

#include "Camera.hpp"
class Planet;
class IGL;
class ILogger;
#include "MutableVector3D.hpp"
#include "Vector3D.hpp"
#include "TouchEvent.hpp"


enum Gesture {
  None,                   // used only for animation, not for gesture
  Drag,
  Zoom,
  Rotate,
  DoubleDrag
};


class CameraRenderer: public Renderer
{
private:
  
  const ILogger * _logger;
  
  IGL *gl;
          
 // void makeRotate(const TouchEvent& touchEvent);
    

protected:
  const Planet* _planet;

  Camera _camera0;                //Initial Camera saved on Down event
  Camera* _camera;         // Camera used at current frame
  
  MutableVector3D _initialPoint;  //Initial point at dragging
  MutableVector3D _initialPixel;  //Initial pixel at start of gesture

  static Gesture _currentGesture;        //Gesture the user is making at the moment

  double _initialFingerSeparation;
  double _initialFingerInclination;
  
  Gesture getGesture(const TouchEvent& touchEvent);


  
public:
  
  CameraRenderer();
  
  void initialize(const InitializationContext* ic);  
  
  int render(const RenderContext* rc);
  
  virtual bool onTouchEvent(const TouchEvent* touchEvent) = 0;
  
  void onResizeViewportEvent(int width, int height);
  
  bool isReadyToRender(const RenderContext* rc) {
    return true;
  }

  
};


#endif
