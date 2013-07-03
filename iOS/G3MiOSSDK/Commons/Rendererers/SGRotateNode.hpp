//
//  SGRotateNode.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//

#ifndef __G3MiOSSDK__SGRotateNode__
#define __G3MiOSSDK__SGRotateNode__

#include "SGNode.hpp"

#include "Vector3D.hpp"

class SGRotateNode : public SGNode {
private:
  const double _x;
  const double _y;
  const double _z;
  const double _angle;
  
  MutableMatrix44D _rotationMatrix;
  
  GLState _glState;
  
public:
  SGRotateNode(const std::string& id,
               const std::string& sId,
               double x,
               double y,
               double z,
               double angle) :
  SGNode(id, sId),
  _x(x),
  _y(y),
  _z(z),
  _angle(angle),
  _rotationMatrix(MutableMatrix44D::createRotationMatrix(Angle::fromDegrees(_angle), Vector3D(_x, _y, _z)))
  {
    //_glState.getGPUProgramState()->setUniformMatrixValue(MODELVIEW, _rotationMatrix, true);
    _glState.setModelView(_rotationMatrix.asMatrix44D(), true);
  }

  GLState* getGLState(GLState* parentGLState){
    _glState.setParent(parentGLState);
    return &_glState;
  }
  
  
};

#endif
