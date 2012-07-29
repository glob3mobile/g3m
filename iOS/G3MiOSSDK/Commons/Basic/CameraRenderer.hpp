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
  
  Camera* _camera;         // Camera used at current frame
  const Planet* _planet;
  IGL *gl;
  
  Camera _camera0;                //Initial Camera saved on Down event
  MutableVector3D _initialPoint;  //Initial point at dragging
  MutableVector3D _initialPixel;  //Initial pixel at start of gesture
  double _initialFingerSeparation;
  double _initialFingerInclination;
  
  Gesture _currentGesture;        //Gesture the user is making at the moment
  
  void onDown(const TouchEvent& touchEvent);
  void onMove(const TouchEvent& touchEvent);
  void onUp(const TouchEvent& touchEvent);
  
  Gesture getGesture(const TouchEvent& touchEvent);
  
  void makeDrag(const TouchEvent& touchEvent);
  void makeZoom(const TouchEvent& touchEvent);
  void makeDoubleDrag(const TouchEvent& touchEvent);
  void makeRotate(const TouchEvent& touchEvent);
  
  Vector3D centerOfViewOnPlanet(const Camera& c) const;
  
  
public:
  
  CameraRenderer();
  
  void initialize(const InitializationContext* ic);  
  
  int render(const RenderContext* rc);
  
  bool onTouchEvent(const TouchEvent* touchEvent);
  
  void onResizeViewportEvent(int width, int height);
  
  bool isReadyToRender(const RenderContext* rc) {
    return true;
  }

  
};


#endif
