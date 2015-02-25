//
//  NonOverlappingMarksRenderer.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 23/1/15.
//
//

#include "NonOverlappingMarksRenderer.hpp"

#include "Camera.hpp"
#include "Planet.hpp"
#include "GLState.hpp"
#include "FloatBufferBuilderFromCartesian2D.hpp"

#include "Context.hpp"
#include "TexturesHandler.hpp"
#include "Camera.hpp"
#include "Vector3D.hpp"
#include "FloatBufferBuilderFromCartesian3D.hpp"
#include "FloatBufferBuilderFromCartesian2D.hpp"
#include "DirectMesh.hpp"
#include "TexturedMesh.hpp"
#include "HUDPosition.hpp"
#include "HUDSize.hpp"
#include "RenderState.hpp"
#include "IImageBuilder.hpp"
#include "IImageBuilderListener.hpp"
#include "SimpleTextureMapping.hpp"
#include "MultiTextureMapping.hpp"
#include "TextureIDReference.hpp"
#include "ITimer.hpp"
#include "IFactory.hpp"
#include "IMathUtils.hpp"
#include "TouchEvent.hpp"
#include "RectangleF.hpp"

#pragma mark MarkWidget

MarkWidget::MarkWidget(IImageBuilder* imageBuilder):
_image(NULL),
_imageBuilder(imageBuilder),
_viewportExtent(NULL),
_geo2Dfeature(NULL),
_glState(NULL),
_x(NANF),
_y(NANF),
_halfHeight(0),
_halfWidth(0)
{
}

MarkWidget::~MarkWidget()
{
  delete _image;
  delete _imageBuilder;

  _glState->_release();
}

void MarkWidget::init(const G3MRenderContext *rc, int viewportWidth, int viewportHeight) {
  if (_glState == NULL) {
    _glState = new GLState();
    _viewportExtent = new ViewportExtentGLFeature(viewportWidth, viewportHeight);

    _texHandler = rc->getTexturesHandler();
    _imageBuilder->build(rc, new WidgetImageListener(this), true);

    _glState->addGLFeature(_viewportExtent, false);
  }
}

void MarkWidget::prepareWidget(const IImage* image,
                               const std::string& imageName) {
  _image = image;
  _imageName = imageName;

  _halfWidth = image->getWidth() / 2;
  _halfHeight = image->getHeight() / 2;

  FloatBufferBuilderFromCartesian2D pos2D;
  pos2D.add( -_halfWidth, -_halfHeight); //vertex 1
  pos2D.add( -_halfWidth, _halfHeight); //vertex 2
  pos2D.add( _halfWidth, -_halfHeight); //vertex 3
  pos2D.add( _halfWidth, _halfHeight); //vertex 4

  _geo2Dfeature = new Geometry2DGLFeature(pos2D.create(),
                                          2,
                                          0,
                                          true,
                                          0,
                                          1.0f,
                                          true,
                                          10.0f,
                                          Vector2F(_x, _y));

  _glState->addGLFeature(_geo2Dfeature,
                         false);

  FloatBufferBuilderFromCartesian2D texCoords;
  texCoords.add( 0.0f, 1.0f); //vertex 1
  texCoords.add( 0.0f, 0.0f); //vertex 2
  texCoords.add( 1.0f, 1.0f); //vertex 3
  texCoords.add( 1.0f, 0.0f); //vertex 4

  const TextureIDReference* textureID = _texHandler->getTextureIDReference(_image,
                                                                           GLFormat::rgba(),
                                                                           _imageName,
                                                                           false);

  SimpleTextureMapping* textureMapping = new SimpleTextureMapping(textureID,
                                                                  texCoords.create(),
                                                                  true,
                                                                  true);



  textureMapping->modifyGLState(*_glState);
}

void MarkWidget::render(const G3MRenderContext *rc, GLState *glState) {
  rc->getGL()->drawArrays(GLPrimitive::triangleStrip(), 0, 4, _glState, *rc->getGPUProgramManager());
}

void MarkWidget::setScreenPos(float x, float y) {
  if (_geo2Dfeature != NULL) {
    _geo2Dfeature->setTranslation(x, y);
  }
  _x = x;
  _y = y;
}

void MarkWidget::resetPosition() {
  if (_geo2Dfeature != NULL) {
    _geo2Dfeature->setTranslation(0,0);
  }
  _x = NANF;
  _y = NANF;
}

void MarkWidget::onResizeViewportEvent(int width, int height) {
  if (_viewportExtent != NULL) {
    _viewportExtent->changeExtent(width, height);
  }
}

