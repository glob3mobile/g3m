//
//  CameraCtrlSingleDragHandler.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 5/12/14.
//
//

#include "CameraCtrlSingleDragHandler.hpp"
#include "MutableVector2D.hpp"
#include "TouchEvent.hpp"
#include "CameraRenderer.hpp"
#include "GL.hpp"
#include "IDeviceInfo.hpp"
#include "IFactory.hpp"
#include "Camera.hpp"
#include "Vector2I.hpp"


bool CameraCtrlSingleDragHandler::onTouchEvent(const G3MEventContext *eventContext,
                                           const TouchEvent* touchEvent,
                                           CameraContext *cameraContext)
{
  // only one finger needed and Ctrl
  if (!touchEvent->isCtrlPressed()){ return false;}
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


void CameraCtrlSingleDragHandler::onDown(const G3MEventContext *eventContext,
                                     const TouchEvent& touchEvent,
                                     CameraContext *cameraContext) {
  
  cameraContext->setCurrentGesture(CtrlDrag);
}


void CameraCtrlSingleDragHandler::onMove(const G3MEventContext *eventContext,
                                     const TouchEvent& touchEvent,
                                     CameraContext *cameraContext) {
  
  if (!touchEvent.isCtrlPressed()){
    cameraContext->setCurrentGesture(None);
    return;
  }
  
  if (cameraContext->getCurrentGesture()!=CtrlDrag) return;
  
  Camera* cam = cameraContext->getNextCamera();
  TaitBryanAngles angles = cam->getHeadingPitchRoll();
  
  const Touch* t = touchEvent.getTouch(0);
  
  const Vector2I delta = t->getPos().sub(t->getPrevPos());
  
  const float heading = (((float)delta._x) / cam->getViewPortWidth()) * (_maxHeadingMovementInDegrees * 0.5f);
  const float pitch = (((float)delta._y) / cam->getViewPortHeight()) * (_maxPitchMovementInDegrees * 0.5f);
  
  cam->setHeadingPitchRoll(angles._heading.add(Angle::fromDegrees(heading)),
                           angles._pitch.add(Angle::fromDegrees(pitch)),
                           angles._roll);
}


void CameraCtrlSingleDragHandler::onUp(const G3MEventContext *eventContext,
                                   const TouchEvent& touchEvent,
                                   CameraContext *cameraContext) {
  cameraContext->setCurrentGesture(None);
}

void CameraCtrlSingleDragHandler::render(const G3MRenderContext* rc, CameraContext *cameraContext)
{
}