//
//  Vector3F.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 2/9/13.
//
//

#include "Vector3F.hpp"

#include "IMathUtils.hpp"


double Vector3F::length() const {
  return IMathUtils::instance()->sqrt(squaredLength());
}

bool Vector3F::isNan() const {
  return (ISNAN(_x) ||
          ISNAN(_y) ||
          ISNAN(_z));
}
