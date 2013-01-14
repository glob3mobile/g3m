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
#include "ITextUtils.hpp"
#include "IImageListener.hpp"


class MarkLabelImageListener : public IImageListener {
private:
  Mark* _mark;

public:
  MarkLabelImageListener(Mark* mark) :
  _mark(mark)
  {

  }

  void imageCreated(IImage* image) {
    if (image == NULL) {
      _mark->onTextureDownloadError();
    }
    else {
      _mark->onTextureDownload(image);
    }
  }
};



class IconDownloadListener : public IImageDownloadListener {
private:
  Mark*             _mark;
  const std::string   _label;
  const LabelPosition _labelPosition;

public:
  IconDownloadListener(Mark* mark,
                       const std::string& label,
                       const LabelPosition labelPosition) :
  _mark(mark),
  _label(label),
  _labelPosition(labelPosition)
  {

  }

  void onDownload(const URL& url,
                  const IImage* image) {
    const bool hasLabel = ( _label.length() != 0 );

    if (hasLabel) {
      ITextUtils::instance()->labelImage(image,
                                         _label,
                                         _labelPosition,
                                         new MarkLabelImageListener(_mark),
                                         true);
    }
    else {
      _mark->onTextureDownload(image);
    }
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





Mark::Mark(const std::string& label,
           const URL          iconURL,
           const Geodetic3D   position,
           const LabelPosition labelPosition,
           double minDistanceToCamera,
           MarkUserData* userData,
           bool autoDeleteUserData,
           MarkTouchListener* listener,
           bool autoDeleteListener) :
_label(label),
_iconURL(iconURL),
_position(position),
_labelPosition(labelPosition),
_textureId(NULL),
_cartesianPosition(NULL),
_vertices(NULL),
_textureSolved(false),
_textureImage(NULL),
_renderedMark(false),
_textureWidth(0),
_textureHeight(0),
_userData(userData),
_autoDeleteUserData(autoDeleteUserData),
_minDistanceToCamera(minDistanceToCamera),
_listener(listener),
_autoDeleteListener(autoDeleteListener)
{

}

Mark::Mark(const std::string& label,
           const Geodetic3D   position,
           double minDistanceToCamera,
           MarkUserData* userData,
           bool autoDeleteUserData,
           MarkTouchListener* listener,
           bool autoDeleteListener) :
_label(label),
_labelPosition(Bottom),
_iconURL("", false),
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
_autoDeleteUserData(autoDeleteUserData),
_minDistanceToCamera(minDistanceToCamera),
_listener(listener),
_autoDeleteListener(autoDeleteListener)
{

}

Mark::Mark(const URL          iconURL,
           const Geodetic3D   position,
           double minDistanceToCamera,
           MarkUserData* userData,
           bool autoDeleteUserData,
           MarkTouchListener* listener,
           bool autoDeleteListener) :
_label(""),
_labelPosition(Bottom),
_iconURL(iconURL),
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
_autoDeleteUserData(autoDeleteUserData),
_minDistanceToCamera(minDistanceToCamera),
_listener(listener),
_autoDeleteListener(autoDeleteListener)
{

}

void Mark::initialize(const G3MContext* context) {
  if (!_textureSolved) {
    const bool hasLabel   = ( _label.length()             != 0 );
    const bool hasIconURL = ( _iconURL.getPath().length() != 0 );

    if (hasIconURL) {
      IDownloader* downloader = context->getDownloader();

      downloader->requestImage(_iconURL,
                               1000000,
                               TimeInterval::fromDays(30),
                               new IconDownloadListener(this, _label, _labelPosition),
                               true);
    }
    else {
      if (hasLabel) {
        ITextUtils::instance()->createLabelImage(_label,
                                                 new MarkLabelImageListener(this),
                                                 true);
      }
      else {
        ILogger::instance()->logWarning("Marker created without label nor icon");
      }
    }
  }
}

void Mark::onTextureDownloadError() {
  _textureSolved = true;

  ILogger::instance()->logError("Can't create texture for Mark (iconURL=\"%s\", label=\"%s\")",
                                _iconURL.getPath().c_str(),
                                _label.c_str());
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
  if (_autoDeleteUserData) {
    delete _userData;
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
                                                                _iconURL.getPath() + "_" + _label,
                                                                false);

          rc->getFactory()->deleteImage(_textureImage);
          _textureImage = NULL;
        }
      }

      if (_textureId != NULL) {
        GL* gl = rc->getGL();

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
