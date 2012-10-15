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
#include "IDownloader.hpp"
#include "IImageDownloadListener.hpp"


class TextureDownloadListener : public IImageDownloadListener {
private:
  Mark* _mark;
  
public:
  TextureDownloadListener(Mark* mark) :
  _mark(mark)
  {
    
  }
  
  void onDownload(const URL& url,
                  const IImage* image) {
    _mark->onTextureDownload(image);
  }
  
  void onError(const URL& url) {
    //    ILogger::instance()->logError("Error trying to download image \"%s\"", url.getPath().c_str());
    _mark->onTextureDownloadError();
  }
  
  void onCancel(const URL& url) {
    //    ILogger::instance()->logError("Download canceled for image \"%s\"", url.getPath().c_str());
    _mark->onTextureDownloadError();
  }
  
  void onCanceledDownload(const URL& url,
                          const IImage* image) {
    // do nothing
  }
};


void Mark::initialize(const InitializationContext* ic) {
  //  todo;
  if (!_textureSolved) {
    IDownloader* downloader = ic->getDownloader();
    
    downloader->requestImage(_textureURL,
                             1000000,
                             new TextureDownloadListener(this),
                             true);
  }
}

void Mark::onTextureDownloadError() {
  _textureSolved = true;
  
  ILogger::instance()->logError("Can't load image \"%s\"", _textureURL.getPath().c_str());
}

void Mark::onTextureDownload(const IImage* image) {
  _textureSolved = true;
  _textureImage = image->shallowCopy();
}


bool Mark::isReady() const {
  return _textureSolved;
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
      
      static Vector2D textureTranslation(0.0, 0.0);
      static Vector2D textureScale(1.0, 1.0);
      gl->transformTexCoords(textureScale, textureTranslation);
      
      if (_textureId == NULL) {
        //        IImage* image = rc->getFactory()->createImageFromFileName(_textureFilename);
        //
        //        _textureId = rc->getTexturesHandler()->getGLTextureId(image,
        //                                                              GLFormat::rgba(),
        //                                                              _textureFilename,
        //                                                              false);
        //
        //        rc->getFactory()->deleteImage(image);
        
        if (_textureImage != NULL) {
          _textureId = rc->getTexturesHandler()->getGLTextureId(_textureImage,
                                                                GLFormat::rgba(),
                                                                _textureURL.getPath(),
                                                                false);
          
          rc->getFactory()->deleteImage(_textureImage);
          _textureImage = NULL;
        }
      }
      
      if (_textureId != NULL) {
        gl->drawBillBoard(_textureId,
                          getVertices(planet),
                          camera->getViewPortRatio());
      }
    }
  }
  
}
