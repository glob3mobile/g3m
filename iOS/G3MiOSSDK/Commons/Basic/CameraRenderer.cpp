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
_initialPoint(0,0,0)
{
}

void CameraRenderer::initialize(const InitializationContext* ic){}

int CameraRenderer::render(const RenderContext* rc)
{
  _camera = rc->getCamera(); //Saving camera reference 
  _planet = rc->getPlanet();
  
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
  _initialPoint = intersectionRayWithPlanet(_camera0.getPos(), ray);
}

void CameraRenderer::onMove(const TouchEvent& event)
{
  Vector2D pixel = event.getTouch(0)->getPos();
  
  Vector3D ray = _camera0.pixel2Vector(pixel);
  Vector3D newPoint = intersectionRayWithPlanet(_camera0.getPos(), ray);
  
  _camera->copyFrom(_camera0);
  
  _camera->dragCamera(_initialPoint, newPoint);
  
}

void CameraRenderer::onUp(const TouchEvent& event)
{
  
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

Vector3D CameraRenderer::intersectionRayWithPlanet(Vector3D pos, Vector3D ray)
{
  std::vector<double> t = _planet->intersections(pos , ray);
  if (t.empty()) return Vector3D(0,0,0);
  Vector3D solution = pos.add(ray.times(t[0]));
  return solution;
}