//
//  CameraFlatSingleDragHandler.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo on 10/07/13.
//
//

#include "CameraFlatSingleDragHandler.hpp"
#include "MutableVector2D.hpp"
#include "TouchEvent.hpp"
#include "CameraRenderer.hpp"
#include "GL.hpp"
#include "Plane.hpp"


bool CameraFlatSingleDragHandler::onTouchEvent(const G3MEventContext *eventContext,
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


void CameraFlatSingleDragHandler::onDown(const G3MEventContext *eventContext,
                                     const TouchEvent& touchEvent,
                                     CameraContext *cameraContext) {
  Camera *camera = cameraContext->getNextCamera();
  _camera0.copyFrom(*camera);
  cameraContext->setCurrentGesture(Drag);
  _lastDesp = _despStep = 0.0;
  
  // dragging
  const Vector2I pixel = touchEvent.getTouch(0)->getPos();
  Vector3D origin = _camera0.getCartesianPosition();
  Vector3D ray = _camera0.pixel2Ray(pixel);
  _initialPoint = Plane::intersectionXYPlaneWithRay(origin, ray).asMutableVector3D();
  _lastFinalPoint = _initialPoint;
  
  //printf ("down 1 finger. Initial point = %f %f %f\n", _initialPoint.x(), _initialPoint.y(), _initialPoint.z());
}

void CameraFlatSingleDragHandler::onMove(const G3MEventContext *eventContext,
                                     const TouchEvent& touchEvent,
                                     CameraContext *cameraContext) {
  
  if (cameraContext->getCurrentGesture()!=Drag) return;
  if (_initialPoint.isNan()) return;
  
  // compute final point
  const Vector2I pixel = touchEvent.getTouch(0)->getPos();
  Vector3D origin = _camera0.getCartesianPosition();
  Vector3D ray = _camera0.pixel2Ray(pixel);
  MutableVector3D finalPoint = Plane::intersectionXYPlaneWithRay(origin, ray).asMutableVector3D();
  if (finalPoint.isNan()) return;
  
  // make drag
  Camera *camera = cameraContext->getNextCamera();
  camera->copyFrom(_camera0);
  camera->translateCamera(_initialPoint.asVector3D(), finalPoint.asVector3D());
  
  _lastDirection = _lastFinalPoint.sub(finalPoint);
  _lastFinalPoint = finalPoint;

  //printf ("_lastdirection=%.2f, %.2f, %.2f)  length=%.2f\n", _lastDirection.x(), _lastDirection.y(), _lastDirection.z(), _lastDirection.length());
}


void CameraFlatSingleDragHandler::onUp(const G3MEventContext *eventContext,
                                   const TouchEvent& touchEvent,
                                   CameraContext *cameraContext) {
  if (_useInertia) {
    // test if animation is needed
    const Touch *touch = touchEvent.getTouch(0);
    Vector2I currPixel = touch->getPos();
    Vector2I prevPixel = touch->getPrevPos();
    double desp        = currPixel.sub(prevPixel).length();
    
    if (cameraContext->getCurrentGesture()==Drag && desp>2) {
      // start inertial effect
      Effect *effect = new SingleTranslationEffect(_lastDirection.asVector3D());
      
      EffectTarget* target = cameraContext->getNextCamera()->getEffectTarget();
      eventContext->getEffectsScheduler()->startEffect(effect, target);
    }
  }
  
  // update gesture
  cameraContext->setCurrentGesture(None);
  //_initialPixel = MutableVector2I::zero();
}

void CameraFlatSingleDragHandler::render(const G3MRenderContext* rc, CameraContext *cameraContext)
{
}
