//
//  MutableVector2F.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/25/15.
//
//

#include "MutableVector2F.hpp"

#include "Vector2F.hpp"

MutableVector2F::MutableVector2F(const Vector2F& that) :
_x(that._x),
_y(that._y)
{

}

Vector2F MutableVector2F::asVector2F() const {
  return Vector2F(_x, _y);
}

MutableVector2F MutableVector2F::nan() {
  return MutableVector2F(NANF, NANF);
}

bool MutableVector2F::isNan() const {
  return (ISNAN(_x) ||
          ISNAN(_y));
}
