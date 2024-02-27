//
//  Mark.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 06/06/12.
//

#include "Mark.hpp"

#include "Camera.hpp"
#include "GL.hpp"
#include "TexturesHandler.hpp"
#include "FloatBufferBuilderFromCartesian3D.hpp"
#include "IGLTextureID.hpp"
#include "IDownloader.hpp"
#include "IImageDownloadListener.hpp"
#include "MarkTouchListener.hpp"
#include "ITextUtils.hpp"
#include "IImageListener.hpp"
#include "FloatBufferBuilderFromCartesian2D.hpp"
#include "GLFeature.hpp"
#include "Vector2D.hpp"
#include "Geodetic3D.hpp"
#include "TextureIDReference.hpp"
#include "ErrorHandling.hpp"
#include "Effects.hpp"
#include "IImageBuilder.hpp"
#include "G3MRenderContext.hpp"
#include "Planet.hpp"
#include "MarksRenderer.hpp"
#include "IImageBuilderListener.hpp"


class MarkImageBuilderListener : public IImageBuilderListener {
private:
  IImageBuilder* _imageBuilder;
  Mark* _mark;

public:
  MarkImageBuilderListener(IImageBuilder* imageBuilder,
                           Mark* mark) :
  _imageBuilder(imageBuilder),
  _mark(mark)
  {

  }

  ~MarkImageBuilderListener() {
    delete _imageBuilder;
  }

  void forgetMark() {
    _mark = NULL;
  }

  void imageCreated(const IImage*      image,
                    const std::string& imageName) {
    if (_mark) {
      _mark->onImageCreated(image, imageName);
    }
  }

  void onError(const std::string& error)  {
    if (_mark) {
      _mark->onImageCreationError(error);
    }
  }

};


class MarkEffectTarget : public EffectTarget {
public:
  ~MarkEffectTarget() {
  }

#ifndef C_CODE
  void unusedMethod() { }
#endif
};


class MarkZoomInEffect : public EffectWithDuration {
private:
  Mark* _mark;
  const float _initialSize;

public:
  MarkZoomInEffect(Mark* mark,
                   const TimeInterval& timeInterval = TimeInterval::fromMilliseconds(500),
                   const float initialSize = 0.01f) :
  EffectWithDuration(timeInterval, false),
  _mark(mark),
  _initialSize(initialSize)
  {
    _mark->setScreenSizeScale(_initialSize, _initialSize);
  }

  void doStep(const G3MRenderContext* rc,
              const TimeInterval& when) {
    const double alpha = getAlpha(when);
    const float  s     = (float) (((1.0 - _initialSize) * alpha) + _initialSize);
    _mark->setScreenSizeScale(s, s);
  }

  void stop(const G3MRenderContext* rc,
            const TimeInterval& when) {
    _mark->setScreenSizeScale(1, 1);
  }

  void cancel(const TimeInterval& when) {
    _mark->setScreenSizeScale(1, 1);
  }

};

class MarkZoomOutAndRemoveEffect : public EffectWithDuration {
private:
  Mark* _mark;
  MarksRenderer* _renderer;
  const bool  _deleteMarkOnDisappears;
  const float _finalSize;

public:
  MarkZoomOutAndRemoveEffect(Mark* mark,
                             MarksRenderer* renderer,
                             bool deleteMarkOnDisappears,
                             const TimeInterval& timeInterval = TimeInterval::fromMilliseconds(300),
                             const float finalSize = 0.01f) :
  EffectWithDuration(timeInterval, false),
  _mark(mark),
  _renderer(renderer),
  _deleteMarkOnDisappears(deleteMarkOnDisappears),
  _finalSize(finalSize)
  {
    _mark->setScreenSizeScale(1, 1);
  }

  ~MarkZoomOutAndRemoveEffect() {
    if (_deleteMarkOnDisappears) {
      if (_mark != NULL) {
        Mark* mark = _mark;
        _mark = NULL;
        delete mark;
      }
    }
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

  void doStep(const G3MRenderContext* rc,
              const TimeInterval& when) {
    if (_mark != NULL) {
      const double alpha = getAlpha(when);
      const float  s     = 1.0f - (float) (((1.0 - _finalSize) * alpha) + _finalSize);
      _mark->setScreenSizeScale(s, s);
    }
  }

  void stop(const G3MRenderContext* rc,
            const TimeInterval& when) {
    if ((_mark != NULL) && (_renderer != NULL)) {
      _renderer->removeMark(_mark);
      _renderer = NULL;
    }
  }

  void cancel(const TimeInterval& when) {
    if ((_mark != NULL) && (_renderer != NULL)) {
      _renderer->removeMark(_mark);
      _renderer = NULL;
    }
  }

};


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

