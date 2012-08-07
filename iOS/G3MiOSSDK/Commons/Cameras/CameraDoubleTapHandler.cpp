//
//  CameraDoubleTapHandler.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 07/08/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

#include <iostream>

#include "CameraDoubleTapHandler.hpp"
#include "MutableVector2D.hpp"
#include "GL.hpp"
#include "TouchEvent.hpp"
#include "CameraRenderer.hpp"


bool CameraDoubleTapHandler::onTouchEvent(const TouchEvent* touchEvent, Gesture &gesture) 
{
  // only one finger needed
  if (touchEvent->getTouchCount()!=1) return false;
  if (touchEvent->getTapCount()!=2) return false;
  if (touchEvent->getType()!=Down) return false;
  
  onDown(*touchEvent, gesture);
  return true;
}


void CameraDoubleTapHandler::onDown(const TouchEvent& touchEvent, Gesture &gesture) 
{  
  _camera0 = Camera(*_camera);
  
  // dragging
  Vector2D pixel = touchEvent.getTouch(0)->getPos();  
  _initialPoint = _camera0.pixel2PlanetPoint(pixel).asMutableVector3D();
  
  printf ("double tapping!\n");
}


int CameraDoubleTapHandler::render(const RenderContext* rc, Gesture &gesture) {
  _planet = rc->getPlanet();
  _camera = rc->getCamera();

  return MAX_TIME_TO_RENDER;
}



