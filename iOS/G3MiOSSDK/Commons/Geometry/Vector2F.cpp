//
//  Vector2F.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/9/13.
//
//

#include "Vector2F.hpp"

#include "Vector2I.hpp"
#include "IMathUtils.hpp"
#include "IStringBuilder.hpp"


const double Vector2F::squaredDistanceTo(const Vector2F& that) const {
  const double dx = _x - that._x;
  const double dy = _y - that._y;
  return (dx * dx) + (dy * dy);
}

const double Vector2F::squaredDistanceTo(float x, float y) const {
  const double dx = _x - x;
  const double dy = _y - y;
  return (dx * dx) + (dy * dy);
}

const double Vector2F::squaredDistanceTo(const Vector2I& that) const {
  const double dx = _x - that._x;
  const double dy = _y - that._y;
  return (dx * dx) + (dy * dy);
}

Vector2F Vector2F::clampLength(float min, float max) const {
  float length = (float) this->length();
  if (length < min) {
    return times(min / length);
  }
  if (length > max) {
    return times(max / length);
  }
  return *this;
}

Vector2F Vector2F::nan() {
  return Vector2F(NANF, NANF);
}

bool Vector2F::isNan() const {
  return ISNAN(_x) || ISNAN(_y);
}

double Vector2F::length() const {
  return IMathUtils::instance()->sqrt(squaredLength());
}

const std::string Vector2F::description() const {
  IStringBuilder* isb = IStringBuilder::newStringBuilder();
  isb->addString("(V2F ");
  isb->addFloat(_x);
  isb->addString(", ");
  isb->addFloat(_y);
  isb->addString(")");
  const std::string s = isb->getString();
  delete isb;
  return s;
}
