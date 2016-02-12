//
//  GLErrorRenderer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 20/06/12.
//

#include "GLErrorRenderer.hpp"

#include "G3MRenderContext.hpp"
#include "GL.hpp"


void GLErrorRenderer::render(const G3MRenderContext* rc,
                             GLState* glState) {
  GL* gl = rc->getGL();
  const ILogger* logger = rc->getLogger();
  
  int error = gl->getError();
  while (error != GLError::noError()) {
    logger->logError("GL Error: %d", error);
    error = gl->getError();
  }

}
