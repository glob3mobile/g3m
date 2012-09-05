//
//  MarksRenderer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 05/06/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#include "MarksRenderer.hpp"
#include "Camera.hpp"
#include "GL.hpp"

void MarksRenderer::initialize(const InitializationContext* ic) {
  
}

bool MarksRenderer::onTouchEvent(const EventContext* ec,
                                 const TouchEvent* touchEvent) {
  return false;
}

void MarksRenderer::render(const RenderContext* rc) {
//  rc.getLogger()->logInfo("MarksRenderer::render()");
  
  GL* gl = rc->getGL();
  
  gl->enableVerticesPosition();
  gl->enableTextures();

  gl->disableDepthTest();
  gl->enableBlend();

  const Vector3D radius = rc->getPlanet()->getRadii();
  const double minDistanceToCamera = (radius.x() + radius.y() + radius.z()) * 2;

  int marksSize = _marks.size();
  for (int i = 0; i < marksSize; i++) {
    Mark* mark = _marks[i];
    //rc->getLogger()->logInfo("Rendering Mark: \"%s\"", mark->getName().c_str());

    mark->render(rc, minDistanceToCamera);
  }
  
  gl->enableDepthTest();
  gl->disableBlend();

  gl->disableTextures();
  gl->disableVerticesPosition();
  gl->disableTexture2D();
  
}
