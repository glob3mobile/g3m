//
//  Angle.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 31/05/12.
//

#ifndef G3MiOSSDK_Angle
#define G3MiOSSDK_Angle

#define THRESHOLD               1e-5

#include <string>

class Angle {
private:
  Angle(const double degrees,
        const double radians) :
  _degrees( degrees ),
  _radians( radians )
  {
  }


public:
  static const Angle _ZERO;
  static const Angle _PI;
  static const Angle _HALF_PI;
  static const Angle _TWO_PI;
  static const Angle _MINUS_HALF_PI;

  const double _degrees;
  const double _radians;


  Angle(const Angle& angle):
  _degrees(angle._degrees),
  _radians(angle._radians)
  {
  }

  static Angle fromDegrees(double degrees);

  static Angle fromDegreesMinutes(double degrees,
                                  double minutes);

  static Angle fromDegreesMinutesSeconds(double degrees,
                                         double minutes,
                                         double seconds);

  static Angle fromRadians(double radians);

  static Angle min(const Angle& a1,
                   const Angle& a2) {
    return (a1._degrees < a2._degrees) ? a1 : a2;
  }

  static Angle max(const Angle& a1,
                   const Angle& a2) {
    return (a1._degrees > a2._degrees) ? a1 : a2;
  }

  static Angle zero() {
    return _ZERO;
  }
  
  static Angle pi() {
    return _PI;
  }

  static Angle halfPi() {
    return _HALF_PI;
  }

  static Angle minusHalfPi() {
    return _MINUS_HALF_PI;
  }

  static Angle nan();

  static Angle midAngle(const Angle& angle1,
                        const Angle& angle2) {
    return Angle::fromRadians((angle1._radians + angle2._radians) / 2);
  }

  static Angle linearInterpolation(const Angle& from,
                                   const Angle& to,
                                   double alpha) {
    return Angle::fromRadians( (1.0-alpha) * from._radians + alpha * to._radians );
  }

  static Angle cosineInterpolation(const Angle& from,
                                   const Angle& to,
                                   double alpha);

  static Angle linearInterpolationFromRadians(const double fromRadians,
                                              const double toRadians,
                                              double alpha) {
    return Angle::fromRadians( (1.0-alpha) * fromRadians + alpha * toRadians );
  }

  static Angle linearInterpolationFromDegrees(const double fromDegrees,
                                              const double toDegrees,
                                              double alpha) {
    return Angle::fromDegrees( (1.0-alpha) * fromDegrees + alpha * toDegrees );
  }

  static double smoothDegrees(double previousDegrees,
                              double degrees);

  static double smoothRadians(double previousRadians,
                              double radians);

  bool isNan() const;

  double tangent() const;

  bool closeTo(const Angle& other) const;

  Angle add(const Angle& a) const;

  Angle sub(const Angle& a) const;

  Angle times(double k) const;

  Angle div(double k) const;

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

  Angle normalized() const;

  bool isZero() const {
    return (_degrees == 0);
  }

  bool isEquals(const Angle& that) const;

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
