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
  
  for (unsigned int i=0; i<_handlers.size(); i++) {
    _handlers[i]->render(rc, _cameraContext);
  }
}

bool CameraRenderer::onTouchEvent(const EventContext* ec,
                                  const TouchEvent* touchEvent) {
  // abort all the camera effect currently running
  if (touchEvent->getType() == Down)
    ec->getEffectsScheduler()->cancellAllEffectsFor((EffectTarget *) _cameraContext);
  
  // pass the event to all the handlers
  for (unsigned int n=0; n<_handlers.size(); n++)
    if (_handlers[n]->onTouchEvent(ec, touchEvent, _cameraContext)) return true;
  
  // if any of them processed the event, return false
  return false;
}

