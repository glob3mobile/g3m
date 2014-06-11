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


void GLErrorRenderer::render(const G3MRenderContext* rc, GLState* glState) {
  GL* gl = rc->getGL();
  const ILogger* logger = rc->getLogger();
  
  int error = gl->getError();
  while (error != GLError::noError()) {
    logger->logError("GL Error: %d", error);
    error = gl->getError();
  }

}
