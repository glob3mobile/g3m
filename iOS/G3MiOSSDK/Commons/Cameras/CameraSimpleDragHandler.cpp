//
//  CameraSimpleDragHandler.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 28/07/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

#include <iostream>

#include "CameraSimpleDragHandler.h"
#include "MutableVector2D.hpp"
#include "IGL.hpp"


bool CameraSimpleDragHandler::onTouchEvent(const TouchEvent* touchEvent) 
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


void CameraSimpleDragHandler::onDown(const TouchEvent& touchEvent) 
{  
  _camera0 = Camera(*_camera);
  _currentGesture = Drag; 
  
  // dragging
  MutableVector2D pixel = touchEvent.getTouch(0)->getPos().asMutableVector2D();
  const Vector3D ray = _camera0.pixel2Ray(pixel.asVector2D());
  _initialPoint = _planet->closestIntersection(_camera0.getPosition(), ray).asMutableVector3D();
  
  //printf ("down 1 finger\n");
}


void CameraSimpleDragHandler::onMove(const TouchEvent& touchEvent) 
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


void CameraSimpleDragHandler::onUp(const TouchEvent& touchEvent) 
{
  _currentGesture = None;
  _initialPixel = Vector3D::nan().asMutableVector3D();
  
  //printf ("end 1 finger\n");
}

int CameraSimpleDragHandler::render(const RenderContext* rc) {
  // TEMP TO DRAW A POINT WHERE USER PRESS
  if (false) {
    if (_currentGesture == Drag) {
      float vertices[] = { 0,0,0};
      unsigned int indices[] = {0};
      gl->enableVerticesPosition();
      gl->disableTexture2D();
      gl->disableTextures();
      gl->vertexPointer(3, 0, vertices);
      gl->color((float) 0, (float) 1, (float) 0, 1);
      gl->pointSize(60);
      gl->pushMatrix();
      
      double height = _planet->toGeodetic3D(_camera->getPosition()).height();
      printf ("altura camara = %f\n", height);
                                         
      
      MutableMatrix44D T = MutableMatrix44D::createTranslationMatrix(_initialPoint.asVector3D().times(1.0001));
      gl->multMatrixf(T);
      gl->drawPoints(1, indices);
      gl->popMatrix();
            
      //Geodetic2D g = _planet->toGeodetic2D(_initialPoint.asVector3D());
      //printf ("zoom with initial point = (%f, %f)\n", g.latitude().degrees(), g.longitude().degrees());
    }
  }

  return MAX_TIME_TO_RENDER;
}



