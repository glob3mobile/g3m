//
//  CameraSimpleDragRenderer.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 28/07/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

#include <iostream>

#include "CameraSimpleDragRenderer.h"


bool CameraSimpleDragRenderer::onTouchEvent(const TouchEvent* touchEvent) 
{
  // only one finger needed
  if (touchEvent->getTouchCount()!=1) return false;

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


void CameraSimpleDragRenderer::onDown(const TouchEvent& touchEvent) 
{  
  _camera0 = Camera(*_camera);
  _currentGesture = Drag; 
  
  // dragging
  MutableVector2D pixel = touchEvent.getTouch(0)->getPos().asMutableVector2D();
  const Vector3D ray = _camera0.pixel2Ray(pixel.asVector2D());
  _initialPoint = _planet->closestIntersection(_camera0.getPosition(), ray).asMutableVector3D();
  
  //printf ("down 1 finger\n");
}


void CameraSimpleDragRenderer::onMove(const TouchEvent& touchEvent) 
{
  //_currentGesture = getGesture(touchEvent);
  if (_currentGesture!=Drag) return;
  if (_initialPoint.isNan()) return;
      
  const Vector2D pixel = touchEvent.getTouch(0)->getPos();
  const Vector3D ray = _camera0.pixel2Ray(pixel);
  const Vector3D pos = _camera0.getPosition();
  
  MutableVector3D finalPoint = _planet->closestIntersection(pos, ray).asMutableVector3D();
  if (finalPoint.isNan()) {
    //INVALID FINAL POINT
    finalPoint = _planet->closestPointToSphere(pos, ray).asMutableVector3D();
  }
  
  _camera->copyFrom(_camera0);
  _camera->dragCamera(_initialPoint.asVector3D(), finalPoint.asVector3D());
  
  //printf ("Moving 1 finger.  gesture=%d\n", _currentGesture);
}


void CameraSimpleDragRenderer::onUp(const TouchEvent& touchEvent) 
{
  _currentGesture = None;
  _initialPixel = Vector3D::nan().asMutableVector3D();
  
  //printf ("end 1 finger\n");
}



