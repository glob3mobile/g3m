//
//  Sector.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 22/06/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#include "Sector.hpp"
#include "Camera.hpp"
#include "Planet.hpp"


bool Sector::contains(const Geodetic2D &position) const {
  return position.isBetween(_lower, _upper);
}

bool Sector::fullContains(const Sector &s) const
{
  return contains(s.upper()) && contains(s.lower());
}

bool Sector::touchesWith(const Sector &that) const {
  // from Real-Time Collision Detection - Christer Ericson
  //   page 79
  
  // Exit with no intersection if separated along an axis
  if (_upper.latitude().lowerThan(that._lower.latitude()) ||
      _lower.latitude().greaterThan(that._upper.latitude())) {
    return false;
  }
  if (_upper.longitude().lowerThan(that._lower.longitude()) ||
      _lower.longitude().greaterThan(that._upper.longitude())) {
    return false;
  }
  
  // Overlapping on all axes means Sectors are intersecting
  return true;
}


Geodetic2D Sector::getInnerPoint(double u, double v) const 
// (u,v) are similar to texture coordinates inside the Sector
// (u,v)=(0,0) in NW point, and (1,1) in SE point
{
  const Angle lat = _lower.latitude().average(1-v, _upper.latitude());
  const Angle lon = _lower.longitude().average(u, _upper.longitude());
  return Geodetic2D(lat, lon);
}


Vector2D Sector::getUVCoordinates(Geodetic2D point) const {
  const double u = point.longitude().sub(_lower.longitude()).div(getDeltaLongitude());
  const double v = _upper.latitude().sub(point.latitude()).div(getDeltaLatitude());
  return Vector2D(u, v);
}


bool Sector::isBackOriented(const RenderContext *rc) const {
  const Vector3D position = rc->getCamera()->getPosition();
  const Geodetic2D corners[5] = { getNE(), getNW(), getSW(), getSE(), getCenter()};
  const Planet *planet = rc->getPlanet();
  
  for (unsigned int i=0; i<5; i++) {
    const Vector3D normal = planet->geodeticSurfaceNormal(corners[i]);
    const Vector3D view = position.sub(planet->toVector3D(corners[i]));
    const double dot = normal.dot(view);
    if (dot>0) return false;
  }
  
  return true;
}
