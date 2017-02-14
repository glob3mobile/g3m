//
//  Matrix44DProvider.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 23/08/13.
//
//

#include "Matrix44DProvider.hpp"
#include "ILogger.hpp"

void Matrix44DMultiplicationHolder::pullMatrixes() const {
  for (int j = 0; j < _matricesSize; j++) {
    const Matrix44D* newMatrix = _providers[j]->getMatrix();

    if (newMatrix != _matrices[j]) {
      if (_matrices[j] != NULL) {
        _matrices[j]->_release();
      }

      _matrices[j] = newMatrix;
      _matrices[j]->_retain();
    }
  }
}

Matrix44DMultiplicationHolder::Matrix44DMultiplicationHolder(const Matrix44DProvider* providers[],
                                                             size_t matricesSize):
_matricesSize(matricesSize),
_modelview(NULL)
{
#ifdef C_CODE
  _matrices  = new const Matrix44D*[_matricesSize];
  _providers = new const Matrix44DProvider*[_matricesSize];
#endif
#ifdef JAVA_CODE
  _matrices  = new Matrix44D[_matricesSize];
  _providers = new Matrix44DProvider[_matricesSize];
#endif
  for (int i = 0; i < _matricesSize; i++) {
    _matrices[i]  = NULL;
    _providers[i] = providers[i];
    _providers[i]->_retain();
  }

  pullMatrixes();
}

Matrix44DMultiplicationHolder::~Matrix44DMultiplicationHolder() {
  for (int j = 0; j < _matricesSize; j++) {
    if (_matrices[j] != NULL) {
      _matrices[j]->_release();
    }
    _providers[j]->_release();
  }

#ifdef C_CODE
  delete[] _matrices;
  delete[] _providers;
#endif

  if (_modelview != NULL) {
    _modelview->_release();
  }

#ifdef JAVA_CODE
  super.dispose();
#endif
}

Matrix44D* Matrix44DMultiplicationHolder::getMatrix() const {
  if (_modelview != NULL) {
    for (int i = 0; i < _matricesSize; i++) {
      const Matrix44D* m = _providers[i]->getMatrix();
      if (m == NULL) {
        ILogger::instance()->logError("Modelview multiplication failure");
      }

      if (_matrices[i] != m) {
        //If one matrix differs we have to raplace all matrixes on Holders and recalculate modelview
        _modelview->_release();//NEW MODELVIEW NEEDED
        _modelview = NULL;

        pullMatrixes();
        break;
      }
    }
  }

  if (_modelview == NULL) {
    _modelview = new Matrix44D(*_matrices[0]);
    for (int i = 1; i < _matricesSize; i++) {
      const Matrix44D* m2 = _matrices[i];
      Matrix44D* m3 = _modelview->createMultiplication(*m2);
      _modelview->_release();
      _modelview = m3;
    }
  }
  
  return _modelview;
}
