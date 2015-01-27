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

#pragma mark MarkWidget

MarkWidget::MarkWidget(const TextureIDReference* texID,
                       float width, float height,
                       float viewportWidth, float viewportHeight){
  
  _glState = new GLState();
  
  width /= 2.0;
  height /= 2.0;
    
    FloatBufferBuilderFromCartesian2D pos2D;
    pos2D.add( -width, -height); //vertex 1
    pos2D.add( -width, height); //vertex 2
    pos2D.add( width, -height); //vertex 3
    pos2D.add( width, height); //vertex 4
  
  _geo2Dfeature = new Geometry2DGLFeature(pos2D.create(),
                                          2,
                                          0,
                                          true,
                                          0,
                                          1.0,
                                          true,
                                          10.0,
                                          Vector2F(0.0,0.0));
  
    _glState->addGLFeature(_geo2Dfeature,
                           false);
  
    FloatBufferBuilderFromCartesian2D texCoords;
    texCoords.add( 0.0f, 1.0f); //vertex 1
    texCoords.add( 0.0f, 0.0f); //vertex 2
    texCoords.add( 1.0f, 1.0f); //vertex 3
    texCoords.add( 1.0f, 0.0f); //vertex 4
    
    SimpleTextureMapping* textureMapping = new SimpleTextureMapping(texID,
                                                                    texCoords.create(),
                                                                    true,
                                                                    true);
  
  _viewportExtent = new ViewportExtentGLFeature((int)viewportWidth, (int)viewportHeight);
  
    _glState->addGLFeature(_viewportExtent, false);
    
    textureMapping->modifyGLState(*_glState);
}

void MarkWidget::render(const G3MRenderContext *rc, GLState *glState){
  rc->getGL()->drawArrays(GLPrimitive::triangleStrip(), 0, 4, _glState, *(rc->getGPUProgramManager()));
}

void MarkWidget::setScreenPos(float x, float y){
  _geo2Dfeature->setTranslation(x, y);
}

void MarkWidget::onResizeViewportEvent(int width, int height){
  _viewportExtent->changeExtent(width, height);
}

#pragma mark NonOverlappingMark

NonOverlappingMark::NonOverlappingMark(IImageBuilder* imageBuilder, const Geodetic3D& position, float springLengthInPixels):
_imageBuilder(imageBuilder),
_geoPosition(position),
_springLengthInPixels(springLengthInPixels),
_anchorScreenPos(NULL),
_screenPos(NULL),
_cartesianPos(NULL),
_dX(0),
_dY(0),
_image(NULL),
_widget(NULL),
_anchorWidget(NULL)
{
  
}

Vector3D NonOverlappingMark::getCartesianPosition(const Planet* planet) const{
  if (_cartesianPos == NULL){
    _cartesianPos = new Vector3D(planet->toCartesian(_geoPosition));
  }
  return *_cartesianPos;
}

void NonOverlappingMark::computeAnchorScreenPos(const Camera* cam, const Planet* planet){
  if (_anchorScreenPos != NULL){
    delete _anchorScreenPos;
  }
  
  _anchorScreenPos = new Vector2F(cam->point2Pixel(getCartesianPosition(planet)));
  
  if (_screenPos == NULL){
    _screenPos = new Vector2F(_anchorScreenPos->_x, _anchorScreenPos->_y + _springLengthInPixels);
  }
}


