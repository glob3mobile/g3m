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

MarkWidget::MarkWidget(IImageBuilder* imageBuilder):
_image(NULL),
_imageBuilder(imageBuilder),
_viewportExtentGLFeature(NULL),
_geo2Dfeature(NULL),
_glState(NULL),
_x(NANF),
_y(NANF),
_halfHeight(0),
_halfWidth(0),
_vertices(NULL),
_textureMapping(NULL)
{
}

MarkWidget::~MarkWidget() {
  delete _image;
  delete _imageBuilder;

  delete _vertices;
  delete _textureMapping;

  if (_glState != NULL) {
    _glState->_release();
  }
}

void MarkWidget::init(const G3MRenderContext* rc) {
  if (_glState == NULL) {
    _glState = new GLState();
    _viewportExtentGLFeature = new ViewportExtentGLFeature(rc->getCurrentCamera());

    _texHandler = rc->getTexturesHandler();
    _imageBuilder->build(rc, new WidgetImageListener(this), true);

    _glState->addGLFeature(_viewportExtentGLFeature, false);
  }
}

void MarkWidget::prepareWidget(const IImage* image,
                               const std::string& imageName) {
  _image     = image;
  _imageName = imageName;

  _halfWidth  = (float) image->getWidth()  / 2.0f;
  _halfHeight = (float) image->getHeight() / 2.0f;

  if (_vertices != NULL) {
    delete _vertices;
  }

  FloatBufferBuilderFromCartesian2D pos2D;
  pos2D.add( -_halfWidth, -_halfHeight );   // vertex 1
  pos2D.add( -_halfWidth,  _halfHeight );   // vertex 2
  pos2D.add(  _halfWidth, -_halfHeight );   // vertex 3
  pos2D.add(  _halfWidth,  _halfHeight );   // vertex 4
// #warning TODO: share vertices for marks of the same size?

  _vertices = pos2D.create();

  _geo2Dfeature = new Geometry2DGLFeature(_vertices,       // buffer
                                          2,               // arrayElementSize
                                          0,               // index
                                          true,            // normalized
                                          0,               // stride
                                          3.0f,            // lineWidth
                                          true,            // needsPointSize
                                          1.0f,            // pointSize
                                          Vector2F(_x, _y) // translation
                                          );

  _glState->addGLFeature(_geo2Dfeature,
                         false);

  FloatBufferBuilderFromCartesian2D texCoords;
  texCoords.add( 0.0f, 1.0f );   // vertex 1
  texCoords.add( 0.0f, 0.0f );   // vertex 2
  texCoords.add( 1.0f, 1.0f );   // vertex 3
  texCoords.add( 1.0f, 0.0f );   // vertex 4

  const TextureIDReference* textureID = _texHandler->getTextureIDReference(_image,
                                                                           GLFormat::rgba(),
                                                                           _imageName,
                                                                           false);

// #warning TODO: share unit texCoords
  if (_textureMapping != NULL) {
    delete _textureMapping;
  }
  _textureMapping = new SimpleTextureMapping(textureID,
                                             texCoords.create(),
                                             true,
                                             true);

  _textureMapping->modifyGLState(*_glState);
}

void MarkWidget::render(const G3MRenderContext *rc,
                        GLState *glState) {
  rc->getGL()->drawArrays(GLPrimitive::triangleStrip(),
                          0,   // first
                          4,   // count
                          _glState,
                          *rc->getGPUProgramManager());
}

void MarkWidget::setAndClampScreenPos(float x,
                                      float y,
                                      int viewportWidth,
                                      int viewportHeight,
                                      float margin) {
  const IMathUtils* mu = IMathUtils::instance();
  const float xx = mu->clamp(x, _halfWidth  + margin, viewportWidth  - _halfWidth  - margin);
  const float yy = mu->clamp(y, _halfHeight + margin, viewportHeight - _halfHeight - margin);

  if (_geo2Dfeature != NULL) {
    _geo2Dfeature->setTranslation(xx, yy);
  }
  _x = xx;
  _y = yy;
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
  if (_viewportExtentGLFeature != NULL) {
    _viewportExtentGLFeature->changeExtent(width, height);
  }
}

int MarkWidget::getWidth() const {
  return _image == NULL ? 0 : _image->getWidth();
}

int MarkWidget::getHeight() const {
  return _image == NULL ? 0 : _image->getHeight();
}

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
                                       float resistanceFactor):
