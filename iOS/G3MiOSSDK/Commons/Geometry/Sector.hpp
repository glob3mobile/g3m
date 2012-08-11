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
  
//  static Sector fromLowerAndUpper(const Geodetic2D& lower,
//                           const Geodetic2D& upper){
//    
//    double upLat = upper.latitude().degrees();
//    while (lower.latitude().degrees() > upLat){
//      upLat += 360.0;
//    }
//    
//    double upLon = upper.longitude().degrees();
//    while (lower.latitude().degrees() > upLon){
//      upLon += 360.0;
//    }
//    
//    Geodetic2D upper2 = Geodetic2D::fromDegrees(upLat, upLon);
//    
//    return Sector(lower, upper2);
//  }
  
  Sector(const Geodetic2D& lower,
         const Geodetic2D& upper) :
  _lower(lower),
  _upper(upper),
  _deltaLatitude(upper.latitude().sub(lower.latitude())),
  _deltaLongitude(upper.longitude().sub(lower.longitude())),
  _center(Angle::midAngle(_lower.latitude(), _upper.latitude()),
          Angle::midAngle(_lower.longitude(), _upper.longitude()))
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
    Geodetic2D lower(Angle::fromDegrees(minLat), Angle::fromDegrees(minLon));
    Geodetic2D upper(Angle::fromDegrees(maxLat), Angle::fromDegrees(maxLon));
    Sector s(lower, upper);
    return s;
  }

  Vector2D getScaleFactor(const Sector& s) const
  {
    double u = _deltaLatitude.div(s._deltaLatitude);
    double v = _deltaLongitude.div(s._deltaLongitude);
    Vector2D scale(u,v);
    return scale;
  }
  
  Vector2D getTranslationFactor(const Sector& s) const
  {
    double diff = _deltaLongitude.div(s._deltaLongitude);
    Vector2D uv = s.getUVCoordinates(_lower);
    
    Vector2D trans(uv.x(), uv.y()- diff);
    return trans;
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
  
  bool contains(const Geodetic3D& position) const
  {
    return contains(position.asGeodetic2D());
  }
  
  
  bool touchesWith(const Sector& that) const;
  
  Angle getDeltaLatitude() const {
    return _deltaLatitude;
  }
  
  Angle getDeltaLongitude() const {
    return _deltaLongitude;
  }
  
  Geodetic2D getSW() const {
    return _lower;
  }
  
  Geodetic2D getNE() const {
    return _upper;
  }
  
  Geodetic2D getNW() const {
    return Geodetic2D(_upper.latitude(), _lower.longitude());
  }
  
  Geodetic2D getSE() const {
    return Geodetic2D(_lower.latitude(), _upper.longitude());
  }
  
  Geodetic2D getCenter() const {
    return _center;
  }
  
  // (u,v) are similar to texture coordinates inside the Sector
  // (u,v)=(0,0) in NW point, and (1,1) in SE point
  Geodetic2D getInnerPoint(double u, double v) const;
  
  Vector2D getUVCoordinates(const Geodetic2D& point) const {
    return getUVCoordinates(point.latitude(), point.longitude());
  }

  Vector2D getUVCoordinates(const Angle& latitude,
                            const Angle& longitude) const {
    const double u = longitude.sub(_lower.longitude()).div(getDeltaLongitude());
    const double v = _upper.latitude().sub(latitude).div(getDeltaLatitude());
    return Vector2D(u, v);
  }
  
  bool isBackOriented(const RenderContext *rc);
  
  Geodetic2D getClosestPoint(const Geodetic2D& pos) const;
  
  Geodetic2D getApproximatedClosestPoint(const Geodetic2D& pos) const;
  
};


#endif
