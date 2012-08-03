//
//  CameraSimpleRenderer.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 04/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "CameraSimpleRenderer.hpp"



void CameraSimpleRenderer::initialize(const InitializationContext* ic){
  _logger = ic->getLogger();
}


void CameraSimpleRenderer::onResizeViewportEvent(int width, int height) {
  if (_camera != NULL) {
    _camera->resizeViewport(width, height);
  }
}


int CameraSimpleRenderer::render(const RenderContext* rc) {
  _camera = rc->getCamera(); //Saving camera reference 
  
  _camera->render(rc);
  
  return MAX_TIME_TO_RENDER;
}



