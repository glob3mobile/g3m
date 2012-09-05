//
//  Matrix.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 05/09/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_Matrix_hpp
#define G3MiOSSDK_Matrix_hpp

#include "Vector3D.hpp"
#include "Vector2D.hpp"

#include "MutableVector3D.hpp"
class FrustumData;

#include "Angle.hpp"

#include "ILogger.hpp"
#include <string>

#include "MutableMatrix44D.hpp"


class Matrix{
  
  //_m23 -> row 2, column 3
  double _m00;
  double _m01; 
  double _m02;
  double _m03;
  double _m10; 
  double _m11;
  double _m12;
  double _m13;
  double _m20;
  double _m21;
  double _m22;
  double _m23;
  double _m30;
  double _m31;
  double _m32;
  double _m33;
  
public:
  
  static void compare(MutableMatrix44D& m, Matrix& m2){
    for (int i = 0; i < 16; i++) {
      if (m.get(i) != m2.get(i)){
        printf("MATRIX FAIL %d\n", i);
      }
    }
    
    float fm[16];
    float fm2[16];
    m.copyToColumnMajorFloatArray(fm);
    m2.copyToColumnMajorFloatArray(fm2);
    
    for (int i = 0; i < 16; i++) {
      if (fm[i] != fm2[i]){
        printf("MATRIX FAIL %d\n", i);
      }
    }
    
    
  }
  
  static void test(){
    MutableMatrix44D m = MutableMatrix44D::identity();
    Matrix m2 = Matrix::identity();
    
    compare(m, m2);

    
    m = MutableMatrix44D::createOrthographicProjectionMatrix(1, 2, 3, 4, 5, 6);
    m2 = Matrix::createOrthographicProjectionMatrix(1, 2, 3, 4, 5, 6);
    
    compare(m, m2);
    
    m = m.multiply(MutableMatrix44D::identity());
    m2 = m2.multiply(Matrix::identity());
    
    compare(m, m2);
    
    m = m.multiply(MutableMatrix44D::createOrthographicProjectionMatrix(1, 2, 3, 4, 5, 6));
    m2 = m2.multiply(Matrix::createOrthographicProjectionMatrix(1, 2, 3, 4, 5, 6));
    

    
    m = m.inversed();
    m2 = m2.inversed();
    
    compare(m, m2);
    
    int viewport[] = {4,5,6,7};
    Vector3D p(1.0,2.0,3.0);
    Vector3D v1 = m.unproject(p, viewport);
    Vector3D v2 = m2.unproject(p, viewport);
    
    if (v1.x() != v2.x()){
      printf("MATRIX FAIL\n");
    }
    
    Vector2D v3 = m.project(p, viewport);
    Vector2D v4 = m2.project(p, viewport);
    
    if (v3.x() != v4.x()){
      printf("MATRIX FAIL\n");
    }
    
    m = MutableMatrix44D::createTranslationMatrix(p);
    m2 = Matrix::createTranslationMatrix(p);
    
    compare(m, m2);
    
    Angle a = Angle::fromDegrees(32);
    m = MutableMatrix44D::createRotationMatrix(a, p);
    m2 = Matrix::createRotationMatrix(a, p);
    
    compare(m, m2);
    
    m= MutableMatrix44D::createGeneralRotationMatrix(a, p, p);
    m2 = Matrix::createGeneralRotationMatrix(a, p, p);
    
    MutableVector3D p2(5,6,7);
    MutableVector3D p3(0,0,0);
     MutableVector3D p4(1,1,1);
    
    m= MutableMatrix44D::createModelMatrix(p2,p3,p4);
    m2= Matrix::createModelMatrix(p2,p3,p4);
    
        compare(m, m2);
    
    
  }
  
  
  

  //CONTRUCTORS
  
  //Contructor parameters in column major order
  Matrix(double m00, double m10, double m20, double m30,
         double m01, double m11, double m21, double m31,
         double m02, double m12, double m22, double m32,
         double m03, double m13, double m23, double m33){
    _m00  = m00;
    _m01  = m01;
    _m02  = m02;
    _m03  = m03;
    
    _m10  = m10;
    _m11  = m11;
    _m12  = m12;
    _m13  = m13;
    
    _m20  = m20;
    _m21  = m21;
    _m22  = m22;
    _m23  = m23;
    
    _m30  = m30;
    _m31  = m31;
    _m32  = m32;
    _m33  = m33;
  }
  
  Matrix() {
    _m00  = 0.0;
    _m01  = 0.0;
    _m02  = 0.0;
    _m03  = 0.0;
    
    _m10  = 0.0;
    _m11  = 0.0;
    _m12  = 0.0;
    _m13  = 0.0;
    
    _m20  = 0.0;
    _m21  = 0.0;
    _m22  = 0.0;
    _m23  = 0.0;
    
    _m30  = 0.0;
    _m31  = 0.0;
    _m32  = 0.0;
    _m33  = 0.0;
  }
  
