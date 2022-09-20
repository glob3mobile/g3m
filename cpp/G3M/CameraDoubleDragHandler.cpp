//
//  CameraDoubleDragHandler.cpp
//  G3M
//
//  Created by Agustin Trujillo Pino on 28/07/12.
//

#include "CameraDoubleDragHandler.hpp"

#include "RenderState.hpp"
#include "TouchEvent.hpp"
#include "Camera.hpp"
#include "CameraContext.hpp"
#include "G3MEventContext.hpp"
#include "Planet.hpp"
#include "ILogger.hpp"


RenderState CameraDoubleDragHandler::getRenderState(const G3MRenderContext* rc) {
  return RenderState::ready();
}

bool CameraDoubleDragHandler::onTouchEvent(const G3MEventContext* eventContext,
                                           const TouchEvent* touchEvent,
                                           CameraContext* cameraContext) {
  // only one finger needed
  if (touchEvent->getTouchCount() != 2) {
    return false;
  }

  switch (touchEvent->getType()) {
    case Down:
      return onDown(eventContext, touchEvent, cameraContext);

    case Move:
      return onMove(eventContext, touchEvent, cameraContext);

    case Up:
      return onUp(eventContext, touchEvent, cameraContext);

    case LongPress:
    case DownUp:
    case MouseWheel:
    default:
      return false;
  }
}

bool CameraDoubleDragHandler::onDown(const G3MEventContext* eventContext,
                                     const TouchEvent* touchEvent,
                                     CameraContext* cameraContext) {
  const Camera* camera = cameraContext->getNextCamera();
  camera->getLookAtParamsInto(_cameraPosition, _cameraCenter, _cameraUp);
  camera->getModelViewMatrixInto(_cameraModelViewMatrix);
  camera->getViewPortInto(_cameraViewPort);

  // double dragging
  const Vector2F pixel0 = touchEvent->getTouch(0)->getPos();
  const Vector2F pixel1 = touchEvent->getTouch(1)->getPos();

  const Vector3D& initialRay0 = camera->pixel2Ray(pixel0);
  const Vector3D& initialRay1 = camera->pixel2Ray(pixel1);

  if ( initialRay0.isNan() || initialRay1.isNan() ) {
    return false;
  }

  cameraContext->setCurrentGesture(DoubleDrag);
  eventContext->getPlanet()->beginDoubleDrag(camera->getCartesianPosition(),
                                             camera->getViewDirection(),
                                             camera->pixel2Ray(pixel0),
                                             camera->pixel2Ray(pixel1));

  return true;
}

bool CameraDoubleDragHandler::onMove(const G3MEventContext* eventContext,
                                     const TouchEvent* touchEvent,
                                     CameraContext* cameraContext) {
  if (cameraContext->getCurrentGesture() != DoubleDrag) {
    //ILogger::instance()->logError("** getCurrentGesture is not DoubleDrag");
    return false;
  }

  // compute transformation matrix
  const Planet* planet = eventContext->getPlanet();
  const Vector2F pixel0 = touchEvent->getTouch(0)->getPos();
  const Vector2F pixel1 = touchEvent->getTouch(1)->getPos();
  const Vector3D& initialRay0 = Camera::pixel2Ray(_cameraPosition, pixel0,
                                                  _cameraViewPort, _cameraModelViewMatrix);
  const Vector3D& initialRay1 = Camera::pixel2Ray(_cameraPosition, pixel1,
                                                  _cameraViewPort, _cameraModelViewMatrix);

  if (initialRay0.isNan() || initialRay1.isNan() ) {
    //ILogger::instance()->logError("** Invalid rays");
    return false;
  }

  MutableMatrix44D matrix = planet->doubleDrag(initialRay0,
                                               initialRay1);
  if (!matrix.isValid()) {
    //ILogger::instance()->logError("** Invalid matrix");
    return false;
  }

  // apply transformation
  cameraContext->getNextCamera()->setLookAtParams(_cameraPosition.transformedBy(matrix, 1.0),
                                                  _cameraCenter.transformedBy(matrix, 1.0),
                                                  _cameraUp.transformedBy(matrix, 0.0));

  return true;
}

bool CameraDoubleDragHandler::onUp(const G3MEventContext* eventContext,
                                   const TouchEvent* touchEvent,
                                   CameraContext* cameraContext) {
  cameraContext->setCurrentGesture(None);
  return true;
}
