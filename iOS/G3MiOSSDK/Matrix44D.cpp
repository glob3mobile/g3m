//
//  Matrix44D.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 01/07/13.
//
//

#include "Matrix44D.hpp"

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
_m33(m33){
  _columnMajorFloatArray = NULL;
}
