//
//  CameraDoubleTapHandler.cpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 07/08/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//


#include "CameraDoubleTapHandler.hpp"
#include "MutableVector2D.hpp"
#include "GL.hpp"
#include "TouchEvent.hpp"

bool CameraDoubleTapHandler::onTouchEvent(const G3MEventContext *eventContext,
                                          const TouchEvent* touchEvent,
                                          CameraContext *cameraContext) {
  // only one finger needed
  if (touchEvent->getTouchCount()!=1) return false;
  if (touchEvent->getTapCount()!=2) return false;
  if (touchEvent->getType()!=Down) return false;
  
  onDown(eventContext, *touchEvent, cameraContext);
  return true;
}

void CameraDoubleTapHandler::onDown(const G3MEventContext *eventContext,
                                    const TouchEvent& touchEvent,
                                    CameraContext *cameraContext) {
/*  // compute globe point where user tapped
  const Vector2I pixel = touchEvent.getTouch(0)->getPos();
  Camera* camera = cameraContext->getNextCamera();
  const Vector3D initialPoint = camera->pixel2PlanetPoint(pixel);
  if (initialPoint.isNan()) return;
  
  // compute central point of view
  const Vector3D centerPoint = camera->getXYZCenterOfView();
  
  // compute drag parameters
  const Vector3D axis = initialPoint.cross(centerPoint);
  const Angle angle   = Angle::fromRadians(- IMathUtils::instance()->asin(axis.length()/initialPoint.length()/centerPoint.length()));
  
  // compute zoom factor
  const double height   = camera->getGeodeticPosition()._height;
  const double distance = height * 0.6;
  
  // create effect
  Effect* effect = new DoubleTapEffect(TimeInterval::fromSeconds(0.75), axis, angle, distance);
  */
  
  const Vector2I pixel = touchEvent.getTouch(0)->getPos();
  const Planet* planet = eventContext->getPlanet();
  Camera* camera = cameraContext->getNextCamera();
  Effect* effect = planet->createDoubleTapEffect(camera->getCartesianPosition(),
                                                 camera->getViewDirection(),
                                                 camera->pixel2Ray(pixel));
  
  if (effect != NULL) {
    EffectTarget* target = cameraContext->getNextCamera()->getEffectTarget();
    eventContext->getEffectsScheduler()->startEffect(effect, target);
  }
}
