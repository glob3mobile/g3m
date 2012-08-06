//
//  Mark.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 06/06/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#include "Mark.hpp"
#include "Camera.hpp"
#include "GL.hpp"
#include "TexturesHandler.hpp"


void Mark::render(const RenderContext* rc,
                  const double minDistanceToCamera) {
  const Camera* camera = rc->getCamera();
  const Planet* planet = rc->getPlanet();
  
  const Vector3D cameraPosition = camera->getPosition();
  const Vector3D markPosition = planet->toVector3D(_position);
  
  const Vector3D markCameraVector = markPosition.sub(cameraPosition);
  const double distanceToCamera = markCameraVector.length();
  
  if (distanceToCamera <= minDistanceToCamera || true) {
    const Vector3D normalAtMarkPosition = planet->geodeticSurfaceNormal(markPosition);
    
    if (normalAtMarkPosition.angleBetween(markCameraVector).radians() > M_PI / 2) {
      GL* gl = rc->getGL();
      
      Vector2D tr(0.0,0.0);
      Vector2D scale(1.0,1.0);
      gl->transformTexCoords(scale, tr);
      
      if (_textureId < 1) {
        _textureId = rc->getTexturesHandler()->getTextureIdFromFileName(_textureFilename, 128, 128);
      }
      
      if (_textureId < 1) {
        rc->getLogger()->logError("Can't load file %s", _textureFilename.c_str());
        return;
      }
      
//    rc->getLogger()->logInfo(" Visible   << %f %f", minDist, distanceToCamera);
      gl->drawBillBoard(_textureId, markPosition, camera->getViewPortRatio());
    }
    
  }
  
}
