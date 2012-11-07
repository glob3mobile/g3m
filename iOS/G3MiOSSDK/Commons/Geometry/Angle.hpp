//
//  Angle.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 31/05/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#ifndef G3MiOSSDK_Angle_hpp
#define G3MiOSSDK_Angle_hpp

#include "IMathUtils.hpp"

#define THRESHOLD               1e-5
#define ISBETWEEN_THRESHOLD     1e-2

#include <string>


class Angle {
private:
  Angle(const double degrees) :
  _degrees( degrees ),
  _radians( degrees / 180.0 * /*GMath.pi()*/ 3.14159265358979323846264338327950288 )
  {
  }
  
public:
  const double _degrees;
  const double _radians;

  static Angle lerp(const Angle& start,
                    const Angle& end,
                    float percent) {
    return start.add(end.sub(start).times(percent));
  }
  
  static Angle fromDegrees(double degrees) {
    return Angle(degrees);
  }
  
  static Angle fromDegreesMinutes(double degrees,
                                  double minutes) {
    return Angle( degrees + ( minutes / 60.0) );
  }
  
  static Angle fromDegreesMinutesSeconds(double degrees,
                                         double minutes,
                                         double seconds) {
    return Angle( degrees + ( minutes / 60.0) + ( seconds / 3600.0 ) );
  }
  
  static Angle fromRadians(double radians) {
    return Angle::fromDegrees(radians / GMath.pi() * 180.0);
  }
  
  static Angle min(const Angle& a1,
                   const Angle& a2) {
    //    if (a1._degrees < a2._degrees) return a1;
    //    else return a2;
    
    return (a1._degrees < a2._degrees) ? a1 : a2;
  }
  
  static Angle max(const Angle& a1,
                   const Angle& a2) {
//    if (a1._degrees > a2._degrees) return a1;
//    else return a2;
    return (a1._degrees > a2._degrees) ? a1 : a2;
  }
  
  static Angle zero() {
    return Angle::fromDegrees(0);
  }
  
  static Angle nan() {
    return Angle::fromDegrees(GMath.NanD());
  }
  
  static Angle midAngle(const Angle& angle1, const Angle& angle2) {
    return Angle::fromDegrees((angle1._degrees + angle2._degrees) / 2);
  }
  
  static Angle interpolation(const Angle& angle1, const Angle& angle2, double v) {
    return Angle::fromDegrees( (1.0-v) * angle1._degrees + v * angle2._degrees );
  }

  bool isNan() const {
    return IMathUtils::instance()->isNan(_degrees);
  }
  
  Angle(const Angle& angle):
  _degrees(angle._degrees),
  _radians(angle._radians)
  {
    
  }
  
  double sinus() const {
    return GMath.sin( _radians );
  }
  
  double cosinus() const {
    return GMath.cos( _radians );
  }
  
  double degrees() const {
    return _degrees;
  }

  double radians() const {
    return _radians;
  }

  bool closeTo(const Angle& other) const {
    return (GMath.abs(_degrees - other._degrees) < THRESHOLD);
  }
    
  Angle add(const Angle& a) const {
    return Angle(_degrees + a._degrees);
  }
  
  Angle sub(const Angle& a) const {
    return Angle(_degrees - a._degrees);
  }
  
  Angle times(double k) const {
    return Angle(k * _degrees);
  }
  
  Angle div(double k) const {
    return Angle(_degrees / k);
  }
  
  double div(const Angle& k) const {
    return _degrees / k._degrees;
  }

  bool greaterThan(const Angle& a) const {
    return (_degrees > a._degrees);
  }
  
  bool lowerThan(const Angle& a) const {
    return (_degrees < a._degrees);
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
    return Angle(degrees);
  }

  bool isZero() const {
    return (_degrees == 0);
  }
  
#ifdef JAVA_CODE
  @Override
	public int hashCode() {
		return Double.toString(_degrees).hashCode();
	}
  
	@Override
	public boolean equals(Object obj) {
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
        if (_degrees != other._degrees) {
            return false;
        }
        return true;
	}
#endif

  ~Angle() {

  }

  const std::string description() const;
};

#endif
