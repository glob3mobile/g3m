//
//  TransformableMesh.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/17/17.
//
//

#ifndef TransformableMesh_hpp
#define TransformableMesh_hpp

#include "Mesh.hpp"
#include "Vector3D.hpp"

class MutableMatrix44D;
class ModelTransformGLFeature;


class TransformableMesh : public Mesh {
private:
  MutableMatrix44D*        _transformMatrix;
  ModelTransformGLFeature* _transformGLFeature;
  MutableMatrix44D*        _userTransformMatrix;
  MutableMatrix44D* getTransformMatrix();
  MutableMatrix44D* createTransformMatrix() const;

  mutable GLState* _glState;

protected:
  const Vector3D      _center;

  TransformableMesh(const Vector3D& center);

  virtual void userTransformMatrixChanged() = 0;

  GLState* getGLState() const;
  virtual void initializeGLState(GLState* glState) const;

  bool hasUserTransform() const;

public:
  void setUserTransformMatrix(MutableMatrix44D* userTransformMatrix);

  virtual ~TransformableMesh();
  
};

#endif
