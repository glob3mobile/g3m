//
//  CameraRenderer.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 04/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "CameraRenderer.hpp"

#include "Camera.hpp"

void CameraRenderer::initialize(const InitializationContext* ic){}

int CameraRenderer::render(const RenderContext* rc)
{
  rc->getCamera()->Draw(rc);
}

bool CameraRenderer::onTouchEvent(const TouchEvent* event){

  int __TODO_JM_at_work;
  
  return false;
}