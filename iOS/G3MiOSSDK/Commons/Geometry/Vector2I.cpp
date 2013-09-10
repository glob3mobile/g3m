//
//  Vector2I.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/19/12.
//
//

#include "Vector2I.hpp"
#include "IMathUtils.hpp"

Vector2I Vector2I::div(double v) const {
  IMathUtils* mu = IMathUtils::instance();
  return Vector2I(mu->toInt(_x / v),
                  mu->toInt(_y / v) );
}

double Vector2I::length() const {
  return IMathUtils::instance()->sqrt(squaredLength());
}

Angle Vector2I::orientation() const {
  return Angle::fromRadians(IMathUtils::instance()->atan2((double) _y, (double) _x));
}

bool Vector2I::isEquals(const Vector2I& that) const {
  return ((_x == that._x) && (_y == that._y));
}
