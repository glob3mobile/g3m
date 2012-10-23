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
  IMathUtils* math = IMathUtils::instance();
  return Vector2I(math->toInt(_x / v),
                  math->toInt(_y / v) );
}

double Vector2I::length() const {
  return GMath.sqrt(squaredLength());
}

Angle Vector2I::orientation() const {
  return Angle::fromRadians(GMath.atan2((double) _y, (double) _x));
}
