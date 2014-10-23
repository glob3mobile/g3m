//
//  MutableVector2F.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/23/14.
//
//

#include "MutableVector2F.hpp"

#include "Vector2F.hpp"

Vector2F MutableVector2F::asVector2F() const {
  return Vector2F(_x, _y);
}
