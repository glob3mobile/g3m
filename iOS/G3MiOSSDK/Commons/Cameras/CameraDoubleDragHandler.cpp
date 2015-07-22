//
//  CameraDoubleDragHandler.cpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 28/07/12.
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
  camera->getLookAtParamsInto(_cameraPosition, _cameraCenter, _cameraUp);
  camera->getModelViewMatrixInto(_cameraModelViewMatrix);
  camera->getViewPortInto(_cameraViewPort);

  // double dragging
  const Vector2F pixel0 = touchEvent.getTouch(0)->getPos();
  const Vector2F pixel1 = touchEvent.getTouch(1)->getPos();
  
  const Vector3D& initialRay0 = camera->pixel2Ray(pixel0);
  const Vector3D& initialRay1 = camera->pixel2Ray(pixel1);
  
  if ( initialRay0.isNan() || initialRay1.isNan() ) return;
  
  cameraContext->setCurrentGesture(DoubleDrag);
  eventContext->getPlanet()->beginDoubleDrag(camera->getCartesianPosition(),
                                             camera->getViewDirection(),
                                             camera->pixel2Ray(pixel0),
                                             camera->pixel2Ray(pixel1));
}


void CameraDoubleDragHandler::onMove(const G3MEventContext *eventContext,
                                     const TouchEvent& touchEvent, 
                                     CameraContext *cameraContext) {
  if (cameraContext->getCurrentGesture() != DoubleDrag) return;

  // compute transformation matrix
  const Planet* planet = eventContext->getPlanet();
  const Vector2F pixel0 = touchEvent.getTouch(0)->getPos();
  const Vector2F pixel1 = touchEvent.getTouch(1)->getPos();
  const Vector3D& initialRay0 = Camera::pixel2Ray(_cameraPosition, pixel0,
                                                  _cameraViewPort, _cameraModelViewMatrix);
  const Vector3D& initialRay1 = Camera::pixel2Ray(_cameraPosition, pixel1,
                                                  _cameraViewPort, _cameraModelViewMatrix);
  
   if (initialRay0.isNan() || initialRay1.isNan() ) return;
  
  MutableMatrix44D matrix = planet->doubleDrag(initialRay0,
                                               initialRay1);
  if (!matrix.isValid()) return;

  // apply transformation
  cameraContext->getNextCamera()->setLookAtParams(_cameraPosition.transformedBy(matrix, 1.0),
                                                  _cameraCenter.transformedBy(matrix, 1.0),
                                                  _cameraUp.transformedBy(matrix, 0.0));
}


void CameraDoubleDragHandler::onUp(const G3MEventContext *eventContext,
                                   const TouchEvent& touchEvent, 
                                   CameraContext *cameraContext) 
{
  cameraContext->setCurrentGesture(None);
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
