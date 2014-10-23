//
//  Vector2F.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/9/13.
//
//

#include "Vector2F.hpp"

#include "Vector2I.hpp"
#include "MutableVector2F.hpp"

const double Vector2F::squaredDistanceTo(const Vector2F& that) const {
  const double dx = _x - that._x;
  const double dy = _y - that._y;
  return (dx * dx) + (dy * dy);
}

const double Vector2F::squaredDistanceTo(const Vector2I& that) const {
  const double dx = _x - that._x;
  const double dy = _y - that._y;
  return (dx * dx) + (dy * dy);
}

Vector2F Vector2F::add(const Vector2F& that) const {
  return Vector2F(_x + that._x,
                  _y + that._y);
}

Vector2F Vector2F::sub(const Vector2F& that) const {
  return Vector2F(_x - that._x,
                  _y - that._y);
}

Vector2F Vector2F::div(double d) const {
  return Vector2F((float) (_x / d),
                  (float) (_y / d));
}

double Vector2F::squaredLength() const {
  return (double) _x * _x + _y * _y;
}

double Vector2F::length() const {
  return IMathUtils::instance()->sqrt(squaredLength());
}

MutableVector2F Vector2F::asMutableVector2F() const {
  return MutableVector2F(_x, _y);
}