  void imageCreated(const IImage* image) {
    delete _iconImage;
    _iconImage = NULL;

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
    ILogger::instance()->logError("Error trying to download image \"%s\"", url._path.c_str());
    _mark->onTextureDownloadError();
  }

  void onCancel(const URL& url) {
    // ILogger::instance()->logError("Download canceled for image \"%s\"", url._path.c_str());
    _mark->onTextureDownloadError();
  }

  void onCanceledDownload(const URL& url,
                          IImage* image,
                          bool expired) {
    // do nothing
  }
};

EffectTarget* Mark::getEffectTarget() {
  if (_effectTarget == NULL) {
    _effectTarget = new MarkEffectTarget();
  }
  return _effectTarget;
}


Mark::Mark(const std::string& label,
           const URL&         iconURL,
           const Geodetic3D&  position,
           AltitudeMode       altitudeMode,
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
_imageBuilder(NULL),
_imageBuilderListener(NULL),
_label(label),
_iconURL(iconURL),
_position(new Geodetic3D(position)),
_altitudeMode(altitudeMode),
_labelBottom(labelBottom),
_labelFontSize(labelFontSize),
_labelFontColor(labelFontColor),
_labelShadowColor(labelShadowColor),
_labelGapSize(labelGapSize),
_textureID(NULL),
_cartesianPosition(NULL),
_textureSolved(false),
_textureImage(NULL),
_renderedMark(false),
_textureWidth(0),
_textureHeight(0),
_userData(userData),
_autoDeleteUserData(autoDeleteUserData),
_minDistanceToCamera(minDistanceToCamera),
_maxDistanceToCamera(0),
_listener(listener),
_autoDeleteListener(autoDeleteListener),
_imageID( iconURL._path + "_" + label ),
_surfaceElevationProvider(NULL),
_currentSurfaceElevation(0.0),
_glState(NULL),
_normalAtMarkPosition(NULL),
_textureSizeSetExternally(false),
_hasTCTransformations(false),
_textureGLF(NULL),
_anchorU(0.5),
_anchorV(0.5),
_billboardGLF(NULL),
_textureHeightScale(1.0),
_textureWidthScale(1.0),
_textureScaleSetExternally(false),
_initialized(false),
_zoomInAppears(true),
_effectsScheduler(NULL),
_firstRender(true),
_effectTarget(NULL),
_zoomOutDisappears(false),
_deleteMarkOnDisappears(false),
_zoomOutDisappearsStarted(false),
_token("")
{

}

Mark::Mark(const std::string& label,
           const Geodetic3D&  position,
           AltitudeMode       altitudeMode,
           double             minDistanceToCamera,
           const float        labelFontSize,
           const Color*       labelFontColor,
           const Color*       labelShadowColor,
           MarkUserData*      userData,
           bool               autoDeleteUserData,
           MarkTouchListener* listener,
           bool               autoDeleteListener) :
_imageBuilder(NULL),
_imageBuilderListener(NULL),
_label(label),
_labelBottom(true),
_iconURL("", false),
_position(new Geodetic3D(position)),
_altitudeMode(altitudeMode),
_labelFontSize(labelFontSize),
_labelFontColor(labelFontColor),
_labelShadowColor(labelShadowColor),
_labelGapSize(2),
_textureID(NULL),
_cartesianPosition(NULL),
_textureSolved(false),
_textureImage(NULL),
_renderedMark(false),
_textureWidth(0),
_textureHeight(0),
_userData(userData),
_autoDeleteUserData(autoDeleteUserData),
_minDistanceToCamera(minDistanceToCamera),
_maxDistanceToCamera(0),
_listener(listener),
_autoDeleteListener(autoDeleteListener),
_imageID( "_" + label ),
_surfaceElevationProvider(NULL),
_currentSurfaceElevation(0.0),
_glState(NULL),
_normalAtMarkPosition(NULL),
_textureSizeSetExternally(false),
_textureGLF(NULL),
_hasTCTransformations(false),
_anchorU(0.5),
_anchorV(0.5),
_billboardGLF(NULL),
_textureHeightScale(1.0),
_textureWidthScale(1.0),
_textureScaleSetExternally(false),
_initialized(false),
_zoomInAppears(true),
_effectsScheduler(NULL),
_firstRender(true),
_effectTarget(NULL),
_zoomOutDisappears(false),
_deleteMarkOnDisappears(false),
_zoomOutDisappearsStarted(false),
_token("")
{

}

Mark::Mark(const URL&         iconURL,
           const Geodetic3D&  position,
           AltitudeMode       altitudeMode,
           double             minDistanceToCamera,
           MarkUserData*      userData,
           bool               autoDeleteUserData,
           MarkTouchListener* listener,
           bool               autoDeleteListener) :
_imageBuilder(NULL),
_imageBuilderListener(NULL),
_label(""),
_labelBottom(true),
_iconURL(iconURL),
_position(new Geodetic3D(position)),
_altitudeMode(altitudeMode),
_labelFontSize(20),
_labelFontColor(Color::newFromRGBA(1, 1, 1, 1)),
_labelShadowColor(Color::newFromRGBA(0, 0, 0, 1)),
_labelGapSize(2),
_textureID(NULL),
_cartesianPosition(NULL),
_textureSolved(false),
_textureImage(NULL),
_renderedMark(false),
_textureWidth(0),
_textureHeight(0),
_userData(userData),
_autoDeleteUserData(autoDeleteUserData),
_minDistanceToCamera(minDistanceToCamera),
_maxDistanceToCamera(0),
_listener(listener),
_autoDeleteListener(autoDeleteListener),
_imageID( iconURL._path + "_" ),
_surfaceElevationProvider(NULL),
_currentSurfaceElevation(0.0),
_glState(NULL),
_normalAtMarkPosition(NULL),
_textureSizeSetExternally(false),
_textureGLF(NULL),
_hasTCTransformations(false),
_anchorU(0.5),
_anchorV(0.5),
_billboardGLF(NULL),
_textureHeightScale(1.0),
_textureWidthScale(1.0),
_textureScaleSetExternally(false),
_initialized(false),
_zoomInAppears(true),
_effectsScheduler(NULL),
_firstRender(true),
_effectTarget(NULL),
_zoomOutDisappears(false),
_deleteMarkOnDisappears(false),
_zoomOutDisappearsStarted(false),
_token("")
{

}

Mark::Mark(const IImage*      image,
           const std::string& imageID,
           const Geodetic3D&  position,
           AltitudeMode       altitudeMode,
           double             minDistanceToCamera,
           MarkUserData*      userData,
           bool               autoDeleteUserData,
           MarkTouchListener* listener,
           bool               autoDeleteListener) :
_imageBuilder(NULL),
_imageBuilderListener(NULL),
_label(""),
_labelBottom(true),
_iconURL(URL("", false)),
_position(new Geodetic3D(position)),
_altitudeMode(altitudeMode),
_labelFontSize(20),
_labelFontColor(NULL),
_labelShadowColor(NULL),
_labelGapSize(2),
_textureID(NULL),
_cartesianPosition(NULL),
_textureSolved(true),
_textureImage(image),
_renderedMark(false),
_textureWidth(image->getWidth()),
_textureHeight(image->getHeight()),
_userData(userData),
_autoDeleteUserData(autoDeleteUserData),
_minDistanceToCamera(minDistanceToCamera),
_maxDistanceToCamera(0),
_listener(listener),
_autoDeleteListener(autoDeleteListener),
_imageID( imageID ),
_surfaceElevationProvider(NULL),
_currentSurfaceElevation(0.0),
_glState(NULL),
_normalAtMarkPosition(NULL),
_textureSizeSetExternally(false),
_hasTCTransformations(false),
_anchorU(0.5),
_anchorV(0.5),
_billboardGLF(NULL),
_textureScaleSetExternally(false),
_textureHeightScale(1.0),
_textureWidthScale(1.0),
_initialized(false),
_zoomInAppears(true),
_effectsScheduler(NULL),
_firstRender(true),
_effectTarget(NULL),
_zoomOutDisappears(false),
_deleteMarkOnDisappears(false),
_zoomOutDisappearsStarted(false),
_token("")
{

}

Mark::Mark(IImageBuilder*     imageBuilder,
           const Geodetic3D&  position,
           AltitudeMode       altitudeMode,
           double             minDistanceToCamera,
           MarkUserData*      userData,
           bool               autoDeleteUserData,
           MarkTouchListener* listener,
           bool               autoDeleteListener) :
_imageBuilder(imageBuilder),
_imageBuilderListener(NULL),
_label(""),
_labelBottom(true),
_iconURL(URL("", false)),
_position(new Geodetic3D(position)),
_altitudeMode(altitudeMode),
_labelFontSize(20),
_labelFontColor(NULL),
_labelShadowColor(NULL),
_labelGapSize(2),
_textureID(NULL),
_cartesianPosition(NULL),
_textureSolved(false),
_textureImage(NULL),
_renderedMark(false),
_textureWidth(0),
_textureHeight(0),
_userData(userData),
_autoDeleteUserData(autoDeleteUserData),
_minDistanceToCamera(minDistanceToCamera),
_maxDistanceToCamera(0),
_listener(listener),
_autoDeleteListener(autoDeleteListener),
_imageID( "" ),
_surfaceElevationProvider(NULL),
_currentSurfaceElevation(0.0),
_glState(NULL),
_normalAtMarkPosition(NULL),
_textureSizeSetExternally(false),
_hasTCTransformations(false),
_anchorU(0.5),
_anchorV(0.5),
_billboardGLF(NULL),
_textureScaleSetExternally(false),
_textureHeightScale(1.0),
_textureWidthScale(1.0),
_initialized(false),
_zoomInAppears(true),
_effectsScheduler(NULL),
_firstRender(true),
_effectTarget(NULL),
_zoomOutDisappears(false),
_deleteMarkOnDisappears(false),
_zoomOutDisappearsStarted(false),
_token("")
{
  if (_imageBuilder->isMutable()) {
    ILogger::instance()->logError("Marks doesn't support mutable image builders");
  }
}


void Mark::initialize(const G3MContext* context,
                      long long downloadPriority) {
  _initialized = true;
  if (_altitudeMode == RELATIVE_TO_GROUND) {
    _surfaceElevationProvider = context->getSurfaceElevationProvider();
    if (_surfaceElevationProvider != NULL) {
      _surfaceElevationProvider->addListener(_position->_latitude,
                                             _position->_longitude,
                                             this);
    }
  }

  if (!_textureSolved) {
    if (_imageBuilder != NULL) {
      _imageBuilderListener = new MarkImageBuilderListener(_imageBuilder, this);
      _imageBuilder->build(context,
                           _imageBuilderListener,
                           true);
      _imageBuilder = NULL; // ownership moved to MarkImageBuilderListener
    }
    else {
      const bool hasIconURL = ( _iconURL._path.length() != 0 );
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
        const bool hasLabel = ( _label.length() != 0 );
        if (hasLabel) {
          ITextUtils::instance()->createLabelImage(_label,
                                                   _labelFontSize,
                                                   _labelFontColor,
                                                   _labelShadowColor,
                                                   new MarkLabelImageListener(NULL, this),
                                                   true);
        }
        else {
          ILogger::instance()->logWarning("Mark created without label nor icon");
        }
      }
    }
  }
}