  Matrix(const Matrix &m){
    _m00  = m._m00;
    _m01  = m._m01;
    _m02  = m._m02;
    _m03  = m._m03;
    
    _m10  = m._m10;
    _m11  = m._m11;
    _m12  = m._m12;
    _m13  = m._m13;
    
    _m20  = m._m20;
    _m21  = m._m21;
    _m22  = m._m22;
    _m23  = m._m23;
    
    _m30  = m._m30;
    _m31  = m._m31;
    _m32  = m._m32;
    _m33  = m._m33;
  }
  
  //SPECIAL MATRICES
  
  static Matrix identity() {
    return Matrix(1, 0, 0, 0,
                            0, 1, 0, 0,
                            0, 0, 1, 0,
                            0, 0, 0, 1);
  }
  
  static Matrix invalid() {
    return Matrix(GMath.NanD(), GMath.NanD(), GMath.NanD(), GMath.NanD(),
                  GMath.NanD(), GMath.NanD(), GMath.NanD(), GMath.NanD(),
                  GMath.NanD(), GMath.NanD(), GMath.NanD(), GMath.NanD(),
                  GMath.NanD(), GMath.NanD(), GMath.NanD(), GMath.NanD());
  }
  
  bool isValid() {
    if (GMath.isNan(_m00)) return false;
    if (GMath.isNan(_m01)) return false;
    if (GMath.isNan(_m02)) return false;
    if (GMath.isNan(_m03)) return false;
    if (GMath.isNan(_m10)) return false;
    if (GMath.isNan(_m11)) return false;
    if (GMath.isNan(_m12)) return false;
    if (GMath.isNan(_m13)) return false;
    if (GMath.isNan(_m20)) return false;
    if (GMath.isNan(_m21)) return false;
    if (GMath.isNan(_m22)) return false;
    if (GMath.isNan(_m23)) return false;
    if (GMath.isNan(_m30)) return false;
    if (GMath.isNan(_m31)) return false;
    if (GMath.isNan(_m32)) return false;
    if (GMath.isNan(_m33)) return false;
    return true;
  }

//  
  //OPERATIONS
  
  Matrix multiply(const Matrix& that) const;
  
  Matrix inversed() const;
  
  Matrix transposed() const;
  
  //Returns values from 0..15 in column mayor order
  double get(int i) const {
    switch (i) {
      case 0:
        return _m00;
        break;
      case 1:
        return _m10;
        break;
      case 2:
        return _m20;
        break;
      case 3:
        return _m30;
        break;
      case 4:
        return _m01;
        break;
      case 5:
        return _m11;
        break;
      case 6:
        return _m21;
        break;
      case 7:
        return _m31;
        break;
      case 8:
        return _m02;
        break;
      case 9:
        return _m12;
        break;
      case 10:
        return _m22;
        break;
      case 11:
        return _m32;
        break;
      case 12:
        return _m03;
        break;
      case 13:
        return _m13;
        break;
      case 14:
        return _m23;
        break;
      case 15:
        return _m33;
        break;
      default:
        ILogger::instance()->logError("Accesing MutableMatrix44D out of index");
        return 0;
        break;
    }
  }
  
  
  void copyToColumnMajorFloatArray(float M[16]) const {
    M[ 0] = (float) _m00;
    M[ 1] = (float) _m10;
    M[ 2] = (float) _m20;
    M[ 3] = (float) _m30;
    
    M[ 4] = (float) _m01;
    M[ 5] = (float) _m11;
    M[ 6] = (float) _m21;
    M[ 7] = (float) _m31;
    
    M[ 8] = (float) _m02;
    M[ 9] = (float) _m12;
    M[10] = (float) _m22;
    M[11] = (float) _m32;
    
    M[12] = (float) _m03;
    M[13] = (float) _m13;
    M[14] = (float) _m23;
    M[15] = (float) _m33;
  }
  
  void print(const std::string& name, const ILogger* log) const;
  
  Vector3D unproject(const Vector3D& pixel3D, const int viewport[4]) const;
  
  Vector2D project(const Vector3D& point, const int viewport[4]) const;
  
  static Matrix createTranslationMatrix(const Vector3D& t);
  
  static Matrix createRotationMatrix(const Angle& angle, const Vector3D& p);
  
  static Matrix createGeneralRotationMatrix(const Angle& angle, 
                                                      const Vector3D& axis, const Vector3D& point);
  
  static Matrix createModelMatrix(const MutableVector3D& pos, 
                                            const MutableVector3D& center,
                                            const MutableVector3D& up);
  
  static Matrix createProjectionMatrix(double left, double right,
                                                 double bottom, double top,
                                                 double znear, double zfar);
  
  static Matrix createProjectionMatrix(const FrustumData& data);
  
  static Matrix createOrthographicProjectionMatrix(double left, double right,
                                                             double bottom, double top,
                                                             double znear, double zfar) {
    // set frustum matrix in double
    const double rl = right - left;
    const double tb = top - bottom;
    const double fn = zfar - znear;
    
    return Matrix(2 / rl, 0, 0, 0,
                            0, 2 / tb, 0, 0,
                            0, 0, -2 / fn, 0,
                            -(right+left) / rl, -(top+bottom) / tb, -(zfar+znear) / fn, 1 );
  }

  

  
};

#endif
