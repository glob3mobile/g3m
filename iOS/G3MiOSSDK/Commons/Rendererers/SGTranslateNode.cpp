//
//  SGTranslateNode.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//

#include "SGTranslateNode.hpp"

#include "GLState.hpp"


SGTranslateNode::SGTranslateNode(const std::string& id,
                                 const std::string& sID,
                                 double x,
                                 double y,
                                 double z) :
SGNode(id, sID),
_x(x),
_y(y),
_z(z),
_translationMatrix(MutableMatrix44D::createTranslationMatrix(_x, _y, _z)),
_glState(new GLState())
{
    _glState->addGLFeature(new ModelTransformGLFeature(_translationMatrix.asMatrix44D()), false);
}

const GLState* SGTranslateNode::createState(const G3MRenderContext* rc,
                                            const GLState* parentState) {
      _glState->setParent(parentState);
      return _glState;
}

SGTranslateNode::~SGTranslateNode() {
    _glState->_release();
    #ifdef JAVA_CODE
        super.dispose();
    #endif
}