void Mark::onTextureDownloadError() {
  _textureSolved = true;

  delete _labelFontColor;
  _labelFontColor = NULL;
  delete _labelShadowColor;
  _labelShadowColor = NULL;

  ILogger::instance()->logError("Can't create texture for Mark (iconURL=\"%s\", label=\"%s\")",
                                _iconURL._path.c_str(),
                                _label.c_str());
}

void Mark::onTextureDownload(const IImage* image) {
  _textureSolved = true;

  delete _labelFontColor;
  _labelFontColor = NULL;
  delete _labelShadowColor;
  _labelShadowColor = NULL;

  _textureImage = image;

  if (!_textureSizeSetExternally) {
    _textureWidth = _textureImage->getWidth();
    _textureHeight = _textureImage->getHeight();

    if (_textureScaleSetExternally) {
      _textureWidth  *= _textureWidthScale;
      _textureHeight *= _textureHeightScale;
    }
  }

}

bool Mark::isReady() const {
  return _textureSolved;
}

Mark::~Mark() {
  if (_imageBuilderListener) {
    _imageBuilderListener->forgetMark();
  }
  delete _imageBuilder;

  //  if (_effectsScheduler != NULL) {
  //    _effectsScheduler->cancelAllEffectsFor(getEffectTarget());
  //  }
  delete _effectTarget;

  delete _labelFontColor;
  delete _labelShadowColor;

  delete _position;

  delete _normalAtMarkPosition;

  if (_surfaceElevationProvider != NULL) {
    if (!_surfaceElevationProvider->removeListener(this)) {
      ILogger::instance()->logError("Couldn't remove mark as listener of Surface Elevation Provider.");
    }
  }

  delete _cartesianPosition;

  if (_autoDeleteListener) {
    delete _listener;
  }
  if (_autoDeleteUserData) {
    delete _userData;
  }

  delete _textureImage;


  if (_glState != NULL) {
    _glState->_release();
  }

  if (_textureID != NULL) {
#ifdef JAVA_CODE
    _textureID.dispose();
#endif
    delete _textureID; //Releasing texture
  }
}

