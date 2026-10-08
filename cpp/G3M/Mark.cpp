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
#include "MarkTouchListener.hpp"
#include "FloatBufferBuilderFromCartesian2D.hpp"
#include "GLFeature.hpp"
#include "Vector2D.hpp"
#include "Geodetic3D.hpp"
#include "TextureIDReference.hpp"
#include "ErrorHandling.hpp"
#include "Effects.hpp"
#include "IImageFactory.hpp"
#include "G3MRenderContext.hpp"
#include "Planet.hpp"
#include "MarksRenderer.hpp"
#include "IImageFactoryListener.hpp"
#include "MarkOutfit.hpp"
#include "MarkAnchor.hpp"
#include "IMathUtils.hpp"


class MarkImageFactoryListener : public IImageFactoryListener {
private:
  IImageFactory* _imageFactory;
  Mark*          _mark;
  const size_t   _outfitIndex;

public:
  MarkImageFactoryListener(IImageFactory* imageFactory,
                           Mark*          mark,
                           const size_t   outfitIndex) :
  _imageFactory(imageFactory),
  _mark(mark),
  _outfitIndex(outfitIndex)
  {

  }

  ~MarkImageFactoryListener() {
    delete _imageFactory;
  }

  void forgetMark() {
    _mark = NULL;
  }

  void imageCreated(const IImage*      image,
                    const std::string& imageName) {
    if (_mark) {
      _mark->onImageCreated(_outfitIndex, image, imageName);
    }
  }

  void onError(const std::string& error)  {
    if (_mark) {
      _mark->onImageCreationError(_outfitIndex, error);
    }
  }

};


/** an outfit's image and texture: the mark's own fields hold the outfit on screen, these the others */
class MarkOutfitImage {
public:
  IImageFactory*            _imageFactory; // until the image is asked for
  MarkImageFactoryListener* _listener;     // while the image is being created
  bool                      _solved;
#ifdef C_CODE
  const IImage*             _image;
  const TextureIDReference* _textureID;
  const MarkAnchor*         _anchor;
#endif
#ifdef JAVA_CODE
  public IImage             _image;
  public TextureIDReference _textureID;
  public MarkAnchor         _anchor;
#endif
  std::string               _imageID;
  float                     _width;
  float                     _height;
  bool                      _hasAnchor; // the outfit's anchor, computed once its image exists
  float                     _anchorU;
  float                     _anchorV;

  MarkOutfitImage(IImageFactory* imageFactory,
                  const MarkAnchor* anchor) :
  _imageFactory(imageFactory),
  _listener(NULL),
  _solved(false),
  _image(NULL),
  _textureID(NULL),
  _anchor(anchor),
  _imageID(""),
  _width(0),
  _height(0),
  _hasAnchor(false),
  _anchorU(0.5f),
  _anchorV(0.5f)
  {
  }

