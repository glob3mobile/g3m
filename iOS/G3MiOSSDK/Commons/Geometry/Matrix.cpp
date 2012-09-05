//
//  Matrix.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 05/09/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "Matrix.hpp"

#include "Frustum.hpp"
#include "MutableVector3D.hpp"


Matrix Matrix::multiply(const Matrix &that) const{
  double that00 = that._m00;
  double that10 = that._m10;
  double that20 = that._m20;
  double that30 = that._m30;
  
  double that01 = that._m01;
  double that11 = that._m11;
  double that21 = that._m21;
  double that31 = that._m31;
  
  double that02 = that._m02;
  double that12 = that._m12;
  double that22 = that._m22;
  double that32 = that._m32;
  
  double that03 = that._m03;
  double that13 = that._m13;
  double that23 = that._m23;
  double that33 = that._m33;
  
  
  double m00 = (_m00 * that00) + (_m01 * that10) + (_m02 * that20) + (_m03 * that30);
  double m01 = (_m00 * that01) + (_m01 * that11) + (_m02 * that21) + (_m03 * that31);
  double m02 = (_m00 * that02) + (_m01 * that12) + (_m02 * that22) + (_m03 * that32);
  double m03 = (_m00 * that03) + (_m01 * that13) + (_m02 * that23) + (_m03 * that33);
  
  double m10 = (_m10 * that00) + (_m11 * that10) + (_m12 * that20) + (_m13 * that30);
  double m11 = (_m10 * that01) + (_m11 * that11) + (_m12 * that21) + (_m13 * that31);
  double m12 = (_m10 * that02) + (_m11 * that12) + (_m12 * that22) + (_m13 * that32);
  double m13 = (_m10 * that03) + (_m11 * that13) + (_m12 * that23) + (_m13 * that33);

  double m20 = (_m20 * that00) + (_m21 * that10) + (_m22 * that20) + (_m23 * that30);
  double m21 = (_m20 * that01) + (_m21 * that11) + (_m22 * that21) + (_m23 * that31);
  double m22 = (_m20 * that02) + (_m21 * that12) + (_m22 * that22) + (_m23 * that32);
  double m23 = (_m20 * that03) + (_m21 * that13) + (_m22 * that23) + (_m23 * that33);

  double m30 = (_m30 * that00) + (_m31 * that10) + (_m32 * that20) + (_m33 * that30);
  double m31 = (_m30 * that01) + (_m31 * that11) + (_m32 * that21) + (_m33 * that31);
  double m32 = (_m30 * that02) + (_m31 * that12) + (_m32 * that22) + (_m33 * that32);
  double m33 = (_m30 * that03) + (_m31 * that13) + (_m32 * that23) + (_m33 * that33);
  
  return Matrix(m00, m01, m02, m03,
                m10, m11, m12, m13,
                m20, m21, m22, m23,
                m30, m31, m32, m33);
}

Matrix Matrix::createProjectionMatrix(const FrustumData& data)
{
  return createProjectionMatrix(data._left, data._right,
                                data._bottom, data._top,
                                data._znear, data._zfar);
}