_cartesianPos(NULL),
//_widgetScreenPosition(MutableVector2F::nan()),
//_anchorScreenPosition(MutableVector2F::nan()),
_speed(MutableVector2F::zero()),
_force(MutableVector2F::zero()),
_geoPosition(position),
_springLengthInPixels(springLengthInPixels),
_springK(springK),
_minSpringLength( minSpringLength > 0 ? minSpringLength : (springLengthInPixels * 0.25f) ),
_maxSpringLength( maxSpringLength > 0 ? maxSpringLength : (springLengthInPixels * 1.25f) ),
_electricCharge(electricCharge),
_anchorElectricCharge(anchorElectricCharge),
_resistanceFactor(resistanceFactor),
_touchListener(touchListener),
_springGLState(NULL),
_springVertices(NULL),
_springViewportExtentGLFeature(NULL)
//_enclosingRadius(0)
{
  _widget = new MarkWidget(imageBuilderWidget);
  _anchorWidget = new MarkWidget(imageBuilderAnchor);
}

NonOverlappingMark::~NonOverlappingMark() {
  delete _touchListener;

  delete _widget;
  delete _anchorWidget;
  delete _cartesianPos;

  if (_springGLState != NULL) {
    _springGLState->_release();
  }

  delete _springVertices;
}

Vector3D NonOverlappingMark::getCartesianPosition(const Planet* planet) const {
// #warning toCartesian without garbage
  if (_cartesianPos == NULL) {
    _cartesianPos = new Vector3D(planet->toCartesian(_geoPosition));
  }
  return *_cartesianPos;
}

void NonOverlappingMark::computeAnchorScreenPos(const Camera* camera,
                                                const Planet* planet) {
  Vector2F sp(camera->point2Pixel(getCartesianPosition(planet)));
//  _anchorScreenPosition.put(sp._x, sp._y);
//  if (_widgetScreenPosition.isNan()) {
//    _widgetScreenPosition.put(sp._x, sp._y + 0.01f);
//  }

  _anchorWidget->setScreenPos(sp._x, sp._y);

  if (_widget->getScreenPos().isNan()) {
    const float deltaX = (float) (IMathUtils::instance()->nextRandomDouble() * 2 - 1);
    const float deltaY = (float) (IMathUtils::instance()->nextRandomDouble() * 2 - 1);
    _widget->setScreenPos(sp._x + deltaX,
                          sp._y + deltaY);
  }
}

void NonOverlappingMark::applyCoulombsLaw(NonOverlappingMark* that) {
  const Vector2F d = getScreenPos().sub(that->getScreenPos());
//  double distance = d.length() - this->_enclosingRadius/3 - that->_enclosingRadius/3;
//  if (distance <= 0) {
//    distance = d.length() + 0.001;
//  }

  const double distance = d.length() + 0.001;
  Vector2F direction = d.div((float)distance);

  float strength = (float)(this->_electricCharge * that->_electricCharge / (distance * distance));

  const Vector2F force = direction.times(strength);

  this->applyForce( force._x,  force._y);
  that->applyForce(-force._x, -force._y);
}

void NonOverlappingMark::applyCoulombsLawFromAnchor(NonOverlappingMark* that) {
  Vector2F dAnchor = getScreenPos().sub(that->getAnchorScreenPos());
  double distanceAnchor = dAnchor.length() + 0.001;
//  double distanceAnchor = dAnchor.length() - this->_enclosingRadius/3;
//  if (distanceAnchor <= 0) {
//    distanceAnchor = dAnchor.length() + 0.001;
//  }

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

  applyForce(direction._x * force,
             direction._y * force);
}

void NonOverlappingMark::renderWidget(const G3MRenderContext* rc,
                                      GLState* glState) {
  if (_widget->isReady()) {
    if (_anchorWidget->isReady()) {
      _widget->render(rc, glState);
//      if (_enclosingRadius == 0) {
//        const float w = _widget->getWidth();
//        const float h = _widget->getHeight();
//        _enclosingRadius = IMathUtils::instance()->sqrt( w*w + h*h ) / 2;
//      }
    }
  }
  else {
    _widget->init(rc);
  }
}

void NonOverlappingMark::renderAnchorWidget(const G3MRenderContext* rc,
                                            GLState* glState) {
  if (_anchorWidget->isReady()) {
    if (_widget->isReady()) {
      _anchorWidget->render(rc, glState);
    }
  }
  else {
    _anchorWidget->init(rc);
  }
}

