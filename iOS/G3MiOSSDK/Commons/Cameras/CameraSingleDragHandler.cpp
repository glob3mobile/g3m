//
//  CameraSingleDragHandler.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 28/07/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//


#include "CameraSingleDragHandler.hpp"
#include "MutableVector2D.hpp"
#include "TouchEvent.hpp"
#include "CameraRenderer.hpp"
#include "GL.hpp"


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
//  _axis = MutableVector3D::nan();
//  _lastRadians = _radiansStep = 0.0;
  
  // dragging
  const Vector2I pixel = touchEvent.getTouch(0)->getPos();
  _origin = _camera0.getCartesianPosition().asMutableVector3D();
  _initialRay = _camera0.pixel2Ray(pixel).asMutableVector3D();

  //_initialPixel = pixel.asMutableVector2I();
  //_initialPoint = _camera0.pixel2PlanetPoint(pixel).asMutableVector3D();
  
/*
  // TEMP AGUSTIN TO TEST OBJECT ELLIPSOIDSHAPE
  if (pixel._x<50) {
    Geodetic3D center(Angle::fromDegrees(39.78), Angle::fromDegrees(-122), 0);
    camera->setPointOfView(center, 1e6, Angle::fromDegrees(180), Angle::fromDegrees(90));
  }*/
  

  
  //printf ("down 1 finger. Initial point = %f %f %f\n", _initialPoint.x(), _initialPoint.y(), _initialPoint.z());
}

void CameraSingleDragHandler::onMove(const G3MEventContext *eventContext,
                                     const TouchEvent& touchEvent, 
                                     CameraContext *cameraContext) {
  
  if (cameraContext->getCurrentGesture()!=Drag) return;
  
 /* if (_initialPoint.isNan()) {
    return;
  }*/
  
  // get final ray
  const Vector2I pixel = touchEvent.getTouch(0)->getPos();
  Vector3D finalRay = _camera0.pixel2Ray(pixel);
  
  // compute transformation matrix
  const Planet* planet = eventContext->getPlanet();
  MutableMatrix44D matrix = planet->dragBetweenIntersections(_origin.asVector3D(),
                                                             _initialRay.asVector3D(),
                                                             finalRay);
  if (!matrix.isValid()) return;
  
  // apply transformation
  Camera *camera = cameraContext->getNextCamera();
  camera->copyFrom(_camera0);
  camera->applyTransform(matrix);

/*
  // save drag parameters
  _axis = _initialPoint.cross(finalPoint);
  
  const double radians = - IMathUtils::instance()->asin(_axis.length()/_initialPoint.length()/finalPoint.length());
  _radiansStep = radians - _lastRadians;
  _lastRadians = radians;*/
}


void CameraSingleDragHandler::onUp(const G3MEventContext *eventContext,
                                   const TouchEvent& touchEvent, 
                                   CameraContext *cameraContext) {
   if (_useInertia) {
    // test if animation is needed
    const Touch *touch = touchEvent.getTouch(0);
    Vector2I currPixel = touch->getPos();
    Vector2I prevPixel = touch->getPrevPos();
    double desp        = currPixel.sub(prevPixel).length();

    if (cameraContext->getCurrentGesture()==Drag && desp>2) {
      Effect* effect = eventContext->getPlanet()->createEffectFromLastDrag();
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
