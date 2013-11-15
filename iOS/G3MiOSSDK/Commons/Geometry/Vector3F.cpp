//
//  Vector3F.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/9/13.
//
//

#include "Vector3F.hpp"

#include "IMathUtils.hpp"

double Vector3F::length() const {
  return IMathUtils::instance()->sqrt(squaredLength());
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
