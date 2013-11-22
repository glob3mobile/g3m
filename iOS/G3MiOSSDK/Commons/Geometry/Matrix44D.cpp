//
//  Matrix44D.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 01/07/13.
//
//

#include "Matrix44D.hpp"

#include "IFactory.hpp"

Matrix44D::Matrix44D(double m00, double m10, double m20, double m30,
                     double m01, double m11, double m21, double m31,
                     double m02, double m12, double m22, double m32,
                     double m03, double m13, double m23, double m33):
_m00(m00),
_m01(m01),
_m02(m02),
_m03(m03),

_m10(m10),
_m11(m11),
_m12(m12),
_m13(m13),

_m20(m20),
_m21(m21),
_m22(m22),
_m23(m23),

_m30(m30),
_m31(m31),
_m32(m32),
_m33(m33) {
  _columnMajorFloatArray = NULL;
  _columnMajorFloatBuffer = NULL;
}

Matrix44D::~Matrix44D()
{
  delete[] _columnMajorFloatArray;
  delete _columnMajorFloatBuffer;

#ifdef JAVA_CODE
  super.dispose();
#endif
}

Matrix44D::Matrix44D(const Matrix44D& m):
_m00(m._m00),
_m01(m._m01),
_m02(m._m02),
_m03(m._m03),

_m10(m._m10),
_m11(m._m11),
_m12(m._m12),
_m13(m._m13),

_m20(m._m20),
_m21(m._m21),
_m22(m._m22),
_m23(m._m23),

_m30(m._m30),
_m31(m._m31),
_m32(m._m32),
_m33(m._m33) {
  _columnMajorFloatArray = NULL;
  _columnMajorFloatBuffer = NULL;
}

Matrix44D* Matrix44D::createMultiplication(const Matrix44D &that) const {

  const double that00 = that._m00;
  const double that10 = that._m10;
  const double that20 = that._m20;
  const double that30 = that._m30;

  const double that01 = that._m01;
  const double that11 = that._m11;
  const double that21 = that._m21;
  const double that31 = that._m31;

  const double that02 = that._m02;
  const double that12 = that._m12;
  const double that22 = that._m22;
  const double that32 = that._m32;

  const double that03 = that._m03;
  const double that13 = that._m13;
  const double that23 = that._m23;
  const double that33 = that._m33;

  //Rows of this X Columns of that
  const double m00 = (_m00 * that00) + (_m01 * that10) + (_m02 * that20) + (_m03 * that30);
  const double m01 = (_m00 * that01) + (_m01 * that11) + (_m02 * that21) + (_m03 * that31);
  const double m02 = (_m00 * that02) + (_m01 * that12) + (_m02 * that22) + (_m03 * that32);
  const double m03 = (_m00 * that03) + (_m01 * that13) + (_m02 * that23) + (_m03 * that33);

  const double m10 = (_m10 * that00) + (_m11 * that10) + (_m12 * that20) + (_m13 * that30);
  const double m11 = (_m10 * that01) + (_m11 * that11) + (_m12 * that21) + (_m13 * that31);
  const double m12 = (_m10 * that02) + (_m11 * that12) + (_m12 * that22) + (_m13 * that32);
  const double m13 = (_m10 * that03) + (_m11 * that13) + (_m12 * that23) + (_m13 * that33);

  const double m20 = (_m20 * that00) + (_m21 * that10) + (_m22 * that20) + (_m23 * that30);
  const double m21 = (_m20 * that01) + (_m21 * that11) + (_m22 * that21) + (_m23 * that31);
  const double m22 = (_m20 * that02) + (_m21 * that12) + (_m22 * that22) + (_m23 * that32);
  const double m23 = (_m20 * that03) + (_m21 * that13) + (_m22 * that23) + (_m23 * that33);

  const double m30 = (_m30 * that00) + (_m31 * that10) + (_m32 * that20) + (_m33 * that30);
  const double m31 = (_m30 * that01) + (_m31 * that11) + (_m32 * that21) + (_m33 * that31);
  const double m32 = (_m30 * that02) + (_m31 * that12) + (_m32 * that22) + (_m33 * that32);
  const double m33 = (_m30 * that03) + (_m31 * that13) + (_m32 * that23) + (_m33 * that33);

  return new Matrix44D(m00, m10, m20, m30,
                       m01, m11, m21, m31,
                       m02, m12, m22, m32,
                       m03, m13, m23, m33);
}

bool Matrix44D::isEquals(const Matrix44D& m) const{
  return (
          (_m00 == m._m00) && (_m01 == m._m01) && (_m02 == m._m02) && (_m03 == m._m03) &&
          (_m10 == m._m10) && (_m11 == m._m11) && (_m12 == m._m12) && (_m13 == m._m13) &&
          (_m20 == m._m20) && (_m21 == m._m21) && (_m22 == m._m22) && (_m23 == m._m23) &&
          (_m30 == m._m30) && (_m31 == m._m31) && (_m32 == m._m32) && (_m33 == m._m33)
          );
}

const IFloatBuffer* Matrix44D::getColumnMajorFloatBuffer() const {
  if (_columnMajorFloatBuffer == NULL) {
    _columnMajorFloatBuffer = IFactory::instance()->createFloatBuffer(
                                                                      (float) _m00,
                                                                      (float) _m10,
                                                                      (float) _m20,
                                                                      (float) _m30,

                                                                      (float) _m01,
                                                                      (float) _m11,
                                                                      (float) _m21,
                                                                      (float) _m31,

                                                                      (float) _m02,
                                                                      (float) _m12,
                                                                      (float) _m22,
                                                                      (float) _m32,

                                                                      (float) _m03,
                                                                      (float) _m13,
                                                                      (float) _m23,
                                                                      (float) _m33
                                                                      );
  }
  return _columnMajorFloatBuffer;
}

bool Matrix44D::isScaleMatrix() const {

  return (_m01 == 0 &&
          _m02 == 0 &&
          _m03 == 0 &&

          _m10 == 0 &&
          _m12 == 0 &&
          _m13 == 0 &&

          _m20 == 0 &&
          _m21 == 0 &&
          _m23 == 0 &&
          
          _m30 == 0 &&
          _m31 == 0 &&
          _m32 == 0 &&
          _m33 == 1.0);
  
  
  //
  //  return MutableMatrix44D(scale._x, 0, 0, 0,
  //                          0, scale._y, 0, 0,
  //                          0, 0, scale._z, 0,
  //                          0, 0, 0, 1);
  
}

bool Matrix44D::isTranslationMatrix() const {

  return (_m00 == 1 &&
          _m01 == 0 &&
          _m02 == 0 &&
          _m03 == 0 &&

          _m10 == 0 &&
          _m11 == 1 &&
          _m12 == 0 &&
          _m13 == 0 &&

          _m20 == 0 &&
          _m21 == 0 &&
          _m22 == 1 &&
          _m23 == 0 &&

          _m33 == 1);



//  return MutableMatrix44D(1, 0, 0, 0,
//                          0, 1, 0, 0,
//                          0, 0, 1, 0,
//                          x, y, z, 1);

}
