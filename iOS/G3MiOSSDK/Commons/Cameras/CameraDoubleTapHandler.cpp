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


bool CameraDoubleTapHandler::onTouchEvent(const EventContext *eventContext,
                                          const TouchEvent* touchEvent, 
                                          CameraContext *cameraContext) 
{
  // only one finger needed
  if (touchEvent->getTouchCount()!=1) return false;
  if (touchEvent->getTapCount()!=2) return false;
  if (touchEvent->getType()!=Down) return false;
  
  onDown(eventContext, *touchEvent, cameraContext);
  return true;
}


void CameraDoubleTapHandler::onDown(const EventContext *eventContext,
                                    const TouchEvent& touchEvent, 
                                    CameraContext *cameraContext) 
{  
  Camera *camera = cameraContext->getCamera();
  _camera0 = Camera(*camera);
  
  // compute globe point where user tapped
  const Vector2D pixel = touchEvent.getTouch(0)->getPos();
  _initialPoint = _camera0.pixel2PlanetPoint(pixel).asMutableVector3D();
  if (_initialPoint.isNan()) return;
  const Vector3D initialPoint = _initialPoint.asVector3D();
  
  // compute central point of view
  const Vector3D centerPoint = camera->centerOfViewOnPlanet();

  // compute drag parameters
//  Vector3D axis = initialPoint.cross(centerPoint);
//  Angle tita = Angle::fromRadians(-asin(axis.length()/initialPoint.length()/centerPoint.length()));
  
  // compute zoom factor
  const double height = eventContext->getPlanet()->toGeodetic3D(camera->getPosition()).height();
  const double d      = height * 0.5;
  
  // move the camera
  camera->dragCamera(initialPoint, centerPoint);
  camera->moveForward(d);


}




