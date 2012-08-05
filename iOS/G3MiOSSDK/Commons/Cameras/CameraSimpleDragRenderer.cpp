//
//  CameraSimpleDragRenderer.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 28/07/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

#include <iostream>

#include "CameraSimpleDragRenderer.h"
#include "MutableVector2D.hpp"
#include "IGL.hpp"


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
  Vector2D pixel = touchEvent.getTouch(0)->getPos();  
  _initialPoint = _camera0.pixel2PlanetPoint(pixel).asMutableVector3D();
  
  //printf ("down 1 finger\n");
}


void CameraSimpleDragRenderer::onMove(const TouchEvent& touchEvent) 
{
  //_currentGesture = getGesture(touchEvent);
  if (_currentGesture!=Drag) return;
  if (_initialPoint.isNan()) return;
      
  const Vector2D pixel = touchEvent.getTouch(0)->getPos();  
  MutableVector3D finalPoint = _camera0.pixel2PlanetPoint(pixel).asMutableVector3D();
  if (finalPoint.isNan()) {
    //INVALID FINAL POINT
    Vector3D ray = _camera0.pixel2Ray(pixel);
    Vector3D pos = _camera0.getPosition();
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

int CameraSimpleDragRenderer::render(const RenderContext* rc) {
  _planet = rc->getPlanet();
  _camera = rc->getCamera();
  _gl = rc->getGL();

  // TEMP TO DRAW A POINT WHERE USER PRESS
  if (false) {
    if (_currentGesture == Drag) {
      float vertices[] = { 0,0,0};
      int indices[] = {0};
      _gl->enableVerticesPosition();
      _gl->disableTexture2D();
      _gl->disableTextures();
      _gl->vertexPointer(3, 0, vertices);
      _gl->color((float) 0, (float) 1, (float) 0, 1);
      _gl->pointSize(60);
      _gl->pushMatrix();
      
      double height = _planet->toGeodetic3D(_camera->getPosition()).height();
      //printf ("altura camara = %f\n", height);
      
      MutableMatrix44D T = MutableMatrix44D::createTranslationMatrix(_initialPoint.asVector3D());
      _gl->multMatrixf(T);
      _gl->drawPoints(1, indices);
      _gl->popMatrix();
            
      //Geodetic2D g = _planet->toGeodetic2D(_initialPoint.asVector3D());
      //printf ("zoom with initial point = (%f, %f)\n", g.latitude().degrees(), g.longitude().degrees());
    }
  }

  return MAX_TIME_TO_RENDER;
}



