//
//  Angle.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 31/05/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#ifndef G3MiOSSDK_Angle
#define G3MiOSSDK_Angle

#define THRESHOLD               1e-5

#include "IMathUtils.hpp"

#include <string>

#define TO_RADIANS(degrees) ((degrees) / 180.0 * 3.14159265358979323846264338327950288)
#define TO_DEGREES(radians) ((radians) * (180.0 / 3.14159265358979323846264338327950288))


class Angle {
private:
//  mutable double _sin;
//  mutable double _cos;

  Angle(const double degrees,
        const double radians) :
  _degrees( degrees ),
  _radians( radians )
//  _sin(2),
//  _cos(2)
  {
  }


public:
  const double _degrees;
  const double _radians;


  Angle(const Angle& angle):
  _degrees(angle._degrees),
  _radians(angle._radians)
//  _sin(angle._sin),
//  _cos(angle._cos)
  {

  }

  static Angle fromDegrees(double degrees) {
    return Angle(degrees,
                 TO_RADIANS(degrees));
  }

  static Angle fromDegreesMinutes(double degrees,
                                  double minutes) {
    const IMathUtils* mu = IMathUtils::instance();
    const double sign = (degrees * minutes) < 0 ? -1.0 : 1.0;
    const double d = sign * ( mu->abs(degrees) + ( mu->abs(minutes) / 60.0) );
    return Angle( d, TO_RADIANS(d) );
  }

  static Angle fromDegreesMinutesSeconds(double degrees,
                                         double minutes,
                                         double seconds) {
    const IMathUtils* mu = IMathUtils::instance();
    const double sign = (degrees * minutes * seconds) < 0 ? -1.0 : 1.0;
    const double d = sign * ( mu->abs(degrees) + ( mu->abs(minutes) / 60.0) + ( mu->abs(seconds) / 3600.0 ) );
    return Angle( d, TO_RADIANS(d) );
  }

  static Angle fromRadians(double radians) {
    return Angle(TO_DEGREES(radians), radians);
  }

  static Angle min(const Angle& a1,
                   const Angle& a2) {
    return (a1._degrees < a2._degrees) ? a1 : a2;
  }

  static Angle max(const Angle& a1,
                   const Angle& a2) {
    return (a1._degrees > a2._degrees) ? a1 : a2;
  }

  static Angle zero() {
    return Angle::fromDegrees(0);
  }

  static Angle pi() {
    return Angle::fromDegrees(180);
  }
  
  static Angle nan() {
    return Angle::fromDegrees(NAND);
  }

  static Angle midAngle(const Angle& angle1, const Angle& angle2) {
    return Angle::fromRadians((angle1._radians + angle2._radians) / 2);
  }

  static Angle linearInterpolation(const Angle& from,
                                   const Angle& to,
                                   double alpha) {
    return Angle::fromRadians( (1.0-alpha) * from._radians + alpha * to._radians );
  }

  bool isNan() const {
    return ISNAN(_degrees);
  }

//  double sinus() const {
////    if (_sin > 1) {
////      _sin = SIN(_radians);
////    }
////    return _sin;
//    return SIN(_radians);
//  }
//
//  double cosinus() const {
////    if (_cos > 1) {
////      _cos = COS(_radians);
////    }
////    return _cos;
//    return COS(_radians);
//  }

  double tangent() const {
    return TAN(_radians);
  }

  bool closeTo(const Angle& other) const {
    return (IMathUtils::instance()->abs(_degrees - other._degrees) < THRESHOLD);
  }

  Angle add(const Angle& a) const {
    const double r = _radians + a._radians;
    return Angle(TO_DEGREES(r), r);
  }

  Angle sub(const Angle& a) const {
    const double r = _radians - a._radians;
    return Angle(TO_DEGREES(r), r);
  }

  Angle times(double k) const {
    const double r = k * _radians;
    return Angle(TO_DEGREES(r), r);
  }

  Angle div(double k) const {
    const double r = _radians / k;
    return Angle(TO_DEGREES(r), r);
  }

  double div(const Angle& k) const {
    return _radians / k._radians;
  }

  bool greaterThan(const Angle& a) const {
    return (_radians > a._radians);
  }

  bool lowerThan(const Angle& a) const {
    return (_radians < a._radians);
  }

  Angle clampedTo(const Angle& min,
                  const Angle& max) const;

  bool isBetween(const Angle& min,
                 const Angle& max) const;

  Angle nearestAngleInInterval(const Angle& min, const Angle& max) const;

  Angle distanceTo(const Angle& other) const;

  Angle normalized() const {
    double degrees = _degrees;
    while (degrees < 0) {
      degrees += 360;
    }
    while (degrees >= 360) {
      degrees -= 360;
    }
    return Angle(degrees, TO_RADIANS(degrees));
  }

  bool isZero() const {
    return (_degrees == 0);
  }

  bool isEquals(const Angle& that) const {
    const IMathUtils* mu = IMathUtils::instance();
    return mu->isEquals(_degrees, that._degrees) || mu->isEquals(_radians, that._radians);
  }

#ifdef JAVA_CODE
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    long temp;
    temp = Double.doubleToLongBits(_radians);
    result = (prime * result) + (int) (temp ^ (temp >>> 32));
    return result;
  }


  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Angle other = (Angle) obj;
    if (Double.doubleToLongBits(_radians) != Double.doubleToLongBits(other._radians)) {
      return false;
    }
    return true;
  }
#endif

  ~Angle() {

  }

  const std::string description() const;

#ifdef JAVA_CODE
  @Override
  public String toString() {
    return description();
  }
#endif

};

#endif
