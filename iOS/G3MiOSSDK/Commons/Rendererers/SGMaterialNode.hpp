//
//  SGMaterialNode.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//

#ifndef __G3MiOSSDK__SGMaterialNode__
#define __G3MiOSSDK__SGMaterialNode__

#include "SGNode.hpp"
#include "Color.hpp"
#include "GLFeature.hpp"

class SGMaterialNode : public SGNode {
private:
#ifdef C_CODE
  const Color* _baseColor;
#endif
#ifdef JAVA_CODE
  private Color _baseColor;
#endif
  const Color* _specularColor;

//  const double _specular;
//  const double _shine;
//  const double _alpha;
//  const double _emit;


  GLState* _glState;

public:

  SGMaterialNode(const std::string& id,
                 const std::string& sId,
                 Color* baseColor,
                 Color* specularColor,
                 double specular,
                 double shine,
                 double alpha,
                 double emit) :
  SGNode(id, sId),
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

  const GLState* createState(const G3MRenderContext* rc,
                             const GLState* parentState) {
    _glState->setParent(parentState);
    return _glState;
  }

  void setBaseColor(Color* baseColor) {
    if (baseColor != _baseColor) {
      delete _baseColor;
      _baseColor = baseColor;
    }
  }

  ~SGMaterialNode() {
    delete _baseColor;
    delete _specularColor;

    _glState->_release();
#ifdef JAVA_CODE
  super.dispose();
#endif

  }

  std::string description() {
    return "SGMaterialNode";
  }

};

#endif
