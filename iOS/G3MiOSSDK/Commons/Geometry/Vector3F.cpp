//
//  Vector3F.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/9/13.
//
//

#include "Vector3F.hpp"

#include "Vector3D.hpp"

Vector3D Vector3F::asVector3D() const{
  return Vector3D(_x, _y, _z);
}
