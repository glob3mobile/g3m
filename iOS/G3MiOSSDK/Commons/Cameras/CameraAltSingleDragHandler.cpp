//
//  CameraAltSingleDragHandler.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 5/12/14.
//
//

#include "CameraAltSingleDragHandler.hpp"
#include "MutableVector2D.hpp"
#include "TouchEvent.hpp"
#include "CameraRenderer.hpp"
#include "GL.hpp"
#include "IDeviceInfo.hpp"
#include "IFactory.hpp"
#include "Camera.hpp"
#include "Vector2I.hpp"


bool CameraAltSingleDragHandler::onTouchEvent(const G3MEventContext *eventContext,
                                           const TouchEvent* touchEvent,
                                           CameraContext *cameraContext)
{
  // only one finger needed and Alt
  if (!touchEvent->isAltPressed()){ return false;}
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


void CameraAltSingleDragHandler::onDown(const G3MEventContext *eventContext,
                                     const TouchEvent& touchEvent,
                                     CameraContext *cameraContext) {
  
  cameraContext->setCurrentGesture(AltDrag);
}


void CameraAltSingleDragHandler::onMove(const G3MEventContext *eventContext,
                                     const TouchEvent& touchEvent,
                                     CameraContext *cameraContext) {
  
  if (!touchEvent.isAltPressed()){
    cameraContext->setCurrentGesture(None);
    return;
  }
  
  if (cameraContext->getCurrentGesture()!=AltDrag) return;
  
  Camera* cam = cameraContext->getNextCamera();
  TaitBryanAngles angles = cam->getHeadingPitchRoll();
  
  const Touch* t = touchEvent.getTouch(0);
  
  const Vector2I delta = t->getPos().sub(t->getPrevPos());
  
  const float heading = (((float)delta._x) / cam->getViewPortWidth()) * (_maxHeadingMovementInDegrees * 0.5f);
  const float pitch = (((float)delta._y) / cam->getViewPortHeight()) * (_maxPitchMovementInDegrees * 0.5f);
  
  double finalHeading = angles._heading._degrees + heading;
  
  double finalPitch = angles._pitch._degrees + pitch;
  if (finalPitch < -90){ //Boundaries
    finalPitch = -90;
  } else if (finalPitch > 90){
    finalPitch = 90;
  }
  
  cam->setHeadingPitchRoll(Angle::fromDegrees(finalHeading),
                           Angle::fromDegrees(finalPitch),
                           angles._roll);
}


void CameraAltSingleDragHandler::onUp(const G3MEventContext *eventContext,
                                   const TouchEvent& touchEvent,
                                   CameraContext *cameraContext) {
  cameraContext->setCurrentGesture(None);
}

void CameraAltSingleDragHandler::render(const G3MRenderContext* rc, CameraContext *cameraContext)
{
}