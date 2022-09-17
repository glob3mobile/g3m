//
//  CameraSingleDragHandler.cpp
//  G3M
//
//  Created by Agustin Trujillo Pino on 28/07/12.
//


#include "CameraSingleDragHandler.hpp"

#include "Camera.hpp"
#include "TouchEvent.hpp"
#include "G3MEventContext.hpp"
#include "Planet.hpp"
#include "IFactory.hpp"
#include "IDeviceInfo.hpp"
#include "CameraContext.hpp"
#include "RenderState.hpp"


RenderState CameraSingleDragHandler::getRenderState(const G3MRenderContext* rc) {
  return RenderState::ready();
}

CameraSingleDragHandler::CameraSingleDragHandler(bool useInertia) :
_useInertia(useInertia),
_previousEventPosition0(NULL),
_previousEventPosition1(NULL)
{
}

CameraSingleDragHandler::~CameraSingleDragHandler() {
  delete _previousEventPosition0;
  delete _previousEventPosition1;

#ifdef JAVA_CODE
  super.dispose();
#endif
}

bool CameraSingleDragHandler::onTouchEvent(const G3MEventContext *eventContext,
                                           const TouchEvent* touchEvent,
                                           CameraContext *cameraContext) {
  if (touchEvent->getTouchCount() != 1) {
    return false;
  }
  if (touchEvent->getTapCount() > 1) {
    return false;
  }

  switch (touchEvent->getType()) {
    case Down:
      onDown(eventContext, *touchEvent, cameraContext);
      return true;

    case Move:
      onMove(eventContext, *touchEvent, cameraContext);
      return true;

    case Up:
      onUp(eventContext, *touchEvent, cameraContext);
      return true;

    default:
      return false;
  }

}

void CameraSingleDragHandler::onDown(const G3MEventContext *eventContext,
                                     const TouchEvent& touchEvent,
                                     CameraContext *cameraContext) {
  const Camera *camera = cameraContext->getNextCamera();
  camera->getLookAtParamsInto(_cameraPosition, _cameraCenter, _cameraUp);
  camera->getModelViewMatrixInto(_cameraModelViewMatrix);
  camera->getViewPortInto(_cameraViewPort);

  // dragging
  const Vector2F pixel      = touchEvent.getTouch(0)->getPos();
  const Vector3D initialRay = camera->pixel2Ray(pixel);
  if (!initialRay.isNan()) {
    cameraContext->setCurrentGesture(Drag);
    eventContext->getPlanet()->beginSingleDrag(camera->getCartesianPosition(), initialRay);
  }
}

void CameraSingleDragHandler::onMove(const G3MEventContext *eventContext,
                                     const TouchEvent& touchEvent,
                                     CameraContext *cameraContext) {

  if (cameraContext->getCurrentGesture() != Drag) {
    return;
  }

  //check finalRay
  const Vector2F pixel = touchEvent.getTouch(0)->getPos();
  Camera::pixel2RayInto(_cameraPosition, pixel,
                        _cameraViewPort, _cameraModelViewMatrix, _finalRay);
  if (_finalRay.isNan()) {
    return;
  }

  // compute transformation matrix
  const Planet* planet = eventContext->getPlanet();
  const MutableMatrix44D matrix = planet->singleDrag(_finalRay.asVector3D());
  if (!matrix.isValid()) {
    return;
  }

  // apply transformation
  cameraContext->getNextCamera()->setLookAtParams(_cameraPosition.transformedBy(matrix, 1.0),
                                                  _cameraCenter.transformedBy(matrix, 1.0),
                                                  _cameraUp.transformedBy(matrix, 0.0));
  delete _previousEventPosition1;
  _previousEventPosition1 = _previousEventPosition0;
  _previousEventPosition0 = new Vector2F(pixel);
}


const Vector2F* CameraSingleDragHandler::getPreviousEventPosition(const Vector2F& currentPosition) const {

  if ((_previousEventPosition1 == NULL) && (_previousEventPosition0 == NULL)) {
    return NULL;
  }
  else if (_previousEventPosition1 == NULL) {
    return _previousEventPosition0;
  }
  else if (_previousEventPosition0 == NULL) {
    return _previousEventPosition1;
  }
  else {
    const double desp0 = _previousEventPosition0->squaredDistanceTo(currentPosition);
    return (desp0 == 0) ? _previousEventPosition1 : _previousEventPosition0;
  }
}


void CameraSingleDragHandler::onUp(const G3MEventContext *eventContext,
                                   const TouchEvent& touchEvent,
                                   CameraContext *cameraContext) {

  // test if animation is needed
  if (_useInertia && (cameraContext->getCurrentGesture() == Drag)) {
    const Touch *touch = touchEvent.getTouch(0);
    const Vector2F currentPosition = touch->getPos();
    const Vector2F* previousEventPosition = getPreviousEventPosition(currentPosition);
    if (previousEventPosition != NULL) {
      const double desp = previousEventPosition->squaredDistanceTo(currentPosition);

#warning method getPixelsInMM is not working fine in iOS devices
      const float delta = IFactory::instance()->getDeviceInfo()->getPixelsInMM(0.2f);
      if (desp > delta) {
        const Planet* planet = eventContext->getPlanet();
        Effect* effect = planet->createEffectFromLastSingleDrag();
        if (effect != NULL) {
          EffectTarget* target = cameraContext->getNextCamera()->getEffectTarget();
          eventContext->getEffectsScheduler()->startEffect(effect, target);
        }
      }
    }
  }

  delete _previousEventPosition0;
  _previousEventPosition0 = NULL;
  delete _previousEventPosition1;
  _previousEventPosition1 = NULL;

  cameraContext->setCurrentGesture(None);
}
