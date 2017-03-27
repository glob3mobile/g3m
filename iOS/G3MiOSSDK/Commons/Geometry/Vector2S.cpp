//
//  Vector2S.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 16/2/16.
//
//

#include "Vector2S.hpp"

#include "Vector2I.hpp"

Vector2I Vector2S::asVector2I() const {
  return Vector2I(_x,_y);
}
