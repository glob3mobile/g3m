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
#include "TextureBuilder.hpp"

#include "FloatBufferBuilderFromCartesian3D.hpp"

#include "IGLTextureId.hpp"

Vector2D Mark::_textureTranslation(0.0, 0.0);
Vector2D Mark::_textureScale(1.0, 1.0);

void Mark::initialize(const InitializationContext* ic) {
//  todo;
}

bool Mark::isReady() const {
//  todo;
}

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
    
    FloatBufferBuilderFromCartesian3D vertex(CenterStrategy::noCenter(), Vector3D::zero());
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
  const Vector3D* markPosition = getCartesianPosition(planet);
  
  const Vector3D markCameraVector = markPosition->sub(cameraPosition);
  //  const double distanceToCamera = markCameraVector.length();
  //  const bool renderMark = distanceToCamera <= minDistanceToCamera;
  const bool renderMark = true;
  
  if (renderMark) {
    const Vector3D normalAtMarkPosition = planet->geodeticSurfaceNormal(*markPosition);
    
    if (normalAtMarkPosition.angleBetween(markCameraVector).radians() > GMath.halfPi()) {
      GL* gl = rc->getGL();
      
      gl->transformTexCoords(_textureScale, _textureTranslation);
      
      if (_textureId == NULL) {
        IImage* image = rc->getFactory()->createImageFromFileName(_textureFilename);
        
        _textureId = rc->getTexturesHandler()->getGLTextureId(image,
                                                              GLFormat::rgba(),
                                                              _textureFilename,
                                                              false);
        
        rc->getFactory()->deleteImage(image);
      }
      
      if (_textureId == NULL) {
        rc->getLogger()->logError("Can't load file %s", _textureFilename.c_str());
      }
      else {
        gl->drawBillBoard(_textureId,
                          getVertices(planet),
                          camera->getViewPortRatio());
      }
    }
  }
  
}
