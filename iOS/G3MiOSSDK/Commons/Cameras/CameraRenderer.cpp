//
//  CameraRenderer.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 30/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "CameraRenderer.hpp"
#include "Context.hpp"
#include "Camera.hpp"
#include "CameraEventHandler.hpp"



void CameraRenderer::initialize(const InitializationContext* ic){
  _logger = ic->getLogger();
}


void CameraRenderer::onResizeViewportEvent(int width, int height) {
  if (_camera != NULL) {
    _camera->resizeViewport(width, height);
  }
}


int CameraRenderer::render(const RenderContext* rc) 
{
  rc->getCamera()->render(rc);
  
  int min = MAX_TIME_TO_RENDER;
  for (unsigned int i=0; i<_handlers.size(); i++) {
    int x = _handlers[i]->render(rc, _gesture);
    if (x<min) min = x; 
  }
  return min;
}


bool CameraRenderer::onTouchEvent(const TouchEvent* touchEvent)
{
  // pass the event to all the handlers
  for (unsigned int n=0; n<_handlers.size(); n++)
    if (_handlers[n]->onTouchEvent(touchEvent, _gesture)) return true;
  
  // if any of them processed the event, return false
  return false;
}


