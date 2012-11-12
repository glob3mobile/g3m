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

void SGTranslateNode::prepareRender(const RenderContext* rc) {
  GL* gl = rc->getGL();

  gl->pushMatrix();
  gl->multMatrixf(MutableMatrix44D::createTranslationMatrix(_x, _y, _z));
}

void SGTranslateNode::cleanUpRender(const RenderContext* rc) {
  GL* gl = rc->getGL();
  gl->popMatrix();
}
