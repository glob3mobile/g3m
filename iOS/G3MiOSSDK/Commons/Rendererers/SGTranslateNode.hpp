//
//  SGTranslateNode.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//

#ifndef __G3MiOSSDK__SGTranslateNode__
#define __G3MiOSSDK__SGTranslateNode__

#include "SGNode.hpp"

#include "MutableMatrix44D.hpp"

class SGTranslateNode : public SGNode {
private:
  const double _x;
  const double _y;
  const double _z;
  
  MutableMatrix44D _translationMatrix;
  
  GLState* _glState;

public:

  SGTranslateNode(const std::string& id,
                  const std::string& sId,
                  double x,
                  double y,
                  double z) :
  SGNode(id, sId),
  _x(x),
  _y(y),
  _z(z),
  _translationMatrix(MutableMatrix44D::createTranslationMatrix(_x, _y, _z)),
  _glState(new GLState())
  {
    _glState->addGLFeature(new ModelTransformGLFeature(_translationMatrix.asMatrix44D()), false);
  }
  
  const GLState* createState(const G3MRenderContext* rc,
                             const GLState* parentState) {
    _glState->setParent(parentState);
    return _glState;
  }

  std::string description() {
    return "SGTranslateNode";
  }

};

#endif
