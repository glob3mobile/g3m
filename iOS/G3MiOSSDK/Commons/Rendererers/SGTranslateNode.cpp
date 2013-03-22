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

void SGTranslateNode::prepareRender(const G3MRenderContext* rc, GLState& state) {
  state.multiplyModelViewMatrix(MutableMatrix44D::createTranslationMatrix(_x, _y, _z));
  SGNode::prepareRender(rc, state);
}

