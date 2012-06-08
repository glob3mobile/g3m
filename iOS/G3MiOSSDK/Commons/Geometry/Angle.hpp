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
  Angle() : _degrees(0) { }
  
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
  
  bool greaterThan(const Angle& a) const {
    return (_degrees > a._degrees);
  }
  
  bool lowerThan(const Angle& a) const {
    return (_degrees < a._degrees);
  }
  
  Angle clampedTo(const Angle& min,
                  const Angle& max) const;  
  
};


#endif