  ~MarkOutfitImage() {
    if (_listener != NULL) {
      _listener->forgetMark();
    }
    delete _imageFactory;
    delete _image;
    if (_textureID != NULL) {
#ifdef JAVA_CODE
      _textureID.dispose();
#endif
      delete _textureID;
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
    _mark->setEffectScale(_initialSize);
  }

  void doStep(const G3MRenderContext* rc,
              const TimeInterval& when) {
    const double alpha = getAlpha(when);
    const float  s     = (float) (((1.0 - _initialSize) * alpha) + _initialSize);
    _mark->setEffectScale(s);
  }

  void stop(const G3MRenderContext* rc,
            const TimeInterval& when) {
    _mark->setEffectScale(1);
  }

  void cancel(const TimeInterval& when) {
    _mark->setEffectScale(1);
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
    _mark->setEffectScale(1);
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
      _mark->setEffectScale(s);
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


EffectTarget* Mark::getEffectTarget() {
  if (_effectTarget == NULL) {
    _effectTarget = new MarkEffectTarget();
  }
  return _effectTarget;
}


Mark::Mark(const std::vector<MarkOutfit*>& outfits,
           const Geodetic3D&               position,
           AltitudeMode                    altitudeMode,
           double                          minDistanceToCamera,
           double                          maxDistanceToCamera,
           MarkUserData*                   userData,
           bool                            autoDeleteUserData,
           MarkTouchListener*              listener,
           bool                            autoDeleteListener,
           bool                            zoomInAppears) :
_outfitIndex(0),
_priority(NAND),
_hasHint(false),
_appAnchorU(0.5),
_appAnchorV(0.5),
_declutterHidden(false),
_declutterTarget(0),
_transitionScale(1),
_lastTransitionMS(-1),
_grazingAngle(NAND),
_horizonScale(1),
_position(new Geodetic3D(position)),
_altitudeMode(altitudeMode),
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
_maxDistanceToCamera(maxDistanceToCamera),
_listener(listener),
_autoDeleteListener(autoDeleteListener),
_imageID( "" ),
_surfaceElevationProvider(NULL),
_currentSurfaceElevation(0.0),
_glState(NULL),
_modelTransformGLF(NULL),
_glPositionOutdated(false),
_normalAtMarkPosition(NULL),
_textureSizeSetExternally(false),
_translationTCX(0),
_translationTCY(0),
_scalingTCX(1),
_scalingTCY(1),
_anchorU(0.5),
_anchorV(0.5),
_billboardGLF(NULL),
_textureGLF(NULL),
_effectScale(1),
_textureHeightScale(1.0),
_textureWidthScale(1.0),
_initialized(false),
_zoomInAppears(zoomInAppears),
_effectsScheduler(NULL),
_firstRender(true),
_effectTarget(NULL),
_zoomOutDisappears(false),
_deleteMarkOnDisappears(false),
_zoomOutDisappearsStarted(false),
_token("")
{
  // element by element: in Java an assignment would share the caller's list
  for (size_t i = 0; i < outfits.size(); i++) {
    MarkOutfit* outfit = outfits[i];
    _outfits.push_back(outfit);

    IImageFactory* imageFactory = outfit->takeImageFactory();
    if (imageFactory->isMutable()) {
      ILogger::instance()->logError("Marks doesn't support mutable image factories");
    }
    _outfitImages.push_back(new MarkOutfitImage(imageFactory, outfit->getAnchor()));
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

  createPendingOutfitImages(context);
}

// every outfit at once: choosing between them needs their sizes
void Mark::createPendingOutfitImages(const G3MContext* context) {
  for (size_t i = 0; i < _outfitImages.size(); i++) {
    MarkOutfitImage* outfitImage = _outfitImages[i];
    if (outfitImage->_imageFactory != NULL) {
      outfitImage->_listener = new MarkImageFactoryListener(outfitImage->_imageFactory, this, i);
      IImageFactory* imageFactory = outfitImage->_imageFactory;
      outfitImage->_imageFactory = NULL; // ownership moved to MarkImageFactoryListener
      imageFactory->create(context,
                           outfitImage->_listener,
                           true);
    }
  }
}

void Mark::onTextureResolved(const IImage* image) {
  _textureSolved = true;

  _textureImage = image;

  if (!_textureSizeSetExternally) {
    _textureWidth  = _textureImage->getWidth();
    _textureHeight = _textureImage->getHeight();
  }
}

bool Mark::isReady() const {
  return _textureSolved;
}

Mark::~Mark() {
  // the zoom-out effect deletes its mark itself: cancelling it from here would delete the mark again
  if (!_zoomOutDisappearsStarted) {
    cancelEffects();
  }

  for (size_t i = 0; i < _outfitImages.size(); i++) {
    delete _outfitImages[i];
  }

  delete _effectTarget;

  for (size_t i = 0; i < _outfits.size(); i++) {
    delete _outfits[i];
  }

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

bool Mark::touched(const TouchEvent* touchEvent) {
  return (_listener == NULL) ? false : _listener->touchedMark(this, touchEvent);
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

  _billboardGLF = new BillboardGLFeature(getScreenWidth(),
                                         getScreenHeight(),
                                         _anchorU, _anchorV);

  _glState->addGLFeature(_billboardGLF,
                         false);
  

  const Vector3D* position = getCartesianPosition(planet);
  const MutableMatrix44D translation = MutableMatrix44D::createTranslationMatrix(*position);

  _modelTransformGLF = new ModelTransformGLFeature(translation.asMatrix44D());
  _glState->addGLFeature(_modelTransformGLF, false);
  _glPositionOutdated = false;


  if (_textureID != NULL) {

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
                  IFloatBuffer* billboardTexCoords,
                  double horizonBandRadiansPerPixel) {
  _renderedMark = false;

  if (!_declutterHidden &&
      isVisibleFrom(planet, cameraPosition, cameraHeight)) {
    updateHorizonScale(horizonBandRadiansPerPixel);
    ensureTexture(rc);
    if (_textureID != NULL) {
      ensureGLState(planet, billboardTexCoords, parentGLState);
      startPendingEffects(rc, renderer);
      draw(rc);
      _renderedMark = true;
    }
  }
}

bool Mark::isVisibleFrom(const Planet* planet,
                         const MutableVector3D& cameraPosition,
                         double cameraHeight) {
  const Vector3D* markPosition = getCartesianPosition(planet);

  _markCameraVector.set(markPosition->_x - cameraPosition.x(),
                        markPosition->_y - cameraPosition.y(),
                        markPosition->_z - cameraPosition.z());

  return (isRenderableByDistance() &&
          !isOccludedByHorizon(planet, cameraPosition, cameraHeight, markPosition));
}

Vector2F Mark::getOutfitScreenSize(size_t outfitIndex) const {
  float width;
  float height;
  if (_textureSizeSetExternally || (outfitIndex == _outfitIndex)) {
    width  = _textureWidth;
    height = _textureHeight;
  }
  else {
    const MarkOutfitImage* outfitImage = _outfitImages[outfitIndex];
    width  = outfitImage->_width;
    height = outfitImage->_height;
  }
  return Vector2F(width * _textureWidthScale, height * _textureHeightScale);
}

Vector2F Mark::getOutfitAnchor(size_t outfitIndex) const {
  const MarkOutfitImage* outfitImage = _outfitImages[outfitIndex];
  if (outfitImage->_hasAnchor) {
    return Vector2F(outfitImage->_anchorU, outfitImage->_anchorV);
  }
  return Vector2F(_appAnchorU, _appAnchorV);
}

bool Mark::isRenderableByDistance() const {
  const bool hasMinDistanceToCamera = (_minDistanceToCamera > 0);
  const bool hasMaxDistanceToCamera = (_maxDistanceToCamera > 0);
  if (!hasMinDistanceToCamera && !hasMaxDistanceToCamera) {
    return true;
  }

  const double squaredDistanceToCamera = _markCameraVector.squaredLength();

  if (hasMinDistanceToCamera &&
      (squaredDistanceToCamera > (_minDistanceToCamera * _minDistanceToCamera))) {
    return false;
  }

  if (hasMaxDistanceToCamera &&
      (squaredDistanceToCamera < (_maxDistanceToCamera * _maxDistanceToCamera))) {
    return false;
  }

  return true;
}

bool Mark::isOccludedByHorizon(const Planet* planet,
                               const MutableVector3D& cameraPosition,
                               double cameraHeight,
                               const Vector3D* markPosition) {
  if (_position->_height > cameraHeight) {
    _grazingAngle = NAND;
    const std::vector<double> dists = planet->intersectionsDistances(cameraPosition.x(),
                                                                     cameraPosition.y(),
                                                                     cameraPosition.z(),
                                                                     _markCameraVector.x(),
                                                                     _markCameraVector.y(),
                                                                     _markCameraVector.z());
    if (dists.size() > 0) {
      const double dist = dists[0];
      return (dist > 0.0 && dist < 1.0);
    }
    return false;
  }

  // if camera position is upper than mark we can compute horizon culling in a much simpler way
  if (_normalAtMarkPosition == NULL) {
    _normalAtMarkPosition = new Vector3D( planet->geodeticSurfaceNormal(*markPosition) );
  }
  const double angle = Vector3D::angleInRadiansBetween(*_normalAtMarkPosition, _markCameraVector);
  _grazingAngle = angle - HALF_PI;
  return (angle <= HALF_PI);
}

// the band is the mark's own apparent height: a big mark starts shrinking earlier than a small one
void Mark::updateHorizonScale(double radiansPerPixel) {
  float horizonScale = 1;
  if ((radiansPerPixel > 0) && !ISNAN(_grazingAngle)) {
    const double apparentAngle = _textureHeight * _textureHeightScale * radiansPerPixel;
    if ((apparentAngle > 0) && (_grazingAngle < apparentAngle)) {
      horizonScale = (float) (_grazingAngle / apparentAngle);
    }
  }
  if (horizonScale != _horizonScale) {
    _horizonScale = horizonScale;
    updateBillboardSize();
  }
}

void Mark::ensureTexture(const G3MRenderContext* rc) {
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
}

void Mark::ensureGLState(const Planet* planet,
                         IFloatBuffer* billboardTexCoords,
                         const GLState* parentGLState) {
  if (_glState == NULL) {
    createGLState(planet, billboardTexCoords);  // If GLState was disposed due to elevation change
  }
  else if (_glPositionOutdated) {
    updateGLPosition(planet);
  }
  _glState->setParent(parentGLState);
}

void Mark::updateGLPosition(const Planet* planet) {
  const Vector3D* position = getCartesianPosition(planet);
  const MutableMatrix44D translation = MutableMatrix44D::createTranslationMatrix(*position);

  _modelTransformGLF->setMatrix(translation.asMatrix44D());
  _glPositionOutdated = false;
}

void Mark::startPendingEffects(const G3MRenderContext* rc,
                               MarksRenderer* renderer) {
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
}

void Mark::draw(const G3MRenderContext* rc) {
  rc->getGL()->drawArrays(GLPrimitive::triangleStrip(),
                          0,
                          4,
                          _glState,
                          *rc->getGPUProgramManager());
}

void Mark::animatedRemove(bool deleteMark) {
  _zoomOutDisappears = true;
  _deleteMarkOnDisappears = deleteMark;
}

void Mark::cancelEffects() {
  if (_effectsScheduler != NULL) {
    _effectsScheduler->cancelAllEffectsFor(getEffectTarget());
  }
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
    // the features are owned by the GLState
    _modelTransformGLF = NULL;
    _billboardGLF      = NULL;
    _textureGLF        = NULL;
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

  delete _normalAtMarkPosition;
  _normalAtMarkPosition = NULL;

  _glPositionOutdated = true;
}

void Mark::setScreenSize(int width, int height) {
  _textureWidth  = width;
  _textureHeight = height;
  _textureSizeSetExternally = true;

  updateBillboardSize();
}

void Mark::setScreenSizeScale(float scaleWidth, float scaleHeight) {
  _textureWidthScale  = scaleWidth;
  _textureHeightScale = scaleHeight;

  updateBillboardSize();
}

void Mark::setEffectScale(float effectScale) {
  _effectScale = effectScale;

  updateBillboardSize();
}

void Mark::updateBillboardSize() {
  if (_billboardGLF != NULL) {
    const IMathUtils* mu = IMathUtils::instance();
    _billboardGLF->changeSize(mu->round(getScreenWidth()),
                              mu->round(getScreenHeight()));
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

  if (_textureGLF != NULL) {

    if (!_textureGLF->hasTranslateAndScale()) {
      clearGLState();
    }

    _textureGLF->setTranslation(_translationTCX, _translationTCY);
    _textureGLF->setScale(_scalingTCX, _scalingTCY);
  }
}

void Mark::setMarkAnchor(float anchorU, float anchorV) {
  _appAnchorU = anchorU;
  _appAnchorV = anchorV;
  changeAnchor(anchorU, anchorV);
}

void Mark::changeAnchor(float anchorU, float anchorV) {
  if (_billboardGLF != NULL) {
    _billboardGLF->changeAnchor(anchorU, anchorV);
  }
  _anchorU = anchorU;
  _anchorV = anchorV;
}

void Mark::addHint(MarkOutfit* hint) {
  _outfits.push_back(hint);
  _outfitImages.push_back(new MarkOutfitImage(hint->takeImageFactory(), hint->getAnchor()));
  _hasHint = true;
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

void Mark::onImageCreationError(size_t outfitIndex,
                                const std::string& error) {
  MarkOutfitImage* outfitImage = _outfitImages[outfitIndex];
  outfitImage->_listener = NULL;
  outfitImage->_solved   = true;

  if (outfitIndex == _outfitIndex) {
    _textureSolved = true;
  }

  ILogger::instance()->logError("Can't create image for Mark: \"%s\"",
                                error.c_str());
}

void Mark::onImageCreated(size_t outfitIndex,
                          const IImage* image,
                          const std::string& imageName) {
  MarkOutfitImage* outfitImage = _outfitImages[outfitIndex];
  outfitImage->_listener = NULL;
  outfitImage->_solved   = true;
  outfitImage->_imageID  = imageName;
  outfitImage->_width    = image->getWidth();
  outfitImage->_height   = image->getHeight();
  if (outfitImage->_anchor != NULL) {
    const Vector2F anchorUV = outfitImage->_anchor->getAnchor(image);
    outfitImage->_hasAnchor = true;
    outfitImage->_anchorU   = anchorUV._x;
    outfitImage->_anchorV   = anchorUV._y;
  }

  if (outfitIndex == _outfitIndex) {
    _imageID = imageName;
    applyOutfitAnchor(outfitImage);
    onTextureResolved(image);
  }
  else {
    outfitImage->_image = image;
  }
}

void Mark::applyOutfitAnchor(const MarkOutfitImage* outfitImage) {
  if (outfitImage->_hasAnchor) {
    changeAnchor(outfitImage->_anchorU, outfitImage->_anchorV);
  }
  else {
    changeAnchor(_appAnchorU, _appAnchorV);
  }
}

void Mark::stepDeclutterTransition(long long nowMS,
                                   long long growMS,
                                   long long shrinkMS) {
  const long long elapsedMS = (_lastTransitionMS < 0) ? 0 : (nowMS - _lastTransitionMS);
  _lastTransitionMS = nowMS;

  const bool targetOnScreen = !_declutterHidden && (_declutterTarget >= 0) && (_outfitIndex == (size_t) _declutterTarget);
  if (targetOnScreen) {
    if (_transitionScale < 1) {
      _transitionScale = (growMS <= 0) ? 1 : IMathUtils::instance()->min(1.0f, _transitionScale + ((float) elapsedMS / growMS));
      updateBillboardSize();
    }
    return;
  }

  if (_declutterHidden) {
    if (_declutterTarget >= 0) {
      setOutfit(_declutterTarget);
      _declutterHidden = false;
      _transitionScale = 0;
      updateBillboardSize();
    }
    return;
  }

  _transitionScale = (shrinkMS <= 0) ? 0 : IMathUtils::instance()->max(0.0f, _transitionScale - ((float) elapsedMS / shrinkMS));
  if (_transitionScale <= 0) {
    if (_declutterTarget >= 0) {
      setOutfit(_declutterTarget);
    }
    else {
      _declutterHidden = true;
    }
  }
  updateBillboardSize();
}

void Mark::resetDeclutter() {
  _declutterTarget  = 0;
  _declutterHidden  = false;
  _transitionScale  = 1;
  _lastTransitionMS = -1;
  setOutfit(0);
  updateBillboardSize();
}

bool Mark::hasPriority() const {
  return !ISNAN(_priority);
}

void Mark::setOutfit(size_t outfitIndex) {
  if (outfitIndex == _outfitIndex) {
    return;
  }

  // the outfit leaving the screen keeps its image or texture
  MarkOutfitImage* leaving = _outfitImages[_outfitIndex];
  leaving->_textureID = _textureID;
  leaving->_image     = _textureImage;
  leaving->_imageID   = _imageID;

  MarkOutfitImage* arriving = _outfitImages[outfitIndex];
  _textureID     = arriving->_textureID;
  _textureImage  = arriving->_image;
  _imageID       = arriving->_imageID;
  _textureSolved = arriving->_solved;
  arriving->_textureID = NULL;
  arriving->_image     = NULL;

  if (!_textureSizeSetExternally) {
    _textureWidth  = arriving->_width;
    _textureHeight = arriving->_height;
  }
  applyOutfitAnchor(arriving);

  _outfitIndex = outfitIndex;

  clearGLState();
}
