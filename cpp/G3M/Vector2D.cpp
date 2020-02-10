//
//  Vector2D.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 31/05/12.
//

#include "Vector2D.hpp"

#include "MutableVector2D.hpp"
#include "IStringBuilder.hpp"


Vector2D Vector2D::nan() {
  return Vector2D(NAND, NAND);
}

double Vector2D::length() const {
  return IMathUtils::instance()->sqrt(squaredLength());
}

Angle Vector2D::orientation() const {
  return Angle::fromRadians(IMathUtils::instance()->atan2(_y, _x));
}

Angle Vector2D::angle() const {
  double a = IMathUtils::instance()->atan2(_y, _x);
  return Angle::fromRadians(a);
}


MutableVector2D Vector2D::asMutableVector2D() const {
  return MutableVector2D(_x, _y);
}

const std::string Vector2D::description() const {
  IStringBuilder* isb = IStringBuilder::newStringBuilder();
  isb->addString("(V2D ");
  isb->addDouble(_x);
  isb->addString(", ");
  isb->addDouble(_y);
  isb->addString(")");
  const std::string s = isb->getString();
  delete isb;
  return s;
}

Vector2D Vector2D::intersectionOfTwoLines(const Vector2D& p1, const Vector2D& r1,
                                          const Vector2D& p2, const Vector2D& r2) {

  //u = (p2 - p1) × r1 / (r1 × r2)
  //out = p2 + u x r2

  const double u = ((p2.sub(p1)).dot(r1)) / r1.dot(r2);
  Vector2D out = p2.add(r2.times(u));

  return out;
}
