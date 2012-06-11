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

enum Gesture {
  None,                   // used only for animation, not for gesture
  Drag,
  Zoom,
  Rotate
};

class CameraRenderer: public Renderer
{
private:

  Camera * _camera;   //Camera used at current frame
  const Planet * _planet;   //Planet
  
  Camera _camera0;                //Initial Camera saved on Down event
  MutableVector3D _initialPoint;  //Initial point at dragging
  
  Gesture _currentGesture;        //Gesture the user is making at the moment
  
  void onDown(const TouchEvent& event);
  void onMove(const TouchEvent& event);
  void onUp(const TouchEvent& event);
  
  Gesture getGesture(const TouchEvent& event) const;
  
  void makeDrag(const TouchEvent& event);
  void makeZoom(const TouchEvent& event);
  void makeRotate(const TouchEvent& event);
  
public:
  
  CameraRenderer();
  
  void initialize(const InitializationContext* ic);  
  
  int render(const RenderContext* rc);
  
  bool onTouchEvent(const TouchEvent* event);
  
  bool onResizeViewportEvent(int width, int height);
  
  
};


#endif
