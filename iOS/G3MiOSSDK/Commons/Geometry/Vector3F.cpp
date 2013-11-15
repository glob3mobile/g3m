//
//  Vector3F.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/9/13.
//
//

#include "Vector3F.hpp"

#include "IMathUtils.hpp"

Vector3F::Vector3F(const float x,
                   const float y,
                   const float z) :
_x(x),
_y(y),
_z(z)
{
}

Vector3F::Vector3F(const Vector3F& that) :
_x(that._x),
_y(that._y),
_z(that._z)
{
}

Vector3F::~Vector3F() {
}


float Vector3F::dot(const Vector3F& v) const {
  return ((_x * v._x) +
          (_y * v._y) +
          (_z * v._z));
}

double Vector3F::length() const {
  return IMathUtils::instance()->sqrt(squaredLength());
}

double Vector3F::squaredLength() const {
  return _x * _x + _y * _y + _z * _z;
}

Vector3F Vector3F::normalized() const {
  const double d = length();
  return Vector3F((float) (_x / d),
                  (float) (_y / d),
                  (float) (_z / d));
}


Vector3F Vector3F::sub(const Vector3F& that) const {
  return Vector3F(_x - that._x,
                  _y - that._y,
                  _z - that._z);
}

Vector3F Vector3F::cross(const Vector3F& other) const {
  return Vector3F(_y * other._z - _z * other._y,
                  _z * other._x - _x * other._z,
                  _x * other._y - _y * other._x);
}
