//
//  SGRotateNode.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//

#include "SGRotateNode.hpp"

#include "Context.hpp"
#include "GL.hpp"
#include "Vector3D.hpp"

GLState* SGRotateNode::createState(const G3MRenderContext* rc,
                     const GLState& parentState) {
  GLState* state = new GLState(parentState);
  state->multiplyModelViewMatrix(MutableMatrix44D::createRotationMatrix(Angle::fromDegrees(_angle),
                                                                       Vector3D(_x, _y, _z)));
  return state;
}