//
//  Angle.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 31/05/12.
//

#include "Angle.hpp"

#include "IStringBuilder.hpp"
#include "IMathUtils.hpp"

Angle Angle::fromDegreesMinutes(double degrees,
                                double minutes) {
  const IMathUtils* mu = IMathUtils::instance();
  const double sign = (degrees * minutes) < 0 ? -1.0 : 1.0;
  const double d = sign * ( mu->abs(degrees) + ( mu->abs(minutes) / 60.0) );
  return Angle( d, TO_RADIANS(d) );
}

Angle Angle::fromDegreesMinutesSeconds(double degrees,
                                       double minutes,
                                       double seconds) {
  const IMathUtils* mu = IMathUtils::instance();
  const double sign = (degrees * minutes * seconds) < 0 ? -1.0 : 1.0;
  const double d = sign * ( mu->abs(degrees) + ( mu->abs(minutes) / 60.0) + ( mu->abs(seconds) / 3600.0 ) );
  return Angle( d, TO_RADIANS(d) );
}

Angle Angle::nan() {
  return Angle::fromDegrees(NAND);
}

bool Angle::isEquals(const Angle& that) const {
  const IMathUtils* mu = IMathUtils::instance();
  return mu->isEquals(_degrees, that._degrees) || mu->isEquals(_radians, that._radians);
}

bool Angle::isNan() const {
  return ISNAN(_degrees);
}

double Angle::tangent() const {
  return TAN(_radians);
}

bool Angle::closeTo(const Angle& other) const {
  return (IMathUtils::instance()->abs(_degrees - other._degrees) < THRESHOLD);
}


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

Angle Angle::cosineInterpolation(const Angle& from,
                                 const Angle& to,
                                 double alpha) {
  return Angle::fromRadians( IMathUtils::instance()->cosineInterpolation(from._radians,
                                                                         to._radians,
                                                                         alpha) );
}
