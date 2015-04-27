//
//  SGShape.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//

#include "SGShape.hpp"

#include "SGNode.hpp"

SGShape::~SGShape() {
  _glState->_release();
  delete _node;
}

void SGShape::initialize(const G3MContext* context) {
  _node->initialize(context, this);
}

bool SGShape::isReadyToRender(const G3MRenderContext* rc) {
  return _node->isReadyToRender(rc);
}

void SGShape::rawRender(const G3MRenderContext* rc,
                        GLState* parentState,
                        bool renderNotReadyShapes) {
  _glState->setParent(parentState);
  _node->render(rc, _glState, renderNotReadyShapes);
}

Vector3D SGShape::mostDistantVertexFromCenter() const{
  Vector3D v = _node->mostDistantVertexFromCenter(MutableMatrix44D::identity());
  Vector3D s = getScale();
  return Vector3D(v._x * s._x, v._y * s._y, v._z * s._z);
}

Vector3D SGShape::getMax(){
  Vector3D v = _node->getMax();
  Vector3D s = getScale();
  return Vector3D(v._x * s._x, v._y * s._y, v._z * s._z);
}

Vector3D SGShape::getMin(){
  Vector3D v = _node->getMin();
  Vector3D s = getScale();
  return Vector3D(v._x * s._x, v._y * s._y, v._z * s._z);
}
