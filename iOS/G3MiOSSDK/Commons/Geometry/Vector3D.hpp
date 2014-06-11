//
//  Vector3D.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 31/05/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#ifndef G3MiOSSDK_Vector3D
#define G3MiOSSDK_Vector3D

#include "IMathUtils.hpp"

#include "MutableMatrix44D.hpp"

class MutableVector3D;

class Vector3D {
private:
  
  Vector3D& operator=(const Vector3D& that);
  
public:

  static Vector3D zero;

  const double _x;
  const double _y;
  const double _z;
  
  
  Vector3D(const double x,
           const double y,
           const double z): _x(x), _y(y), _z(z) {
    
  }
  
  ~Vector3D() {
  }
  
  Vector3D(const Vector3D &v): _x(v._x), _y(v._y), _z(v._z) {
    
  }
  
  static Vector3D nan() {
    return Vector3D(NAND,
                    NAND,
                    NAND);
  }
  
//  static Vector3D zero() {
//    return Vector3D(0, 0, 0);
//  }

  static Vector3D upX() {
    return Vector3D(1,0,0);
  }

  static Vector3D downX() {
    return Vector3D(-1,0,0);
  }
  
  static Vector3D upY() {
    return Vector3D(0,1,0);
  }

  static Vector3D downY() {
    return Vector3D(0,-1,0);
  }

  static Vector3D upZ() {
    return Vector3D(0,0,1);
  }

  static Vector3D downZ() {
    return Vector3D(0,0,-1);
  }

  bool isNan() const {
    return (ISNAN(_x) ||
            ISNAN(_y) ||
            ISNAN(_z));
  }

  bool isEquals(const Vector3D& v) const {
    return (v._x == _x &&
            v._y == _y &&
            v._z == _z);
  }

  bool isZero() const {
    return (_x == 0) && (_y == 0) && (_z == 0);
  }
  
  Vector3D normalized() const;
  
  double length() const {
    return IMathUtils::instance()->sqrt(squaredLength());
  }
  
  double squaredLength() const {
    return _x * _x + _y * _y + _z * _z;
  }
  
  double dot(const Vector3D& v) const {
    return _x * v._x + _y * v._y + _z * v._z;
  }

  bool isPerpendicularTo(const Vector3D& v) const{
    return IMathUtils::instance()->abs(_x * v._x + _y * v._y + _z * v._z) < 0.00001;
  }
  
  Vector3D add(const Vector3D& v) const {
    return Vector3D(_x + v._x,
                    _y + v._y,
                    _z + v._z);
  }

  Vector3D add(double d) const {
    return Vector3D(_x + d,
                    _y + d,
                    _z + d);
  }

  Vector3D sub(const Vector3D& v) const {
    return Vector3D(_x - v._x,
                    _y - v._y,
                    _z - v._z);
  }

  Vector3D sub(double d) const {
    return Vector3D(_x - d,
                    _y - d,
                    _z - d);
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
  double angleInRadiansBetween(const Vector3D& other) const;
  Angle signedAngleBetween(const Vector3D& other, const Vector3D& up) const;
  
  Vector3D rotateAroundAxis(const Vector3D& axis,
                            const Angle& theta) const;

  Vector3D transformedBy(const MutableMatrix44D &m, const double homogeneus) const;
  
  MutableVector3D asMutableVector3D() const;
  
  double maxAxis() const;
  double minAxis() const;
  
  double axisAverage() const;
  
  Vector3D projectionInPlane(const Vector3D& normal) const;
  
  const std::string description() const;
#ifdef JAVA_CODE
  @Override
  public String toString() {
    return description();
  }
#endif

  const Vector3D clamp(const Vector3D& min,
                       const Vector3D& max) const;

  const double squaredDistanceTo(const Vector3D& that) const;

  const double distanceTo(const Vector3D& that) const;

};


#endif
