//
//  CameraMouseWheelHandler.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 10/10/14.
//
//

#include "CameraMouseWheelHandler.hpp"
#include "Camera.hpp"
#include "TouchEvent.hpp"
#include "G3MWidget.hpp"

bool CameraMouseWheelHandler::onTouchEvent(const G3MEventContext *eventContext,
                  const TouchEvent* touchEvent,
                  CameraContext *cameraContext){
  
  if (touchEvent->getType() == MouseWheelChanged){
    onMouseWheel(eventContext, *touchEvent, cameraContext);
    return true;
  }
  return false;
  
/*
  //**** THIS CODE IS TO TEST MOUSEWHEELHANDLER
  // only one finger needed
  if (touchEvent->getTouchCount()!=1) return false;
  if (touchEvent->getTapCount()>1) return false;
  if (touchEvent->getType() == MouseWheelChanged){
    return false;
  }
  switch (touchEvent->getType()) {
    case Down:
      onMouseWheel(eventContext, *touchEvent, cameraContext);
      break;
    default:
      break;
  }
  return true;
*/

}


void CameraMouseWheelHandler::onMouseWheel(const G3MEventContext *eventContext,
                                           const TouchEvent& touchEvent,
                                           CameraContext *cameraContext){
  MutableVector3D cameraPosition;
  MutableVector3D cameraCenter;
  MutableVector3D cameraUp;
  MutableVector2I cameraViewPort;
  MutableMatrix44D cameraModelViewMatrix;
  
  // save params
  Camera *camera = cameraContext->getNextCamera();
  camera->getLookAtParamsInto(cameraPosition, cameraCenter, cameraUp);
  camera->getModelViewMatrixInto(cameraModelViewMatrix);
  camera->getViewPortInto(cameraViewPort);
  
  const double delta = touchEvent.getMouseWheelDelta();
  double factor = 0.1;
  if (delta < 0){
    factor *= -1;
  }
  
  G3MWidget* widget = eventContext->getWidget();
  const Vector2F pixel = touchEvent.getTouch(0)->getPos();
  Vector3D touchedPosition = widget->getScenePositionForPixel((int)pixel._x, (int)pixel._y);
  if (touchedPosition.isNan()) return;
  
  const Vector3D& initialRay = Camera::pixel2Ray(cameraPosition, pixel,
                                                 cameraViewPort, cameraModelViewMatrix);
  if (initialRay.isNan()) return;
  
  const Planet* planet = eventContext->getPlanet();
  MutableMatrix44D matrix = planet->zoomUsingMouseWheel(factor,
                                                        camera->getCartesianPosition(),
                                                        camera->getViewDirection(),
                                                        widget->getScenePositionForCentralPixel(),
                                                        touchedPosition,
                                                        initialRay);
  if (!matrix.isValid()) return;
  
  // apply transformation
  cameraContext->getNextCamera()->setLookAtParams(cameraPosition.transformedBy(matrix, 1.0),
                                                  cameraCenter.transformedBy(matrix, 1.0),
                                                  cameraUp.transformedBy(matrix, 0.0));

}