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

const GLState* SGMaterialNode::createState(const G3MRenderContext* rc,
                                           const GLState& parentState) {
  if (_specularColor == NULL) {
    return NULL;
  }
  
  GLState* state = new GLState(parentState);
  const float colorsIntensity = 1;
  state->enableFlatColor(*_specularColor, colorsIntensity);

  return state;
}
