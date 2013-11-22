//
//  SGShape.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//

#include "SGShape.hpp"

#include "SGNode.hpp"
#include "OrientedBox.hpp"
#include "Camera.hpp"


SGShape::~SGShape()
{
  _glState->_release();
  delete _node;
  if (_boundingVolume)
    delete _boundingVolume;
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

std::vector<double> SGShape::intersectionsDistances(const Planet* planet,
                                                    const Vector3D& origin,
                                                    const Vector3D& direction) const {
  return _boundingVolume->intersectionsDistances(origin, direction);
}


bool SGShape::isVisible(const G3MRenderContext *rc)
{
  return getBoundingVolume(rc)->touchesFrustum(rc->getCurrentCamera()->getFrustumInModelCoordinates());
}


BoundingVolume* SGShape::getBoundingVolume(const G3MRenderContext *rc)
{
  if (_boundingVolume == NULL) {
    Box* boundingBox = _node->getCopyBoundingBox();
    _boundingVolume = new OrientedBox(boundingBox, *getTransformMatrix(rc->getPlanet()));
    delete boundingBox;
  }
  return _boundingVolume;
}
