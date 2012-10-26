//
//  Sector.h
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


class Sector {
  
private:
  const Geodetic2D _lower;
  const Geodetic2D _upper;
  const Geodetic2D _center;
  
  const Angle _deltaLatitude;
  const Angle _deltaLongitude;


  
public:
  
  ~Sector(){}
    
  Sector(const Geodetic2D& lower,
         const Geodetic2D& upper) :
  _lower(lower),
  _upper(upper),
  _deltaLatitude(upper.latitude().sub(lower.latitude())),
  _deltaLongitude(upper.longitude().sub(lower.longitude())),
  _center(Angle::midAngle(lower.latitude(), upper.latitude()),
          Angle::midAngle(lower.longitude(), upper.longitude()))
  {
  }

  
  Sector(const Sector& sector) :
  _lower(sector._lower),
  _upper(sector._upper),
  _deltaLatitude(sector._deltaLatitude),
  _deltaLongitude(sector._deltaLongitude),
  _center(sector._center)
  {
  }
  
  static Sector fromDegrees(double minLat, double minLon, double maxLat, double maxLon){
    const Geodetic2D lower(Angle::fromDegrees(minLat), Angle::fromDegrees(minLon));
    const Geodetic2D upper(Angle::fromDegrees(maxLat), Angle::fromDegrees(maxLon));
    
    return Sector(lower, upper);
  }

  Vector2D getScaleFactor(const Sector& that) const {
    const double u = _deltaLatitude.div(that._deltaLatitude);
    const double v = _deltaLongitude.div(that._deltaLongitude);
    return Vector2D(u, v);
  }
  
  Vector2D getTranslationFactor(const Sector& that) const {
    const double diff = _deltaLongitude.div(that._deltaLongitude);
    const Vector2D uv = that.getUVCoordinates(_lower);
    
    return Vector2D(uv._x, uv._y - diff);
  }
  
  bool fullContains(const Sector& s) const;
  
  Sector intersection(const Sector& s) const;
  
  static Sector fullSphere() {
    return Sector(Geodetic2D(Angle::fromDegrees(-90), Angle::fromDegrees(-180)),
                  Geodetic2D(Angle::fromDegrees(90), Angle::fromDegrees(180)));
  }
  
  const Geodetic2D lower() const {
    return _lower;
  }
  
  const Geodetic2D upper() const {
    return _upper;
  }
  
  bool contains(const Geodetic2D& position) const;
  
  bool contains(const Geodetic3D& position) const {
    return contains(position.asGeodetic2D());
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
    return Geodetic2D(_upper.latitude(), _lower.longitude());
  }
  
  const Geodetic2D getSE() const {
    return Geodetic2D(_lower.latitude(), _upper.longitude());
  }
  
  const Geodetic2D getCenter() const {
    return _center;
  }
  
  // (u,v) are similar to texture coordinates inside the Sector
  // (u,v)=(0,0) in NW point, and (1,1) in SE point
  const Geodetic2D getInnerPoint(double u, double v) const;
  
  const Vector2D getUVCoordinates(const Geodetic2D& point) const {
    return getUVCoordinates(point.latitude(), point.longitude());
  }

  Vector2D getUVCoordinates(const Angle& latitude,
                            const Angle& longitude) const {
    const double u = longitude.sub(_lower.longitude()).div(getDeltaLongitude());
    const double v = _upper.latitude().sub(latitude).div(getDeltaLatitude());
    return Vector2D(u, v);
  }
  
  bool isBackOriented(const RenderContext *rc) const;
  
  const Geodetic2D getClosestPoint(const Geodetic2D& pos) const;
  
  const Geodetic2D getApproximatedClosestPoint(const Geodetic2D& pos) const;
  
  const std::string description() const;
  
  Sector* shrinkedByPercentP(float percent) const {
    Angle deltaLatitude  = _deltaLatitude.times(percent).div(2);
    Angle deltaLongitude = _deltaLongitude.times(percent).div(2);
    
    Geodetic2D delta = Geodetic2D(deltaLatitude, deltaLongitude);
    
    return new Sector(_lower.add( delta ),
                      _upper.sub( delta ) );
    
  }
  
};


#endif
