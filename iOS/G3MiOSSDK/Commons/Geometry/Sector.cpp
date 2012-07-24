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

bool Sector::fullContains(const Sector &s) const {
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

// (u,v) are similar to texture coordinates inside the Sector
// (u,v)=(0,0) in NW point, and (1,1) in SE point
Geodetic2D Sector::getInnerPoint(double u, double v) const {
  const Angle lat = Angle::lerp(_lower.latitude(),  _upper.latitude(), 1-v);
  const Angle lon = Angle::lerp(_lower.longitude(), _upper.longitude(), u);
  return Geodetic2D(lat, lon);
}

bool Sector::isBackOriented(const RenderContext *rc) const {
  const Vector3D cameraPosition = rc->getCamera()->getPosition();
  const Planet*  planet         = rc->getPlanet();
  
  const Geodetic2D corners[5] = { 
    getNE(),
    getNW(),
    getSW(),
    getSE(),
    getCenter()
  };
  
  for (unsigned int i = 0; i < 5; i++) {
    const Geodetic2D corner = corners[i];
    
    const Vector3D normal = planet->geodeticSurfaceNormal(corner);
    const Vector3D view   = cameraPosition.sub(planet->toVector3D(corner));
    
    const double dot = normal.dot(view);
    if (dot > 0) {
      return false;
    }
  }
  
  return true;
}

Sector Sector::intersection(const Sector& s) const{
  double lowerLat;
  if (lower().latitude().degrees() > s.lower().latitude().degrees()){
    lowerLat = lower().latitude().degrees();
  } else{
    lowerLat = s.lower().latitude().degrees();
  }
  int todo_JM;
  return Sector::fullSphere();
}
