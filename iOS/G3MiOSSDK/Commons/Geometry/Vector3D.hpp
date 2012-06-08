//
//  Vector3D.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 31/05/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#ifndef G3MiOSSDK_Vector3D_hpp
#define G3MiOSSDK_Vector3D_hpp

#include <math.h>

#include "MutableMatrix44D.hpp"

class MutableVector3D;

class Vector3D {
private:
  const double _x;
  const double _y;
  const double _z;
  
public:
  
  Vector3D(const double x,
           const double y,
           const double z): _x(x), _y(y), _z(z) {
    
  }
  
  Vector3D(const Vector3D &v): _x(v._x), _y(v._y), _z(v._z) {
    
  }
  
  static Vector3D nan() {
    return Vector3D(NAN, NAN, NAN);
  }
  
  bool isNan() const {
    return isnan(_x*_y*_z);
  }
  
  Vector3D normalized() const;
    
  double length() const {
    return sqrt(squaredLength());
  }
  
  double squaredLength() const {
    return _x * _x + _y * _y + _z * _z;
  }
  
  double dot(const Vector3D& v) const {
    return _x * v._x + _y * v._y + _z * v._z;
  }
  
  Vector3D add(const Vector3D& v) const {
    return Vector3D(_x + v._x,
                    _y + v._y,
                    _z + v._z);
  }
  
  Vector3D sub(const Vector3D& v) const {
    return Vector3D(_x - v._x,
                    _y - v._y,
                    _z - v._z);
  }
  
  Vector3D times(const Vector3D& v) const {
    return Vector3D(_x * v._x,
                    _y * v._y,
                    _z * v._z);
  }
  
  Vector3D times(const double magnitude) const {
    return Vector3D(_x * magnitude,
                    _y * magnitude,
                    _z * magnitude);
  }
  
  Vector3D div(const Vector3D& v) const {
    return Vector3D(_x / v._x,
                    _y / v._y,
                    _z / v._z);
  }
  
  Vector3D div(const double v) const {
    return Vector3D(_x / v,
                    _y / v,
                    _z / v);
  }
  
  Vector3D cross(const Vector3D& other) const {
    return Vector3D(_y * other._z - _z * other._y,
                    _z * other._x - _x * other._z,
                    _x * other._y - _y * other._x);
  }
  
  double angleBetween(const Vector3D& other) const;
    
  Vector3D rotateAroundAxis(const Vector3D& axis, double theta) const;
  
  double x() const {
    return _x;
  }
  
  double y() const {
    return _y;
  }
  
  double z() const {
    return _z;
  }
  
  Vector3D applyTransform(const MutableMatrix44D &m) const
  {
    const double * M = m.getMatrix();
    
    Vector3D v(_x * M[0] + _y * M[4] + _z * M[8] + M[12],
             _x * M[1] + _y * M[5] + _z * M[9] + M[13],
             _x * M[2] + _y * M[6] + _z * M[10] + M[14]);
    
    return v;
  }
  
  MutableVector3D asMutableVector3D() const;
  
};


#endif
