//
//  SGRotateNode.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//

#include "SGRotateNode.hpp"

#include "Angle.hpp"
#include "Vector3D.hpp"
#include "GLState.hpp"


SGRotateNode::SGRotateNode(const std::string& id,
                           const std::string& sID,
                           double x,
                           double y,
                           double z,
                           double angle) :
SGNode(id, sID),
_x(x),
_y(y),
_z(z),
_angle(angle),
_rotationMatrix(MutableMatrix44D::createRotationMatrix(Angle::fromDegrees(_angle), Vector3D(_x, _y, _z))),
_glState(new GLState())
{
    _glState->addGLFeature(new ModelTransformGLFeature(_rotationMatrix.asMatrix44D()), false);
    }

SGRotateNode::~SGRotateNode() {
    _glState->_release();
    #ifdef JAVA_CODE
    super.dispose();
    #endif
    }

const GLState* SGRotateNode::createState(const G3MRenderContext* rc,
                                         const GLState* parentState) {
    _glState->setParent(parentState);
    return _glState;
}
