//
//  CameraDoubleDragHandler.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 28/07/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

#include "CameraDoubleDragHandler.hpp"
#include "GL.hpp"
#include "TouchEvent.hpp"



bool CameraDoubleDragHandler::onTouchEvent(const EventContext *eventContext,
                                           const TouchEvent* touchEvent, 
                                           CameraContext *cameraContext) 
{
  // only one finger needed
  if (touchEvent->getTouchCount()!=2) return false;
  
  switch (touchEvent->getType()) {
    case Down:
      onDown(eventContext, *touchEvent, cameraContext);
      break;
    case Move:
      onMove(eventContext, *touchEvent, cameraContext);
      break;
    case Up:
      onUp(eventContext, *touchEvent, cameraContext);
    default:
      break;
  }
  
  return true;
}


void CameraDoubleDragHandler::onDown(const EventContext *eventContext,
                                     const TouchEvent& touchEvent, 
                                     CameraContext *cameraContext) 
{
  Camera *camera = cameraContext->getNextCamera();
  _camera0.copyFrom(*camera);
  cameraContext->setCurrentGesture(DoubleDrag);  
  
  // double dragging
  Vector2D pixel0 = touchEvent.getTouch(0)->getPos();
  _initialPoint0  = _camera0.pixel2PlanetPoint(pixel0).asMutableVector3D();
  Vector2D pixel1 = touchEvent.getTouch(1)->getPos();
  _initialPoint1  = _camera0.pixel2PlanetPoint(pixel1).asMutableVector3D();
  
  // both pixels must intersect globe
  if (_initialPoint0.isNan() || _initialPoint1.isNan()) {
    cameraContext->setCurrentGesture(None);
    return;
  }
  
  // middle point in 3D
  const Planet *planet = eventContext->getPlanet();
  Geodetic2D g0 = planet->toGeodetic2D(_initialPoint0.asVector3D());
  Geodetic2D g1 = planet->toGeodetic2D(_initialPoint1.asVector3D());
  Geodetic2D g  = planet->getMidPoint(g0, g1);
  _initialPoint = planet->toCartesian(g).asMutableVector3D();
  
  // fingers difference
  Vector2D difPixel = pixel1.sub(pixel0);
  _initialFingerSeparation = difPixel.length();
  _initialFingerInclination = difPixel.orientation().radians();
  
  //printf ("down 2 finger\n");
}


void CameraDoubleDragHandler::onMove(const EventContext *eventContext,
                                     const TouchEvent& touchEvent, 
                                     CameraContext *cameraContext) 
{
  if (cameraContext->getCurrentGesture() != DoubleDrag) return;
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
    Vector3D centerPoint = tempCamera.getXYZCenterOfView();    
    double distance = tempCamera.getCartesianPosition().sub(centerPoint).length();
    double d = distance*(factor-1)/factor;
    tempCamera.moveForward(d);
    dAccum += d;
    //tempCamera.updateModelMatrix();
    double angle0 = tempCamera.compute3DAngularDistance(pixel0, pixel1).degrees();
    if (GMath.isNan(angle0)) return;
    //printf("distancia angular original = %.4f     d=%.1f   angulo step0=%.4f\n", angle, d, angle0);
 
    // step 1
    d = GMath.abs((distance-d)*0.3);
    if (angle0 < angle) d*=-1;
    tempCamera.moveForward(d);
    dAccum += d;
    //tempCamera.updateModelMatrix();
    double angle1 = tempCamera.compute3DAngularDistance(pixel0, pixel1).degrees();
    double angle_n1=angle0, angle_n=angle1;
    
    // iterations
    int iter=0;
    double precision = GMath.pow(10, GMath.log10(distance)-8.5);
    while (GMath.abs(angle_n-angle) > precision) {
      iter++;
      if ((angle_n1-angle_n)/(angle_n-angle) < 0) d*=-0.5;
      tempCamera.moveForward(d);
      dAccum += d;
      //tempCamera.updateModelMatrix();
      angle_n1 = angle_n;
      angle_n = tempCamera.compute3DAngularDistance(pixel0, pixel1).degrees();  
    }
    //printf("-----------  iteraciones=%d  precision=%f angulo final=%.4f  distancia final=%.1f\n", iter, precision, angle_n, dAccum);
  }
  
  // create temp camera to test gesture first
  Camera tempCamera(_camera0);

  // computer center view point
  Vector3D centerPoint = tempCamera.getXYZCenterOfView();
  
  // drag from initialPoint to centerPoint
  {
    Vector3D initialPoint = _initialPoint.asVector3D();
    const Vector3D rotationAxis = initialPoint.cross(centerPoint);
    const Angle rotationDelta = Angle::fromRadians( - GMath.acos(initialPoint.normalized().dot(centerPoint.normalized())) );
    if (rotationDelta.isNan()) return; 
    tempCamera.rotateWithAxis(rotationAxis, rotationDelta);  
  }
  
  // move the camera
  if (_processZoom) {
    tempCamera.moveForward(dAccum);
  }
     
  // compute 3D point of view center
  //tempCamera.updateModelMatrix();
  Vector3D centerPoint2 = tempCamera.getXYZCenterOfView();
  
