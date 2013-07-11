//
//  Sector.hpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 12/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_Sector_h
#define G3MiOSSDK_Sector_h

#include <vector>

#include "Geodetic2D.hpp"
#include "Context.hpp"
#include "Vector2D.hpp"
#include "Geodetic3D.hpp"

class Sector_Geodetic2DCachedData;

class Sector {

private:
  const Geodetic2D _lower;
  const Geodetic2D _upper;
  const Geodetic2D _center;

  const Angle _deltaLatitude;
  const Angle _deltaLongitude;


  const Geodetic2D getClosestPoint(const Geodetic2D& pos) const;

  // cached values for speed up in isBackOriented()
  mutable Sector_Geodetic2DCachedData* _nwData;
  mutable Sector_Geodetic2DCachedData* _neData;
  mutable Sector_Geodetic2DCachedData* _swData;
  mutable Sector_Geodetic2DCachedData* _seData;

public:

  ~Sector();

  Sector(const Geodetic2D& lower,
         const Geodetic2D& upper) :
  _lower(lower),
  _upper(upper),
  _deltaLatitude(upper.latitude().sub(lower.latitude())),
  _deltaLongitude(upper.longitude().sub(lower.longitude())),
  _center(Angle::midAngle(lower.latitude(), upper.latitude()),
          Angle::midAngle(lower.longitude(), upper.longitude())),
  _nwData(NULL),
  _neData(NULL),
  _swData(NULL),
  _seData(NULL)
  {
//    if (_deltaLatitude._degrees < 0) {
//      printf("break point\n");
//    }
//    if (_deltaLongitude._degrees < 0) {
//      printf("break point\n");
//    }
  }


  Sector(const Sector& sector) :
  _lower(sector._lower),
  _upper(sector._upper),
  _deltaLatitude(sector._deltaLatitude),
  _deltaLongitude(sector._deltaLongitude),
  _center(sector._center),
  _nwData(NULL),
  _neData(NULL),
  _swData(NULL),
  _seData(NULL)
  {
  }

  static Sector fromDegrees(double minLat, double minLon, double maxLat, double maxLon){
    const Geodetic2D lower(Angle::fromDegrees(minLat), Angle::fromDegrees(minLon));
    const Geodetic2D upper(Angle::fromDegrees(maxLat), Angle::fromDegrees(maxLon));

    return Sector(lower, upper);
  }

  const Vector2D div(const Sector& that) const;

//  Vector2D getTranslationFactor(const Sector& that) const;

  bool fullContains(const Sector& that) const;

  Sector intersection(const Sector& that) const;

  Sector mergedWith(const Sector& that) const;

  static Sector fullSphere() {
    return Sector(Geodetic2D(Angle::fromDegrees(-90), Angle::fromDegrees(-180)),
                  Geodetic2D(Angle::fromDegrees( 90), Angle::fromDegrees( 180)));
  }

  const Geodetic2D lower() const {
    return _lower;
  }

  const Angle lowerLatitude() const {
    return _lower.latitude();
  }

  const Angle lowerLongitude() const {
    return _lower.longitude();
  }

  const Geodetic2D upper() const {
    return _upper;
  }

  const Angle upperLatitude() const {
    return _upper.latitude();
  }

  const Angle upperLongitude() const {
    return _upper.longitude();
  }

  
  bool contains(const Angle& latitude,
                const Angle& longitude) const;
  
  bool contains(const Geodetic2D& position) const {
    return contains(position.latitude(),
                    position.longitude());
  }

  bool contains(const Geodetic3D& position) const {
    return contains(position.latitude(),
                    position.longitude());
  }

  bool touchesWith(const Sector& that) const;

  const Angle getDeltaLatitude() const {
    return _deltaLatitude;
  }

  const Angle getDeltaLongitude() const {
    return _deltaLongitude;
  }

  const Geodetic2D getSW() const {
    return _lower;
  }

  const Geodetic2D getNE() const {
    return _upper;
  }

  const Geodetic2D getNW() const {
    return Geodetic2D(_upper.latitude(),
                      _lower.longitude());
  }

  const Geodetic2D getSE() const {
    return Geodetic2D(_lower.latitude(),
                      _upper.longitude());
  }

  const Geodetic2D getCenter() const {
    return _center;
  }

  // (u,v) are similar to texture coordinates inside the Sector
  // (u,v)=(0,0) in NW point, and (1,1) in SE point
  const Geodetic2D getInnerPoint(double u, double v) const;

  const Angle getInnerPointLongitude(double u) const;
  const Angle getInnerPointLatitude(double v) const;


  const Vector2D getUVCoordinates(const Geodetic2D& point) const {
    return getUVCoordinates(point.latitude(), point.longitude());
  }

  Vector2D getUVCoordinates(const Angle& latitude,
                            const Angle& longitude) const {
    return Vector2D(getUCoordinate(longitude),
                    getVCoordinate(latitude));
  }

  double getUCoordinate(const Angle& longitude) const {
    return (longitude._radians - _lower.longitude()._radians) / _deltaLongitude._radians;
  }

  double getVCoordinate(const Angle& latitude) const {
    return (_upper.latitude()._radians - latitude._radians)   / _deltaLatitude._radians;
  }


  bool isBackOriented(const G3MRenderContext *rc,
                      double minHeight) const;

  const Geodetic2D clamp(const Geodetic2D& pos) const;

  const std::string description() const;

  Sector* shrinkedByPercentP(float percent) const {
    const Angle deltaLatitude  = _deltaLatitude.times(percent).div(2);
    const Angle deltaLongitude = _deltaLongitude.times(percent).div(2);

    const Geodetic2D delta(deltaLatitude, deltaLongitude);

    return new Sector(_lower.add( delta ),
                      _upper.sub( delta ) );
  }

  Sector shrinkedByPercent(float percent) const {
    const Angle deltaLatitude  = _deltaLatitude.times(percent).div(2);
    const Angle deltaLongitude = _deltaLongitude.times(percent).div(2);

    const Geodetic2D delta(deltaLatitude, deltaLongitude);

    return Sector(_lower.add( delta ),
                  _upper.sub( delta ) );
  }

  bool isEqualsTo(const Sector& that) const {
    return _lower.isEqualsTo(that._lower) && _upper.isEqualsTo(that._upper);
  }
  
  bool touchesNorthPole() const {
    return (_upper.latitude()._degrees >= 89.9);
  }
  
  bool touchesSouthPole() const {
    return (_lower.latitude()._degrees <= -89.9);
  }


#ifdef JAVA_CODE
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + ((_lower == null) ? 0 : _lower.hashCode());
    result = (prime * result) + ((_upper == null) ? 0 : _upper.hashCode());
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
    final Sector other = (Sector) obj;
    if (_lower == null) {
      if (other._lower != null) {
        return false;
      }
    }
    else if (!_lower.equals(other._lower)) {
      return false;
    }
    if (_upper == null) {
      if (other._upper != null) {
        return false;
      }
    }
    else if (!_upper.equals(other._upper)) {
      return false;
    }
    return true;
  }
#endif

};


#endif