int MarkWidget::getWidth() const {
  return _image == NULL ? 0 : _image->getWidth();
}

int MarkWidget::getHeight() const {
  return _image == NULL ? 0 : _image->getHeight();
}

void MarkWidget::clampPositionInsideScreen(int viewportWidth, int viewportHeight, float margin) {
  const IMathUtils* mu = IMathUtils::instance();
  float x = mu->clamp(_x, _halfWidth  + margin, viewportWidth  - _halfWidth  - margin);
  float y = mu->clamp(_y, _halfHeight + margin, viewportHeight - _halfHeight - margin);

  setScreenPos(x, y);
}

#pragma mark NonOverlappingMark

NonOverlappingMark::NonOverlappingMark(IImageBuilder* imageBuilderWidget,
                                       IImageBuilder* imageBuilderAnchor,
                                       const Geodetic3D& position,
                                       NonOverlappingMarkTouchListener* touchListener,
                                       float springLengthInPixels,
                                       float springK,
                                       float minSpringLength,
                                       float maxSpringLength,
                                       float electricCharge,
                                       float anchorElectricCharge,
                                       float minWidgetSpeedInPixelsPerSecond,
                                       float maxWidgetSpeedInPixelsPerSecond,
                                       float resistanceFactor):
_cartesianPos(NULL),
_dX(0),
_dY(0),
_fX(0),
_fY(0),
_geoPosition(position),
_springLengthInPixels(springLengthInPixels),
_springK(springK),
_minSpringLength( minSpringLength > 0 ? minSpringLength : (springLengthInPixels * 0.85f) ),
_maxSpringLength( maxSpringLength > 0 ? maxSpringLength : (springLengthInPixels * 1.15f) ),
_electricCharge(electricCharge),
_anchorElectricCharge(anchorElectricCharge),
_minWidgetSpeedInPixelsPerSecond(minWidgetSpeedInPixelsPerSecond),
_maxWidgetSpeedInPixelsPerSecond(maxWidgetSpeedInPixelsPerSecond),
_resistanceFactor(resistanceFactor),
_touchListener(touchListener)
{
  _widget = new MarkWidget(imageBuilderWidget);
  _anchorWidget = new MarkWidget(imageBuilderAnchor);
}

NonOverlappingMark::~NonOverlappingMark() {
  delete _touchListener;

  delete _widget;
  delete _anchorWidget;
  delete _cartesianPos;
}

Vector3D NonOverlappingMark::getCartesianPosition(const Planet* planet) const {
  if (_cartesianPos == NULL) {
    _cartesianPos = new Vector3D(planet->toCartesian(_geoPosition));
  }
  return *_cartesianPos;
}

void NonOverlappingMark::computeAnchorScreenPos(const Camera* cam, const Planet* planet) {
  Vector2F sp(cam->point2Pixel(getCartesianPosition(planet)));
  _anchorWidget->setScreenPos(sp._x, sp._y);

  if (_widget->getScreenPos().isNaN()) {
    _widget->setScreenPos(sp._x, sp._y + 0.01f);
  }
}


void NonOverlappingMark::applyCoulombsLaw(NonOverlappingMark* that) { //EM

  Vector2F d = getScreenPos().sub(that->getScreenPos());
  double distance = d.length()  + 0.001;
  Vector2F direction = d.div((float)distance);

  float strength = (float)(this->_electricCharge * that->_electricCharge / (distance * distance));

  Vector2F force = direction.times(strength);

  this->applyForce(force._x, force._y);
  that->applyForce(-force._x, -force._y);

  //  var d = point1.p.subtract(point2.p);
  //  var distance = d.magnitude() + 0.1; // avoid massive forces at small distances (and divide by zero)
  //  var direction = d.normalise();
  //
  //  // apply force to each end point
  //  point1.applyForce(direction.multiply(this.repulsion).divide(distance * distance * 0.5));
  //  point2.applyForce(direction.multiply(this.repulsion).divide(distance * distance * -0.5));

}

void NonOverlappingMark::applyCoulombsLawFromAnchor(NonOverlappingMark* that) { //EM

  Vector2F dAnchor = getScreenPos().sub(that->getAnchorScreenPos());
  double distanceAnchor = dAnchor.length()  + 0.001;
  Vector2F directionAnchor = dAnchor.div((float)distanceAnchor);

  float strengthAnchor = (float)(this->_electricCharge * that->_anchorElectricCharge / (distanceAnchor * distanceAnchor));

  this->applyForce(directionAnchor._x * strengthAnchor,
                   directionAnchor._y * strengthAnchor);
}

