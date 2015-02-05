//
//  Angle.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 31/05/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#include "Angle.hpp"

#include "IStringBuilder.hpp"

Angle Angle::clampedTo(const Angle& min,
                       const Angle& max) const {
  if (_radians < min._radians) {
    return min;
  }
  
  if (_radians > max._radians) {
    return max;
  }
  
  return *this;
}

bool Angle::isBetween(const Angle& min,
                      const Angle& max) const {
  return (_radians >= min._radians) && (_radians <= max._radians);
}


Angle Angle::distanceTo(const Angle& other) const {
  double dif = IMathUtils::instance()->abs(_degrees - other._degrees);
  if (dif > 180) dif = 360 - dif;
  return Angle::fromDegrees(dif);
}


Angle Angle::nearestAngleInInterval(const Angle& min,
                                    const Angle& max) const {
  // it the interval contains the angle, return this value
  if (greaterThan(min) && lowerThan(max)) {
    return (*this);
  }
  
  // look for the extreme closest to the angle
  const Angle dif0 = distanceTo(min);
  const Angle dif1 = distanceTo(max);
  return (dif0.lowerThan(dif1))? min : max;
}

const std::string Angle::description() const {
  IStringBuilder* isb = IStringBuilder::newStringBuilder();
  isb->addDouble(_degrees);
//  isb->addString("°");
  isb->addString("d");
  const std::string s = isb->getString();
  delete isb;
  return s;
}
