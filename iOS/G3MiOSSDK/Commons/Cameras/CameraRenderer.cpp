//
//  CameraRenderer.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 04/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "CameraRenderer.hpp"



void CameraRenderer::initialize(const InitializationContext* ic){
  _logger = ic->getLogger();
}


void CameraRenderer::onResizeViewportEvent(int width, int height) {
  if (_camera != NULL) {
    _camera->resizeViewport(width, height);
  }
}


int CameraRenderer::render(const RenderContext* rc) {
  _camera = rc->getCamera(); //Saving camera reference 
  _planet = rc->getPlanet();
  gl = rc->getGL();
  
  _camera->render(rc);
  
  return MAX_TIME_TO_RENDER;
}



