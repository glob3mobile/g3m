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

void MarkWidget::init(const G3MRenderContext *rc, int viewportWidth, int viewportHeight){
  if (_glState == NULL){
    _glState = new GLState();
    _viewportExtent = new ViewportExtentGLFeature(viewportWidth, viewportHeight);
    
    _texHandler = rc->getTexturesHandler();
    _imageBuilder->build(rc, new WidgetImageListener(this), true);
    
    _glState->addGLFeature(_viewportExtent, false);
  }
}

void MarkWidget::prepareWidget(const IImage* image,
                               const std::string& imageName){
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
                                          1.0,
                                          true,
                                          10.0,
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

void MarkWidget::render(const G3MRenderContext *rc, GLState *glState){
  rc->getGL()->drawArrays(GLPrimitive::triangleStrip(), 0, 4, _glState, *(rc->getGPUProgramManager()));
}

void MarkWidget::setScreenPos(float x, float y){
  if (_geo2Dfeature != NULL){
    _geo2Dfeature->setTranslation(x, y);
  }
  _x = x;
  _y = y;
}

void MarkWidget::resetPosition(){
  if (_geo2Dfeature != NULL){
    _geo2Dfeature->setTranslation(0,0);
  }
  _x = NANF;
  _y = NANF;
}

void MarkWidget::onResizeViewportEvent(int width, int height){
  if (_viewportExtent != NULL){
    _viewportExtent->changeExtent(width, height);
  }
}


void MarkWidget::clampPositionInsideScreen(int viewportWidth, int viewportHeight, int margin){
  const IMathUtils* mu = IMathUtils::instance();
  float x = mu->clamp(_x, _halfWidth + margin, viewportWidth - _halfWidth - margin);
  float y = mu->clamp(_y, _halfHeight + margin, viewportHeight - _halfHeight - margin);

  setScreenPos(x, y);
}

#pragma mark NonOverlappingMark

NonOverlappingMark::NonOverlappingMark(IImageBuilder* imageBuilderWidget,
                                       IImageBuilder* imageBuilderAnchor,
                                       const Geodetic3D& position,
                                       float springLengthInPixels,
                                       float springK,
                                       float electricCharge,
                                       float anchorElectricCharge,
                                       float maxWidgetSpeedInPixelsPerSecond,
                                       float minWidgetSpeedInPixelsPerSecond,
                                       float resistanceFactor):
_geoPosition(position),
_springLengthInPixels(springLengthInPixels),
_cartesianPos(NULL),
_dX(0),
_dY(0),
_fX(0),
_fY(0),
_widget(imageBuilderWidget),
_anchorWidget(imageBuilderAnchor),
_springK(springK),
_electricCharge(electricCharge),
_maxWidgetSpeedInPixelsPerSecond(maxWidgetSpeedInPixelsPerSecond),
_anchorElectricCharge(anchorElectricCharge),
_resistanceFactor(resistanceFactor),
_minWidgetSpeedInPixelsPerSecond(minWidgetSpeedInPixelsPerSecond)
{
  
}

NonOverlappingMark::~NonOverlappingMark()
{
  delete _cartesianPos;
}

Vector3D NonOverlappingMark::getCartesianPosition(const Planet* planet) const{
  if (_cartesianPos == NULL){
    _cartesianPos = new Vector3D(planet->toCartesian(_geoPosition));
  }
  return *_cartesianPos;
}

void NonOverlappingMark::computeAnchorScreenPos(const Camera* cam, const Planet* planet){
  
  Vector2F sp(cam->point2Pixel(getCartesianPosition(planet)));
  _anchorWidget.setScreenPos(sp._x, sp._y);
  
  if (_widget.getScreenPos().isNaN()){
    _widget.setScreenPos(sp._x, sp._y + 0.01f);
  }
}


void NonOverlappingMark::applyCoulombsLaw(NonOverlappingMark* that){ //EM
  
  Vector2F d = getScreenPos().sub(that->getScreenPos());
  double distance = d.length()  + 0.001;
  Vector2F direction = d.div((float)distance);
  
  float strength = (float)(this->_electricCharge * that->_electricCharge / (distance * distance));
  
  Vector2F force = direction.times(strength);
  //printf("FC %f, %f\n", force._x, force._y);
  
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

void NonOverlappingMark::applyCoulombsLawFromAnchor(NonOverlappingMark* that){ //EM
  
  Vector2F dAnchor = getScreenPos().sub(that->getAnchorScreenPos());
  double distanceAnchor = dAnchor.length()  + 0.001;
  Vector2F directionAnchor = dAnchor.div((float)distanceAnchor);
  
  float strengthAnchor = (float)(this->_electricCharge * that->_anchorElectricCharge / (distanceAnchor * distanceAnchor));
  
  this->applyForce(directionAnchor._x * strengthAnchor,
                   directionAnchor._y * strengthAnchor);
}

void NonOverlappingMark::applyHookesLaw(){   //Spring
  
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

void NonOverlappingMark::render(const G3MRenderContext* rc, GLState* glState){
  
  if (_widget.isReady() && _anchorWidget.isReady()){
    _widget.render(rc, glState);
    _anchorWidget.render(rc, glState);
  } else{
    _widget.init(rc, rc->getCurrentCamera()->getViewPortWidth(), rc->getCurrentCamera()->getViewPortHeight());
    _anchorWidget.init(rc, rc->getCurrentCamera()->getViewPortWidth(), rc->getCurrentCamera()->getViewPortHeight());
  }
}

void NonOverlappingMark::updatePositionWithCurrentForce(double elapsedMS, float viewportWidth, float viewportHeight){
  
  Vector2D oldVelocity(_dX, _dY);
  Vector2D force(_fX, _fY);
  
  //Assuming Widget Mass = 1.0
  float time = (float)(elapsedMS / 1000.0);
  Vector2D velocity = oldVelocity.add(force.times(time)).times(_resistanceFactor); //Resistance force applied as x0.85
  
  //Force has been applied and must be reset
  _fX = 0;
  _fY = 0;
  
  //Clamping Velocity
  double velocityPPS = velocity.length();
  if (velocityPPS > _maxWidgetSpeedInPixelsPerSecond){
    _dX = (float)(velocity._x * (_maxWidgetSpeedInPixelsPerSecond / velocityPPS));
    _dY = (float)(velocity._y * (_maxWidgetSpeedInPixelsPerSecond / velocityPPS));
  } else{
    if (velocityPPS < _minWidgetSpeedInPixelsPerSecond){
      _dX = 0.0;
      _dY = 0.0;
    } else{
      //Normal case
      _dX = (float)velocity._x;
      _dY = (float)velocity._y;
    }
  }
  
  //Update position
  if (_dX != 0.0 || _dY != 0.0){
    Vector2F position = _widget.getScreenPos();
    
    float newX = position._x + (_dX * time);
    float newY = position._y + (_dY * time);
    
    _widget.setScreenPos(newX, newY);
    _widget.clampPositionInsideScreen((int)viewportWidth, (int)viewportHeight, 0); // pixels of margin
  }
  
}

void NonOverlappingMark::onResizeViewportEvent(int width, int height){
  _widget.onResizeViewportEvent(width, height);
  _anchorWidget.onResizeViewportEvent(width, height);
}

#pragma-mark Renderer

NonOverlappingMarksRenderer::NonOverlappingMarksRenderer(int maxVisibleMarks):
_maxVisibleMarks(maxVisibleMarks),
_lastPositionsUpdatedTime(0),
_connectorsGLState(NULL)
{
  
}


NonOverlappingMarksRenderer::~NonOverlappingMarksRenderer(){
  _connectorsGLState->_release();
  
  for (int i = 0; i < _marks.size(); i++) {
    delete _marks[i];
  }
}

void NonOverlappingMarksRenderer::addMark(NonOverlappingMark* mark){
  _marks.push_back(mark);
  
}

void NonOverlappingMarksRenderer::computeMarksToBeRendered(const Camera* cam, const Planet* planet) {
  
  _visibleMarks.clear();
  
  const Frustum* frustrum = cam->getFrustumInModelCoordinates();
  
  for (int i = 0; i < _marks.size(); i++) {
    NonOverlappingMark* m = _marks[i];
    
    if (_visibleMarks.size() < _maxVisibleMarks &&
        frustrum->contains(m->getCartesianPosition(planet))){
      _visibleMarks.push_back(m);
    }
    else{
      //Resetting marks location of invisible anchors
      m->resetWidgetPositionVelocityAndForce();
    }
  }
}

void NonOverlappingMarksRenderer::renderConnectorLines(const G3MRenderContext* rc){
  if (_connectorsGLState == NULL){
    _connectorsGLState = new GLState();
    
    _connectorsGLState->addGLFeature(new FlatColorGLFeature(Color::black()), false);
  }
  
  _connectorsGLState->clearGLFeatureGroup(NO_GROUP);
  
  FloatBufferBuilderFromCartesian2D pos2D;
  
  for (int i = 0; i < _visibleMarks.size(); i++){
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
                                                            3.0,
                                                            true,
                                                            10.0,
                                                            Vector2F(0.0,0.0)),
                                   false);
  
  _connectorsGLState->addGLFeature(new ViewportExtentGLFeature((int)rc->getCurrentCamera()->getViewPortWidth(),
                                                               (int)rc->getCurrentCamera()->getViewPortHeight()), false);
  
  rc->getGL()->drawArrays(GLPrimitive::lines(), 0, pos2D.size()/2, _connectorsGLState, *(rc->getGPUProgramManager()));
}

void NonOverlappingMarksRenderer::computeForces(const Camera* cam, const Planet* planet){
  
  //Compute Mark Anchor Screen Positions
  for (int i = 0; i < _visibleMarks.size(); i++) {
    _visibleMarks[i]->computeAnchorScreenPos(cam, planet);
  }
  
  //Compute Mark Forces
  for (int i = 0; i < _visibleMarks.size(); i++) {
    NonOverlappingMark* mark = _visibleMarks[i];
    mark->applyHookesLaw();
    
    for (int j = i+1; j < _visibleMarks.size(); j++) {
      mark->applyCoulombsLaw(_visibleMarks[j]);
    }
    
    for (int j = 0; j < _visibleMarks.size(); j++) {
      if (i != j){
        mark->applyCoulombsLawFromAnchor(_visibleMarks[j]);
      }
    }
  }
}

void NonOverlappingMarksRenderer::renderMarks(const G3MRenderContext *rc, GLState *glState){
  //Draw Lines
  renderConnectorLines(rc);
  
  //Draw Anchors and Marks
  for (int i = 0; i < _visibleMarks.size(); i++) {
    _visibleMarks[i]->render(rc, glState);
  }
}

void NonOverlappingMarksRenderer::applyForces(long long now, const Camera* cam){
  
  if (_lastPositionsUpdatedTime != 0){ //If not First frame
    
    //Update Position based on last Forces
    for (int i = 0; i < _visibleMarks.size(); i++) {
      _visibleMarks[i]->updatePositionWithCurrentForce(now - _lastPositionsUpdatedTime,
                                                       cam->getViewPortWidth(), cam->getViewPortHeight());
    }
  }
  
  _lastPositionsUpdatedTime = now;
}

void NonOverlappingMarksRenderer::render(const G3MRenderContext* rc, GLState* glState){
  
  const Camera* cam = rc->getCurrentCamera();
  const Planet* planet = rc->getPlanet();
  
  computeMarksToBeRendered(cam, planet);
  
  computeForces(cam, planet);
  
  renderMarks(rc, glState);
  
  applyForces(rc->getFrameStartTimer()->nowInMilliseconds(), cam);
}

void NonOverlappingMarksRenderer::onResizeViewportEvent(const G3MEventContext* ec, int width, int height){
  for (int i = 0; i < _marks.size(); i++) {
    _marks[i]->onResizeViewportEvent(width, height);
  }
  
}