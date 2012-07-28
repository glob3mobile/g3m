//
//  CameraDoubleDragRenderer.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 28/07/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

#include <iostream>

#include "CameraDoubleDragRenderer.h"


bool CameraDoubleDragRenderer::onTouchEvent(const TouchEvent* touchEvent) 
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


void CameraDoubleDragRenderer::onDown(const TouchEvent& touchEvent) 
{
  // two fingers needed
  if (touchEvent.getTouchCount()!=2) return;

  //Saving Camera0
  _camera0 = Camera(*_camera);
  
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
  _currentGesture = DoubleDrag;  
}


void CameraDoubleDragRenderer::onMove(const TouchEvent& touchEvent) 
{
  _currentGesture = getGesture(touchEvent);
  if (_currentGesture==DoubleDrag) makeDoubleDrag(touchEvent);
}


void CameraDoubleDragRenderer::onUp(const TouchEvent& touchEvent) 
{
  _currentGesture = None;
  _initialPixel = Vector3D::nan().asMutableVector3D();
}


void CameraDoubleDragRenderer::makeDoubleDrag(const TouchEvent& touchEvent) {
  
  int __agustin_at_work;
  
  if (!_initialPoint.isNan()) {
    
    Vector2D pixel0 = touchEvent.getTouch(0)->getPos();
    Vector2D pixel1 = touchEvent.getTouch(1)->getPos();    
    Vector2D difPixel = pixel1.sub(pixel0);
    double finalFingerSeparation = difPixel.length();
    double angle = difPixel.orientation().radians() - _initialFingerInclination;
    double factor = finalFingerSeparation/_initialFingerSeparation;
    
    _camera->copyFrom(_camera0);
    //_camera->dragCameraWith2Fingers(_initialPoint.asVector3D(), centerP, finalPoint, finalFingerSeparation/_initialFingerSeparation);
    
    // computer center view point
    Vector2D centerPixel(_camera->getWidth()*0.5, _camera->getHeight()*0.5);
    Vector3D centerRay = _camera->pixel2Ray(centerPixel);
    Vector3D centerPoint = _planet->closestIntersection(_camera->getPosition(), centerRay);
    
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
      _camera->updateModelMatrix();
      
      // compute 3D point of view center
      Vector2D centerPixel(_camera->getWidth()*0.5, _camera->getHeight()*0.5);
      Vector3D centerRay = _camera->pixel2Ray(centerPixel);
      Vector3D centerPoint = _planet->closestIntersection(_camera->getPosition(), centerRay);
      
      // middle point in 3D
      Vector3D ray0 = _camera->pixel2Ray(pixel0);
      Vector3D P0 = _planet->closestIntersection(_camera->getPosition(), ray0);
      Vector3D ray1 = _camera->pixel2Ray(pixel1);
      Vector3D P1 = _planet->closestIntersection(_camera->getPosition(), ray1);
      Geodetic2D g = _planet->getMidPoint(_planet->toGeodetic2D(P0), _planet->toGeodetic2D(P1));
      Vector3D finalPoint = _planet->toVector3D(g);     
      
      /*
       // middle point in 2D
       Vector2D averagePixel = pixel0.add(pixel1).div(2);
       Vector3D ray = _camera->pixel2Ray(averagePixel);
       Vector3D finalPoint = _planet->closestIntersection(_camera->getPosition(), ray);*/
      
      // rotate globe from centerPoint to finalPoint
      const Vector3D rotationAxis = centerPoint.cross(finalPoint);
      const Angle rotationDelta = Angle::fromRadians( - acos(centerPoint.normalized().dot(finalPoint.normalized())) );
      if (rotationDelta.isNan()) return; 
      _camera->rotateWithAxis(rotationAxis, rotationDelta);  
    }
    
  }
}



