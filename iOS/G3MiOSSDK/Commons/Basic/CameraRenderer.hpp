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

class CameraRenderer: public Renderer
{
private:

  Camera * _camera;   //Camera used at current frame
  const Planet * _planet;   //Planet
  
  Camera _camera0;                //Initial Camera saved on Down event
  MutableVector3D _initialPoint;  //Initial point at dragging
  
  void onDown(const TouchEvent& event);
  void onMove(const TouchEvent& event);
  void onUp(const TouchEvent& event);
  
  Vector3D intersectionRayWithPlanet(Vector3D pos, Vector3D ray);
  
public:
  
  CameraRenderer();
  
  void initialize(const InitializationContext* ic);  
  
  int render(const RenderContext* rc);
  
  bool onTouchEvent(const TouchEvent* event);
  
  
};


#endif
