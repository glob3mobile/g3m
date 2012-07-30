//
//  CameraDoubleDragRenderer.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 28/07/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

#include <iostream>

#include "CameraDoubleDragHandler.h"
#include "IGL.hpp"


bool CameraDoubleDragHandler::onTouchEvent(const TouchEvent* touchEvent) 
{
  // only one finger needed
  if (touchEvent->getTouchCount()!=2) return false;
  
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


void CameraDoubleDragHandler::onDown(const TouchEvent& touchEvent) 
{
  _camera0 = Camera(*_camera);
  _currentGesture = DoubleDrag;  
  
  // double dragging
  Vector2D pixel0 = touchEvent.getTouch(0)->getPos();
  Vector2D pixel1 = touchEvent.getTouch(1)->getPos();
  
  // middle point in 3D
  Vector3D ray0 = _camera0.pixel2Ray(pixel0);
  Vector3D P0 = _planet->closestIntersection(_camera0.getPosition(), ray0);
  Vector3D ray1 = _camera0.pixel2Ray(pixel1);
  Vector3D P1 = _planet->closestIntersection(_camera0.getPosition(), ray1);
  Geodetic2D g = _planet->getMidPoint(_planet->toGeodetic2D(P0), _planet->toGeodetic2D(P1));
  _initialPoint = _planet->toVector3D(g).asMutableVector3D();
  
  // fingers difference
  Vector2D difPixel = pixel1.sub(pixel0);
  _initialFingerSeparation = difPixel.length();
  _initialFingerInclination = difPixel.orientation().radians();
  
  //printf ("down 2 finger\n");
}


void CameraDoubleDragHandler::onMove(const TouchEvent& touchEvent) 
{
  //_currentGesture = getGesture(touchEvent);
  if (_currentGesture!=DoubleDrag) return;
  if (_initialPoint.isNan()) return;
    
  Vector2D pixel0 = touchEvent.getTouch(0)->getPos();
  Vector2D pixel1 = touchEvent.getTouch(1)->getPos();    
  Vector2D difPixel = pixel1.sub(pixel0);
  double finalFingerSeparation = difPixel.length();
  double angle = difPixel.orientation().radians() - _initialFingerInclination;
  double factor = finalFingerSeparation/_initialFingerSeparation;
  
  _camera->copyFrom(_camera0);
  
  // computer center view point
  Vector3D centerPoint = _camera->centerOfViewOnPlanet(_planet);
  
  // rotate globe from initialPoint to centerPoing
  {
    Vector3D initialPoint = _initialPoint.asVector3D();
    const Vector3D rotationAxis = initialPoint.cross(centerPoint);
    const Angle rotationDelta = Angle::fromRadians( - acos(initialPoint.normalized().dot(centerPoint.normalized())) );
    if (rotationDelta.isNan()) return; 
    _camera->rotateWithAxis(rotationAxis, rotationDelta);  
  }
  
  // move the camara 
  {
    double distance = _camera->getPosition().sub(centerPoint).length();
    _camera->moveForward(distance*(factor-1)/factor);
  }
  
  // rotate the camera
  {
    _camera->rotateWithAxis(_camera->getCenter().sub(_camera->getPosition()), Angle::fromRadians(-angle));
  }
  
  // detect new final point
  {
    // compute 3D point of view center
    _camera->updateModelMatrix();
    Vector3D centerPoint = _camera->centerOfViewOnPlanet(_planet);
    
    // middle point in 3D
    Vector3D ray0 = _camera->pixel2Ray(pixel0);
    Vector3D P0 = _planet->closestIntersection(_camera->getPosition(), ray0);
    Vector3D ray1 = _camera->pixel2Ray(pixel1);
    Vector3D P1 = _planet->closestIntersection(_camera->getPosition(), ray1);
    Geodetic2D g = _planet->getMidPoint(_planet->toGeodetic2D(P0), _planet->toGeodetic2D(P1));
    Vector3D finalPoint = _planet->toVector3D(g);     
          
    // rotate globe from centerPoint to finalPoint
    const Vector3D rotationAxis = centerPoint.cross(finalPoint);
    const Angle rotationDelta = Angle::fromRadians( - acos(centerPoint.normalized().dot(finalPoint.normalized())) );
    if (rotationDelta.isNan()) return; 
    _camera->rotateWithAxis(rotationAxis, rotationDelta);  
  }
  
  //printf ("moving 2 fingers\n");
}


void CameraDoubleDragHandler::onUp(const TouchEvent& touchEvent) 
{
  _currentGesture = None;
  _initialPixel = Vector3D::nan().asMutableVector3D();
  
  //printf ("end 2 fingers.  gesture=%d\n", _currentGesture);
}


int CameraDoubleDragHandler::render(const RenderContext* rc) {
  // TEMP TO DRAW A POINT WHERE USER PRESS
  if (false) {
    if (_currentGesture == DoubleDrag) {
      float vertices[] = { 0,0,0};
      unsigned int indices[] = {0};
      gl->enableVerticesPosition();
      gl->disableTexture2D();
      gl->disableTextures();
      gl->vertexPointer(3, 0, vertices);
      gl->color((float) 1, (float) 1, (float) 1, 1);
      gl->pushMatrix();
      MutableMatrix44D T = MutableMatrix44D::createTranslationMatrix(_initialPoint.asVector3D().times(1.01));
      gl->multMatrixf(T);
      gl->drawPoints(1, indices);
      gl->popMatrix();
      //Geodetic2D g = _planet->toGeodetic2D(_initialPoint.asVector3D());
      //printf ("zoom with initial point = (%f, %f)\n", g.latitude().degrees(), g.longitude().degrees());
    }
  }
  
  return MAX_TIME_TO_RENDER;
}


