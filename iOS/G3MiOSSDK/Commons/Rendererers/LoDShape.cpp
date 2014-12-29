//
//  LoDShape.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 29/12/14.
//
//

#include "LoDShape.hpp"

#include "SGNode.hpp"
#include "OrientedBox.hpp"
#include "Camera.hpp"

#include "SGShape.hpp"

LoDLevel::~LoDLevel(){
  delete _node;
  delete _boundingVolume;
  delete _sgShape;
}

LoDShape::~LoDShape()
{
  _glState->_release();
  
  for (int i = 0; i < _nodes.size(); i++) {
    delete _nodes.at(i);
  }
}

void LoDShape::initialize(const G3MContext* context) {
  for (int i = 0; i < _nodes.size(); i++) {
    
    LoDLevel* level = _nodes.at(i);
    
    SGShape* shape = new SGShape(level->_node,
                                  level->_uriPrefix,
                                 level->_isTransparent,
                                 new Geodetic3D(Geodetic3D::fromDegrees(0, 0, 0)),
                                 ABSOLUTE);
    shape->initialize(context);
    level->_sgShape = shape;
  }
}

bool LoDShape::isReadyToRender(const G3MRenderContext* rc) {
  for (int i = 0; i < _nodes.size(); i++) {
    if (!_nodes.at(i)->_node->isReadyToRender(rc)){
      return false;
    }
  }
  
  return true;
}

void LoDShape::calculateRenderableLevel(const G3MRenderContext* rc){
  
  double bestDistanceDifference = IMathUtils::instance()->maxDouble();
  
  Vector3D shapePos = rc->getPlanet()->toCartesian(getPosition());
  Vector3D cameraPos = rc->getCurrentCamera()->getCartesianPosition();
  
  double distSq = shapePos.squaredDistanceTo(cameraPos);
  
  printf("DIST %f\n", sqrt(distSq)  );
  
  IMathUtils* mu = IMathUtils::instance();
  
  for (int i = 0; i < _nodes.size(); i++) {
    LoDLevel* level = _nodes.at(i);
    
    double distDiff = mu->abs(level->_perfectDistanceSquared - distSq);
    if (bestDistanceDifference > distDiff){
      _lastRenderedLevel = level;
      bestDistanceDifference = distDiff;
    }
  }
}

void LoDShape::rawRender(const G3MRenderContext* rc,
                        GLState* parentState,
                        bool renderNotReadyShapes) {
  _glState->setParent(parentState);
  
  calculateRenderableLevel(rc);
  
  _lastRenderedLevel->_node->render(rc, _glState, renderNotReadyShapes);
}

std::vector<double> LoDShape::intersectionsDistances(const Planet* planet,
                                                    const Camera* camera,
                                                    const Vector3D& origin,
                                                    const Vector3D& direction) {
  return _lastRenderedLevel->_boundingVolume->intersectionsDistances(origin, direction);
}


bool LoDShape::isVisible(const G3MRenderContext *rc)
{
  return getBoundingVolume(rc)->touchesFrustum(rc->getCurrentCamera()->getFrustumInModelCoordinates());
}


BoundingVolume* LoDShape::getBoundingVolume(const G3MRenderContext *rc)
{
  if (_lastRenderedLevel->_boundingVolume == NULL) {
    Box* boundingBox = _lastRenderedLevel->_node->getCopyBoundingBox();
    _lastRenderedLevel->_boundingVolume = new OrientedBox(boundingBox, *getTransformMatrix(rc->getPlanet()));
    
    delete boundingBox;
  }
  return _lastRenderedLevel->_boundingVolume;
}

void LoDShape::zRawRender(const G3MRenderContext* rc,
                         GLState* parentState) {
  
  GLState* glState = new GLState();
  if (_lastRenderedLevel->_isTransparent){
    glState->addGLFeature(new BlendingModeGLFeature(true, GLBlendFactor::srcAlpha(), GLBlendFactor::oneMinusSrcAlpha()), false);
  } else{
    glState->addGLFeature(new BlendingModeGLFeature(false, GLBlendFactor::srcAlpha(), GLBlendFactor::oneMinusSrcAlpha()), false);
  }
  glState->setParent(parentState);
  
  _lastRenderedLevel->_node->zRender(rc, glState);
  
  glState->_release();
}
