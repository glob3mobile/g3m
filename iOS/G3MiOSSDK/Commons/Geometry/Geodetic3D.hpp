//
//  Geodetic3D.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 31/05/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#ifndef G3MiOSSDK_Geodetic3D
#define G3MiOSSDK_Geodetic3D

#include "Angle.hpp"
#include "Geodetic2D.hpp"

//Altitude modes taken from KML standard (with the exception of relative to sea floor)
enum AltitudeMode{
  RELATIVE_TO_GROUND,   //Relative to elevation provided by any SurfaceElevationProvider (tipycally PlanetRenderer)
  ABSOLUTE              //Relative to surface of geometrical planet definition (Ellipsoid, sphere, flat...)
};


/**
 * Class to represent a position in the globe by latitude, longitud and altitude.
 */
class Geodetic3D {

public:
  const Angle _latitude;
  const Angle _longitude;
  const double _height;

  
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
  
  static Geodetic3D linearInterpolation(const Geodetic3D& from,
                                        const Geodetic3D& to,
                                        double alpha) {
    return Geodetic3D(Angle::linearInterpolation(from._latitude,  to._latitude,  alpha),
                      Angle::linearInterpolation(from._longitude, to._longitude, alpha),
                      IMathUtils::instance()->linearInterpolation(from._height, to._height, alpha)
                      //((1.0 - alpha) * from._height) + (alpha * to._height)
                      );
  }
  
  Geodetic3D(const Angle& latitude,
             const Angle& longitude,
             const double height) :
  _latitude(latitude),
  _longitude(longitude),
  _height(height)
  {
  }
  
  Geodetic3D(const Geodetic2D& g2,
             const double height):
  _latitude(g2._latitude),
  _longitude(g2._longitude),
  _height(height)
  {
  }
  
  Geodetic3D(const Geodetic3D& g) :
  _latitude(g._latitude),
  _longitude(g._longitude),
  _height(g._height)
  {
  }

  ~Geodetic3D() {
    
  }
  
  Geodetic2D asGeodetic2D() const {
    return Geodetic2D(_latitude, _longitude);
  }
  
  const std::string description() const;
#ifdef JAVA_CODE
  @Override
  public String toString() {
    return description();
  }
#endif


  Geodetic3D add(const Geodetic3D& that) const {
    return Geodetic3D(_latitude.add(that._latitude),
                      _longitude.add(that._longitude),
                      _height + that._height);
  }
  
  Geodetic3D sub(const Geodetic3D& that) const {
    return Geodetic3D(_latitude.sub(that._latitude),
                      _longitude.sub(that._longitude),
                      _height - that._height);
  }
  
  Geodetic3D times(const double magnitude) const {
    return Geodetic3D(_latitude.times(magnitude),
                      _longitude.times(magnitude),
                      _height * magnitude);
  }
  
  Geodetic3D div(const double magnitude) const {
    return Geodetic3D(_latitude.div(magnitude),
                      _longitude.div(magnitude),
                      _height / magnitude);
  }

  bool isEquals(const Geodetic3D& that) const;

};



#endif
