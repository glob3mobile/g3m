//
//  Vector2I.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/19/12.
//
//


#include "Vector2I.hpp"

#include "IMathUtils.hpp"
#include "Angle.hpp"
#include "MutableVector2I.hpp"
#include "IStringBuilder.hpp"
#include "Vector2S.hpp"


Vector2I Vector2I::div(double v) const {
  const IMathUtils* mu = IMathUtils::instance();
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

bool Vector2I::isEquals(const Vector2S& that) const {
  return ((_x == that._x) && (_y == that._y));
}

MutableVector2I Vector2I::asMutableVector2I() const {
  return MutableVector2I(_x, _y);
}

const std::string Vector2I::description() const {
  IStringBuilder* isb = IStringBuilder::newStringBuilder();
  isb->addString("(V2I ");
  isb->addDouble(_x);
  isb->addString(", ");
  isb->addDouble(_y);
  isb->addString(")");
  const std::string s = isb->getString();
  delete isb;
  return s;
}