void NonOverlappingMark::applyHookesLaw() {   //Spring

  Vector2F d = getScreenPos().sub(getAnchorScreenPos());
  double mod = d.length();
  double displacement = _springLengthInPixels - mod;
  Vector2F direction = d.div((float)mod);

  float force = (float)(_springK * displacement);

  applyForce((float)(direction._x * force),
             (float)(direction._y * force));

  //  var d = spring.point2.p.subtract(spring.point1.p); // the direction of the spring
  //  var displacement = spring.length - d.magnitude();
  //  var direction = d.normalise();
  //
  //  // apply force to each end point
  //  spring.point1.applyForce(direction.multiply(spring.k * displacement * -0.5));
  //  spring.point2.applyForce(direction.multiply(spring.k * displacement * 0.5));
}

void NonOverlappingMark::render(const G3MRenderContext* rc, GLState* glState) {

  if (_widget->isReady() && _anchorWidget->isReady()) {
    _widget->render(rc, glState);
    _anchorWidget->render(rc, glState);
  } else{
    _widget->init(rc, rc->getCurrentCamera()->getViewPortWidth(), rc->getCurrentCamera()->getViewPortHeight());
    _anchorWidget->init(rc, rc->getCurrentCamera()->getViewPortWidth(), rc->getCurrentCamera()->getViewPortHeight());
  }
}

void NonOverlappingMark::updatePositionWithCurrentForce(double elapsedMS,
                                                        int viewportWidth,
                                                        int viewportHeight,
                                                        float viewportMargin) {

  Vector2D oldVelocity(_dX, _dY);
  Vector2D force(_fX, _fY);

  //Assuming Widget Mass = 1.0
  float time = (float)(elapsedMS / 1000.0);
  Vector2D velocity = oldVelocity.add(force.times(time)).times(_resistanceFactor);

  //Force has been applied and must be reset
  _fX = 0;
  _fY = 0;

  //Clamping Velocity
  double velocityPPS = velocity.length();
  if (velocityPPS > _maxWidgetSpeedInPixelsPerSecond) {
    _dX = (float)(velocity._x * (_maxWidgetSpeedInPixelsPerSecond / velocityPPS));
    _dY = (float)(velocity._y * (_maxWidgetSpeedInPixelsPerSecond / velocityPPS));
  }
  else if (velocityPPS < _minWidgetSpeedInPixelsPerSecond) {
    _dX = 0.0;
    _dY = 0.0;
  }
  else {
    _dX = (float)velocity._x;
    _dY = (float)velocity._y;
  }

  //Update position
  Vector2F position = _widget->getScreenPos();

  float newX = position._x + (_dX * time);
  float newY = position._y + (_dY * time);

  Vector2F anchorPos = _anchorWidget->getScreenPos();

  Vector2F spring = Vector2F(newX,newY).sub(anchorPos).clampLength(_minSpringLength, _maxSpringLength);
  Vector2F finalPos = anchorPos.add(spring);

  _widget->setScreenPos(finalPos._x, finalPos._y);
  _widget->clampPositionInsideScreen(viewportWidth, viewportHeight, viewportMargin);
}

void NonOverlappingMark::onResizeViewportEvent(int width, int height) {
  _widget->onResizeViewportEvent(width, height);
  _anchorWidget->onResizeViewportEvent(width, height);
}

bool NonOverlappingMark::onTouchEvent(const Vector2F& touchedPixel) {
  if (_touchListener != NULL) {
    return _touchListener->touchedMark(this, touchedPixel);
  }
  return false;
}

#pragma-mark Renderer

NonOverlappingMarksRenderer::NonOverlappingMarksRenderer(int maxVisibleMarks,
                                                         float viewportMargin,
                                                         int maxConvergenceSteps):
_maxVisibleMarks(maxVisibleMarks),
_viewportMargin(viewportMargin),
_maxConvergenceSteps(maxConvergenceSteps),
_lastPositionsUpdatedTime(0),
_connectorsGLState(NULL),
_visibleMarksIDsBuilder( IStringBuilder::newStringBuilder() ),
_visibleMarksIDs(""),
_touchListener(NULL)
{

}


NonOverlappingMarksRenderer::~NonOverlappingMarksRenderer() {
  _connectorsGLState->_release();

  const int marksSize = _marks.size();
  for (int i = 0; i < marksSize; i++) {
    delete _marks[i];
  }

  for (int i = 0; i < _visibilityListeners.size(); i++) {
    delete _visibilityListeners[i];
  }

  delete _visibleMarksIDsBuilder;
}

