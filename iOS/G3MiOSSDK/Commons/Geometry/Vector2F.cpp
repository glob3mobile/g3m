//
//  Vector2F.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/9/13.
//
//

#include "Vector2F.hpp"

#include "Vector2I.hpp"

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
