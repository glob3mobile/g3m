//
//  BusyRenderer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 20/07/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#include "BusyRenderer.hpp"

#include "Context.hpp"
#include "GL.hpp"


void BusyRenderer::initialize(const InitializationContext* ic)
{
  int res = 12;
  _vertices = new float[res * res * 3];
  _numIndices = 2 * (res - 1) * (res + 1);
  _index = new int[_numIndices];
  
  // create vertices
  
  n = 0;
  for (int j = 0; j < res - 1; j++) {
    if (j > 0) _index[n++] = (char) (j * res);
    for (int i = 0; i < res; i++) {
      _index[n++] = (char) (j * res + i);
      _index[n++] = (char) (j * res + i + res);
    }
    _index[n++] = (char) (j * res + 2 * res - 1);
  }
}  


int BusyRenderer::render(const RenderContext* rc) {
  int __TODO_render_a_busy_wheel;
  
  GL* gl = rc->getGL();
  gl->clearScreen(0, 0.2, 0.4, 1);

  return MAX_TIME_TO_RENDER;
}
