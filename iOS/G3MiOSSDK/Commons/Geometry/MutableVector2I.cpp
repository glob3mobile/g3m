//
//  MutableVector2I.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/20/12.
//
//

#include "MutableVector2I.hpp"
#include "Vector2I.hpp"

 Vector2I MutableVector2I::asVector2I() const {
 return Vector2I(_x, _y);
 }


