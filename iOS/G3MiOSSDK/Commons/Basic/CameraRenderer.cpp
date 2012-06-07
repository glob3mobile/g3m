//
//  CameraRenderer.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 04/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "CameraRenderer.hpp"

#include "Camera.hpp"

CameraRenderer::CameraRenderer():
_camera0(0,0),
_initialPoint(0,0,0),
_cameraFixed(false)
{
}

void CameraRenderer::initialize(const InitializationContext* ic){}

int CameraRenderer::render(const RenderContext* rc)
{
  
  
  _camera = rc->getCamera(); //Saving camera reference 
  _planet = rc->getPlanet();
  
  if (!_cameraFixed) _camera->applyInertia();    //AutoDragging
  
  rc->getCamera()->draw(*rc);
  return 0;
}

void CameraRenderer::onDown(const TouchEvent& event)
{
  //Saving Camera0
  Camera c(*_camera);
  _camera0 = c;
  
  Vector2D pixel = event.getTouch(0)->getPos();
  
  Vector3D ray = _camera0.pixel2Vector(pixel);
  _initialPoint = _planet->closestIntersection(_camera0.getPos(), ray);
  _initialPoint.print();
  
  _cameraFixed = true;
}

void CameraRenderer::onMove(const TouchEvent& event)
{
  int n = event.getNumTouch();
  if (n == 1){
    
    if (_initialPoint.length() > 0){ //VALID INITIAL POINT
    
      Vector2D pixel = event.getTouch(0)->getPos();
      Vector3D ray = _camera0.pixel2Vector(pixel);
      Vector3D pos = _camera0.getPos();
      
      Vector3D finalPoint = _planet->closestIntersection(pos, ray);
      
      if (finalPoint.length() <= 0.0){ //INVALID FINAL POINT
        //We take the closest point to the sphere
        Vector3D finalPoint2 = _planet->closestPointToSphere(pos, ray);
        _camera->copyFrom(_camera0);
        _camera->dragCamera(_initialPoint, finalPoint2);
        
      } else {
        _camera->copyFrom(_camera0);
        _camera->dragCamera(_initialPoint, finalPoint);
      }
    }
  }

}

void CameraRenderer::onUp(const TouchEvent& event)
{
  _cameraFixed = false;
}

bool CameraRenderer::onTouchEvent(const TouchEvent* event)
{
  switch (event->getType()) {
    case Down:
      onDown(*event);
      break;
    case Move:
      onMove(*event);
      break;
    case Up:
      onUp(*event);
    default:
      break;
  }
  
  return true;
}