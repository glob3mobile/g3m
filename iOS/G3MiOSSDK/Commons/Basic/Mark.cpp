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
  
  const Camera* camera = rc->getCamera();
  const Vector3D cameraPosition = camera->getPos();
  
  const Vector3D position = planet->toVector3D(_position);
  const Vector3D markCameraVector = position.sub(cameraPosition);
  const double distanceToCamera = markCameraVector.length();
  
  const Vector3D radius = planet->getRadii();
  const double minDist = (radius.x() + radius.y() + radius.z()) * 2;
  
  
  if (distanceToCamera <= minDist || true) {
    const Vector3D normal = planet->geodeticSurfaceNormal(position);
    
    if (normal.angleBetween(markCameraVector).radians() > M_PI / 2) {
      IGL* gl = rc->getGL();
      
      if (_textureId < 1) {
        _textureId = gl->uploadTexture(*_textureImage, 128, 128);
        rc->getLogger()->logInfo("Loaded textureId=%i", _textureId);
      }
      
      //    rc->getLogger()->logInfo(" Visible   << %f %f", minDist, distanceToCamera);
      gl->drawBillBoard(_textureId,
                        (float) position.x(), (float) position.y(), (float) position.z(),
                        camera->getViewPortRatio());
    }
    
  }
  
}
