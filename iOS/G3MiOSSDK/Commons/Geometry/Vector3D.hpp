//
//  Vector3D.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 31/05/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#ifndef G3MiOSSDK_Vector3D_hpp
#define G3MiOSSDK_Vector3D_hpp

#include "IMathUtils.hpp"

#include "MutableMatrix44D.hpp"

class MutableVector3D;

class Vector3D {
private:
  
  Vector3D& operator=(const Vector3D& that);
  
public:
  const double _x;
  const double _y;
  const double _z;
  
  
  Vector3D(const double x,
           const double y,
           const double z): _x(x), _y(y), _z(z) {
    
  }
  
  ~Vector3D() {}
  
  Vector3D(const Vector3D &v): _x(v._x), _y(v._y), _z(v._z) {
    
  }
  
  static Vector3D nan() {
    return Vector3D(GMath.NanD(), GMath.NanD(), GMath.NanD());
  }
  
  static Vector3D zero() {
    return Vector3D(0.0, 0.0, 0.0);
  }
  
  bool isNan() const {
    return (GMath.isNan(_x) || GMath.isNan(_y) || GMath.isNan(_z));
  }
  
  bool isZero() const {
    return (_x == 0) && (_y == 0) && (_z == 0);
  }
  
  Vector3D normalized() const;
  
  double length() const {
    return GMath.sqrt(squaredLength());
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
  
  Angle angleBetween(const Vector3D& other) const;
  
  Vector3D rotateAroundAxis(const Vector3D& axis,
                            const Angle& theta) const;
  
  //  double x() const {
  //    return _x;
  //  }
  //
  //  double y() const {
  //    return _y;
  //  }
  //
  //  double z() const {
  //    return _z;
  //  }
  
  Vector3D transformedBy(const MutableMatrix44D &m, const double homogeneus) const;
  
  MutableVector3D asMutableVector3D() const;
  
  double maxAxis() const;
  
  Vector3D projectionInPlane(const Vector3D& normal) const;
  
  const std::string description() const;
  
};


#endif
