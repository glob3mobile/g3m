//
//  CameraSingleDragHandler.cpp
//  G3MiOSSDK
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
_useInertia(useInertia)
{
}

CameraSingleDragHandler::~CameraSingleDragHandler() {
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
}

void CameraSingleDragHandler::onUp(const G3MEventContext *eventContext,
                                   const TouchEvent& touchEvent,
                                   CameraContext *cameraContext) {
  const Planet* planet = eventContext->getPlanet();

  // test if animation is needed
  if (_useInertia) {
    const Touch *touch = touchEvent.getTouch(0);
    const Vector2F currPixel = touch->getPos();
    const Vector2F prevPixel = touch->getPrevPos();
    const double desp        = currPixel.sub(prevPixel).length();

#warning method getPixelsInMM is not working fine in iOS devices
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

  cameraContext->setCurrentGesture(None);
}

void CameraSingleDragHandler::render(const G3MRenderContext* rc, CameraContext *cameraContext) {
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
