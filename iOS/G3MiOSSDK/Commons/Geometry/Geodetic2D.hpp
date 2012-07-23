//
//  Geodetic2D.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 31/05/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#ifndef G3MiOSSDK_Geodetic2D_hpp
#define G3MiOSSDK_Geodetic2D_hpp

#include "Angle.hpp"

/**
 * Class to represent a position in the globe by latitude, longitud and altitude.
 */
class Geodetic2D {
private:
  const Angle _latitude;
  const Angle _longitude;
  
public:
  
  static Geodetic2D zero() {
    return Geodetic2D(Angle::zero(), Angle::zero());
  }
  static Geodetic2D fromDegrees(double lat, double lon) {
    return Geodetic2D(Angle::fromDegrees(lat), Angle::fromDegrees(lon));
  }
  
  Geodetic2D(const Angle& latitude,
             const Angle& longitude): _latitude(latitude), _longitude(longitude) {
  }
  
  Geodetic2D(const Geodetic2D& g): _latitude(g._latitude), _longitude(g._longitude) {
  }
  
  const Angle latitude() const {
    return _latitude;
  }
  
  const Angle longitude() const {
    return _longitude;
  }
  
  bool closeTo(const Geodetic2D& other) const;
  
  bool isBetween(const Geodetic2D& min, const Geodetic2D& max) const;
  
};


#endif
