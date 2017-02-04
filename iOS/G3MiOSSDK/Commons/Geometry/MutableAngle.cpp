//
//  MutableAngle.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/4/17.
//
//

#include "MutableAngle.hpp"

#include "IMathUtils.hpp"
#include "IStringBuilder.hpp"


MutableAngle MutableAngle::fromDegrees(double degrees) {
  return MutableAngle(degrees,
                      TO_RADIANS(degrees));
}

MutableAngle MutableAngle::fromRadians(double radians) {
  return MutableAngle(TO_DEGREES(radians), radians);
}

void MutableAngle::setDegrees(double degrees) {
  _degrees = degrees;
  _radians = TO_RADIANS(degrees);
}

void MutableAngle::setRadians(double radians) {
  _degrees = TO_DEGREES(radians);
  _radians = radians;
}

const std::string MutableAngle::description() const {
  IStringBuilder* isb = IStringBuilder::newStringBuilder();
  isb->addDouble(_degrees);
  isb->addString("d");
  const std::string s = isb->getString();
  delete isb;
  return s;
}
