//
//  MarksRenderer.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 05/06/12.
//

#include "MarksRenderer.hpp"
#include "RenderState.hpp"
#include "Camera.hpp"
#include "GL.hpp"
#include "TouchEvent.hpp"
#include "RectangleF.hpp"
#include "Mark.hpp"
#include "MarkTouchListener.hpp"
#include "DownloadPriority.hpp"
#include "FloatBufferBuilderFromCartesian2D.hpp"
#include "GPUProgram.hpp"
#include "GPUProgramManager.hpp"
#include "Vector2F.hpp"
#include "G3MEventContext.hpp"
#include "MarkFilter.hpp"
#include "G3MRenderContext.hpp"
#include "TimeInterval.hpp"
#include "ITimer.hpp"
#include "IImage.hpp"
#include "IImageFactory.hpp"
#include "IImageFactoryListener.hpp"
#include "StaticImageFactory.hpp"
#include "FixedMarkAnchor.hpp"
#include "MarkOutfit.hpp"
#include "ILogger.hpp"


// owns the factory while the image is created: the renderer may go first
class MarksRenderer_HintListener : public IImageFactoryListener {
private:
  MarksRenderer* _renderer;
  IImageFactory* _imageFactory;

public:
  MarksRenderer_HintListener(MarksRenderer* renderer,
                             IImageFactory* imageFactory) :
  _renderer(renderer),
  _imageFactory(imageFactory)
  {
  }

  ~MarksRenderer_HintListener() {
    delete _imageFactory;
  }

  void forgetRenderer() {
    _renderer = NULL;
  }

  void imageCreated(const IImage*      image,
                    const std::string& imageName) {
    if (_renderer == NULL) {
      delete image;
    }
    else {
      _renderer->onHintImageCreated(image, imageName);
    }
  }

  void onError(const std::string& error) {
    ILogger::instance()->logError("Can't create the marks' hint image: \"%s\"", error.c_str());
  }
};


void MarksRenderer::setMarkTouchListener(MarkTouchListener* markTouchListener,
                                         bool autoDelete) {
  if ( _autoDeleteMarkTouchListener ) {
    delete _markTouchListener;
  }

  _markTouchListener = markTouchListener;
  _autoDeleteMarkTouchListener = autoDelete;
}

MarksRenderer::MarksRenderer(bool readyWhenMarksReady,
                             bool renderInReverse,
                             bool progressiveInitialization) :
_readyWhenMarksReady(readyWhenMarksReady),
_renderInReverse(renderInReverse),
_progressiveInitialization(progressiveInitialization),
_declutter(false),
_horizonBand(true),
_declutterMargin(2),
_growMS(500),
_shrinkMS(300),
_hintImageFactory(NULL),
_hintListener(NULL),
_hintImage(NULL),
_hintImageName(""),
_lastCamera(NULL),
_markTouchListener(NULL),
_autoDeleteMarkTouchListener(false),
_downloadPriority(DownloadPriority::MEDIUM),
_glState(new GLState()),
_billboardTexCoords(NULL),
_initializationTimer(NULL)
{
  _context = NULL;
}


MarksRenderer::~MarksRenderer() {
  delete _initializationTimer;

  if (_hintListener != NULL) {
    _hintListener->forgetRenderer();
  }
  delete _hintImageFactory; // only while it waits for a context
  delete _hintImage;

  const size_t marksSize = _marks.size();
  for (size_t i = 0; i < marksSize; i++) {
    delete _marks[i];
  }

  if ( _autoDeleteMarkTouchListener ) {
    delete _markTouchListener;
  }
  _markTouchListener = NULL;

  _glState->_release();

  delete _billboardTexCoords;

#ifdef JAVA_CODE
  super.dispose();
#endif
}

void MarksRenderer::onChangedContext() {
  startHintImage();

  const size_t marksSize = _marks.size();
  for (size_t i = 0; i < marksSize; i++) {
    Mark* mark = _marks[i];
    mark->initialize(_context, _downloadPriority);
  }
}

const bool MarksRenderer::hasMarks() const {
  return !_marks.empty();
}

void MarksRenderer::addMark(Mark* mark) {
  attachHint(mark);
  _marks.push_back(mark);
  if ((_context != NULL) && !_progressiveInitialization) {
    mark->initialize(_context, _downloadPriority);
  }
}

void MarksRenderer::removeMark(Mark* mark) {
  const size_t marksSize = _marks.size();
  for (size_t i = 0; i < marksSize; i++) {
    if (_marks[i] == mark) {
#ifdef C_CODE
      _marks.erase(_marks.begin() + i);
#endif
#ifdef JAVA_CODE
      _marks.remove(i);
#endif
      break;
    }
  }
}

