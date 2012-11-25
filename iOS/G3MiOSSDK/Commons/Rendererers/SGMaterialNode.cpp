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

void SGMaterialNode::prepareRender(const G3MRenderContext* rc) {
  //GL *gl = rc->getGL();

  int TEMP_commented_by_Agustin_until_decision_about_glstate;
  /*
  if (_specularColor == NULL) {
    gl->disableVertexFlatColor();
  }
  else {
    const float colorsIntensity = 1;
    gl->enableVertexFlatColor(*_specularColor, colorsIntensity);
  }*/

  SGNode::prepareRender(rc);
}

void SGMaterialNode::cleanUpRender(const G3MRenderContext* rc) {
  //GL *gl = rc->getGL();

  int TEMP_commented_by_Agustin_until_decision_about_glstate;
  /*
  gl->disableVertexFlatColor();
   */

  SGNode::cleanUpRender(rc);
}
