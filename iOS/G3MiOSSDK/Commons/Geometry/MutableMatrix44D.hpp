//
//  MutableMatrix44D.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 05/09/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_MutableMatrix44D_hpp
#define G3MiOSSDK_MutableMatrix44D_hpp

class FrustumData;
class Vector3D;
class Vector2D;
class MutableVector3D;

#include "Angle.hpp"

#include "ILogger.hpp"
#include <string>

#include "MutableMatrix44D.hpp"


class MutableMatrix44D{
  
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
  
  //Contructor parameters in column major order
  MutableMatrix44D(double m00, double m10, double m20, double m30,
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
  
public:
 
  //CONTRUCTORS
  MutableMatrix44D() {
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
  
  MutableMatrix44D(const MutableMatrix44D &m){
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
  
  static MutableMatrix44D identity() {
    return MutableMatrix44D(1, 0, 0, 0,
                            0, 1, 0, 0,
                            0, 0, 1, 0,
                            0, 0, 0, 1);
  }
  
  static MutableMatrix44D invalid() {
    return MutableMatrix44D(GMath.NanD(), GMath.NanD(), GMath.NanD(), GMath.NanD(),
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
  
  MutableMatrix44D multiply(const MutableMatrix44D& that) const;
  
  MutableMatrix44D inversed() const;
  
  MutableMatrix44D transposed() const;
  
  //METHODS TO EXTRACT VALUES FROM THE MATRIX
  
  //Returns values from 0..15 in column mayor order
  double get(int i) const {
    switch (i) {
      case 0:
        return _m00;
      case 1:
        return _m10;
      case 2:
        return _m20;
      case 3:
        return _m30;
      case 4:
        return _m01;
      case 5:
        return _m11;
      case 6:
        return _m21;
      case 7:
        return _m31;
      case 8:
        return _m02;
      case 9:
        return _m12;
      case 10:
        return _m22;
      case 11:
        return _m32;
      case 12:
        return _m03;
      case 13:
        return _m13;
      case 14:
        return _m23;
      case 15:
        return _m33;
      default:
        ILogger::instance()->logError("Accesing MutableMutableMatrix44D44D out of index");
        return 0;
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
  
  //OTHER OPERATIONS
  
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
  
  static MutableMatrix44D createOrthographicProjectionMatrix(double left, double right,
                                                             double bottom, double top,
                                                             double znear, double zfar);
};

#endif