void NonOverlappingMark::renderSpringWidget(const G3MRenderContext* rc,
                                            GLState* glState) {
  if (!_widget->isReady() || !_anchorWidget->isReady()) {
    return;
  }

  const Vector2F sp  = getScreenPos();
  const Vector2F asp = getAnchorScreenPos();

  if (_springGLState == NULL) {
    _springGLState = new GLState();

    _springGLState->addGLFeature(new FlatColorGLFeature(Color::black()), false);

    _springVertices = rc->getFactory()->createFloatBuffer(2 * 2);
    _springVertices->rawPut(0,  sp._x);
    _springVertices->rawPut(1, -sp._y);
    _springVertices->rawPut(2,  asp._x);
    _springVertices->rawPut(3, -asp._y);

    _springGLState->addGLFeature(new Geometry2DGLFeature(_springVertices,  // buffer
                                                         2,                // arrayElementSize
                                                         0,                // index
                                                         true,             // normalized
                                                         0,                // stride
                                                         3.0f,             // lineWidth
                                                         true,             // needsPointSize
                                                         1.0f,             // pointSize
                                                         Vector2F::zero()  // translation
                                                         ),
                                 false);

    _springViewportExtentGLFeature = new ViewportExtentGLFeature(rc->getCurrentCamera());
    _springGLState->addGLFeature(_springViewportExtentGLFeature, false);
  }
  else {
    _springVertices->put(0,  sp._x);
    _springVertices->put(1, -sp._y);
    _springVertices->put(2,  asp._x);
    _springVertices->put(3, -asp._y);
  }

  rc->getGL()->drawArrays(GLPrimitive::lines(),
                          0,                    // first
                          2,                    // count
                          _springGLState,
                          *rc->getGPUProgramManager());
}

void NonOverlappingMark::updatePositionWithCurrentForce(float timeInSeconds,
                                                        int viewportWidth,
                                                        int viewportHeight,
                                                        float viewportMargin) {
  _speed.add(_force._x * timeInSeconds,
             _force._y * timeInSeconds);
  _speed.times(_resistanceFactor);

  //Force has been applied and must be reset
  _force.set(0, 0);

  //Update position
  Vector2F widgetPosition = _widget->getScreenPos();

  const float newX = widgetPosition._x + (_speed._x * timeInSeconds);
  const float newY = widgetPosition._y + (_speed._y * timeInSeconds);

  Vector2F anchorPosition = _anchorWidget->getScreenPos();

//  Vector2F spring = Vector2F(newX,newY).sub(anchorPosition).clampLength(_minSpringLength, _maxSpringLength);
  Vector2F spring = Vector2F(newX - anchorPosition._x, newY - anchorPosition._y).clampLength(_minSpringLength, _maxSpringLength);

  _widget->setAndClampScreenPos(anchorPosition._x + spring._x,
                                anchorPosition._y + spring._y,
                                viewportWidth,
                                viewportHeight,
                                viewportMargin);
}

void NonOverlappingMark::onResizeViewportEvent(int width, int height) {
  _widget->onResizeViewportEvent(width, height);
  _anchorWidget->onResizeViewportEvent(width, height);

  if (_springViewportExtentGLFeature != NULL) {
    _springViewportExtentGLFeature->changeExtent(width, height);
  }
}

bool NonOverlappingMark::onTouchEvent(const Vector2F& touchedPixel) {
  if (_touchListener != NULL) {
    return _touchListener->touchedMark(this, touchedPixel);
  }
  return false;
}

NonOverlappingMarksRenderer::NonOverlappingMarksRenderer(size_t maxVisibleMarks,
                                                         float viewportMargin):
_maxVisibleMarks(maxVisibleMarks),
_viewportMargin(viewportMargin),
_lastPositionsUpdatedTime(0),
//_connectorsGLState(NULL),
_visibleMarksIDsBuilder( IStringBuilder::newStringBuilder() ),
_visibleMarksIDs(""),
_touchListener(NULL)
{

}

NonOverlappingMarksRenderer::~NonOverlappingMarksRenderer() {
//  _connectorsGLState->_release();

  const size_t marksSize = _marks.size();
  for (size_t i = 0; i < marksSize; i++) {
    delete _marks[i];
  }

  for (size_t i = 0; i < _visibilityListeners.size(); i++) {
    delete _visibilityListeners[i];
  }

  delete _visibleMarksIDsBuilder;
}

void NonOverlappingMarksRenderer::removeAllListeners() {
  for (size_t i = 0; i < _visibilityListeners.size(); i++) {
    delete _visibilityListeners[i];
  }
  _visibilityListeners.clear();
}

void NonOverlappingMarksRenderer::addMark(NonOverlappingMark* mark) {
  _marks.push_back(mark);
}

void NonOverlappingMarksRenderer::removeAllMarks() {
  const size_t marksSize = _marks.size();
  for (size_t i = 0; i < marksSize; i++) {
    delete _marks[i];
  }
  _marks.clear();
  _visibleMarks.clear();
}

