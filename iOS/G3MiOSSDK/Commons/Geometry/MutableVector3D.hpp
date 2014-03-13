//
//  MutableVector3D.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 31/05/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#ifndef G3MiOSSDK_MutableVector3D
#define G3MiOSSDK_MutableVector3D

#include "IMathUtils.hpp"

#include "MutableMatrix44D.hpp"

#include <stdio.h>
#include "Vector3D.hpp"
//class Vector3D;

class MutableVector3D {
private:
  double _x;
  double _y;
  double _z;

public:

  MutableVector3D(): _x(0), _y(0), _z(0) {}

  MutableVector3D(const double x,
                  const double y,
                  const double z): _x(x), _y(y), _z(z) {

  }

  MutableVector3D(const MutableVector3D &v): _x(v._x), _y(v._y), _z(v._z) {

  }

  MutableVector3D normalized() const;

  static MutableVector3D nan() {
    return MutableVector3D(NAND, NAND, NAND);
  }

  bool equalTo(const MutableVector3D& v) const{
    return (v._x == _x && v._y == _y && v._z == _z);
  }

  bool isNan() const {
    return (ISNAN(_x) ||
            ISNAN(_y) ||
            ISNAN(_z));
  }

  bool isZero() const {
    return (_x == 0) && (_y == 0) && (_z == 0);
  }

  double length() const {
    return IMathUtils::instance()->sqrt(squaredLength());
  }

  double squaredLength() const {
    return _x * _x + _y * _y + _z * _z;
  }

  double dot(const MutableVector3D& v) const {
    return _x * v._x + _y * v._y + _z * v._z;
  }

  MutableVector3D add(const MutableVector3D& v) const {
    return MutableVector3D(_x + v._x,
                           _y + v._y,
                           _z + v._z);
  }

  MutableVector3D sub(const MutableVector3D& v) const {
    return MutableVector3D(_x - v._x,
                           _y - v._y,
                           _z - v._z);
  }

  Vector3D sub(const Vector3D& v) const {
    return Vector3D(_x - v._x,
                    _y - v._y,
                    _z - v._z);
  }

  MutableVector3D times(const MutableVector3D& v) const {
    return MutableVector3D(_x * v._x,
                           _y * v._y,
                           _z * v._z);
  }

  MutableVector3D times(const Vector3D& v) const {
    return MutableVector3D(_x * v._x,
                           _y * v._y,
                           _z * v._z);
  }

  MutableVector3D times(const double magnitude) const {
    return MutableVector3D(_x * magnitude,
                           _y * magnitude,
                           _z * magnitude);
  }

  MutableVector3D div(const MutableVector3D& v) const {
    return MutableVector3D(_x / v._x,
                           _y / v._y,
                           _z / v._z);
  }

  MutableVector3D div(const double v) const {
    return MutableVector3D(_x / v,
                           _y / v,
                           _z / v);
  }

  MutableVector3D cross(const MutableVector3D& other) const {
    return MutableVector3D(_y * other._z - _z * other._y,
                           _z * other._x - _x * other._z,
                           _x * other._y - _y * other._x);
  }

  Angle angleBetween(const MutableVector3D& other) const;

  MutableVector3D rotatedAroundAxis(const MutableVector3D& other,
                                    const Angle& theta) const;

  double x() const {
    return _x;
  }

  double y() const {
    return _y;
  }

  double z() const {
    return _z;
  }

  MutableVector3D transformedBy(const MutableMatrix44D &m,
                                const double homogeneus) const {
    return MutableVector3D(_x * m.get0() + _y * m.get4() + _z * m.get8() + homogeneus * m.get12(),
                           _x * m.get1() + _y * m.get5() + _z * m.get9() + homogeneus * m.get13(),
                           _x * m.get2() + _y * m.get6() + _z * m.get10() + homogeneus * m.get14());
  }

  Vector3D asVector3D() const {
    return Vector3D(_x, _y, _z);
  }

};


#endif
