//
//  Angle.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 31/05/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#define PI        3.14159265358979323846
#define THRESHOLD 1e-5

#include <math.h>

class Angle {
private:
  const double _degrees;
  
  Angle(const double degrees) : _degrees(degrees) {
    
  }
  
public:
  
  static Angle fromDegrees(const double degrees) {
    return Angle(degrees);
  }
  
  static Angle fromRadians(const double radians) {
    return Angle::fromDegrees(radians * (180.0 / PI));
  }
  
  static Angle zero() {
    return Angle::fromDegrees(0);
  }
  
  Angle(const Angle& angle): _degrees(angle._degrees) {
    
  }
  
  double sinus() {
    return sin(_degrees / 180.0 * PI);
  }
  
  double cosinus() {
    return cos(_degrees / 180.0 * PI);
  }
  
  double degrees() {
    return _degrees;
  }
  
  double radians() {
    return _degrees / 180.0 * PI;
  }
  
  Angle clampedTo(const Angle& min,
                  const Angle& max) const;
  
  bool closeTo(const Angle& other) const {
    return (fabs(_degrees - other._degrees) < THRESHOLD);
  }
  
};