void MarksRenderer::removeAllMarks(bool deleteMarks) {
  if (deleteMarks) {
    const size_t marksSize = _marks.size();
    for (size_t i = 0; i < marksSize; i++) {
      delete _marks[i];
    }
  }
  _marks.clear();
}

bool MarksRenderer::onTouchEvent(const G3MEventContext* ec,
                                 const TouchEvent* touchEvent) {

  bool handled = false;
  if ( touchEvent->getType() == DownUp ) {
    if (_lastCamera != NULL) {
      const Vector2F touchedPixel = touchEvent->getTouch(0)->getPos();

      const Planet* planet = ec->getPlanet();

      double minSqDistance = IMathUtils::instance()->maxDouble();
      Mark* nearestMark = NULL;

      const size_t marksSize = _marks.size();
      for (size_t i = 0; i < marksSize; i++) {
        Mark* mark = _marks[i];

        if (!mark->isReady()) {
          continue;
        }
        if (!mark->isRendered() || mark->isShowingHint()) {
          continue;
        }

        const float markWidth = mark->getScreenWidth();
        if (markWidth <= 0) {
          continue;
        }

        const float markHeight = mark->getScreenHeight();
        if (markHeight <= 0) {
          continue;
        }

        const Vector3D* cartesianMarkPosition = mark->getCartesianPosition(planet);
        const Vector2F markPixel = _lastCamera->point2Pixel(*cartesianMarkPosition);

        const RectangleF markPixelBounds(markPixel._x - (markWidth  * mark->getMarkAnchorU()),
                                         markPixel._y - (markHeight * mark->getMarkAnchorV()),
                                         markWidth,
                                         markHeight);

        if (markPixelBounds.contains(touchedPixel._x, touchedPixel._y)) {
          const double sqDistance = markPixel.squaredDistanceTo(touchedPixel);
          if (sqDistance < minSqDistance) {
            nearestMark   = mark;
            minSqDistance = sqDistance;
          }
        }
      }

      if (nearestMark != NULL) {
        handled = nearestMark->touched(touchEvent);
        if (!handled) {
          if (_markTouchListener != NULL) {
            handled = _markTouchListener->touchedMark(nearestMark, touchEvent);
          }
        }
      }
    }
  }

  return handled;
}

RenderState MarksRenderer::getRenderState(const G3MRenderContext* rc) {
  if (_readyWhenMarksReady) {
    const size_t marksSize = _marks.size();
    for (size_t i = 0; i < marksSize; i++) {
      if (!_marks[i]->isReady()) {
        return RenderState::busy();
      }
    }
  }

  return RenderState::ready();
}

IFloatBuffer* MarksRenderer::getBillboardTexCoords() {
  if (_billboardTexCoords == NULL) {
    FloatBufferBuilderFromCartesian2D texCoor;
    texCoor.add(1,1);
    texCoor.add(1,0);
    texCoor.add(0,1);
    texCoor.add(0,0);
    _billboardTexCoords = texCoor.create();
  }
  return _billboardTexCoords;
}

void MarksRenderer::render(const G3MRenderContext* rc, GLState* glState) {
  const size_t marksSize = _marks.size();

  if (marksSize > 0) {
    const Camera* camera = rc->getCurrentCamera();

    _lastCamera = camera; // Saving camera for use in onTouchEvent

    MutableVector3D cameraPosition;
    camera->getCartesianPositionMutable(cameraPosition);
    const double cameraHeight = camera->getGeodeticHeight();

    updateGLState(rc);

    const Planet* planet = rc->getPlanet();
    GL* gl = rc->getGL();

    IFloatBuffer* billboardTexCoord = getBillboardTexCoords();

    if (_progressiveInitialization) {
      if (_initializationTimer == NULL) {
        _initializationTimer = rc->getFactory()->createTimer();
      }
      else {
        _initializationTimer->start();
      }

      for (size_t i = 0; i < marksSize; i++) {
        if (_initializationTimer->elapsedTimeInMilliseconds() > 5) {
          break;
        }

        const size_t ii = _renderInReverse ? i : (marksSize-1-i);
        Mark* mark = _marks[ii];
        if (!mark->isInitialized()) {
          mark->initialize(_context, _downloadPriority);
        }
      }
    }

    if (_declutter) {
      declutter(camera, planet, cameraPosition, cameraHeight);

      const long long nowMS = rc->getFrameStartTimer()->nowInMilliseconds();
      for (size_t i = 0; i < marksSize; i++) {
        _marks[i]->stepDeclutterTransition(nowMS, _growMS, _shrinkMS);
      }
    }

    const double horizonBandRadiansPerPixel = _horizonBand ? (camera->getVerticalFOV()._radians / camera->getViewPortHeight()) : 0;

    for (size_t i = 0; i < marksSize; i++) {
      const size_t ii = _renderInReverse ? (marksSize-1-i) : i;
      Mark* mark = _marks[ii];
      if (mark->isReady()) {
        mark->render(rc,
                     this,
                     cameraPosition,
                     cameraHeight,
                     _glState,
                     planet,
                     gl,
                     billboardTexCoord,
                     horizonBandRadiansPerPixel);
      }
    }
  }
}

