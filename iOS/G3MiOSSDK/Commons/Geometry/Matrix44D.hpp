//
//  Matrix44D.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 01/07/13.
//
//

#ifndef __G3MiOSSDK__Matrix44D__
#define __G3MiOSSDK__Matrix44D__

#include "IFloatBuffer.hpp"

#include "RCObject.hpp"

class Matrix44D: public RCObject {

public:

  //_m23 -> row 2, column 3
  const double _m00;
  const double _m01;
  const double _m02;
  const double _m03;
  const double _m10;
  const double _m11;
  const double _m12;
  const double _m13;
  const double _m20;
  const double _m21;
  const double _m22;
  const double _m23;
  const double _m30;
  const double _m31;
  const double _m32;
  const double _m33;

  mutable float*        _columnMajorFloatArray;
  mutable IFloatBuffer* _columnMajorFloatBuffer;

public:

  explicit Matrix44D(const Matrix44D& m);

  Matrix44D(double m00, double m10, double m20, double m30,
            double m01, double m11, double m21, double m31,
            double m02, double m12, double m22, double m32,
            double m03, double m13, double m23, double m33);

  ~Matrix44D();

  Matrix44D* createMultiplication(const Matrix44D &that) const;

#ifdef C_CODE
  float* getColumnMajorFloatArray() const
#else
  float[] getColumnMajorFloatArray() const
#endif
  {
    if (_columnMajorFloatArray == NULL) {
      _columnMajorFloatArray = new float[16];

      _columnMajorFloatArray[ 0] = (float) _m00;
      _columnMajorFloatArray[ 1] = (float) _m10;
      _columnMajorFloatArray[ 2] = (float) _m20;
      _columnMajorFloatArray[ 3] = (float) _m30;

      _columnMajorFloatArray[ 4] = (float) _m01;
      _columnMajorFloatArray[ 5] = (float) _m11;
      _columnMajorFloatArray[ 6] = (float) _m21;
      _columnMajorFloatArray[ 7] = (float) _m31;

      _columnMajorFloatArray[ 8] = (float) _m02;
      _columnMajorFloatArray[ 9] = (float) _m12;
      _columnMajorFloatArray[10] = (float) _m22;
      _columnMajorFloatArray[11] = (float) _m32;

      _columnMajorFloatArray[12] = (float) _m03;
      _columnMajorFloatArray[13] = (float) _m13;
      _columnMajorFloatArray[14] = (float) _m23;
      _columnMajorFloatArray[15] = (float) _m33;
    }

    return _columnMajorFloatArray;
  }

  const IFloatBuffer* getColumnMajorFloatBuffer() const;

  bool isEqualsTo(const Matrix44D& m) const;

  static Matrix44D* createIdentity() {
    return new Matrix44D(1, 0, 0, 0,
                         0, 1, 0, 0,
                         0, 0, 1, 0,
                         0, 0, 0, 1);
  }


  bool isScaleMatrix() const;

  bool isTranslationMatrix() const;

};


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
  const Matrix44DProvider** _matrixHolders;
  int _nMatrix;
  mutable Matrix44D* _modelview;

  void pullMatrixes() const{
    for (int j = 0; j < _nMatrix; j++) {
      const Matrix44D* newMatrix = _matrixHolders[j]->getMatrix();

      if (newMatrix != _matrix[j]){
        if (_matrix[j] != NULL){
          _matrix[j]->_release();
        }

        _matrix[j] = newMatrix;
        _matrix[j]->_retain();
      }
    }
  }

public:
  Matrix44DMultiplicationHolder(const Matrix44DProvider* matrixHolders[], int nMatrix):
  _matrixHolders(new const Matrix44DProvider*[nMatrix]),
  _nMatrix(nMatrix),
  _modelview(NULL)
  {
    #ifdef C_CODE
        _matrix = new const Matrix44D*[nMatrix];
    #endif
    #ifdef JAVA_CODE
        _matrix = new Matrix44D[nMatrix];
    #endif
    for (int i = 0; i < _nMatrix; i++) {
      _matrix[i] = NULL;
      _matrixHolders[i] = matrixHolders[i];
    }

    pullMatrixes();
  }

  ~Matrix44DMultiplicationHolder() {

    for (int j = 0; j < _nMatrix; j++) {
      if (_matrix[j] != NULL){
        _matrix[j]->_release();
      }
    }

#ifdef C_CODE
    delete[] _matrix;
    delete[] _matrixHolders;
#endif
    if (_modelview != NULL) {
      _modelview->_release();
    }
  }

  Matrix44D* getMatrix() const {

    if (_modelview != NULL) {
      for (int i = 0; i < _nMatrix; i++) {
        const Matrix44D* m = _matrixHolders[i]->getMatrix();
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
  
};


#endif /* defined(__G3MiOSSDK__Matrix44D__) */
