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


bool CameraDoubleTapHandler::onTouchEvent(const TouchEvent* touchEvent, CameraContext *cameraContext) 
{
  // only one finger needed
  if (touchEvent->getTouchCount()!=1) return false;
  if (touchEvent->getTapCount()!=2) return false;
  if (touchEvent->getType()!=Down) return false;
  
  onDown(*touchEvent, cameraContext);
  return true;
}


void CameraDoubleTapHandler::onDown(const TouchEvent& touchEvent, CameraContext *cameraContext) 
{  
  _camera0 = Camera(*_camera);
  
  // compute globe point where user tapped
  const Vector2D pixel = touchEvent.getTouch(0)->getPos();
  _initialPoint = _camera0.pixel2PlanetPoint(pixel).asMutableVector3D();
  if (_initialPoint.isNan()) return;
  const Vector3D initialPoint = _initialPoint.asVector3D();
  
  // compute central point of view
  const Vector3D centerPoint = _camera->centerOfViewOnPlanet();

  // compute drag parameters
//  Vector3D axis = initialPoint.cross(centerPoint);
//  Angle tita = Angle::fromRadians(-asin(axis.length()/initialPoint.length()/centerPoint.length()));
  
  // compute zoom factor
  const double height = _planet->toGeodetic3D(_camera->getPosition()).height();
  const double d      = height * 0.5;
  
  // move the camera
  _camera->dragCamera(initialPoint, centerPoint);
  _camera->moveForward(d);


}


int CameraDoubleTapHandler::render(const RenderContext* rc, CameraContext *cameraContext) {
  _planet = rc->getPlanet();
  _camera = rc->getCamera();

  return MAX_TIME_TO_RENDER;
}



