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

class SGMaterialNode : public SGNode {
private:
  Color* _specularColor;

  double _shine;
  double _specular;

//  baseColor { r: 0.0, g: 0.0, b: 0.0 }
//  specularColor { r: 0.0, g: 0.0, b: 0.0 }
//
//  specular 1
//  shine 10
//  alpha 1.0
//  emit 0.0


protected:
  void prepareRender(const G3MRenderContext* rc);

  void cleanUpRender(const G3MRenderContext* rc);


public:
  SGMaterialNode() :
  _specularColor(NULL),
  _shine(0),
  _specular(0)
  {

  }

  ~SGMaterialNode() {
    delete _specularColor;
  }

  void setSpecularColor(Color* color) {
    delete _specularColor;
    _specularColor = color;
  }

  void setShine(double shine) {
    _shine = shine;
  }

  void setSpecular(double specular) {
    _specular = specular;
  }

};

#endif
