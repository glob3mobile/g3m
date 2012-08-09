//
//  Angle.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 31/05/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#ifndef G3MiOSSDK_Angle_hpp
#define G3MiOSSDK_Angle_hpp

#include <math.h>

#define THRESHOLD               1e-5
#define ISBETWEEN_THRESHOLD     1e-2



class Angle {
private:
  const double _degrees;
  
  Angle(const double degrees) : _degrees(degrees) {  }
  
public:
  static Angle lerp(const Angle& start,
                    const Angle& end,
                    float percent) {
    return start.add(end.sub(start).times(percent));
  }
  
  static Angle fromDegrees(const double degrees) {
    return Angle(degrees);
  }
  
  static Angle fromRadians(const double radians) {
    return Angle::fromDegrees(radians / M_PI * 180.0);
  }
  
  static Angle getMin(const Angle& a1, const Angle& a2) {
    if (a1._degrees < a2._degrees) return a1;
    else return a2;
  }
  
  static Angle getMax(const Angle& a1, const Angle& a2) {
    if (a1._degrees > a2._degrees) return a1;
    else return a2;
  }
  
  static Angle zero() {
    return Angle::fromDegrees(0);
  }
  
  static Angle nan() {
    return Angle::fromDegrees(NAN);
  }
  
  static Angle midAngle(const Angle& angle1, const Angle& angle2) {
    return Angle::fromDegrees((angle1.degrees() + angle2.degrees()) / 2);
  }

  
  bool isNan() const {
    return isnan(_degrees);
  }
  
  Angle(const Angle& angle): _degrees(angle._degrees) {
    
  }
  
  double sinus() const {
    return sin(_degrees / 180.0 * M_PI);
  }
  
  double cosinus() const {
    return cos(_degrees / 180.0 * M_PI);
  }
  
  double degrees() const {
    return _degrees;
  }
  
  double radians() const {
    return _degrees / 180.0 * M_PI;
  }
  
  bool closeTo(const Angle& other) const {
    return (fabs(_degrees - other._degrees) < THRESHOLD);
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
  
#ifdef JAVA_CODE
  @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(_degrees);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}
  
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Angle other = (Angle) obj;
		if (Double.doubleToLongBits(_degrees) != Double
				.doubleToLongBits(other._degrees))
			return false;
		return true;
	}
#endif
  
};

#endif