void NonOverlappingMarksRenderer::removeAllListeners() {
  for (int i = 0; i < _visibilityListeners.size(); i++) {
    delete _visibilityListeners[i];
  }
  _visibilityListeners.clear();
}

void NonOverlappingMarksRenderer::addMark(NonOverlappingMark* mark) {
  _marks.push_back(mark);
}

void NonOverlappingMarksRenderer::removeAllMarks() {
  const int marksSize = _marks.size();
  for (int i = 0; i < marksSize; i++) {
    delete _marks[i];
  }
  _marks.clear();
}

void NonOverlappingMarksRenderer::computeMarksToBeRendered(const Camera* camera,
                                                           const Planet* planet) {
  _visibleMarks.clear();
  _visibleMarksIDsBuilder->clear();

  const Frustum* frustrum = camera->getFrustumInModelCoordinates();

  const int marksSize = _marks.size();
  for (int i = 0; i < marksSize; i++) {
    NonOverlappingMark* m = _marks[i];

    if (_visibleMarks.size() < _maxVisibleMarks &&
        frustrum->contains(m->getCartesianPosition(planet))) {
      _visibleMarks.push_back(m);

      _visibleMarksIDsBuilder->addInt(i);
      _visibleMarksIDsBuilder->addString("/");
    }
    else {
      //Resetting marks location of invisible anchors
#warning Do we really need this?
      m->resetWidgetPositionVelocityAndForce();
    }
  }

  const std::string currentVisibleMarksIDs = _visibleMarksIDsBuilder->getString();
  if (_visibleMarksIDs != currentVisibleMarksIDs) {
    _visibleMarksIDs = currentVisibleMarksIDs;
    for (int i = 0; i < _visibilityListeners.size(); i++) {
      _visibilityListeners.at(i)->onVisibilityChange(_visibleMarks);
    }
  }
}

void NonOverlappingMarksRenderer::renderConnectorLines(const G3MRenderContext* rc) {
  if (_connectorsGLState == NULL) {
    _connectorsGLState = new GLState();

    _connectorsGLState->addGLFeature(new FlatColorGLFeature(Color::black()), false);
  }

  _connectorsGLState->clearGLFeatureGroup(NO_GROUP);

  FloatBufferBuilderFromCartesian2D pos2D;

  const int visibleMarksSize = _visibleMarks.size();
  for (int i = 0; i < visibleMarksSize; i++) {
    Vector2F sp = _visibleMarks[i]->getScreenPos();
    Vector2F asp = _visibleMarks[i]->getAnchorScreenPos();

    pos2D.add(sp._x, -sp._y);
    pos2D.add(asp._x, -asp._y);
  }

  _connectorsGLState->addGLFeature( new Geometry2DGLFeature(pos2D.create(),
                                                            2,
                                                            0,
                                                            true,
                                                            0,
                                                            3.0f,
                                                            true,
                                                            10.0f,
                                                            Vector2F::zero()),
                                   false);

  _connectorsGLState->addGLFeature(new ViewportExtentGLFeature((int)rc->getCurrentCamera()->getViewPortWidth(),
                                                               (int)rc->getCurrentCamera()->getViewPortHeight()), false);

  rc->getGL()->drawArrays(GLPrimitive::lines(), 0, pos2D.size()/2, _connectorsGLState, *rc->getGPUProgramManager());
}

void NonOverlappingMarksRenderer::computeForces(const Camera* cam, const Planet* planet) {
  const int visibleMarksSize = _visibleMarks.size();

  //Compute Mark Anchor Screen Positions
  for (int i = 0; i < visibleMarksSize; i++) {
    _visibleMarks[i]->computeAnchorScreenPos(cam, planet);
  }

  //Compute Mark Forces
  for (int i = 0; i < visibleMarksSize; i++) {
    NonOverlappingMark* mark = _visibleMarks[i];
    mark->applyHookesLaw();

    for (int j = i+1; j < visibleMarksSize; j++) {
      mark->applyCoulombsLaw(_visibleMarks[j]);
    }

    for (int j = 0; j < visibleMarksSize; j++) {
      if (i != j) {
        mark->applyCoulombsLawFromAnchor(_visibleMarks[j]);
      }
    }
  }
}

