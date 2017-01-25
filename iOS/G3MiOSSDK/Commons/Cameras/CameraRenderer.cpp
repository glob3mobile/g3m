//
//  CameraRenderer.cpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 30/07/12.
//

#include "CameraRenderer.hpp"

#include "CameraEventHandler.hpp"
#include "G3MRenderContext.hpp"
#include "TouchEvent.hpp"
#include "Camera.hpp"
#include "G3MEventContext.hpp"
#include "ILogger.hpp"
#include "RenderState.hpp"


CameraRenderer::~CameraRenderer() {
  delete _cameraContext;
  const size_t handlersSize = _handlers.size();
  for (size_t i = 0; i < handlersSize; i++) {
    CameraEventHandler* handler = _handlers[i];
    delete handler;
  }
}

void CameraRenderer::removeAllHandlers(bool deleteHandlers) {
  if (deleteHandlers) {
    const size_t handlersSize = _handlers.size();
    for (size_t i = 0; i < handlersSize; i++) {
      CameraEventHandler* handler = _handlers[i];
      delete handler;
    }
  }
  _handlers.clear();
}

void CameraRenderer::render(const G3MRenderContext* rc,
                            GLState* glState) {
  if (_cameraContext == NULL) {
    _cameraContext = new CameraContext(None, rc->getNextCamera());
  }

  // render camera object
  //rc->getCurrentCamera()->render(rc, parentState);

  const size_t handlersSize = _handlers.size();
  for (size_t i = 0; i < handlersSize; i++) {
    _handlers[i]->render(rc, _cameraContext);
  }
}

bool CameraRenderer::onTouchEvent(const G3MEventContext* ec,
                                  const TouchEvent* touchEvent) {
  if (_processTouchEvents) {
    // abort all the camera effect currently running
    if (touchEvent->getType() == Down) {
      EffectTarget* target = _cameraContext->getNextCamera()->getEffectTarget();
      ec->getEffectsScheduler()->cancelAllEffectsFor(target);
    }

    // pass the event to all the handlers
    const size_t handlersSize = _handlers.size();
    for (size_t i = 0; i < handlersSize; i++) {
      if (_handlers[i]->onTouchEvent(ec, touchEvent, _cameraContext)) {
        return true;
      }
    }
  }

  // if no handler processed the event, return not-handled
  return false;
}

void CameraRenderer::removeHandler(CameraEventHandler* handler) {

#ifdef C_CODE
  size_t size = _handlers.size();
  for (size_t i = 0; i < size; i++) {
    if (_handlers[i] == handler) {
      _handlers.erase(_handlers.begin() + i);
      return;
    }
  }
#endif
#ifdef JAVA_CODE
  if ( _handlers.remove(handler) ) {
    return;
  }
#endif

  ILogger::instance()->logError("Could not remove camera handler.");
}

void CameraRenderer::addHandler(CameraEventHandler* handler) {
  _handlers.push_back(handler);
}

RenderState CameraRenderer::getRenderState(const G3MRenderContext* rc) {
  return RenderState::ready();
}
