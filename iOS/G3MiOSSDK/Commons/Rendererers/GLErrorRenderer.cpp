//
//  GLErrorRenderer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 20/06/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#include "GLErrorRenderer.hpp"

#include "Context.hpp"
#include "GL.hpp"


void GLErrorRenderer::initialize(const InitializationContext* ic) {
  
}

bool GLErrorRenderer::onTouchEvent(const EventContext* ec,
                                   const TouchEvent* touchEvent) {
  return false;
}

GLErrorRenderer::~GLErrorRenderer() {
}

void GLErrorRenderer::onResizeViewportEvent(const EventContext* ec,
                                            int width, int height) {
}

int GLErrorRenderer::render(const RenderContext *rc) {
  GL* gl = rc->getGL();
  const ILogger* logger = rc->getLogger();
  
  GLError error = gl->getError();
#ifdef C_CODE
  while (error != NoError) {
#else
  while (error != GLError.NoError) { 
#endif
    logger->logError("GL Error: %i", error);
    error = gl->getError();
  }
  
  return Renderer::maxTimeToRender;
}