void NonOverlappingMarksRenderer::renderMarks(const G3MRenderContext *rc, GLState *glState) {
  //Draw Lines
  renderConnectorLines(rc);

  //Draw Anchors and Marks
  const int visibleMarksSize = _visibleMarks.size();
  for (int i = 0; i < visibleMarksSize; i++) {
    _visibleMarks[i]->render(rc, glState);
  }
}

void NonOverlappingMarksRenderer::applyForces(long long now, const Camera* camera) {

  if (_lastPositionsUpdatedTime != 0) { //If not First frame

    const int viewPortWidth  = camera->getViewPortWidth();
    const int viewPortHeight = camera->getViewPortHeight();

    const double elapsedMS = now - _lastPositionsUpdatedTime;

    //Update Position based on last Forces
    const int visibleMarksSize = _visibleMarks.size();
    for (int i = 0; i < visibleMarksSize; i++) {
      _visibleMarks[i]->updatePositionWithCurrentForce(elapsedMS,
                                                       viewPortWidth,
                                                       viewPortHeight,
                                                       _viewportMargin);
    }
  }

  _lastPositionsUpdatedTime = now;
}

void NonOverlappingMarksRenderer::render(const G3MRenderContext* rc, GLState* glState) {
  const Camera* camera = rc->getCurrentCamera();
  const Planet* planet = rc->getPlanet();

  computeMarksToBeRendered(camera, planet);
  computeForces(camera, planet);

  if (_maxConvergenceSteps > 0) {
    //Looking for convergence on _maxConvergenceSteps
    long long timeStep = 40;
    applyForces(_lastPositionsUpdatedTime + timeStep, camera);

    int iteration = 0;
    while (marksAreMoving() && iteration < _maxConvergenceSteps) {
      computeForces(camera, planet);
      applyForces(_lastPositionsUpdatedTime + timeStep, camera);
      iteration++;
    }
  }
  else {
    //Real Time
    applyForces(rc->getFrameStartTimer()->nowInMilliseconds(), camera);
  }

  renderMarks(rc, glState);
}

void NonOverlappingMarksRenderer::onResizeViewportEvent(const G3MEventContext* ec, int width, int height) {
  const int marksSize = _marks.size();
  for (int i = 0; i < marksSize; i++) {
    _marks[i]->onResizeViewportEvent(width, height);
  }
}

int NonOverlappingMark::getWidth() const {
  return _widget->getWidth();
}

int NonOverlappingMark::getHeight() const {
  return _widget->getHeight();
}

bool NonOverlappingMarksRenderer::onTouchEvent(const G3MEventContext* ec, const TouchEvent* touchEvent) {
  bool handled = false;

  if ( touchEvent->getType() == DownUp ) {
    const Vector2F touchedPixel = touchEvent->getTouch(0)->getPos();

    double minSqDistance = IMathUtils::instance()->maxDouble();
    NonOverlappingMark* nearestMark = NULL;

    const int visibleMarksSize = _visibleMarks.size();
    for (int i = 0; i < visibleMarksSize; i++) {
      NonOverlappingMark* mark = _visibleMarks[i];

      const int markWidth = mark->getWidth();
      if (markWidth <= 0) {
        continue;
      }

      const int markHeight = mark->getHeight();
      if (markHeight <= 0) {
        continue;
      }

      const Vector2F markPixel = mark->getScreenPos();

      const RectangleF markPixelBounds(markPixel._x - ((float) markWidth / 2),
                                       markPixel._y - ((float) markHeight / 2),
                                       markWidth,
                                       markHeight);

      if (markPixelBounds.contains(touchedPixel._x, touchedPixel._y)) {
        const double sqDistance = markPixel.squaredDistanceTo(touchedPixel);
        if (sqDistance < minSqDistance) {
          nearestMark = mark;
          minSqDistance = sqDistance;
        }
      }
    }

    if (nearestMark != NULL) {
      handled = nearestMark->onTouchEvent(touchedPixel);
      if (!handled) {
        if (_touchListener != NULL) {
          handled = _touchListener->touchedMark(nearestMark, touchedPixel);
        }
      }
    }

  }

  return handled;
}

bool NonOverlappingMarksRenderer::marksAreMoving() const{
  const int visibleMarksSize = _visibleMarks.size();
  for (int i = 0; i < visibleMarksSize; i++) {
    if (_visibleMarks[i]->isMoving()) {
      return true;
    }
  }
  return false;
}

void NonOverlappingMarksRenderer::setTouchListener(NonOverlappingMarkTouchListener* touchListener) {
  if (_touchListener != NULL && _touchListener != touchListener) {
    delete _touchListener;
  }
  _touchListener = touchListener;
}
