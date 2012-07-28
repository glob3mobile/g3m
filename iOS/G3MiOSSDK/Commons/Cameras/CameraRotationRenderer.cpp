//
//  CameraRotationRenderer.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 28/07/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

#include <iostream>

#include "CameraRotationRenderer.h"


bool CameraRotationRenderer::onTouchEvent(const TouchEvent* touchEvent) 
{
  // only one finger needed
  if (touchEvent->getTouchCount()!=3) return false;
  
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


void CameraRotationRenderer::onDown(const TouchEvent& touchEvent) 
{  
  _camera0 = Camera(*_camera);
  _currentGesture = Rotate;
  
  // middle pixel in 2D 
  Vector2D pixel0 = touchEvent.getTouch(0)->getPos();
  Vector2D pixel1 = touchEvent.getTouch(1)->getPos();
  Vector2D pixel2 = touchEvent.getTouch(2)->getPos();
  Vector2D averagePixel = pixel0.add(pixel1).add(pixel2).div(3);
  _initialPixel = MutableVector3D(averagePixel.x(), averagePixel.y(), 0);
  lastYValid = _initialPixel.y();
  
  // compute center of view
  _initialPoint = _camera->centerOfViewOnPlanet(_planet).asMutableVector3D();
  if (_initialPoint.isNan())
    printf ("CAMERA ERROR: center point does not intersect globe!!\n");
 
  printf ("down 3 fingers\n");
}


void CameraRotationRenderer::onMove(const TouchEvent& touchEvent) 
{
  //_currentGesture = getGesture(touchEvent);
  if (_currentGesture!=Rotate) return;
  
  // current middle pixel in 2D 
  Vector2D c0 = touchEvent.getTouch(0)->getPos();
  Vector2D c1 = touchEvent.getTouch(1)->getPos();
  Vector2D c2 = touchEvent.getTouch(2)->getPos();
  Vector2D cm = c0.add(c1).add(c2).div(3);
  
  // previous middle pixel in 2D 
  Vector2D p0 = touchEvent.getTouch(0)->getPrevPos();
  Vector2D p1 = touchEvent.getTouch(1)->getPrevPos();
  Vector2D p2 = touchEvent.getTouch(2)->getPrevPos();
  Vector2D pm = p0.add(p1).add(p2).div(3);
  
/*  // rotate less than 90 degrees or more than 180 is not allowed
  Vector3D normal = _planet->geodeticSurfaceNormal(_initialPoint.asVector3D());
  Vector3D po = _initialPoint.sub(_camera0.getPosition().asMutableVector3D()).asVector3D();
  double pe = normal.normalized().dot(po.normalized());
  if (pe < -1) pe = -1.0;
  if (pe > 1) pe = 1.0;
  double ang = acos(pe) * 180 / M_PI - (cm.y() - pm.y()) * 0.25;
  
  // don't allow a minimum height above ground
  if (cm.y() < lastYValid && ang < 179) lastYValid = cm.y();
  //double height = GetPosGeo3D().height();
  //if (py > lastYValid && ang > 100 && height > MIN_CAMERA_HEIGHT * 0.25) lastYValid = py;
 */
  
  // horizontal rotation over the original camera horizontal axix
  Vector3D u = _camera0.getHorizontalVector();
  Angle angle = Angle::fromDegrees((cm.y() - lastYValid) * 0.25);
  MutableMatrix44D trans1   = MutableMatrix44D::createTranslationMatrix(_initialPoint.asVector3D());
  MutableMatrix44D rotation = MutableMatrix44D::createRotationMatrix(angle, u);
  MutableMatrix44D trans2   = MutableMatrix44D::createTranslationMatrix(_initialPoint.times(-1.0).asVector3D());
  MutableMatrix44D M        = trans1.multiply(rotation).multiply(trans2);

  // update camera only if new view intersects globe
  Camera camTest(_camera0);
  camTest.applyTransform(M);
  camTest.updateModelMatrix();
  if (!camTest.centerOfViewOnPlanet(_planet).isNan()) {
    _camera->copyFrom(_camera0);
    _camera->applyTransform(M);
    printf ("rotating from %.0f to %.0f.  Angle=%.1f\n", lastYValid, pm.y(), angle.degrees());
  } 
}


void CameraRotationRenderer::onUp(const TouchEvent& touchEvent) 
{
  _currentGesture = None;
  _initialPixel = Vector3D::nan().asMutableVector3D();
  printf ("end rotation\n");
}

