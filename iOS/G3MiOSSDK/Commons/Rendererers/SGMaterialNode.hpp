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

protected:
  void prepareRender(const RenderContext* rc);

  void cleanUpRender(const RenderContext* rc);


public:
  SGMaterialNode() :
  _specularColor(NULL)
  {

  }

  ~SGMaterialNode() {
    delete _specularColor;
  }

  void setSpecularColor(Color* color) {
    delete _specularColor;
    _specularColor = color;
  }
};

#endif
