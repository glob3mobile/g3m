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

#define THRESHOLD     1e-5



class Angle {
private:
  const double _degrees;
  
  Angle(const double degrees) : _degrees(degrees) {  }
  
public:
  
  static Angle fromDegrees(const double degrees) {
    return Angle(degrees);
  }
  
  static Angle fromRadians(const double radians) {
    return Angle::fromDegrees(radians / M_PI * 180.0);
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
  
  bool greaterThan(const Angle& a) const {
    return (_degrees > a._degrees);
  }
  
  bool lowerThan(const Angle& a) const {
    return (_degrees < a._degrees);
  }
  
  Angle clampedTo(const Angle& min,
                  const Angle& max) const;  
  
  Angle average(const double t, const Angle& a) const;
    
  bool isBetween(const Angle& min,
                 const Angle& max) const;
  
};


#endif
