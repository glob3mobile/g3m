//
//  CameraDoubleDragRenderer.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 28/07/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

#include <iostream>

#include "CameraDoubleDragRenderer.h"
#include "IGL.hpp"


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
  _camera0 = Camera(*_camera);
  _currentGesture = DoubleDrag;  
  
  // double dragging
  Vector2D pixel0 = touchEvent.getTouch(0)->getPos();
  Vector3D ray0 = _camera0.pixel2Ray(pixel0);
  initialPoint0 = _planet->closestIntersection(_camera0.getPosition(), ray0).asMutableVector3D();
  Vector2D pixel1 = touchEvent.getTouch(1)->getPos();
  Vector3D ray1 = _camera0.pixel2Ray(pixel1);
  initialPoint1 = _planet->closestIntersection(_camera0.getPosition(), ray1).asMutableVector3D();
  
  // both pixels must intersect globe
  if (initialPoint0.isNan() || initialPoint1.isNan()) {
    _currentGesture = None;
    return;
  }
  
  // middle point in 3D
  Geodetic2D g0 = _planet->toGeodetic2D(initialPoint0.asVector3D());
  Geodetic2D g1 = _planet->toGeodetic2D(initialPoint1.asVector3D());
  Geodetic2D g = _planet->getMidPoint(g0, g1);
  _initialPoint = _planet->toVector3D(g).asMutableVector3D();
  
  // fingers difference
  Vector2D difPixel = pixel1.sub(pixel0);
  _initialFingerSeparation = difPixel.length();
  _initialFingerInclination = difPixel.orientation().radians();
  
  //printf ("down 2 finger\n");
}


void CameraDoubleDragRenderer::onMove(const TouchEvent& touchEvent) 
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
  
  // create temp camera to test gesture first
  Camera tempCamera(_camera0);

  // computer center view point
  Vector3D centerPoint = tempCamera.centerOfViewOnPlanet(_planet);
  
  // rotate globe from initialPoint to centerPoing
  {
    Vector3D initialPoint = _initialPoint.asVector3D();
    const Vector3D rotationAxis = initialPoint.cross(centerPoint);
    const Angle rotationDelta = Angle::fromRadians( - acos(initialPoint.normalized().dot(centerPoint.normalized())) );
    if (rotationDelta.isNan()) return; 
    tempCamera.rotateWithAxis(rotationAxis, rotationDelta);  
  }
  
  // move the camara 
  {
    double distance = tempCamera.getPosition().sub(centerPoint).length();
    tempCamera.moveForward(distance*(factor-1)/factor);
  }
  
  // rotate the camera
  {
    tempCamera.updateModelMatrix();
    Vector3D normal = _planet->geodeticSurfaceNormal(_initialPoint.asVector3D());
    tempCamera.rotateWithAxis(normal, Angle::fromRadians(angle));
  }
   
  // detect new final point
  {
    // compute 3D point of view center
    tempCamera.updateModelMatrix();
    Vector3D centerPoint = tempCamera.centerOfViewOnPlanet(_planet);
    
    // middle point in 3D
    Vector3D ray0 = tempCamera.pixel2Ray(pixel0);
    Vector3D P0 = _planet->closestIntersection(tempCamera.getPosition(), ray0);
    Vector3D ray1 = tempCamera.pixel2Ray(pixel1);
    Vector3D P1 = _planet->closestIntersection(tempCamera.getPosition(), ray1);
    Geodetic2D g = _planet->getMidPoint(_planet->toGeodetic2D(P0), _planet->toGeodetic2D(P1));
    Vector3D finalPoint = _planet->toVector3D(g);    
    
    // rotate globe from centerPoint to finalPoint
    const Vector3D rotationAxis = centerPoint.cross(finalPoint);
    const Angle rotationDelta = Angle::fromRadians( - acos(centerPoint.normalized().dot(finalPoint.normalized())) );
    if (rotationDelta.isNan()) {
      return;
    }
    tempCamera.rotateWithAxis(rotationAxis, rotationDelta);  
  }
  
  // the gesture was valid. Copy data to final camera
  tempCamera.updateModelMatrix();
    
  // adjust orientation of projected points
  Vector2D p00 = _camera0.point2Pixel(initialPoint0.asVector3D());
  Vector2D p10 = _camera0.point2Pixel(initialPoint1.asVector3D());
  Vector2D p0n = tempCamera.point2Pixel(initialPoint0.asVector3D());
  Vector2D p1n = tempCamera.point2Pixel(initialPoint1.asVector3D());
  Angle a0 = p10.sub(p00).orientation();
  Angle a1 = p1n.sub(p0n).orientation();          
  Vector3D normal = _planet->geodeticSurfaceNormal(_initialPoint.asVector3D());
  MutableMatrix44D trans1   = MutableMatrix44D::createTranslationMatrix(_initialPoint.asVector3D());
  MutableMatrix44D rotation = MutableMatrix44D::createRotationMatrix(a0.sub(a1).div(6), normal);
  MutableMatrix44D trans2   = MutableMatrix44D::createTranslationMatrix(_initialPoint.times(-1.0).asVector3D());
  MutableMatrix44D M        = trans1.multiply(rotation).multiply(trans2);
  tempCamera.applyTransform(M);
  _camera->copyFrom(tempCamera);

  //printf ("moving 2 fingers\n");
}


void CameraDoubleDragRenderer::onUp(const TouchEvent& touchEvent) 
{
  _currentGesture = None;
  _initialPixel = Vector3D::nan().asMutableVector3D();
  
  //printf ("end 2 fingers.  gesture=%d\n", _currentGesture);
}


int CameraDoubleDragRenderer::render(const RenderContext* rc) {
  _planet = rc->getPlanet();
  _camera = rc->getCamera();
  _gl = rc->getGL();
  
  // TEMP TO DRAW A POINT WHERE USER PRESS
  if (false) {
    if (_currentGesture == DoubleDrag) {
      float vertices[] = { 0,0,0};
      unsigned int indices[] = {0};
      _gl->enableVerticesPosition();
      _gl->disableTexture2D();
      _gl->disableTextures();
      _gl->vertexPointer(3, 0, vertices);
      _gl->color((float) 1, (float) 1, (float) 1, 1);
      _gl->pointSize(10);
      _gl->pushMatrix();
      MutableMatrix44D T = MutableMatrix44D::createTranslationMatrix(_initialPoint.asVector3D());
      _gl->multMatrixf(T);
      _gl->drawPoints(1, indices);
      _gl->popMatrix();
      
      // draw each finger
      _gl->pointSize(60);
      _gl->pushMatrix();
      MutableMatrix44D T0 = MutableMatrix44D::createTranslationMatrix(initialPoint0.asVector3D());
      _gl->multMatrixf(T0);
      _gl->drawPoints(1, indices);
      _gl->popMatrix();
      _gl->pushMatrix();
      MutableMatrix44D T1 = MutableMatrix44D::createTranslationMatrix(initialPoint1.asVector3D());
      _gl->multMatrixf(T1);
      _gl->drawPoints(1, indices);
      _gl->popMatrix();
      
      
      //Geodetic2D g = _planet->toGeodetic2D(_initialPoint.asVector3D());
      //printf ("zoom with initial point = (%f, %f)\n", g.latitude().degrees(), g.longitude().degrees());
    }
  }
  
  return MAX_TIME_TO_RENDER;
}