void MarksRenderer::setHint(IImageFactory* hintImageFactory) {
  if (_hintListener != NULL) {
    _hintListener->forgetRenderer();
    _hintListener = NULL;
  }
  delete _hintImageFactory;
  delete _hintImage;
  _hintImage     = NULL;
  _hintImageName = "";

  _hintImageFactory = hintImageFactory;
  startHintImage();
}

void MarksRenderer::startHintImage() {
  if ((_context == NULL) || (_hintImageFactory == NULL)) {
    return;
  }
  IImageFactory* hintImageFactory = _hintImageFactory;
  _hintImageFactory = NULL; // ownership moved to the listener
  _hintListener = new MarksRenderer_HintListener(this, hintImageFactory);
  hintImageFactory->create(_context, _hintListener, true);
}

void MarksRenderer::onHintImageCreated(const IImage* image,
                                       const std::string& imageName) {
  _hintListener  = NULL;
  _hintImage     = image;
  _hintImageName = imageName;

  for (size_t i = 0; i < _marks.size(); i++) {
    attachHint(_marks[i]);
  }
}

// every mark gets its own copy of the image; the same name shares the texture
void MarksRenderer::attachHint(Mark* mark) {
  if ((_hintImage == NULL) || mark->hasHint()) {
    return;
  }
  mark->addHint(new MarkOutfit(new StaticImageFactory(_hintImage->shallowCopy(), _hintImageName),
                               new FixedMarkAnchor(0.5f, 0.5f)));
  if (mark->isInitialized()) {
    mark->createPendingOutfitImages(_context);
  }
}

void MarksRenderer::setDeclutterTransitionDurations(const TimeInterval& grow,
                                                    const TimeInterval& shrink) {
  _growMS   = grow.milliseconds();
  _shrinkMS = shrink.milliseconds();
}

void MarksRenderer::setDeclutter(bool declutter) {
  _declutter = declutter;
  if (!_declutter) {
    for (size_t i = 0; i < _marks.size(); i++) {
      _marks[i]->resetDeclutter();
    }
  }
}

bool MarksRenderer::isFree(float left, float top, float right, float bottom) const {
  const size_t takenSize = _takenLeft.size();
  for (size_t i = 0; i < takenSize; i++) {
    if ((left < _takenRight[i]) && (right > _takenLeft[i]) &&
        (top < _takenBottom[i]) && (bottom > _takenTop[i])) {
      return false;
    }
  }
  return true;
}

