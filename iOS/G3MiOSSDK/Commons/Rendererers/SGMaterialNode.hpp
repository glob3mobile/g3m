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
  const Color* _baseColor;
  const Color* _specularColor;

  const double _specular;
  const double _shine;
  const double _alpha;
  const double _emit;

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
  _specular(specular),
  _shine(shine),
  _alpha(alpha),
  _emit(emit)
  {

  }

  ~SGMaterialNode() {
    delete _baseColor;
    delete _specularColor;
  }

};

#endif
