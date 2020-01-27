//
//  SGMaterialNode.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//

#include "SGMaterialNode.hpp"

#include "GLState.hpp"


SGMaterialNode::SGMaterialNode(const std::string& id,
                               const std::string& sID,
                               const Color* baseColor,
                               const Color* specularColor,
                               double specular,
                               double shine,
                               double alpha,
                               double emit) :
SGNode(id, sID),
_baseColor(baseColor),
_specularColor(specularColor),
_glState(new GLState())
//  _specular(specular),
//  _shine(shine),
//  _alpha(alpha),
//  _emit(emit)
{
#ifdef C_CODE
  _glState->addGLFeature(new FlatColorGLFeature(*_baseColor, false, 0, 0), false);
#endif
#ifdef JAVA_CODE
  _glState.addGLFeature(new FlatColorGLFeature(_baseColor, false, 0, 0), false);
#endif
}

const GLState* SGMaterialNode::createState(const G3MRenderContext* rc,
                                           const GLState* parentState) {
  _glState->setParent(parentState);
  return _glState;
}

SGMaterialNode::~SGMaterialNode() {
  delete _baseColor;
  delete _specularColor;

  _glState->_release();
#ifdef JAVA_CODE
  super.dispose();
#endif
}

void SGMaterialNode::setBaseColor(Color* baseColor) {
  if (baseColor != _baseColor) {
    delete _baseColor;
    _baseColor = baseColor;
  }
}