const Vector3D* Mark::getCartesianPosition(const Planet* planet) {
  if (_cartesianPosition == NULL) {
    double altitude = _position->_height;
    if (_altitudeMode == RELATIVE_TO_GROUND) {
      altitude += _currentSurfaceElevation;
    }

    Geodetic3D positionWithSurfaceElevation(_position->_latitude,
                                            _position->_longitude,
                                            altitude);

    _cartesianPosition = new Vector3D( planet->toCartesian(positionWithSurfaceElevation) );
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

void Mark::setMaxDistanceToCamera(double maxDistanceToCamera) {
  _maxDistanceToCamera = maxDistanceToCamera;
}

double Mark::getMaxDistanceToCamera() {
  return _maxDistanceToCamera;
}

void Mark::createGLState(const Planet* planet,
                         IFloatBuffer* billboardTexCoords) {
  _glState = new GLState();

  _billboardGLF = new BillboardGLFeature(_textureWidth,
                                         _textureHeight,
                                         _anchorU, _anchorV);

  _glState->addGLFeature(_billboardGLF,
                         false);
  

  const Vector3D* position = getCartesianPosition(planet);
  const MutableMatrix44D translation = MutableMatrix44D::createTranslationMatrix(*position);

  ModelTransformGLFeature* modelTransformGLF = new ModelTransformGLFeature(translation.asMatrix44D());
  _glState->addGLFeature(modelTransformGLF, false);


  if (_textureID != NULL) {

    if (_hasTCTransformations) {
      _textureGLF = new TextureGLFeature(_textureID->getID(),
                                         billboardTexCoords,
                                         2,
                                         0,
                                         false,
                                         0,
                                         true,
                                         _textureID->isPremultiplied() ? GLBlendFactor::one() : GLBlendFactor::srcAlpha(),
                                         GLBlendFactor::oneMinusSrcAlpha(),
                                         _translationTCX,
                                         _translationTCY,
                                         _scalingTCX,
                                         _scalingTCY,
                                         0.0f,
                                         0.0f,
                                         0.0f);
    }
    else {
      _textureGLF = new TextureGLFeature(_textureID->getID(),
                                         billboardTexCoords,
                                         2,
                                         0,
                                         false,
                                         0,
                                         true,
                                         _textureID->isPremultiplied() ? GLBlendFactor::one() : GLBlendFactor::srcAlpha(),
                                         GLBlendFactor::oneMinusSrcAlpha(),
                                         0.0f,
                                         0.0f,
                                         1.0f,
                                         1.0f,
                                         0.0f,
                                         0.0f,
                                         0.0f);
    }

    _glState->addGLFeature(_textureGLF,
                           false);
  }
}

void Mark::render(const G3MRenderContext* rc,
                  MarksRenderer* renderer,
                  const MutableVector3D& cameraPosition,
                  double cameraHeight,
                  const GLState* parentGLState,
                  const Planet* planet,
                  GL* gl,
                  IFloatBuffer* billboardTexCoords) {

  const Vector3D* markPosition = getCartesianPosition(planet);

  _markCameraVector.set(markPosition->_x - cameraPosition.x(),
                        markPosition->_y - cameraPosition.y(),
                        markPosition->_z - cameraPosition.z());

  bool renderableByDistance = true;
  {
    const bool hasMinDistanceToCamera = (_minDistanceToCamera > 0);
    const bool hasMaxDistanceToCamera = (_maxDistanceToCamera > 0);
    if (hasMinDistanceToCamera || hasMaxDistanceToCamera) {
      const double squaredDistanceToCamera = _markCameraVector.squaredLength();

      if (hasMinDistanceToCamera &&
          (squaredDistanceToCamera > (_minDistanceToCamera * _minDistanceToCamera))) {
        renderableByDistance = false;
      }

      if (hasMaxDistanceToCamera &&
          ( squaredDistanceToCamera < (_maxDistanceToCamera * _maxDistanceToCamera))) {
        renderableByDistance = false;
      }
    }
  }

  _renderedMark = false;

  if (renderableByDistance) {
    bool occludedByHorizon = false;

    if (_position->_height > cameraHeight) {
      // Computing horizon culling
      const std::vector<double> dists = planet->intersectionsDistances(cameraPosition.x(),
                                                                       cameraPosition.y(),
                                                                       cameraPosition.z(),
                                                                       _markCameraVector.x(),
                                                                       _markCameraVector.y(),
                                                                       _markCameraVector.z());
      if (dists.size() > 0) {
        const double dist = dists[0];
        if (dist > 0.0 && dist < 1.0) {
          occludedByHorizon = true;
        }
      }
    }
    else {
      // if camera position is upper than mark we can compute horizon culling in a much simpler way
      if (_normalAtMarkPosition == NULL) {
        _normalAtMarkPosition = new Vector3D( planet->geodeticSurfaceNormal(*markPosition) );
      }
      //      occludedByHorizon = (_normalAtMarkPosition->angleInRadiansBetween(markCameraVector) <= HALF_PI);
      occludedByHorizon = (Vector3D::angleInRadiansBetween(*_normalAtMarkPosition, _markCameraVector) <= HALF_PI);
    }

    if (!occludedByHorizon) {
      if ((_textureID == NULL) && (_textureImage != NULL)) {
        _textureID = rc->getTexturesHandler()->getTextureIDReference(_textureImage,
                                                                     GLFormat::rgba(),
                                                                     _imageID,
                                                                     false,
                                                                     GLTextureParameterValue::clampToEdge(),
                                                                     GLTextureParameterValue::clampToEdge());

        delete _textureImage;
        _textureImage = NULL;
      }

      if (_textureID != NULL) {
        if (_glState == NULL) {
          createGLState(planet, billboardTexCoords);  // If GLState was disposed due to elevation change
        }
        _glState->setParent(parentGLState);

        if (_firstRender) {
          _firstRender = false;
          if (_zoomInAppears) {
            _effectsScheduler = rc->getEffectsScheduler();
            _effectsScheduler->startEffect(new MarkZoomInEffect(this),
                                           getEffectTarget());
          }
        }

        if (_zoomOutDisappears && !_zoomOutDisappearsStarted) {
          _zoomOutDisappearsStarted = true;
          if (_effectsScheduler != NULL) {
            _effectsScheduler->cancelAllEffectsFor(getEffectTarget());
          }
          else {
            _effectsScheduler = rc->getEffectsScheduler();
          }
          _effectsScheduler->startEffect(new MarkZoomOutAndRemoveEffect(this, renderer, _deleteMarkOnDisappears),
                                         getEffectTarget());
        }

        rc->getGL()->drawArrays(GLPrimitive::triangleStrip(),
                                0,
                                4,
                                _glState,
                                *rc->getGPUProgramManager());

        _renderedMark = true;
      }
    }
  }

}

void Mark::animatedRemove(bool deleteMark) {
  _zoomOutDisappears = true;
  _deleteMarkOnDisappears = deleteMark;
}

void Mark::elevationChanged(const Geodetic2D& position,
                            double rawElevation,  // Without considering vertical exaggeration
                            double verticalExaggeration) {

  if (ISNAN(rawElevation)) {
    _currentSurfaceElevation = 0;    //USING 0 WHEN NO ELEVATION DATA
  }
  else {
    _currentSurfaceElevation = rawElevation * verticalExaggeration;
  }

  delete _cartesianPosition;
  _cartesianPosition = NULL;

  clearGLState();
}

void Mark::clearGLState() {
  if (_glState != NULL) {
    _glState->_release();
    _glState = NULL;
  }
}

void Mark::setPosition(const Geodetic3D& position) {
  if (_altitudeMode == RELATIVE_TO_GROUND) {
    THROW_EXCEPTION("Position change with (_altitudeMode == RELATIVE_TO_GROUND) not supported");
  }

  delete _position;
#ifdef C_CODE
  _position = new Geodetic3D(position);
#endif
#ifdef JAVA_CODE
  _position = position;
#endif

  delete _cartesianPosition;
  _cartesianPosition = NULL;

  clearGLState();
}

void Mark::setScreenSize(int width, int height) {
  _textureWidth  = width;
  _textureHeight = height;
  _textureSizeSetExternally = true;

  if (_glState != NULL) {
    BillboardGLFeature* b = (BillboardGLFeature*) _glState->getGLFeature(GLF_BILLBOARD);
    if (b != NULL) {
      b->changeSize(IMathUtils::instance()->round(_textureWidth),
                    IMathUtils::instance()->round(_textureHeight));
    }
  }
}

void Mark::setScreenSizeScale(float scaleWidth, float scaleHeight) {
  _textureWidthScale  = scaleWidth;
  _textureHeightScale = scaleHeight;
  _textureScaleSetExternally = true;

  if (_glState != NULL) {
    BillboardGLFeature* b = (BillboardGLFeature*) _glState->getGLFeature(GLF_BILLBOARD);
    if (b != NULL) {
      b->changeSize(IMathUtils::instance()->round(_textureWidth  * _textureWidthScale),
                    IMathUtils::instance()->round(_textureHeight * _textureHeightScale));
    }
  }
}

void Mark::setTextureCoordinatesTransformation(const Vector2F& translation,
                                               const Vector2F& scaling) {
  setTextureCoordinatesTransformation(translation._x,
                                      translation._y,
                                      scaling._x,
                                      scaling._y);
}

void Mark::setTextureCoordinatesTransformation(const float translationX,
                                               const float translationY,
                                               const float scalingX,
                                               const float scalingY) {

  _translationTCX = translationX;
  _translationTCY = translationY;

  _scalingTCX = scalingX;
  _scalingTCY = scalingY;

  if (_translationTCX != 0 || _translationTCY != 0 || _scalingTCX != 1 || _scalingTCY != 1) {
    _hasTCTransformations = true;
  }

  if (_textureGLF != NULL) {

    if (!_textureGLF->hasTranslateAndScale()) {
      clearGLState();
    }

    _textureGLF->setTranslation(_translationTCX, _translationTCY);
    _textureGLF->setScale(_scalingTCX, _scalingTCY);
  }
}

void Mark::setMarkAnchor(float anchorU, float anchorV) {
  if (_billboardGLF != NULL) {
    _billboardGLF->changeAnchor(anchorU, anchorV);
  }
  _anchorU = anchorU;
  _anchorV = anchorV;
}

Vector2F Mark::getMarkAnchor() const {
  return Vector2F(_anchorU, _anchorV);
}

float Mark::getMarkAnchorU() const {
  return _anchorU;
}

float Mark::getMarkAnchorV() const {
  return _anchorV;
}

void Mark::onImageCreationError(const std::string& error) {
  _textureSolved = true;

  //  delete _labelFontColor;
  //  _labelFontColor = NULL;
  //  delete _labelShadowColor;
  //  _labelShadowColor = NULL;

  //  delete _imageBuilder;
  //  _imageBuilder = NULL;
  _imageBuilderListener = NULL;

  ILogger::instance()->logError("Can't create image for Mark: \"%s\"",
                                error.c_str());
}

void Mark::onImageCreated(const IImage* image,
                          const std::string& imageName) {
  _textureSolved = true;
  _imageID = imageName;

  //  delete _labelFontColor;
  //  _labelFontColor = NULL;
  //  delete _labelShadowColor;
  //  _labelShadowColor = NULL;

  //  delete _imageBuilder;
  //  _imageBuilder = NULL;
  _imageBuilderListener = NULL;

  _textureImage = image;

  if (!_textureSizeSetExternally) {
    _textureWidth  = _textureImage->getWidth();
    _textureHeight = _textureImage->getHeight();

    if (_textureScaleSetExternally) {
      _textureWidth  *= _textureWidthScale;
      _textureHeight *= _textureHeightScale;
    }
  }
}
