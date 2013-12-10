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

#include <vector>


class Matrix44DProvider: public RCObject{
protected:
  virtual ~Matrix44DProvider() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

public:
  virtual const Matrix44D* getMatrix() const = 0;
};

class Matrix44DHolder: public Matrix44DProvider {
private:
#ifdef C_CODE
  const Matrix44D* _matrix;
#endif
#ifdef JAVA_CODE
  private Matrix44D _matrix;
#endif

  ~Matrix44DHolder() {
    _matrix->_release();

#ifdef JAVA_CODE
    super.dispose();
#endif
  }

public:
  Matrix44DHolder(const Matrix44D* matrix):_matrix(matrix) {
    if (matrix == NULL) {
      ILogger::instance()->logError("Setting NULL in Matrix44D Holder");
    }
    _matrix->_retain();
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

class Matrix44DMultiplicationHolder : public Matrix44DProvider {
private:
  const Matrix44D** _matrix;
  const Matrix44DProvider** _providers;
  int _nMatrix;
  mutable Matrix44D* _modelview;

  void pullMatrixes() const;

  ~Matrix44DMultiplicationHolder();

public:
  Matrix44DMultiplicationHolder(const Matrix44DProvider* providers[], int nMatrix);

  Matrix44D* getMatrix() const;
  
};

class Matrix44DMultiplicationHolderBuilder{

  std::vector<const Matrix44DProvider*> _providers;
public:

  ~Matrix44DMultiplicationHolderBuilder() {
    const int providersSize = _providers.size();
    for (int i = 0; i < providersSize; i++) {
      _providers[i]->_release();
    }
  }

  int size() const{
    return _providers.size();
  }
  
  void add(const Matrix44DProvider* provider) {
    _providers.push_back(provider);
    provider->_retain();
  }

  Matrix44DMultiplicationHolder* create() const {
    const int providersSize = _providers.size();
    const Matrix44DProvider* ps[providersSize];
    for (int i = 0; i < providersSize; i++) {
      ps[i] = _providers[i];
    }

    return new Matrix44DMultiplicationHolder(ps, providersSize);
  }

};


#endif /* defined(__G3MiOSSDK__Matrix44DProvider__) */