void MarksRenderer::declutter(const Camera* camera,
                              const Planet* planet,
                              const MutableVector3D& cameraPosition,
                              double cameraHeight) {
  // the order the marks end up on top: the last drawn is the first one
  std::vector<Mark*>& candidates = _declutterCandidates;
  candidates.clear();
  const size_t marksSize = _marks.size();
  for (size_t i = 0; i < marksSize; i++) {
    const size_t ii = _renderInReverse ? i : (marksSize-1-i);
    Mark* mark = _marks[ii];
    if (mark->isReady() && mark->isVisibleFrom(planet, cameraPosition, cameraHeight)) {
      candidates.push_back(mark);
    }
  }

  // explicit priorities first, the highest first; a stable insertion keeps the drawing order for the rest
  std::vector<Mark*>& ordered = _declutterOrder;
  ordered.clear();
  for (size_t i = 0; i < candidates.size(); i++) {
    Mark* mark = candidates[i];
    if (mark->hasPriority()) {
      size_t position = 0;
      while ((position < ordered.size()) && (ordered[position]->getPriority() >= mark->getPriority())) {
        position++;
      }
#ifdef C_CODE
      ordered.insert(ordered.begin() + position, mark);
#endif
#ifdef JAVA_CODE
      ordered.add(position, mark);
#endif
    }
  }
  for (size_t i = 0; i < candidates.size(); i++) {
    Mark* mark = candidates[i];
    if (!mark->hasPriority()) {
      ordered.push_back(mark);
    }
  }

  _takenLeft.clear();
  _takenTop.clear();
  _takenRight.clear();
  _takenBottom.clear();

  for (size_t i = 0; i < ordered.size(); i++) {
    Mark* mark = ordered[i];
    const Vector2F markPixel = camera->point2Pixel(*mark->getCartesianPosition(planet));

    bool placed = false;
    const size_t outfitsCount = mark->getOutfitsCount();
    for (size_t outfitIndex = 0; (outfitIndex < outfitsCount) && !placed; outfitIndex++) {
      const Vector2F size = mark->getOutfitScreenSize(outfitIndex);
      if ((size._x <= 0) || (size._y <= 0)) {
        continue; // its image does not exist yet
      }
      const Vector2F anchor = mark->getOutfitAnchor(outfitIndex);
      const float left = markPixel._x - (size._x * anchor._x);
      const float top  = markPixel._y - (size._y * anchor._y);

      const int   target = mark->getDeclutterTarget();
      const bool  grows  = (target < 0) || ((int) outfitIndex < target);
      const float margin = grows ? _declutterMargin : 0;

      if (isFree(left - margin, top - margin, left + size._x + margin, top + size._y + margin)) {
        mark->setDeclutterTarget((int) outfitIndex);
        _takenLeft.push_back(left);
        _takenTop.push_back(top);
        _takenRight.push_back(left + size._x);
        _takenBottom.push_back(top + size._y);
        placed = true;
      }
    }

    if (!placed) {
      mark->setDeclutterTarget(-1);
    }
  }
}

void MarksRenderer::updateGLState(const G3MRenderContext* rc) {
  const Camera* camera = rc->getCurrentCamera();

  ModelViewGLFeature* f = (ModelViewGLFeature*) _glState->getGLFeature(GLF_MODEL_VIEW);
  if (f == NULL) {
    _glState->addGLFeature(new ModelViewGLFeature(camera), true);
  }
  else {
    f->setMatrix(camera->getModelViewMatrix44D());
  }

  if (_glState->getGLFeature(GLF_VIEWPORT_EXTENT) == NULL) {
    _glState->clearGLFeatureGroup(NO_GROUP);
    _glState->addGLFeature(new ViewportExtentGLFeature(camera,
                                                       rc->getViewMode()),
                           false);
  }
}

void MarksRenderer::onResizeViewportEvent(const G3MEventContext* ec,
                                          int width, int height) {
  _glState->clearGLFeatureGroup(NO_GROUP);

  int logicWidth = width;
  if (ec->getViewMode() == STEREO) {
    logicWidth /= 2;
  }

  _glState->addGLFeature(new ViewportExtentGLFeature(logicWidth, height),
                         false);
}

size_t MarksRenderer::removeAllMarks(const MarkFilter& filter,
                                     bool animated,
                                     bool deleteMarks) {
  size_t removed = 0;
  const size_t marksSize = _marks.size();

  if (animated) {
    std::vector<Mark*> survivingMarks;
    for (size_t i = 0; i < marksSize; i++) {
      Mark* mark = _marks[i];
      if (filter.test(mark)) {
        removed++;
        const bool visible = isEnable() && mark->isRendered();
        if (visible || mark->isDisappearing()) {
          mark->animatedRemove(deleteMarks);
          survivingMarks.push_back(mark); // the zoom-out effect removes it when done
        }
        else {
          // nobody sees it, and the zoom-out only starts on render, which may never come
          mark->cancelEffects();
          if (deleteMarks) {
            delete mark;
          }
        }
      }
      else {
        survivingMarks.push_back(mark);
      }
    }

    if (removed > 0) {
      _marks = survivingMarks;
    }
  }
  else {
    std::vector<Mark*> survivingMarks;
    for (size_t i = 0; i < marksSize; i++) {
      Mark* mark = _marks[i];
      if (filter.test(mark)) {
        if (deleteMarks) {
          delete mark;
        }
        removed++;
      }
      else {
        survivingMarks.push_back(mark);
      }
    }

    if (removed > 0) {
      _marks = survivingMarks;
    }
  }
  return removed;
}


const std::vector<Mark*> MarksRenderer::getAllMarks(const MarkFilter& filter) const {
  std::vector<Mark*> result;

  const size_t marksSize = _marks.size();
  for (size_t i = 0; i < marksSize; i++) {
    Mark* mark = _marks[i];
    if (filter.test(mark)) {
      result.push_back( mark );
    }
  }

  return result;
}
