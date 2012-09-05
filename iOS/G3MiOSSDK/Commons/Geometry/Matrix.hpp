//
//  Matrix.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 05/09/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_Matrix_hpp
#define G3MiOSSDK_Matrix_hpp


class Vector3D;
class Vector2D;
class MutableVector3D;
class FrustumData;

#include "Angle.hpp"

#include "ILogger.hpp"
#include <string>

#include "MutableMatrix44D.hpp"


class Matrix{
  
  double _m00, _m01, _m02, _m03;
  double _m10, _m11, _m12, _m13;
  double _m20, _m21, _m22, _m23;
  double _m30, _m31, _m32, _m33;
  
public:
  
  static void test(){
    
//    MutableMatrix44D mx = MutableMatrix44D(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16);
//    MutableMatrix44D mx2 = mx.transposed();
    
    
    MutableMatrix44D m = MutableMatrix44D::identity();
    Matrix m2 = Matrix::identity();
    
    for (int i = 0; i < 16; i++) {
      if (m.get(i) != m2.get(i)){
        printf("MATRIX FAIL");
      }
    }

    
    m = MutableMatrix44D::createOrthographicProjectionMatrix(1, 2, 3, 4, 5, 6);
    m2 = Matrix::createOrthographicProjectionMatrix(1, 2, 3, 4, 5, 6);
    
    for (int i = 0; i < 16; i++) {
      if (m.get(i) != m2.get(i)){
        printf("MATRIX FAIL");
      }
    }
    
    m = m.multiply(MutableMatrix44D::identity());
    m2 = m2.multiply(Matrix::identity());
    
    for (int i = 0; i < 16; i++) {
      if (m.get(i) != m2.get(i)){
        printf("MATRIX FAIL");
      }
    }
    
    MutableMatrix44D mm1 = MutableMatrix44D::createOrthographicProjectionMatrix(1, 2, 3, 4, 5, 6);
    Matrix m1 = Matrix::createOrthographicProjectionMatrix(1, 2, 3, 4, 5, 6);
    
    for (int i = 0; i < 16; i++) {
      if (mm1.get(i) != m1.get(i)){
        printf("MATRIX FAIL");
      }
    }
    
    m = m.multiply(mm1);
    m2 = m1.multiply(m2);
    
    for (int i = 0; i < 16; i++) {
      if (m.get(i) != m2.get(i)){
        printf("MATRIX FAIL %d\n", i);
      }
    }
    
    
  }
  
  
  
  //CONTRUCTORS
  
  Matrix(double m00, double m01, double m02, double m03,
                   double m10, double m11, double m12, double m13,
                   double m20, double m21, double m22, double m23,
                   double m30, double m31, double m32, double m33) {
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
  
  Matrix(const double M[16]){
    _m00  = M[0];
    _m01  = M[1];
    _m02  = M[2];
    _m03  = M[3];
    
    _m10  = M[4];
    _m11  = M[5];
    _m12  = M[6];
    _m13  = M[7];
    
    _m20  = M[8];
    _m21  = M[9];
    _m22  = M[10];
    _m23  = M[11];
    
    _m30  = M[12];
    _m31  = M[13];
    _m32  = M[14];
    _m33  = M[15];
  }
  
  Matrix(const float M[16]){
    _m00  = (double) M[0];
    _m01  = (double) M[1];
    _m02  = (double) M[2];
    _m03  = (double) M[3];
    
    _m10  = (double) M[4];
    _m11  = (double) M[5];
    _m12  = (double) M[6];
    _m13  = (double) M[7];
    
    _m20  = (double) M[8];
    _m21  = (double) M[9];
    _m22  = (double) M[10];
    _m23  = (double) M[11];
    
    _m30  = (double) M[12];
    _m31  = (double) M[13];
    _m32  = (double) M[14];
    _m33  = (double) M[15];
  }

  //SPECIAL MATRICES
  
  static Matrix identity() {
    float I[16] = {
      1, 0, 0, 0,
      0, 1, 0, 0,
      0, 0, 1, 0,
      0, 0, 0, 1
    };
    return Matrix(I);
  }
//  
//  static Matrix fromRotationX(const Angle& angle) {
//    double c = angle.cosinus();
//    double s = angle.sinus();
//    return Matrix(1.0, 0.0, 0.0, 0.0,
//                  0.0,   c,  -s, 0.0,
//                  0.0,   s,   c, 0.0,
//                  0.0, 0.0, 0.0, 1.0);
//    
//  }
//  
//  static Matrix fromRotationY(const Angle& angle) {
//    double c = angle.cosinus();
//    double s = angle.sinus();
//    return Matrix(c, 0.0,   s, 0.0,
//                  0.0, 1.0, 0.0, 0.0,
//                  -s, 0.0,   c, 0.0,
//                  0.0, 0.0, 0.0, 1.0);
//    
//  }
//  
//  static Matrix fromRotationZ(const Angle& angle) {
//    double c = angle.cosinus();
//    double s = angle.sinus();
//    return Matrix(c,  -s, 0.0, 0.0,
//                  s,   c, 0.0, 0.0,
//                  0.0, 0.0, 1.0, 0.0,
//                  0.0, 0.0, 0.0, 1.0);
//  }
//  
//  static Matrix fromTranslation(const MutableVector3D& translation);
//  
//  static Matrix fromScale(const MutableVector3D& scale);
//  
  //OPERATIONS
  
  Matrix multiply(const Matrix& that) const;
  
  Matrix inversed() const;
  
  Matrix transposed() const;
  
  double get(int i) const {
    switch (i) {
      case 0:
        return _m00;
        break;
      case 1:
        return _m01;
        break;
      case 2:
        return _m02;
        break;
      case 3:
        return _m03;
        break;
      case 4:
        return _m10;
        break;
      case 5:
        return _m11;
        break;
      case 6:
        return _m12;
        break;
      case 7:
        return _m13;
        break;
      case 8:
        return _m20;
        break;
      case 9:
        return _m21;
        break;
      case 10:
        return _m22;
        break;
      case 11:
        return _m23;
        break;
      case 12:
        return _m30;
        break;
      case 13:
        return _m31;
        break;
      case 14:
        return _m32;
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
  
  
  void copyToFloatMatrix(float M[16]) const { 
    M[ 0] = (float) _m00;
    M[ 1] = (float) _m01;
    M[ 2] = (float) _m02;
    M[ 3] = (float) _m03;
    
    M[ 4] = (float) _m10;
    M[ 5] = (float) _m11;
    M[ 6] = (float) _m12;
    M[ 7] = (float) _m13;
    
    M[ 8] = (float) _m20;
    M[ 9] = (float) _m21;
    M[10] = (float) _m22;
    M[11] = (float) _m23;
    
    M[12] = (float) _m30;
    M[13] = (float) _m31;
    M[14] = (float) _m32;
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
    
    double P[16];
    P[0] = 2 / rl;
    P[1] = P[2] = P[3] = P[4] = 0;
    P[5] = 2 / tb;
    P[6] = P[7] = P[8] = P[9] = 0;
    P[10] = -2 / fn;
    P[11] = 0;
    P[12] = -(right+left) / rl;
    P[13] = -(top+bottom) / tb;
    P[14] = -(zfar+znear) / fn;
    P[15] = 1;
    
    return Matrix(P);
  }

  

  
};

#endif
