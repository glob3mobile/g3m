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
  _initialPoint0  = _camera0.pixel2PlanetPoint(pixel0).asMutableVector3D();
  Vector2D pixel1 = touchEvent.getTouch(1)->getPos();
  _initialPoint1  = _camera0.pixel2PlanetPoint(pixel1).asMutableVector3D();
  
  // both pixels must intersect globe
  if (_initialPoint0.isNan() || _initialPoint1.isNan()) {
    _currentGesture = None;
    return;
  }
  
  // middle point in 3D
  Geodetic2D g0 = _planet->toGeodetic2D(_initialPoint0.asVector3D());
  Geodetic2D g1 = _planet->toGeodetic2D(_initialPoint1.asVector3D());
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
  if (_currentGesture!=DoubleDrag) return;
  if (_initialPoint.isNan()) return;
    
  Vector2D pixel0 = touchEvent.getTouch(0)->getPos();
  Vector2D pixel1 = touchEvent.getTouch(1)->getPos();    
  Vector2D difPixel = pixel1.sub(pixel0);
  double finalFingerSeparation = difPixel.length();
  double factor = finalFingerSeparation/_initialFingerSeparation;
  
  // compute camera translation using numerical iterations until convergence
  double dAccum=0;
  {
    Camera tempCamera(_camera0);
    Angle originalAngle = _initialPoint0.angleBetween(_initialPoint1);
    double angle = originalAngle.degrees();
    
    // compute estimated camera translation
    Vector3D centerPoint = tempCamera.centerOfViewOnPlanet();    
    double distance = tempCamera.getPosition().sub(centerPoint).length();
    double d = distance*(factor-1)/factor;
    tempCamera.moveForward(d);
    dAccum += d;
    tempCamera.updateModelMatrix();
    double angle0 = tempCamera.compute3DAngularDistance(pixel0, pixel1).degrees();
    if (isnan(angle0)) return;
    //printf("distancia angular original = %.4f     d=%.1f   angulo step0=%.4f\n", angle, d, angle0);
 
    // step 1
    d = fabs((distance-d)*0.3);
    if (angle0 < angle) d*=-1;
    tempCamera.moveForward(d);
    dAccum += d;
    tempCamera.updateModelMatrix();
    double angle1 = tempCamera.compute3DAngularDistance(pixel0, pixel1).degrees();
    double angle_n1=angle0, angle_n=angle1;
    
    // iterations
    int iter=0;
    double precision = pow(10, log10(distance)-8.5);
    while (fabs(angle_n-angle) > precision) {
      iter++;
      if ((angle_n1-angle_n)/(angle_n-angle) < 0) d*=-0.5;
      tempCamera.moveForward(d);
      dAccum += d;
      tempCamera.updateModelMatrix();
      angle_n1 = angle_n;
      angle_n = tempCamera.compute3DAngularDistance(pixel0, pixel1).degrees();  
    }
    //printf("-----------  iteraciones=%d  precision=%f angulo final=%.4f  distancia final=%.1f\n", iter, precision, angle_n, dAccum);
  }
  
  // create temp camera to test gesture first
  Camera tempCamera(_camera0);

  // computer center view point
  Vector3D centerPoint = tempCamera.centerOfViewOnPlanet();
  
  // drag from initialPoint to centerPoint
  {
    Vector3D initialPoint = _initialPoint.asVector3D();
    const Vector3D rotationAxis = initialPoint.cross(centerPoint);
    const Angle rotationDelta = Angle::fromRadians( - acos(initialPoint.normalized().dot(centerPoint.normalized())) );
    if (rotationDelta.isNan()) return; 
    tempCamera.rotateWithAxis(rotationAxis, rotationDelta);  
  }
  
  // move the camara 
  tempCamera.moveForward(dAccum);
     
  // compute 3D point of view center
  tempCamera.updateModelMatrix();
  Vector3D centerPoint2 = tempCamera.centerOfViewOnPlanet();
  
  // middle point in 3D
  Vector3D P0 = tempCamera.pixel2PlanetPoint(pixel0);
  Vector3D P1 = tempCamera.pixel2PlanetPoint(pixel1);
  Geodetic2D g = _planet->getMidPoint(_planet->toGeodetic2D(P0), _planet->toGeodetic2D(P1));
  Vector3D finalPoint = _planet->toVector3D(g);    
  
  // drag globe from centerPoint to finalPoint
  const Vector3D rotationAxis = centerPoint2.cross(finalPoint);
  const Angle rotationDelta = Angle::fromRadians( - acos(centerPoint2.normalized().dot(finalPoint.normalized())) );
  if (rotationDelta.isNan()) {
    return;
  }
  tempCamera.rotateWithAxis(rotationAxis, rotationDelta);  
  
  // the gesture was valid. Copy data to final camera
  tempCamera.updateModelMatrix();
    
  // camera rotation
  {
    Vector3D normal = _planet->geodeticSurfaceNormal(centerPoint2);
    Vector3D v0     = _initialPoint0.asVector3D().sub(centerPoint2).projectionInPlane(normal);
    Vector3D v1     = tempCamera.pixel2PlanetPoint(pixel0).sub(centerPoint2).projectionInPlane(normal);
    double angle    = v0.angleBetween(v1).degrees();
    double sign     = v1.cross(v0).dot(normal);
    if (sign<0) angle = -angle;
    tempCamera.rotateWithAxisAndPoint(normal, centerPoint2, Angle::fromDegrees(angle));
  }
  
  // copy final transformation to camera
  tempCamera.updateModelMatrix();
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
      int indices[] = {0};
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
      MutableMatrix44D T0 = MutableMatrix44D::createTranslationMatrix(_initialPoint0.asVector3D());
      _gl->multMatrixf(T0);
      _gl->drawPoints(1, indices);
      _gl->popMatrix();
      _gl->pushMatrix();
      MutableMatrix44D T1 = MutableMatrix44D::createTranslationMatrix(_initialPoint1.asVector3D());
      _gl->multMatrixf(T1);
      _gl->drawPoints(1, indices);
      _gl->popMatrix();
      
      
      //Geodetic2D g = _planet->toGeodetic2D(_initialPoint.asVector3D());
      //printf ("zoom with initial point = (%f, %f)\n", g.latitude().degrees(), g.longitude().degrees());
    }
  }
  
  return MAX_TIME_TO_RENDER;
}

