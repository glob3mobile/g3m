//
//  SGMaterialNode.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//

#include "SGMaterialNode.hpp"

#include "Context.hpp"
#include "GL.hpp"

void SGMaterialNode::prepareRender(const RenderContext* rc) {
  GL *gl = rc->getGL();

  if (_specularColor == NULL) {
    gl->disableVertexFlatColor();
  }
  else {
    const float colorsIntensity = 1;
    gl->enableVertexFlatColor(*_specularColor, colorsIntensity);
  }

}

void SGMaterialNode::cleanUpRender(const RenderContext* rc) {
  GL *gl = rc->getGL();

  gl->disableVertexFlatColor();
}
