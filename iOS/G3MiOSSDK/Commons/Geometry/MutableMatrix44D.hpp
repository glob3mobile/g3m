//
//  Mat4.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 06/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_Mat4_h
#define G3MiOSSDK_Mat4_h

class Vector3D;
class Vector2D;
//class Angle;
class MutableVector3D;
class FrustumData;

#include "Angle.hpp"

#include "ILogger.hpp"
#include <string>

// class to keep a 4x4 matrix
class MutableMatrix44D {
private:
  double _m[16];
  
  static bool invert_matrix(const double m[16], double out[16]);
  
  void transformPoint(double out[4], const double in[4]) const;
  
  
public:
  
  static MutableMatrix44D identity() {
    float I[16] = {
      1, 0, 0, 0,
      0, 1, 0, 0,
      0, 0, 1, 0,
      0, 0, 0, 1
    };
    return MutableMatrix44D(I);
  }
  
  static MutableMatrix44D fromRotationX(const Angle& angle) {
    double c = angle.cosinus();
    double s = angle.sinus();
    return MutableMatrix44D(1.0, 0.0, 0.0, 0.0,
                            0.0,   c,  -s, 0.0,
                            0.0,   s,   c, 0.0,
                            0.0, 0.0, 0.0, 1.0);
    
  }
  
  static MutableMatrix44D fromRotationY(const Angle& angle) {
    double c = angle.cosinus();
    double s = angle.sinus();
    return MutableMatrix44D(  c, 0.0,   s, 0.0,
                            0.0, 1.0, 0.0, 0.0,
                             -s, 0.0,   c, 0.0,
                            0.0, 0.0, 0.0, 1.0);
    
  }
  
  static MutableMatrix44D fromRotationZ(const Angle& angle) {
    double c = angle.cosinus();
    double s = angle.sinus();
    return MutableMatrix44D(  c,  -s, 0.0, 0.0,
                              s,   c, 0.0, 0.0,
                            0.0, 0.0, 1.0, 0.0,
                            0.0, 0.0, 0.0, 1.0);
  }
  
  static MutableMatrix44D fromTranslation(const MutableVector3D& translation);
  
  static MutableMatrix44D fromScale(const MutableVector3D& scale);
  
  MutableMatrix44D() {
    for (int i = 0; i < 16; i++) {
      _m[i] = 0.0;
    }
  }
  
  MutableMatrix44D(const MutableMatrix44D &m){
    for (int i = 0; i < 16; i++) {
      _m[i] = m._m[i];
    }
  }
  
  MutableMatrix44D(const double M[16]){
    for (int i = 0; i < 16; i++) {
      _m[i] = M[i];
    }
  }
  
  MutableMatrix44D(const float M[16]){
    for (int i = 0; i < 16; i++) {
      _m[i] = M[i];
    }
  }
  
  MutableMatrix44D(double m11, double m12, double m13, double m14,
                   double m21, double m22, double m23, double m24,
                   double m31, double m32, double m33, double m34,
                   double m41, double m42, double m43, double m44) {
    _m[0]  = m11;
    _m[1]  = m12;
    _m[2]  = m13;
    _m[3]  = m14;
    
    _m[4]  = m21;
    _m[5]  = m22;
    _m[6]  = m23;
    _m[7]  = m24;
    
    _m[8]  = m31;
    _m[9]  = m32;
    _m[10] = m33;
    _m[11] = m34;
    
    _m[12] = m41;
    _m[13] = m42;
    _m[14] = m43;
    _m[15] = m44;
  }
  
  MutableMatrix44D multiply(const MutableMatrix44D& m) const;
  
  MutableMatrix44D inversed() const;
  
  MutableMatrix44D transposed() const;
  
  double get(int i) const {
    return _m[i];
  }
  
  //const double * getMatrix() const { return _m;}
  
  void copyToFloatMatrix(float M[16]) const { 
//    for (int i = 0; i < 16; i++) {
//      M[i] = (float) _m[i];
//    }
    M[ 0] = (float) _m[ 0];
    M[ 1] = (float) _m[ 1];
    M[ 2] = (float) _m[ 2];
    M[ 3] = (float) _m[ 3];
    M[ 4] = (float) _m[ 4];
    M[ 5] = (float) _m[ 5];
    M[ 6] = (float) _m[ 6];
    M[ 7] = (float) _m[ 7];
    M[ 8] = (float) _m[ 8];
    M[ 9] = (float) _m[ 9];
    M[10] = (float) _m[10];
    M[11] = (float) _m[11];
    M[12] = (float) _m[12];
    M[13] = (float) _m[13];
    M[14] = (float) _m[14];
    M[15] = (float) _m[15];
  }
  
  void print(const std::string& name, const ILogger* log) const;
  
  Vector3D unproject(const Vector3D& pixel3D, const int viewport[4]) const;
  
  Vector2D project(const Vector3D& point, const int viewport[4]) const;
  
  static MutableMatrix44D createTranslationMatrix(const Vector3D& t);
  
  static MutableMatrix44D createRotationMatrix(const Angle& angle, const Vector3D& p);
  
  static MutableMatrix44D createGeneralRotationMatrix(const Angle& angle, 
                                                      const Vector3D& axis, const Vector3D& point);
  
  static MutableMatrix44D createModelMatrix(const MutableVector3D& pos, 
                                            const MutableVector3D& center,
                                            const MutableVector3D& up);
  
  static MutableMatrix44D createProjectionMatrix(double left, double right,
                                                 double bottom, double top,
                                                 double znear, double zfar);
  
  static MutableMatrix44D createProjectionMatrix(const FrustumData& data);
  
};


#endif
