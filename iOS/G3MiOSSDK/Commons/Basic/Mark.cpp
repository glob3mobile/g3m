//
//  Mark.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 06/06/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#include "Mark.hpp"
#include "Camera.hpp"

void Mark::render(const RenderContext* rc, const Planet* planet) {
  int __dgd_at_work;

  IGL* gl = rc->getGL();
  
  if (_textureId < 1) {
    _textureId = gl->uploadTexture(*_textureImage, 128, 128);
  }
  
  const Camera* camera = rc->getCamera();
  const Vector3D cameraPosition = camera->getPos();

  const Vector3D vec = planet->toVector3D(_position);
  const double distanceToCamera = vec.sub(cameraPosition).length();
  
  const Vector3D radius = planet->getRadii();
  const double minDist = (radius.x() + radius.y() + radius.z()) / 2;

  
  if (distanceToCamera > minDist) {
    rc->getLogger()->logInfo(" INVISIBLE >> %f %f", minDist, distanceToCamera);
  }
  else {
    rc->getLogger()->logInfo(" Visible   << %f %f", minDist, distanceToCamera);
    gl->drawBillBoard(_textureId,
//                      (float) vec.x(), (float) vec.y(), (float) vec.z(),
                      10000, 10000, 10000,
                      camera->getViewPortRatio());
  }
  
}
