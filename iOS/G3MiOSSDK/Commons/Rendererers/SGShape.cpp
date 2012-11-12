//
//  SGShape.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//

#include "SGShape.hpp"

#include "SGNode.hpp"


void SGShape::initialize(const InitializationContext* ic) {
  _node->initialize(ic);
}

bool SGShape::isReadyToRender(const RenderContext* rc) {
  return _node->isReadyToRender(rc);
}

void SGShape::rawRender(const RenderContext* rc) {
  _node->render(rc);
}
