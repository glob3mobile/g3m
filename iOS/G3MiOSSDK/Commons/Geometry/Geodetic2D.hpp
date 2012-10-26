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

#include <string>

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

  /**
   * Returns the (initial) bearing from this point to the supplied point
   *   see http://williams.best.vwh.net/avform.htm#Crs
   */
  Angle bearingTo(const Geodetic2D& that) const {
    const Angle dLon = that.longitude().sub(longitude());
    const Angle lat1 = latitude();
    const Angle lat2 = that.latitude();

    const double y = dLon.sinus() * lat2.cosinus();
    const double x = lat1.cosinus()*lat2.sinus() - lat1.sinus()*lat2.cosinus()*dLon.cosinus();
    const double radians = GMath.atan2(y, x);

    return Angle::fromRadians(radians);
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
    + ((_latitude == null) ? 0 : _latitude.hashCode());
		result = prime * result
    + ((_longitude == null) ? 0 : _longitude.hashCode());
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
		Geodetic2D other = (Geodetic2D) obj;
		if (_latitude == null) {
			if (other._latitude != null)
				return false;
		} else if (!_latitude.equals(other._latitude))
			return false;
		if (_longitude == null) {
			if (other._longitude != null)
				return false;
		} else if (!_longitude.equals(other._longitude))
			return false;
		return true;
	}
#endif
  
};


#endif
