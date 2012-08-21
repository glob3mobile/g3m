//
//  Geodetic3D.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 31/05/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#ifndef G3MiOSSDK_Geodetic3D_hpp
#define G3MiOSSDK_Geodetic3D_hpp

#include "Angle.hpp"
#include "Geodetic2D.hpp"


/**
 * Class to represent a position in the globe by latitude, longitud and altitude.
 */
class Geodetic3D {
private:
  const Angle _latitude;
  const Angle _longitude;
  const double _height;
  
public:
  
  static Geodetic3D nan() {
    return Geodetic3D(Angle::nan(), Angle::nan(), 0);
  }
  
  bool isNan() const{
    return _latitude.isNan() || _longitude.isNan();
  }
  
  static Geodetic3D zero() {
    return Geodetic3D(Angle::zero(), Angle::zero(), 0);
  }
  
  static Geodetic3D fromDegrees(double lat, double lon, double height) {
    return Geodetic3D(Angle::fromDegrees(lat), Angle::fromDegrees(lon), height);
  }
  
  Geodetic3D(const Angle& latitude,
             const Angle& longitude,
             const double height): _latitude(latitude), _longitude(longitude), _height(height) {
  }
  
  Geodetic3D(const Geodetic2D& g2,
             const double height): _latitude(g2.latitude()), _longitude(g2.longitude()), _height(height) {
  }
  
  Geodetic3D(const Geodetic3D& g): _latitude(g._latitude), _longitude(g._longitude), _height(g._height) {
  }
  
  const Angle latitude() const {
    return _latitude;
  }
  
  const Angle longitude() const {
    return _longitude;
  }
  
  double height() const {
    return _height;
  }
  
  Geodetic2D asGeodetic2D() const {
    return Geodetic2D(_latitude, _longitude);
  }

  const std::string description() const;

};



#endif
