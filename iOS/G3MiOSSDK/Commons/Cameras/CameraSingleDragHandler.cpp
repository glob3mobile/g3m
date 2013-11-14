//
//  CameraSingleDragHandler.cpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 28/07/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//


#include "CameraSingleDragHandler.hpp"
#include "MutableVector2D.hpp"
#include "TouchEvent.hpp"
#include "CameraRenderer.hpp"
#include "GL.hpp"
#include "IDeviceInfo.hpp"

bool CameraSingleDragHandler::onTouchEvent(const G3MEventContext *eventContext,
                                           const TouchEvent* touchEvent, 
                                           CameraContext *cameraContext) 
{
  // only one finger needed
  if (touchEvent->getTouchCount()!=1) return false;
  if (touchEvent->getTapCount()>1) return false;

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


void CameraSingleDragHandler::onDown(const G3MEventContext *eventContext,
                                     const TouchEvent& touchEvent, 
                                     CameraContext *cameraContext) {  
  Camera *camera = cameraContext->getNextCamera();
  _camera0.copyFrom(*camera);
  cameraContext->setCurrentGesture(Drag); 

  // dragging
  const Vector2I pixel = touchEvent.getTouch(0)->getPos();
  eventContext->getPlanet()->beginSingleDrag(_camera0.getCartesianPosition(),
                                             _camera0.pixel2Ray(pixel));
}


void CameraSingleDragHandler::onMove(const G3MEventContext *eventContext,
                                     const TouchEvent& touchEvent, 
                                     CameraContext *cameraContext) {
  
  if (cameraContext->getCurrentGesture()!=Drag) return;
    
  // compute transformation matrix
  const Planet* planet = eventContext->getPlanet();
  const Vector2I pixel = touchEvent.getTouch(0)->getPos();
  MutableMatrix44D matrix = planet->singleDrag(_camera0.pixel2Ray(pixel));
  if (!matrix.isValid()) return;
  
  // apply transformation
  Camera *camera = cameraContext->getNextCamera();
  camera->copyFrom(_camera0);
  camera->applyTransform(matrix);
}


void CameraSingleDragHandler::onUp(const G3MEventContext *eventContext,
                                   const TouchEvent& touchEvent, 
                                   CameraContext *cameraContext) {
  const Planet* planet = eventContext->getPlanet();
  
  // test if animation is needed
  if (_useInertia) {
    const Touch *touch = touchEvent.getTouch(0);
    const Vector2I currPixel = touch->getPos();
    const Vector2I prevPixel = touch->getPrevPos();
    const double desp        = currPixel.sub(prevPixel).length();

    const float delta = IFactory::instance()->getDeviceInfo()->getPixelsInMM(0.2f);

    if ((cameraContext->getCurrentGesture() == Drag) &&
        (desp > delta)) {
      Effect* effect = planet->createEffectFromLastSingleDrag();
      if (effect != NULL) {
        EffectTarget* target = cameraContext->getNextCamera()->getEffectTarget();
        eventContext->getEffectsScheduler()->startEffect(effect, target);
      }
    }
  }

  // update gesture
  cameraContext->setCurrentGesture(None);
}

void CameraSingleDragHandler::render(const G3MRenderContext* rc, CameraContext *cameraContext)
{
//  // TEMP TO DRAW A POINT WHERE USER PRESS
//  if (false) {
//    if (cameraContext->getCurrentGesture() == Drag) {
//      GL* gl = rc->getGL();
//      float vertices[] = { 0,0,0};
//      int indices[] = {0};
//      gl->enableVerticesPosition();
//      gl->disableTexture2D();
//      gl->disableTextures();
//      gl->vertexPointer(3, 0, vertices);
//      gl->color((float) 0, (float) 1, (float) 0, 1);
//      gl->pointSize(60);
//      gl->pushMatrix();
//      MutableMatrix44D T = MutableMatrix44D::createTranslationMatrix(_initialPoint.asVector3D());
//      gl->multMatrixf(T);
//      gl->drawPoints(1, indices);
//      gl->popMatrix();
//            
//      //Geodetic2D g = _planet->toGeodetic2D(_initialPoint.asVector3D());
//      //printf ("zoom with initial point = (%f, %f)\n", g._latitude._degrees, g._longitude._degrees);
//    }
//  }
}
