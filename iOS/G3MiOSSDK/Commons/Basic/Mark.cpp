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
#include "IGLTextureId.hpp"
#include "IDownloader.hpp"
#include "IImageDownloadListener.hpp"
#include "MarkTouchListener.hpp"
#include "ITextUtils.hpp"
#include "IImageListener.hpp"
#include "FloatBufferBuilderFromCartesian2D.hpp"
#include "GLFeature.hpp"
#include "Vector2D.hpp"

class MarkLabelImageListener : public IImageListener {
private:
  IImage* _iconImage;
  Mark*   _mark;

public:
  MarkLabelImageListener(IImage* iconImage,
                         Mark* mark) :
  _iconImage(iconImage),
  _mark(mark)
  {

  }

  void imageCreated(IImage* image) {
    if (_iconImage != NULL) {
      IFactory::instance()->deleteImage(_iconImage);
      _iconImage = NULL;
    }

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
  Mark*              _mark;
  const std::string  _label;
  const bool         _labelBottom;
  const float        _labelFontSize;
  const Color*       _labelFontColor;
  const Color*       _labelShadowColor;
  const int          _labelGapSize;

public:
  IconDownloadListener(Mark* mark,
                       const std::string& label,
                       bool  labelBottom,
                       const float labelFontSize,
                       const Color* labelFontColor,
                       const Color* labelShadowColor,
                       const int labelGapSize) :
  _mark(mark),
  _label(label),
  _labelBottom(labelBottom),
  _labelFontSize(labelFontSize),
  _labelFontColor(labelFontColor),
  _labelShadowColor(labelShadowColor),
  _labelGapSize(labelGapSize)
  {

  }

  void onDownload(const URL& url,
                  IImage* image,
                  bool expired) {
    const bool hasLabel = ( _label.length() != 0 );

    if (hasLabel) {
#ifdef C_CODE
      LabelPosition labelPosition = _labelBottom ? Bottom : Right;
#endif
#ifdef JAVA_CODE
      LabelPosition labelPosition = _labelBottom ? LabelPosition.Bottom : LabelPosition.Right;
#endif

      ITextUtils::instance()->labelImage(image,
                                         _label,
                                         labelPosition,
                                         _labelGapSize,
                                         _labelFontSize,
                                         _labelFontColor,
                                         _labelShadowColor,
                                         new MarkLabelImageListener(image, _mark),
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
                          IImage* image,
                          bool expired) {
    // do nothing
  }
};


IFloatBuffer* Mark::_billboardTexCoord = NULL;


Mark::Mark(const std::string& label,
           const URL          iconURL,
           const Geodetic3D&  position,
           double             minDistanceToCamera,
           const bool         labelBottom,
           const float        labelFontSize,
           const Color*       labelFontColor,
           const Color*       labelShadowColor,
           const int          labelGapSize,
           MarkUserData*      userData,
           bool               autoDeleteUserData,
           MarkTouchListener* listener,
           bool               autoDeleteListener) :
_label(label),
_iconURL(iconURL),
_position(position),
_labelBottom(labelBottom),
_labelFontSize(labelFontSize),
_labelFontColor(labelFontColor),
_labelShadowColor(labelShadowColor),
_labelGapSize(labelGapSize),
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
_autoDeleteListener(autoDeleteListener),
_imageID( iconURL.getPath() + "_" + label ),
_surfaceElevationProvider(NULL)
{

}

Mark::Mark(const std::string& label,
           const Geodetic3D&  position,
           double             minDistanceToCamera,
           const float        labelFontSize,
           const Color*       labelFontColor,
           const Color*       labelShadowColor,
           MarkUserData*      userData,
           bool               autoDeleteUserData,
           MarkTouchListener* listener,
           bool               autoDeleteListener) :
_label(label),
_labelBottom(true),
_iconURL("", false),
_position(position),
_labelFontSize(labelFontSize),
_labelFontColor(labelFontColor),
_labelShadowColor(labelShadowColor),
_labelGapSize(2),
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
_autoDeleteListener(autoDeleteListener),
_imageID( "_" + label ),
_surfaceElevationProvider(NULL)
{

}

Mark::Mark(const URL          iconURL,
           const Geodetic3D&  position,
           double             minDistanceToCamera,
           MarkUserData*      userData,
           bool               autoDeleteUserData,
           MarkTouchListener* listener,
           bool               autoDeleteListener) :
_label(""),
_labelBottom(true),
_iconURL(iconURL),
_position(position),
_labelFontSize(20),
_labelFontColor(Color::newFromRGBA(1, 1, 1, 1)),
_labelShadowColor(Color::newFromRGBA(0, 0, 0, 1)),
_labelGapSize(2),
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
_autoDeleteListener(autoDeleteListener),
_imageID( iconURL.getPath() + "_" ),
_surfaceElevationProvider(NULL)
{

}

Mark::Mark(IImage*            image,
           const std::string& imageID,
           const Geodetic3D&  position,
           double             minDistanceToCamera,
           MarkUserData*      userData,
           bool               autoDeleteUserData,
           MarkTouchListener* listener,
           bool               autoDeleteListener) :
_label(""),
_labelBottom(true),
_iconURL(URL("", false)),
_position(position),
_labelFontSize(20),
_labelFontColor(NULL),
_labelShadowColor(NULL),
_labelGapSize(2),
_textureId(NULL),
_cartesianPosition(NULL),
_vertices(NULL),
_textureSolved(true),
_textureImage(image),
_renderedMark(false),
_textureWidth(image->getWidth()),
_textureHeight(image->getHeight()),
_userData(userData),
_autoDeleteUserData(autoDeleteUserData),
_minDistanceToCamera(minDistanceToCamera),
_listener(listener),
_autoDeleteListener(autoDeleteListener),
_imageID( imageID ),
_surfaceElevationProvider(NULL)
{

}

void Mark::initialize(const G3MContext* context,
                      long long downloadPriority) {

  _surfaceElevationProvider = context->getSurfaceElevationProvider();
  if (_surfaceElevationProvider != NULL) {
    _surfaceElevationProvider->addListener(_position._latitude,
                                           _position._longitude,
                                           this);
  }

  if (!_textureSolved) {
    const bool hasLabel   = ( _label.length()             != 0 );
    const bool hasIconURL = ( _iconURL.getPath().length() != 0 );

    if (hasIconURL) {
      IDownloader* downloader = context->getDownloader();

      downloader->requestImage(_iconURL,
                               downloadPriority,
                               TimeInterval::fromDays(30),
                               true,
                               new IconDownloadListener(this,
                                                        _label,
                                                        _labelBottom,
                                                        _labelFontSize,
                                                        _labelFontColor,
                                                        _labelShadowColor,
                                                        _labelGapSize),
                               true);
    }
    else {
      if (hasLabel) {
        ITextUtils::instance()->createLabelImage(_label,
                                                 _labelFontSize,
                                                 _labelFontColor,
                                                 _labelShadowColor,
                                                 new MarkLabelImageListener(NULL, this),
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

  delete _labelFontColor;
  delete _labelShadowColor;

  ILogger::instance()->logError("Can't create texture for Mark (iconURL=\"%s\", label=\"%s\")",
                                _iconURL.getPath().c_str(),
                                _label.c_str());
}

void Mark::onTextureDownload(IImage* image) {
  _textureSolved = true;

  delete _labelFontColor;
  delete _labelShadowColor;

  _textureImage = image;
  _textureWidth = _textureImage->getWidth();
  _textureHeight = _textureImage->getHeight();
}

bool Mark::isReady() const {
  return _textureSolved;
}

Mark::~Mark() {
  if (_surfaceElevationProvider != NULL) {
    _surfaceElevationProvider->removeListener(this);
  }

  delete _cartesianPosition;
  delete _vertices;
  if (_autoDeleteListener) {
    delete _listener;
  }
  if (_autoDeleteUserData) {
    delete _userData;
  }
  if (_textureImage != NULL) {
    IFactory::instance()->deleteImage(_textureImage);
  }

  JAVA_POST_DISPOSE
}

Vector3D* Mark::getCartesianPosition(const Planet* planet) {
  if (_cartesianPosition == NULL) {
    _cartesianPosition = new Vector3D( planet->toCartesian(_position) );
  }
  return _cartesianPosition;
}

bool Mark::touched() {
  return (_listener == NULL) ? false : _listener->touchedMark(this);
}

void Mark::setMinDistanceToCamera(double minDistanceToCamera) {
  _minDistanceToCamera = minDistanceToCamera;
}

double Mark::getMinDistanceToCamera() {
  return _minDistanceToCamera;
}

void Mark::createGLState(const Planet* planet){

  if (_vertices == NULL) {
    const Vector3D pos( planet->toCartesian(_position) );
    FloatBufferBuilderFromCartesian3D vertex(CenterStrategy::noCenter(), Vector3D::zero());
    vertex.add(pos);
    vertex.add(pos);
    vertex.add(pos);
    vertex.add(pos);

    _vertices = vertex.create();
  }

  _glState.addGLFeature(new TextureExtentGLFeature(_textureWidth, _textureHeight), false);

  _glState.addGLFeature(new GeometryGLFeature(_vertices,    // The attribute is a float vector of 4 elements
                                              3,            // Our buffer contains elements of 3
                                              0,            // Index 0
                                              false,        // Not normalized
                                              0,
                                              false,        // NO DEPTH TEST
                                              false, 0,     // NO CULLING
                                              false, 0, 0,  // NO POLYGON OFFSET
                                              1.0f,         // LINE WIDTH
                                              false, 1.0f), // POINT SIZE
                        false);

  _glState.addGLFeature(new TextureGLFeature(_textureId,
                                             getBillboardTexCoords(),
                                             2,
                                             0,
                                             false,
                                             0,
                                             true, GLBlendFactor::srcAlpha(), GLBlendFactor::oneMinusSrcAlpha(),
                                             false, Vector2D::zero(), Vector2D::zero()),
                        false);
}

IFloatBuffer* Mark::getBillboardTexCoords() {
  if (_billboardTexCoord == NULL) {
    FloatBufferBuilderFromCartesian2D texCoor;
    texCoor.add(1,1);
    texCoor.add(1,0);
    texCoor.add(0,1);
    texCoor.add(0,0);
    _billboardTexCoord = texCoor.create();
  }
  return _billboardTexCoord;
}

void Mark::render(const G3MRenderContext* rc,
                  const Vector3D& cameraPosition,
                  const GLState* parentGLState,
                  const Planet* planet,
                  GL* gl) {

  const Vector3D* markPosition = getCartesianPosition(planet);

  const Vector3D markCameraVector = markPosition->sub(cameraPosition);

  // mark will be renderered only if is renderable by distance and placed on a visible globe area
  bool renderableByDistance;
  if (_minDistanceToCamera == 0) {
    renderableByDistance = true;
  }
  else {
    const double squaredDistanceToCamera = markCameraVector.squaredLength();
    renderableByDistance = ( squaredDistanceToCamera <= (_minDistanceToCamera * _minDistanceToCamera) );
  }

  _renderedMark = false;

  if (renderableByDistance) {
    const Vector3D normalAtMarkPosition = planet->geodeticSurfaceNormal(*markPosition);

    if (normalAtMarkPosition.angleBetween(markCameraVector)._radians > HALF_PI) {

      if (_textureId == NULL) {
        if (_textureImage != NULL) {
          _textureId = rc->getTexturesHandler()->getGLTextureId(_textureImage,
                                                                GLFormat::rgba(),
                                                                _imageID,
                                                                false);

          rc->getFactory()->deleteImage(_textureImage);
          _textureImage = NULL;
          createGLState(rc->getPlanet());
        }
      } else{
        _glState.setParent(parentGLState); //Linking with parent

        rc->getGL()->drawArrays(GLPrimitive::triangleStrip(),
                                0,
                                4,
                                &_glState,
                                *rc->getGPUProgramManager());
        
        _renderedMark = true;
      }
    }
  }
  
}
