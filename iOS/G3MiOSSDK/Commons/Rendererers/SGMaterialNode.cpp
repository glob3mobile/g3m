//
//  SGMaterialNode.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//

#include "SGMaterialNode.hpp"

#include "Context.hpp"
#include "GLState.hpp"

GLState* SGMaterialNode::createState(const G3MRenderContext* rc,
                                           const GLState& parentState) {
  if (_baseColor == NULL) {
    return NULL;
  }
  
  GLState* state = new GLState(parentState);
  const float colorsIntensity = 1;
  //state->enableFlatColor(*_specularColor, colorsIntensity);
  state->enableFlatColor(*_baseColor, colorsIntensity);

  return state;
}
