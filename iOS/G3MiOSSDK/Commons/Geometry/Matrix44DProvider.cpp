//
//  Matrix44DProvider.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 23/08/13.
//
//

#include "Matrix44DProvider.hpp"

void Matrix44DMultiplicationHolder::pullMatrixes() const{
  for (int j = 0; j < _nMatrix; j++) {
    const Matrix44D* newMatrix = _providers[j]->getMatrix();

    if (newMatrix != _matrix[j]) {
      if (_matrix[j] != NULL) {
        _matrix[j]->_release();
      }

      _matrix[j] = newMatrix;
      _matrix[j]->_retain();
    }
  }
}

Matrix44DMultiplicationHolder::Matrix44DMultiplicationHolder(const Matrix44DProvider* providers[], int nMatrix):
_nMatrix(nMatrix),
_modelview(NULL)
{
#ifdef C_CODE
  _matrix = new const Matrix44D*[nMatrix];
  _providers = new const Matrix44DProvider*[nMatrix];
#endif
#ifdef JAVA_CODE
  _matrix = new Matrix44D[nMatrix];
  _providers = new Matrix44DProvider[nMatrix];
#endif
  for (int i = 0; i < _nMatrix; i++) {
    _matrix[i] = NULL;
    _providers[i] = providers[i];
    _providers[i]->_retain();
  }

  pullMatrixes();
}

Matrix44DMultiplicationHolder::~Matrix44DMultiplicationHolder() {

  for (int j = 0; j < _nMatrix; j++) {
    if (_matrix[j] != NULL) {
      _matrix[j]->_release();
    }
    _providers[j]->_release();
  }


#ifdef C_CODE
  delete[] _matrix;
  delete[] _providers;
#endif
  if (_modelview != NULL) {
    _modelview->_release();
  }
  {

#ifdef JAVA_CODE
    super.dispose();
#endif
  }
}

Matrix44D* Matrix44DMultiplicationHolder::getMatrix() const {

  if (_modelview != NULL) {
    for (int i = 0; i < _nMatrix; i++) {
      const Matrix44D* m = _providers[i]->getMatrix();
      if (m == NULL) {
        ILogger::instance()->logError("Modelview multiplication failure");
      }

      if (_matrix[i] != m) {
        //If one matrix differs we have to raplace all matrixes on Holders and recalculate modelview
        _modelview->_release();//NEW MODELVIEW NEEDED
        _modelview = NULL;

        pullMatrixes();
        break;
      }
    }
  }

  if (_modelview == NULL) {
    _modelview = new Matrix44D(*_matrix[0]);
    for (int i = 1; i < _nMatrix; i++) {
      const Matrix44D* m2 = _matrix[i];
      Matrix44D* m3 = _modelview->createMultiplication(*m2);
      _modelview->_release();
      _modelview = m3;
    }
  }
  return _modelview;
}
