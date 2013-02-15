//
//  Vector3F.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/9/13.
//
//

#include "Vector3F.hpp"


Vector3F Vector3F::normalized() const {
  const double d = length();
  return Vector3F((float) (_x / d),
                  (float) (_y / d),
                  (float) (_z / d));
}
