//
//  CameraRotationRenderer.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 28/07/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

#include <iostream>

#include "CameraRotationRenderer.h"


bool CameraRotationRenderer::onTouchEvent(const TouchEvent* touchEvent) 
{
  // only one finger needed
  if (touchEvent->getTouchCount()!=3) return false;
  
  switch (touchEvent->getType()) {
    case Down:
      onDown(*touchEvent);
      break;
    case Move:
      onMove(*touchEvent);
      break;
    case Up:
      onUp(*touchEvent);
    default:
      break;
  }
  
  return true;
}


void CameraRotationRenderer::onDown(const TouchEvent& touchEvent) 
{  
  _camera0 = Camera(*_camera);
  _currentGesture = Rotate;
  
  // middle pixel in 2D 
  Vector2D pixel0 = touchEvent.getTouch(0)->getPos();
  Vector2D pixel1 = touchEvent.getTouch(1)->getPos();
  Vector2D pixel2 = touchEvent.getTouch(2)->getPos();
  Vector2D averagePixel = pixel0.add(pixel1).add(pixel2).div(3);
  _initialPixel = MutableVector3D(averagePixel.x(), averagePixel.y(), 0);
  
  // compute center of view
  _initialPoint = _camera->centerOfViewOnPlanet(_planet).asMutableVector3D();
}


void CameraRotationRenderer::onMove(const TouchEvent& touchEvent) 
{
  _currentGesture = getGesture(touchEvent);
  if (_currentGesture!=Rotate) return;
  
  printf ("moving three fingers...\n");
}


void CameraRotationRenderer::onUp(const TouchEvent& touchEvent) 
{
  _currentGesture = None;
  _initialPixel = Vector3D::nan().asMutableVector3D();
}

