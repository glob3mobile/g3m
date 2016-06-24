//
//  MutableVector2F.cpp
//  G3MiOSSDK
//
<<<<<<< HEAD
//  Created by Diego Gomez Deck on 10/23/14.
=======
//  Created by Diego Gomez Deck on 2/25/15.
>>>>>>> 882166c33bdf9946c54ea507ad5e1c47fb3e83e0
//
//

#include "MutableVector2F.hpp"

#include "Vector2F.hpp"

<<<<<<< HEAD
Vector2F MutableVector2F::asVector2F() const {
  return Vector2F(_x, _y);
}
=======
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
>>>>>>> 882166c33bdf9946c54ea507ad5e1c47fb3e83e0
