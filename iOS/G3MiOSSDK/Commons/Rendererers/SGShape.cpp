//
//  SGShape.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//

#include "SGShape.hpp"

#include "SGNode.hpp"


void SGShape::initialize(const G3MContext* context) {
  _node->initialize(context, this);
}

bool SGShape::isReadyToRender(const RenderContext* rc) {
  return _node->isReadyToRender(rc);
}

void SGShape::rawRender(const RenderContext* rc) {
  _node->render(rc);
}

bool SGShape::isTransparent(const RenderContext* rc) {
  int __DIEGO_AT_WORK;
//  return _node->isTransparent(rc);
  return false;
}
