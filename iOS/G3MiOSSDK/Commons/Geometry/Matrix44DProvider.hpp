//
//  Matrix44DProvider.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 23/08/13.
//
//

#ifndef __G3MiOSSDK__Matrix44DProvider__
#define __G3MiOSSDK__Matrix44DProvider__

#include <iostream>

#include "RCObject.hpp"
#include "Matrix44D.hpp"


class Matrix44DProvider: public RCObject{
public:
  virtual ~Matrix44DProvider(){}
  virtual const Matrix44D* getMatrix() const = 0;
};

class Matrix44DHolder: public Matrix44DProvider{
#ifdef C_CODE
  const Matrix44D* _matrix;
#endif
#ifdef JAVA_CODE
  private Matrix44D _matrix;
#endif
public:
  Matrix44DHolder(const Matrix44D* matrix):_matrix(matrix) {
    if (matrix == NULL) {
      ILogger::instance()->logError("Setting NULL in Matrix44D Holder");
    }
    _matrix->_retain();
  }

  ~Matrix44DHolder() {
    _matrix->_release();
  }

  void setMatrix(const Matrix44D* matrix) {
    if (matrix == NULL) {
      ILogger::instance()->logError("Setting NULL in Matrix44D Holder");
    }

    if (matrix != _matrix) {
      if (_matrix != NULL) {
        _matrix->_release();
      }
      _matrix = matrix;
      _matrix->_retain();
    }
  }

  const Matrix44D* getMatrix() const{
    return _matrix;
  }
};

//class Matrix44DMultiplicationHolder: public Matrix44DProvider{
//  const Matrix44D** _lastMatrixes;
//  const Matrix44DProvider** _providers;
//  int _nMatrix;
//  mutable Matrix44D* _modelview;
//public:
//  Matrix44DMultiplicationHolder(const Matrix44DProvider* providers[], int nMatrix):
//  _providers(new const Matrix44DProvider*[nMatrix]),
//  _nMatrix(nMatrix),
//  _modelview(NULL)
//  {
//#ifdef C_CODE
//    _lastMatrixes = new const Matrix44D*[nMatrix];
//#endif
//#ifdef JAVA_CODE
//    _lastMatrixes = new Matrix44D[nMatrix];
//#endif
//    for (int i = 0; i < _nMatrix; i++) {
//      _providers[i] = providers[i];
//      _lastMatrixes[i] = _providers[i]->getMatrix();
//      _providers[i]->_retain();
//      if (_lastMatrixes[i] == NULL){
//        ILogger::instance()->logError("Modelview multiplication failure");
//      }
//    }
//  }
//
//  ~Matrix44DMultiplicationHolder(){
//#ifdef C_CODE
//    delete[] _lastMatrixes;
//    delete[] _providers;
//#endif
//    if (_modelview != NULL){
//      _modelview->_release();
//    }
//    for (int i = 0; i < _nMatrix; i++) {
//      _providers[i]->_release();
//    }
//  }
//
//  const Matrix44D* getMatrix() const {
//
//    if (_modelview != NULL){
//      for (int i = 0; i < _nMatrix; i++) {
//        const Matrix44D* m = _providers[i]->getMatrix();
//        if (m == NULL){
//          ILogger::instance()->logError("Modelview multiplication failure");
//        }
//
//        if (_lastMatrixes[i] != m){
//
//          //If one matrix differs we have to raplace all matrixes on Holders and recalculate modelview
//          _modelview->_release();//NEW MODELVIEW NEEDED
//          _modelview = NULL;
//
//          for (int j = 0; j < _nMatrix; j++) {
//            _lastMatrixes[j] = _providers[j]->getMatrix();
//          }
//          break;
//        }
//      }
//    }
//
//
//    if (_modelview == NULL){
//      _modelview = new Matrix44D(*_lastMatrixes[0]);
//      for (int i = 1; i < _nMatrix; i++){
//        const Matrix44D* m2 = _lastMatrixes[i];
//        Matrix44D* m3 = _modelview->createMultiplication(*m2);
//        _modelview->_release();
//        _modelview = m3;
//      }
//    }
//    return _modelview;
//  }
//
//
//};

class Matrix44DMultiplicationHolder : public Matrix44DProvider{
  const Matrix44D** _matrix;
  const Matrix44DProvider** _providers;
  int _nMatrix;
  mutable Matrix44D* _modelview;

  void pullMatrixes() const;

public:
  Matrix44DMultiplicationHolder(const Matrix44DProvider* providers[], int nMatrix);

  ~Matrix44DMultiplicationHolder();

  Matrix44D* getMatrix() const;
  
};


#endif /* defined(__G3MiOSSDK__Matrix44DProvider__) */