void NonOverlappingMarksRenderer::computeMarksToBeRendered(const Camera* camera,
                                                           const Planet* planet) {
  _visibleMarks.clear();
  _visibleMarksIDsBuilder->clear();

  const Frustum* frustrum = camera->getFrustumInModelCoordinates();

  const size_t marksSize = _marks.size();
  for (size_t i = 0; i < marksSize; i++) {
    NonOverlappingMark* m = _marks[i];

    if (_visibleMarks.size() < _maxVisibleMarks &&
        frustrum->contains(m->getCartesianPosition(planet))) {
      _visibleMarks.push_back(m);

      _visibleMarksIDsBuilder->addLong(i);
      _visibleMarksIDsBuilder->addString("/");
    }
    else {
      // Resetting marks location of invisible anchors
// #warning Do we really need this?
      m->resetWidgetPositionVelocityAndForce();
    }
  }

  if (!_visibleMarksIDsBuilder->contentEqualsTo(_visibleMarksIDs)) {
    _visibleMarksIDs = _visibleMarksIDsBuilder->getString();
    for (int i = 0; i < _visibilityListeners.size(); i++) {
      _visibilityListeners.at(i)->onVisibilityChange(_visibleMarks);
    }
  }

}

void NonOverlappingMarksRenderer::computeForces(const Camera* camera, const Planet* planet) {
  const size_t visibleMarksSize = _visibleMarks.size();

  //Compute Mark Anchor Screen Positions
  for (size_t i = 0; i < visibleMarksSize; i++) {
    _visibleMarks[i]->computeAnchorScreenPos(camera, planet);
  }

  //Compute Mark Forces
  for (size_t i = 0; i < visibleMarksSize; i++) {
    NonOverlappingMark* mark = _visibleMarks[i];
    mark->applyHookesLaw();

    for (size_t j = i+1; j < visibleMarksSize; j++) {
      mark->applyCoulombsLaw(_visibleMarks[j]);
    }

    for (size_t j = 0; j < visibleMarksSize; j++) {
      if (i != j) {
        mark->applyCoulombsLawFromAnchor(_visibleMarks[j]);
      }
    }
  }
}

void NonOverlappingMarksRenderer::renderMarks(const G3MRenderContext *rc,
                                              GLState *glState) {
  const size_t visibleMarksSize = _visibleMarks.size();
  if (visibleMarksSize > 0) {
    // draw all the springs in a shot to avoid OpenGL state changes
    for (size_t i = 0; i < visibleMarksSize; i++) {
      _visibleMarks[i]->renderSpringWidget(rc, glState);
    }

    // draw all the anchorwidgets in a shot to avoid OpenGL state changes
    for (size_t i = 0; i < visibleMarksSize; i++) {
      _visibleMarks[i]->renderAnchorWidget(rc, glState);
    }

    // draw all the widgets in a shot to avoid OpenGL state changes
    for (size_t i = 0; i < visibleMarksSize; i++) {
      _visibleMarks[i]->renderWidget(rc, glState);
    }
  }
}

void NonOverlappingMarksRenderer::applyForces(long long now, const Camera* camera) {
  if (_lastPositionsUpdatedTime != 0) { //If not First frame
    const int viewPortWidth  = camera->getViewPortWidth();
    const int viewPortHeight = camera->getViewPortHeight();

    const double elapsedMS = now - _lastPositionsUpdatedTime;
    float timeInSeconds = (float) (elapsedMS / 1000.0);
    if (timeInSeconds > 0.03f) {
      timeInSeconds = 0.03f;
    }

    //Update Position based on last Forces
    const size_t visibleMarksSize = _visibleMarks.size();
    for (size_t i = 0; i < visibleMarksSize; i++) {
      _visibleMarks[i]->updatePositionWithCurrentForce(timeInSeconds,
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
  applyForces(rc->getFrameStartTimer()->nowInMilliseconds(), camera);

  renderMarks(rc, glState);
}

void NonOverlappingMarksRenderer::onResizeViewportEvent(const G3MEventContext* ec,
                                                        int width,
                                                        int height) {
  const size_t marksSize = _marks.size();
  for (size_t i = 0; i < marksSize; i++) {
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

    const size_t visibleMarksSize = _visibleMarks.size();
    for (size_t i = 0; i < visibleMarksSize; i++) {
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

void NonOverlappingMarksRenderer::setTouchListener(NonOverlappingMarkTouchListener* touchListener) {
  if (_touchListener != NULL && _touchListener != touchListener) {
    delete _touchListener;
  }
  _touchListener = touchListener;
}
