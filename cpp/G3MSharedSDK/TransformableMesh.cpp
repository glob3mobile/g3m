//
//  TransformableMesh.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/17/17.
//
//

#include "TransformableMesh.hpp"

#include "GLState.hpp"


TransformableMesh::TransformableMesh(const Vector3D& center) :
_center(center),
_glState(NULL),
_transformMatrix(NULL),
_userTransformMatrix( MutableMatrix44D::newIdentity() )
{
  _transformGLFeature = new ModelTransformGLFeature(getTransformMatrix()->asMatrix44D());
}

TransformableMesh::~TransformableMesh() {
  delete _transformMatrix;
  delete _userTransformMatrix;

  _transformGLFeature->_release();

  if (_glState != NULL) {
    _glState->_release();
  }

#ifdef JAVA_CODE
  super.dispose();
#endif
}

bool TransformableMesh::hasUserTransform() const {
  return !_userTransformMatrix->isIdentity();
}

GLState* TransformableMesh::getGLState() const {
  if (_glState == NULL) {
    _glState = new GLState();
    initializeGLState(_glState);
  }
  return _glState;
}

MutableMatrix44D* TransformableMesh::getTransformMatrix() {
  if (_transformMatrix == NULL) {
    _transformMatrix = createTransformMatrix();
  }
  return _transformMatrix;
}

void TransformableMesh::setUserTransformMatrix(MutableMatrix44D* userTransformMatrix) {
  if (userTransformMatrix == NULL) {
    THROW_EXCEPTION("userTransformMatrix is NULL");
  }

  if (userTransformMatrix != _userTransformMatrix) {
    delete _userTransformMatrix;
    _userTransformMatrix = userTransformMatrix;
  }

  delete _transformMatrix;
  _transformMatrix = NULL;

  _transformGLFeature->setMatrix(getTransformMatrix()->asMatrix44D());
}

MutableMatrix44D* TransformableMesh::createTransformMatrix() const {
  if (_center.isNan() || _center.isZero()) {
    return new MutableMatrix44D(*_userTransformMatrix);
  }

  const MutableMatrix44D centerM = MutableMatrix44D::createTranslationMatrix(_center);
  if (_userTransformMatrix == NULL) {
    return new MutableMatrix44D(centerM);
  }

  MutableMatrix44D* result = new MutableMatrix44D();
  result->copyValueOfMultiplication(centerM,
                                    *_userTransformMatrix);

  return result;
}

void TransformableMesh::initializeGLState(GLState* glState) const {
  glState->addGLFeature(_transformGLFeature, true);
}
