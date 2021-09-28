//
//  Angle.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 31/05/12.
//

#include "Angle.hpp"

#include "IStringBuilder.hpp"
#include "IMathUtils.hpp"

const Angle Angle::_ZERO          = Angle::fromRadians(0);
const Angle Angle::_PI            = Angle::fromRadians(PI);      // 180 degrees
const Angle Angle::_HALF_PI       = Angle::fromRadians(PI / 2);  // 90 degrees
const Angle Angle::_TWO_PI        = Angle::fromRadians(PI * 2);  // 360 degrees
const Angle Angle::_MINUS_HALF_PI = Angle::fromRadians(-PI / 2); // -90 degrees


Angle Angle::fromGradians(double gradians) {
  return fromRadians( gradians / 200.0 * PI );
}

Angle Angle::fromDegrees(double degrees) {
  return Angle(degrees,
               TO_RADIANS(degrees));
}

Angle Angle::fromRadians(double radians) {
  return Angle(TO_DEGREES(radians), radians);
}

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

Angle Angle::add(const Angle& a) const {
  const double r = _radians + a._radians;
  return Angle(TO_DEGREES(r), r);
}

Angle Angle::sub(const Angle& a) const {
  const double r = _radians - a._radians;
  return Angle(TO_DEGREES(r), r);
}

Angle Angle::times(double k) const {
  const double r = k * _radians;
  return Angle(TO_DEGREES(r), r);
}

Angle Angle::div(double k) const {
  const double r = _radians / k;
  return Angle(TO_DEGREES(r), r);
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

Angle Angle::normalized() const {
  double degrees = _degrees;
  while (degrees < 0) {
    degrees += 360;
  }
  while (degrees >= 360) {
    degrees -= 360;
  }
  return Angle(degrees, TO_RADIANS(degrees));
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

double Angle::smoothDegrees(double previousDegrees,
                            double degrees) {
  if (ISNAN(previousDegrees)) {
    return degrees;
  }

  const double delta = previousDegrees - degrees;
  if (IMathUtils::instance()->abs(delta) < 180.0) {
    return degrees;
  }

  return (delta < 0.0) ? (degrees - 360.0) : (degrees + 360.0);
}

double Angle::smoothRadians(double previousRadians,
                            double radians) {
  if (ISNAN(previousRadians)) {
    return radians;
  }

  const double delta = previousRadians - radians;
  if (IMathUtils::instance()->abs(delta) < PI) {
    return delta;
  }

  const double pi2 = PI*2;
  return (delta < 0.0) ? (radians - pi2) : (radians + pi2);
}
