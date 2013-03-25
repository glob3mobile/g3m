//
//  SGTranslateNode.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//

#include "SGTranslateNode.hpp"

#include "Context.hpp"
#include "GL.hpp"

GLState* SGTranslateNode::createState(const G3MRenderContext* rc,
                     const GLState& parentState) {
  
  GLState *state = new GLState(parentState);
  
  state->multiplyModelViewMatrix(MutableMatrix44D::createTranslationMatrix(_x, _y, _z));
  //SGNode::prepareRender(rc, state);
  
  return state;
}