//<<<<<<< HEAD
//  // rotate the camera
//  {
//    tempCamera.updateModelMatrix();
//    Vector3D normal = _planet->geodeticSurfaceNormal(_initialPoint.asVector3D());
//    tempCamera.rotateWithAxis(normal, Angle::fromRadians(angle));
//  }
//   
//  // detect new final point
//  {
//    // compute 3D point of view center
//    tempCamera.updateModelMatrix();
//    Vector3D newCenterPoint = tempCamera.centerOfViewOnPlanet(_planet);
//    
//    // middle point in 3D
//    Vector3D ray0 = tempCamera.pixel2Ray(pixel0);
//    Vector3D P0 = _planet->closestIntersection(tempCamera.getPosition(), ray0);
//    Vector3D ray1 = tempCamera.pixel2Ray(pixel1);
//    Vector3D P1 = _planet->closestIntersection(tempCamera.getPosition(), ray1);
//    Geodetic2D g = _planet->getMidPoint(_planet->toGeodetic2D(P0), _planet->toGeodetic2D(P1));
//    Vector3D finalPoint = _planet->toVector3D(g);    
//    
//    // rotate globe from newCenterPoint to finalPoint
//    const Vector3D rotationAxis = newCenterPoint.cross(finalPoint);
//    const Angle rotationDelta = Angle::fromRadians( - acos(newCenterPoint.normalized().dot(finalPoint.normalized())) );
//    if (rotationDelta.isNan()) {
//      return;
//    }
//    tempCamera.rotateWithAxis(rotationAxis, rotationDelta);  
//=======
  // middle point in 3D
  Vector3D P0 = tempCamera.pixel2PlanetPoint(pixel0);
  Vector3D P1 = tempCamera.pixel2PlanetPoint(pixel1);
  const Planet *planet = eventContext->getPlanet();
  Geodetic2D g = planet->getMidPoint(planet->toGeodetic2D(P0), planet->toGeodetic2D(P1));
  Vector3D finalPoint = planet->toCartesian(g);    
  
  // drag globe from centerPoint to finalPoint
  const Vector3D rotationAxis = centerPoint2.cross(finalPoint);
  const Angle rotationDelta = Angle::fromRadians( - GMath.acos(centerPoint2.normalized().dot(finalPoint.normalized())) );
  if (rotationDelta.isNan()) {
    return;
//>>>>>>> master
  }
  tempCamera.rotateWithAxis(rotationAxis, rotationDelta);  
  
  // the gesture was valid. Copy data to final camera
  //tempCamera.updateModelMatrix();
    
  // camera rotation
  if (_processRotation) {
    Vector3D normal = planet->geodeticSurfaceNormal(centerPoint2);
    Vector3D v0     = _initialPoint0.asVector3D().sub(centerPoint2).projectionInPlane(normal);
    Vector3D v1     = tempCamera.pixel2PlanetPoint(pixel0).sub(centerPoint2).projectionInPlane(normal);
    double angle    = v0.angleBetween(v1).degrees();
    double sign     = v1.cross(v0).dot(normal);
    if (sign<0) angle = -angle;
    tempCamera.rotateWithAxisAndPoint(normal, centerPoint2, Angle::fromDegrees(angle));
  }
  
  // copy final transformation to camera
  //tempCamera.updateModelMatrix();
  cameraContext->getNextCamera()->copyFrom(tempCamera);

  //printf ("moving 2 fingers\n");
}


void CameraDoubleDragHandler::onUp(const EventContext *eventContext,
                                   const TouchEvent& touchEvent, 
                                   CameraContext *cameraContext) 
{
  cameraContext->setCurrentGesture(None);
  _initialPixel = Vector3D::nan().asMutableVector3D();
  
  //printf ("end 2 fingers.  gesture=%d\n", _currentGesture);
}


void CameraDoubleDragHandler::render(const RenderContext* rc, CameraContext *cameraContext)
{  
//  // TEMP TO DRAW A POINT WHERE USER PRESS
//  if (false) {
//    if (cameraContext->getCurrentGesture() == DoubleDrag) {
//      GL *gl = rc->getGL();
//      float vertices[] = { 0,0,0};
//      int indices[] = {0};
//      gl->enableVerticesPosition();
//      gl->disableTexture2D();
//      gl->disableTextures();
//      gl->vertexPointer(3, 0, vertices);
//      gl->color((float) 1, (float) 1, (float) 1, 1);
//      gl->pointSize(10);
//      gl->pushMatrix();
//      MutableMatrix44D T = MutableMatrix44D::createTranslationMatrix(_initialPoint.asVector3D());
//      gl->multMatrixf(T);
//      gl->drawPoints(1, indices);
//      gl->popMatrix();
//      
//      // draw each finger
//      gl->pointSize(60);
//      gl->pushMatrix();
//      MutableMatrix44D T0 = MutableMatrix44D::createTranslationMatrix(_initialPoint0.asVector3D());
//      gl->multMatrixf(T0);
//      gl->drawPoints(1, indices);
//      gl->popMatrix();
//      gl->pushMatrix();
//      MutableMatrix44D T1 = MutableMatrix44D::createTranslationMatrix(_initialPoint1.asVector3D());
//      gl->multMatrixf(T1);
//      gl->drawPoints(1, indices);
//      gl->popMatrix();
//      
//      
//      //Geodetic2D g = _planet->toGeodetic2D(_initialPoint.asVector3D());
//      //printf ("zoom with initial point = (%f, %f)\n", g.latitude().degrees(), g.longitude().degrees());
//    }
//  }

}
