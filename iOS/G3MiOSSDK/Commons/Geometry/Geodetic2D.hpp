//
//  Geodetic2D.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 31/05/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#ifndef G3MiOSSDK_Geodetic2D
#define G3MiOSSDK_Geodetic2D

#include "Angle.hpp"

#include <string>

/**
 * Class to represent a position in the globe by latitude, longitud and altitude.
 */
class Geodetic2D {
public:
  const Angle _latitude;
  const Angle _longitude;

  
  static Geodetic2D zero() {
    return Geodetic2D(Angle::zero(),
                      Angle::zero());
  }
  
  static Geodetic2D fromDegrees(double lat,
                                double lon) {
    return Geodetic2D(Angle::fromDegrees(lat),
                      Angle::fromDegrees(lon));
  }

  static Geodetic2D fromRadians(double lat,
                                double lon) {
    return Geodetic2D(Angle::fromRadians(lat),
                      Angle::fromRadians(lon));
  }

  static Geodetic2D linearInterpolation(const Geodetic2D& from,
                                        const Geodetic2D& to,
                                        double alpha) {
    return Geodetic2D(Angle::linearInterpolation(from._latitude,  to._latitude,  alpha),
                      Angle::linearInterpolation(from._longitude, to._longitude, alpha));
  }
  
  
  /**
   * Returns the (initial) bearing from this point to the supplied point
   *   see http://williams.best.vwh.net/avform.htm#Crs
   */
  static Angle bearing(const Angle& fromLatitude,
                       const Angle& fromLongitude,
                       const Angle& toLatitude,
                       const Angle& toLongitude) {
    return Angle::fromRadians( bearingInRadians(fromLatitude,
                                                fromLongitude,
                                                toLatitude,
                                                toLongitude) );
  }

  /**
   * Returns the (initial) bearing from this point to the supplied point
   *   see http://williams.best.vwh.net/avform.htm#Crs
   */
  static double bearingInRadians(const Angle& fromLatitude,
                                 const Angle& fromLongitude,
                                 const Angle& toLatitude,
                                 const Angle& toLongitude) {
    const Angle dLon = toLongitude.sub(fromLongitude);

//    const double toLatCos = toLatitude.cosinus();
    const double toLatCos = COS(toLatitude._radians);

//    const double y = dLon.sinus() * toLatCos;
    const double y = SIN(dLon._radians) * toLatCos;
//    const double x = fromLatitude.cosinus() * toLatitude.sinus() - fromLatitude.sinus() * toLatCos * dLon.cosinus();
    const double x = COS(fromLatitude._radians) * SIN(toLatitude._radians) - SIN(fromLatitude._radians) * toLatCos * COS(dLon._radians);
    const double radians = IMathUtils::instance()->atan2(y, x);

    return radians;
  }
  
  /**
   * Returns the (initial) bearing from this point to the supplied point
   *   see http://williams.best.vwh.net/avform.htm#Crs
   */
  static Angle bearing(const Geodetic2D& from,
                       const Geodetic2D& to) {
    return bearing(from._latitude,
                   from._longitude,
                   to._latitude,
                   to._longitude);
  }


  Geodetic2D(const Angle& latitude,
             const Angle& longitude) :
  _latitude(latitude),
  _longitude(longitude)
  {
  }

  Geodetic2D(const Geodetic2D& g) :
  _latitude(g._latitude),
  _longitude(g._longitude)
  {
  }

  ~Geodetic2D() {

  }
  
  Geodetic2D add(const Geodetic2D& that) const {
    return Geodetic2D(_latitude.add(that._latitude),
                      _longitude.add(that._longitude));
  }
  
  Geodetic2D sub(const Geodetic2D& that) const {
    return Geodetic2D(_latitude.sub(that._latitude),
                      _longitude.sub(that._longitude));
  }
  
  Geodetic2D times(const double magnitude) const {
    return Geodetic2D(_latitude.times(magnitude),
                      _longitude.times(magnitude));
  }
  
  Geodetic2D div(const double magnitude) const {
    return Geodetic2D(_latitude.div(magnitude),
                      _longitude.div(magnitude));
  }
  
  bool closeTo(const Geodetic2D& other) const;
  
  bool isBetween(const Geodetic2D& min, const Geodetic2D& max) const;
  
  Angle angleTo(const Geodetic2D& other) const;
  
  
  /**
   * Returns the (initial) bearing from this point to the supplied point
   *   see http://williams.best.vwh.net/avform.htm#Crs
   */
  Angle bearingTo(const Geodetic2D& that) const {
    return bearing(_latitude,
                   _longitude,
                   that._latitude,
                   that._longitude);
    
  }
  
#ifdef C_CODE
  bool operator<(const Geodetic2D& that) const {
    return lowerThan(that);
  }
#endif
  
  bool lowerThan(const Geodetic2D& that) const {
    if (_latitude.lowerThan(that._latitude)) {
      return true;
    }
    else if (_latitude.greaterThan(that._latitude)) {
      return false;
    }
    return _longitude.lowerThan(that._longitude);
  }
  
  const std::string description() const;
#ifdef JAVA_CODE
  @Override
  public String toString() {
    return description();
  }
#endif

  bool isEquals(const Geodetic2D& that) const {
    return _latitude.isEquals(that._latitude) && _longitude.isEquals(that._longitude);
  }
  
#ifdef JAVA_CODE
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + ((_latitude == null) ? 0 : _latitude.hashCode());
    result = (prime * result) + ((_longitude == null) ? 0 : _longitude.hashCode());
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
    final Geodetic2D other = (Geodetic2D) obj;
    if (_latitude == null) {
      if (other._latitude != null) {
        return false;
      }
    }
    else if (!_latitude.equals(other._latitude)) {
      return false;
    }
    if (_longitude == null) {
      if (other._longitude != null) {
        return false;
      }
    }
    else if (!_longitude.equals(other._longitude)) {
      return false;
    }
    return true;
  }
#endif

};


#endif
