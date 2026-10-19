//
//  CameraRenderer.cpp
//  G3M
//
//  Created by Agustin Trujillo Pino on 30/07/12.
//

#include "CameraRenderer.hpp"


#include "CameraEventHandler.hpp"
#include "G3MRenderContext.hpp"
#include "CameraContext.hpp"
#include "TouchEvent.hpp"
#include "Camera.hpp"
#include "G3MEventContext.hpp"
#include "RenderState.hpp"
#include "ILogger.hpp"


CameraRenderer::~CameraRenderer() {
  delete _cameraContext;
  for (size_t i = 0; i < _handlersSize; i++) {
    CameraEventHandler* handler = _handlers[i];
    delete handler;
  }
}

void CameraRenderer::render(const G3MRenderContext* rc,
                            GLState* glState) {
  if (_cameraContext == NULL) {
    _cameraContext = new CameraContext(None, rc->getNextCamera());
  }

  //rc->getCurrentCamera()->render(rc, parentState);

  for (size_t i = 0; i < _handlersSize; i++) {
    CameraEventHandler* handler = _handlers[i];
    handler->render(rc, _cameraContext);
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
    for (size_t i = 0; i < _handlersSize; i++) {
      CameraEventHandler* handler = _handlers[i];
      if (handler->onTouchEvent(ec, touchEvent, _cameraContext)) {
        if (_verboseHandlers) {
          ILogger::instance()->logInfo("== %s handled event %s",
                                       handler->name().c_str(),
                                       touchEvent->description().c_str());
        }
        return true;
      }
    }
  }

  if (_verboseHandlers) {
    ILogger::instance()->logInfo("== NOT HANDLED event %s",
                                 touchEvent->description().c_str());
  }

  // if no handler processed the event, return not-handled
  return false;
}

RenderState CameraRenderer::getRenderState(const G3MRenderContext* rc) {
  _renderStateCollector.reset();

  for (int i = 0; i < _handlersSize; i++) {
    CameraEventHandler* handler = _handlers[i];
    _renderStateCollector.add(handler->getRenderState(rc));
  }

  return _renderStateCollector.getState();
}

void CameraRenderer::addHandler(CameraEventHandler* handler) {
  _handlers.push_back(handler);
  _handlersSize = _handlers.size();
}

void CameraRenderer::removeHandler(CameraEventHandler* handler) {
#ifdef C_CODE
  for (size_t i = 0; i < _handlersSize; i++) {
    if (_handlers[i] == handler) {
      _handlers.erase(_handlers.begin() + i);
      _handlersSize = _handlers.size();
      return;
    }
  }
#endif
#ifdef JAVA_CODE
  if ( _handlers.remove(handler) ) {
    _handlersSize = _handlers.size();
    return;
  }
#endif
  ILogger::instance()->logError("Could not remove camera handler.");
}

void CameraRenderer::removeAllHandlers(bool deleteHandlers) {
  if (deleteHandlers) {
    for (size_t i = 0; i < _handlersSize; i++) {
      CameraEventHandler* handler = _handlers[i];
      delete handler;
    }
  }
  _handlers.clear();
  _handlersSize = 0;
}
