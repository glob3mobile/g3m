//
//  CameraDoubleTapHandler.cpp
//  G3M
//
//  Created by Agustin Trujillo Pino on 07/08/12.
//


#include "CameraDoubleTapHandler.hpp"

#include "MutableVector2D.hpp"
#include "GL.hpp"
#include "TouchEvent.hpp"
#include "G3MEventContext.hpp"
#include "Planet.hpp"
#include "Camera.hpp"
#include "Effects.hpp"
#include "CameraContext.hpp"
#include "RenderState.hpp"


RenderState CameraDoubleTapHandler::getRenderState(const G3MRenderContext* rc) {
  return RenderState::ready();
}

bool CameraDoubleTapHandler::onTouchEvent(const G3MEventContext *eventContext,
                                          const TouchEvent* touchEvent,
                                          CameraContext *cameraContext) {
  // only one finger needed
  if (touchEvent->getTouchCount() != 1) {
    return false;
  }
  if (touchEvent->getTapCount() != 2) {
    return false;
  }
  if (touchEvent->getType() != Down) {
    return false;
  }

  onDown(eventContext, *touchEvent, cameraContext);
  return true;
}

void CameraDoubleTapHandler::onDown(const G3MEventContext *eventContext,
                                    const TouchEvent& touchEvent,
                                    CameraContext *cameraContext) {
  const Vector2F pixel = touchEvent.getTouch(0)->getPos();
  const Planet* planet = eventContext->getPlanet();
  const Camera* camera = cameraContext->getNextCamera();
  Effect* effect = planet->createDoubleTapEffect(camera->getCartesianPosition(),
                                                 camera->getViewDirection(),
                                                 camera->pixel2Ray(pixel));

  if (effect != NULL) {
    EffectTarget* target = cameraContext->getNextCamera()->getEffectTarget();
    eventContext->getEffectsScheduler()->startEffect(effect, target);
  }
}
