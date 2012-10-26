//
//  CameraRenderer.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 30/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "CameraRenderer.hpp"
#include "Camera.hpp"
#include "CameraEventHandler.hpp"
#include "TouchEvent.hpp"


void CameraRenderer::initialize(const InitializationContext* ic) {
  //_logger = ic->getLogger();
  //cameraContext = new CameraContext(
}

void CameraRenderer::onResizeViewportEvent(const EventContext* ec,
                                           int width, int height) {
  if (_cameraContext != NULL) {
    _cameraContext->getNextCamera()->resizeViewport(width, height);
  }
}

void CameraRenderer::render(const RenderContext* rc) {
  // create the CameraContext
  if (_cameraContext == NULL) {
    _cameraContext = new CameraContext(None, rc->getNextCamera());
  }
  
  // render camera object
  rc->getCurrentCamera()->render(rc);

  const int handlersSize = _handlers.size();
  for (unsigned int i = 0; i < handlersSize; i++) {
    _handlers[i]->render(rc, _cameraContext);
  }
}

bool CameraRenderer::onTouchEvent(const EventContext* ec,
                                  const TouchEvent* touchEvent) {
  if (_processTouchEvents) {
    // abort all the camera effect currently running
    if (touchEvent->getType() == Down){
      EffectTarget* target = _cameraContext->getNextCamera()->getEffectTarget();
      ec->getEffectsScheduler()->cancellAllEffectsFor(target);
    }

    // pass the event to all the handlers
    const int handlersSize = _handlers.size();
    for (unsigned int i = 0; i < handlersSize; i++) {
      if (_handlers[i]->onTouchEvent(ec, touchEvent, _cameraContext)) {
        return true;
      }
    }
  }
  
  // if no handler processed the event, return not-handled
  return false;
}
