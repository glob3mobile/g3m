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
                        GLState* parentState,
                        bool renderNotReadyShapes) {
  _glState->setParent(parentState);
  _node->render(rc, _glState, renderNotReadyShapes);
}

void SGShape::zRawRender(const G3MRenderContext* rc,
                        GLState* parentState) {

  GLState* glState = new GLState();
  if (_isTransparent){
    glState->addGLFeature(new BlendingModeGLFeature(true, GLBlendFactor::srcAlpha(), GLBlendFactor::oneMinusSrcAlpha()), false);
  } else{
    glState->addGLFeature(new BlendingModeGLFeature(false, GLBlendFactor::srcAlpha(), GLBlendFactor::oneMinusSrcAlpha()), false);
  }
  glState->setParent(parentState);

  _node->zRender(rc, glState);

  glState->_release();
}
