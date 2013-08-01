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



bool CameraDoubleDragHandler::onTouchEvent(const G3MEventContext *eventContext,
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


void CameraDoubleDragHandler::onDown(const G3MEventContext *eventContext,
                                     const TouchEvent& touchEvent, 
                                     CameraContext *cameraContext) 
{
  Camera *camera = cameraContext->getNextCamera();
  _camera0.copyFrom(*camera);
  cameraContext->setCurrentGesture(DoubleDrag);  

  // double dragging
  const Vector2I pixel0 = touchEvent.getTouch(0)->getPos();
  Vector2I pixel1 = touchEvent.getTouch(1)->getPos();

  
  
  _initialPoint0  = _camera0.pixel2PlanetPoint(pixel0).asMutableVector3D();
  _initialPoint1  = _camera0.pixel2PlanetPoint(pixel1).asMutableVector3D();

  
  
  
  
   // compute rays
   const Vector3D origin = _camera0.getCartesianPosition();
   const Vector3D ray0 = _camera0.pixel2Ray(pixel0);
   const Vector3D ray1 = _camera0.pixel2Ray(pixel1);
  _initialRaysAngle = ray0.angleBetween(ray1).degrees();
  

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
  Vector2I difPixel = pixel1.sub(pixel0);
  _initialFingerSeparation = difPixel.length();
  _initialFingerInclination = difPixel.orientation()._radians;
  
  
  planet->beginDoubleDrag(origin, _camera0.getViewDirection(), ray0, ray1);
  
  //printf ("down 2 finger\n");
}


void CameraDoubleDragHandler::onMove(const G3MEventContext *eventContext,
                                     const TouchEvent& touchEvent, 
                                     CameraContext *cameraContext) {
  
  if (cameraContext->getCurrentGesture() != DoubleDrag) return;

  // compute transformation matrix
  const Planet* planet = eventContext->getPlanet();
  const Vector2I pixel0 = touchEvent.getTouch(0)->getPos();
  const Vector2I pixel1 = touchEvent.getTouch(1)->getPos();
  MutableMatrix44D matrix = planet->doubleDrag(_camera0.pixel2Ray(pixel0),
                                               _camera0.pixel2Ray(pixel1));
  if (!matrix.isValid()) return;

  // apply transformation
  Camera *camera = cameraContext->getNextCamera();
  camera->copyFrom(_camera0);
  camera->applyTransform(matrix);
}


void CameraDoubleDragHandler::onUp(const G3MEventContext *eventContext,
                                   const TouchEvent& touchEvent, 
                                   CameraContext *cameraContext) 
{
  cameraContext->setCurrentGesture(None);
  _initialPixel = Vector3D::nan().asMutableVector3D();
  
  //printf ("end 2 fingers.  gesture=%d\n", _currentGesture);
}


void CameraDoubleDragHandler::render(const G3MRenderContext* rc,
                                     CameraContext *cameraContext)
{  
//  // TEMP TO DRAW A POINT WHERE USER PRESS
//  if (false) {
//    if (cameraContext->getCurrentGesture() == DoubleDrag) {
//      GL* gl = rc->getGL();
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
//      //printf ("zoom with initial point = (%f, %f)\n", g._latitude._degrees, g._longitude._degrees);
//    }
//  }

}
