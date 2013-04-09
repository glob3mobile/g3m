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

bool SGShape::isReadyToRender(const G3MRenderContext* rc) {
  return _node->isReadyToRender(rc);
}

void SGShape::rawRender(const G3MRenderContext* rc,
                        const GLState& parentState, const GPUProgramState* parentProgramState) {
  _node->render(rc, parentState);
}

bool SGShape::isTransparent(const G3MRenderContext* rc) {
//  return _node->isTransparent(rc);
  return false;
}
