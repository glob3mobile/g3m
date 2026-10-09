//
//  SGShape.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//

#include "SGShape.hpp"
#include "IMathUtils.hpp"
#include "MutableVector3D.hpp"
#include "Box.hpp"

#include "SGNode.hpp"
#include "GLState.hpp"


SGShape::SGShape(SGNode* node,
                 const std::string& uriPrefix,
                 bool isTransparent,
                 Geodetic3D* position,
                 AltitudeMode altitudeMode) :
Shape(position, altitudeMode),
_node(node),
_uriPrefix(uriPrefix),
_isTransparent(isTransparent)
{
  _glState = new GLState();
  const bool blend = _isTransparent;
  _glState->addGLFeature(new BlendingModeGLFeature(blend,
                                                   GLBlendFactor::srcAlpha(),
                                                   GLBlendFactor::oneMinusSrcAlpha()),
                         false);
}

SGShape::~SGShape() {
  _glState->_release();
  delete _node;
}

void SGShape::initialize(const G3MContext* context) {
  _node->initialize(context, _uriPrefix);
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
                                                    const Vector3D& direction) const
{
#warning TODO
  std::vector<double> intersections;
  return intersections;
}

Box* SGShape::createModelBoundingBox(const G3MRenderContext* rc) {
  const IMathUtils* mu = IMathUtils::instance();
  MutableVector3D lower(mu->maxDouble(), mu->maxDouble(), mu->maxDouble());
  MutableVector3D upper(-mu->maxDouble(), -mu->maxDouble(), -mu->maxDouble());
  _node->addToModelBounds(MutableMatrix44D::identity(), lower, upper);
  if (lower.x() > upper.x()) {
    return NULL; // no vertices
  }
  return new Box(lower.asVector3D(), upper.asVector3D());
}
