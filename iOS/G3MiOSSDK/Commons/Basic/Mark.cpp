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

#include "FloatBufferBuilderFromCartesian3D.hpp"


Mark::~Mark() {
  if (_cartesianPosition != NULL) {
    delete _cartesianPosition;
  }
  if (_vertices != NULL) {
    delete _vertices;
  }
}

Vector3D* Mark::getCartesianPosition(const Planet* planet) {
  if (_cartesianPosition == NULL) {
    _cartesianPosition = new Vector3D( planet->toCartesian(_position) );
  }
  return _cartesianPosition;
}

IFloatBuffer* Mark::getVertices(const Planet* planet) {

  if (_vertices == NULL) {
    const Vector3D* pos = getCartesianPosition(planet);

#ifdef C_CODE
    FloatBufferBuilderFromCartesian3D vertex(NoCenter, Vector3D::zero());
#else
    FloatBufferBuilderFromCartesian3D vertex(CenterStrategy.NoCenter, Vector3D::zero());
#endif
    vertex.add(*pos);
    vertex.add(*pos);
    vertex.add(*pos);
    vertex.add(*pos);
    
    _vertices = vertex.create();
  }
  return _vertices;
}

void Mark::render(const RenderContext* rc,
                  const double minDistanceToCamera) {
  const Camera* camera = rc->getCurrentCamera();
  const Planet* planet = rc->getPlanet();
  
  const Vector3D cameraPosition = camera->getCartesianPosition();
//  const Vector3D markPosition = planet->toCartesian(_position);
  const Vector3D* markPosition = getCartesianPosition(planet);
  
  const Vector3D markCameraVector = markPosition->sub(cameraPosition);
  const double distanceToCamera = markCameraVector.length();
  
  if (distanceToCamera <= minDistanceToCamera || true) {
    const Vector3D normalAtMarkPosition = planet->geodeticSurfaceNormal(*markPosition);
    
    if (normalAtMarkPosition.angleBetween(markCameraVector).radians() > GMath.pi() / 2) {
      GL* gl = rc->getGL();
      
      Vector2D tr(0.0,0.0);
      Vector2D scale(1.0,1.0);
      gl->transformTexCoords(scale, tr);
      
      if (!_textureId.isValid()) {
        _textureId = rc->getTexturesHandler()->getGLTextureIdFromFileName(_textureFilename, 128, 128, false);
      }
      
      if (!_textureId.isValid()) {
        rc->getLogger()->logError("Can't load file %s", _textureFilename.c_str());
        return;
      }
      
//    rc->getLogger()->logInfo(" Visible   << %f %f", minDist, distanceToCamera);
      gl->drawBillBoard(_textureId,
                        getVertices(planet),
                        camera->getViewPortRatio());
    }
    
  }
  
}
