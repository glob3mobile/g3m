//
//  Angle.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 31/05/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#include "Angle.hpp"


Angle Angle::clampedTo(const Angle& min,
                       const Angle& max) const {
  if (_degrees < min._degrees) {
    return min;
  }
  
  if (_degrees > max._degrees) {
    return max;
  }
  
  return *this;
}

bool Angle::isBetween(const Angle& min,
                      const Angle& max) const {
  return (_degrees + ISBETWEEN_THRESHOLD >= min._degrees) && (_degrees - ISBETWEEN_THRESHOLD <= max._degrees);
}
