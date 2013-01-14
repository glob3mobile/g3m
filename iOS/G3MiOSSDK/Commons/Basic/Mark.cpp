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
#include "MarkTouchListener.hpp"


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
    ILogger::instance()->logError("Error trying to download image \"%s\"", url.getPath().c_str());
    _mark->onTextureDownloadError();
  }

  void onCancel(const URL& url) {
    // ILogger::instance()->logError("Download canceled for image \"%s\"", url.getPath().c_str());
    _mark->onTextureDownloadError();
  }

  void onCanceledDownload(const URL& url,
                          const IImage* image) {
    // do nothing
  }
};


Mark::Mark(const std::string& name,
           const URL          textureURL,
           const Geodetic3D   position,
           double minDistanceToCamera,
           void* userData,
           MarkTouchListener* listener,
           bool autoDeleteListener) :
_name(name),
_textureURL(textureURL),
_position(position),
_textureId(NULL),
_cartesianPosition(NULL),
_vertices(NULL),
_textureSolved(false),
_textureImage(NULL),
_renderedMark(false),
_textureWidth(0),
_textureHeight(0),
_userData(userData),
_minDistanceToCamera(minDistanceToCamera),
_listener(listener),
_autoDeleteListener(autoDeleteListener)
{

}

Mark::Mark(const std::string& name,
           IImage*            textureImage,
           const Geodetic3D   position,
           double minDistanceToCamera,
           void* userData,
           MarkTouchListener* listener,
           bool autoDeleteListener) :
_name(name),
_textureURL("", false),
_position(position),
_textureId(NULL),
_cartesianPosition(NULL),
_vertices(NULL),
_textureSolved(true),
_textureImage(textureImage),
_renderedMark(false),
_textureWidth(textureImage->getWidth()),
_textureHeight(textureImage->getHeight()),
_userData(userData),
_minDistanceToCamera(minDistanceToCamera),
_listener(listener),
_autoDeleteListener(autoDeleteListener)
{

}


void Mark::initialize(const G3MContext* context) {
  if (!_textureSolved) {
    IDownloader* downloader = context->getDownloader();

    downloader->requestImage(_textureURL,
                             1000000,
                             TimeInterval::fromDays(30),
                             new TextureDownloadListener(this),
                             true);
  }
}

void Mark::onTextureDownloadError() {
  _textureSolved = true;

  ILogger::instance()->logError("Can't load image \"%s\"", _textureURL.getPath().c_str());
  //=======
  //    //  todo;
  //    if (!_textureSolved) {
  //        IDownloader* downloader = context->getDownloader();
  //
  //        downloader->requestImage(_textureURL,
  //                                 1000000,
  //                                 TimeInterval::fromDays(30),
  //                                 new TextureDownloadListener(this),
  //                                 true);
  //    }
}

void Mark::onTextureDownload(const IImage* image) {
  _textureSolved = true;
  _textureImage = image->shallowCopy();
  _textureWidth = _textureImage->getWidth();
  _textureHeight = _textureImage->getHeight();
}

bool Mark::isReady() const {
  return _textureSolved;
}

Mark::~Mark() {
  delete _cartesianPosition;
  delete _vertices;
  if (_autoDeleteListener) {
    delete _listener;
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

void Mark::render(const G3MRenderContext* rc) {
  const Camera* camera = rc->getCurrentCamera();
  const Planet* planet = rc->getPlanet();

  const Vector3D cameraPosition = camera->getCartesianPosition();
  const Vector3D* markPosition = getCartesianPosition(planet);

  const Vector3D markCameraVector = markPosition->sub(cameraPosition);
  const double distanceToCamera = markCameraVector.length();

  _renderedMark = (_minDistanceToCamera == 0) || (distanceToCamera <= _minDistanceToCamera);

  if (_renderedMark) {
    const Vector3D normalAtMarkPosition = planet->geodeticSurfaceNormal(*markPosition);

    if (normalAtMarkPosition.angleBetween(markCameraVector)._radians > GMath.halfPi()) {

      if (_textureId == NULL) {
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
        GL* gl = rc->getGL();

        // static Vector2D textureTranslation(0.0, 0.0);
        // static Vector2D textureScale(1.0, 1.0);
        // gl->transformTexCoords(textureScale, textureTranslation);

        gl->drawBillBoard(_textureId,
                          getVertices(planet),
                          _textureWidth,
                          _textureHeight);
      }
    }
  }
}

bool Mark::touched() {
  if (_listener == NULL) {
    return false;
  }
  return _listener->touchedMark(this);
}