void NonOverlappingMark::applyCoulombsLaw(NonOverlappingMark* that){ //EM
  
  Vector2F d = _screenPos->sub(*that->_screenPos);
  double distance = d.length()  + 0.001;
  Vector2F direction = d.div((float)distance);
  
  float repulsion = 1.0;
  
  float strength = repulsion / (distance * distance);
  
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

void NonOverlappingMark::applyHookesLaw(){   //Spring
  
  Vector2F d = _screenPos->sub(*_anchorScreenPos);
  double mod = d.length();
  double displacement = _springLengthInPixels - mod;
  Vector2F direction = d.div((float)mod);
  
  float springK = 10.0;
  applyForce((float)(direction._x * springK * displacement),
             (float)(direction._y * springK * displacement));
  
//  var d = spring.point2.p.subtract(spring.point1.p); // the direction of the spring
//  var displacement = spring.length - d.magnitude();
//  var direction = d.normalise();
//  
//  // apply force to each end point
//  spring.point1.applyForce(direction.multiply(spring.k * displacement * -0.5));
//  spring.point2.applyForce(direction.multiply(spring.k * displacement * 0.5));
  
}

void NonOverlappingMark::render(const G3MRenderContext* rc, GLState* glState){
  
  _imageBuilder->build(rc, new NonOverlappingMark::NonOverlappingMarkImageListener(this), true);
  if (_image == NULL){
    return;
  }
  
  if (_widget == NULL){
    const TextureIDReference* textureID = rc->getTexturesHandler()->getTextureIDReference(_image,
                                                                                          GLFormat::rgba(),
                                                                                          _imageName,
                                                                                          false);
    _widget = new MarkWidget(textureID,
                             _image->getWidth(), _image->getHeight(),
                             rc->getCurrentCamera()->getViewPortWidth(), rc->getCurrentCamera()->getViewPortHeight());
  }
  
  if (_anchorWidget == NULL){
    const TextureIDReference* textureID = rc->getTexturesHandler()->getTextureIDReference(_image,
                                                                                          GLFormat::rgba(),
                                                                                          _imageName,
                                                                                          false);
    _anchorWidget = new MarkWidget(textureID,
                             _image->getWidth(), _image->getHeight(),
                             rc->getCurrentCamera()->getViewPortWidth(), rc->getCurrentCamera()->getViewPortHeight());
  }
  
  _anchorWidget->setScreenPos(_anchorScreenPos->_x, _anchorScreenPos->_y);
  _widget->setScreenPos(_screenPos->_x, _screenPos->_y);
  //printf("%f, %f\n", _screenPos->_x, _screenPos->_y);
  
  _widget->render(rc, glState);
  
  _anchorWidget->render(rc, glState);
  

}

void NonOverlappingMark::updatePositionWithCurrentForce(double elapsedMS){
  
  _dX *= (elapsedMS / 1000);
  _dY *= (elapsedMS / 1000);

  if (IMathUtils::instance()->abs(_dX) < 0.5 && IMathUtils::instance()->abs(_dY) < 0.5){ //STOP CONDITION
    return;
  }
  
  Vector2F* newScreenPos = new Vector2F(_screenPos->_x + _dX,
                                        _screenPos->_y + _dY);
  
  delete _screenPos;
  _screenPos = newScreenPos;
  
  _dY = 0.0;
  _dY = 0.0;
  
}

void NonOverlappingMark::onResizeViewportEvent(int width, int height){
  if (_widget != NULL){
    _widget->onResizeViewportEvent(width, height);
  }
  if (_anchorWidget != NULL){
    _anchorWidget->onResizeViewportEvent(width, height);
  }
}

#pragma-mark Renderer

NonOverlappingMarksRenderer::NonOverlappingMarksRenderer(int maxVisibleMarks):
_maxVisibleMarks(maxVisibleMarks),
_lastPositionsUpdatedTime(0)
{
  
}


void NonOverlappingMarksRenderer::addMark(NonOverlappingMark* mark){
  _marks.push_back(mark);
  
}

std::vector<NonOverlappingMark*> NonOverlappingMarksRenderer::getMarksToBeRendered(const Camera* cam, const Planet* planet) const{
  
  std::vector<NonOverlappingMark*> out;
  
  for (int i = 0; i < _marks.size(); i++) {
    NonOverlappingMark* m = _marks[i];
    
    if (cam->getFrustumInModelCoordinates()->contains(m->getCartesianPosition(planet)) ){
      out.push_back(m);
      if (out.size() == _maxVisibleMarks){
        break;
      }
    }
  }
  
  return out;
}

void NonOverlappingMarksRenderer::render(const G3MRenderContext* rc, GLState* glState){
  
  
  std::vector<NonOverlappingMark*> visibleMarks = getMarksToBeRendered(rc->getCurrentCamera(), rc->getPlanet());
  
  //Compute Mark Anchor Screen Positions
  for (int i = 0; i < visibleMarks.size(); i++) {
    visibleMarks[i]->computeAnchorScreenPos(rc->getCurrentCamera(), rc->getPlanet());
  }
  
  //Compute Mark Forces
  for (int i = 0; i < visibleMarks.size(); i++) {
    visibleMarks[i]->applyHookesLaw();
    
    for (int j = i+1; j < visibleMarks.size(); j++) {
      visibleMarks[i]->applyCoulombsLaw(visibleMarks[j]);
    }
  }
  
  //Draw Lines
  
  //Draw Anchors
  
  //Draw Marks
  for (int i = 0; i < visibleMarks.size(); i++) {
    visibleMarks[i]->render(rc, glState);
  }
  
  long long now = rc->getFrameStartTimer()->nowInMilliseconds();
  
  //Update Position based on last Forces
  for (int i = 0; i < visibleMarks.size(); i++) {
    visibleMarks[i]->updatePositionWithCurrentForce(now - _lastPositionsUpdatedTime);
  }
  
  _lastPositionsUpdatedTime = now;
  
}

void NonOverlappingMarksRenderer::onResizeViewportEvent(const G3MEventContext* ec, int width, int height){
  for (int i = 0; i < _marks.size(); i++) {
    _marks[i]->onResizeViewportEvent(width, height);
  }

}