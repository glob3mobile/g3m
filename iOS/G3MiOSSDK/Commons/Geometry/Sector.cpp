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


bool Sector::isBackOriented(const RenderContext *rc) const
{
  const Camera* camera = rc->getCamera();
  const Planet* planet = rc->getPlanet();
  
  // computer center view point
  Vector2D centerPixel(camera->getWidth()*0.5, camera->getHeight()*0.5);
  Vector3D centerRay = camera->pixel2Ray(centerPixel);
  Vector3D centerPoint = planet->closestIntersection(camera->getPosition(), centerRay);
  Geodetic2D center = planet->toGeodetic2D(centerPoint);

  // compute sector point nearest to centerPoint
  Geodetic2D point = this->getClosestPoint(center);
  
  // compute angle between normals
  Vector3D normal = planet->geodeticSurfaceNormal(point);
  Vector3D view   = camera->getPosition().sub(centerPoint);
  double dot = normal.dot(view);
  return (dot<0)? true : false;  
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


Geodetic2D Sector::getClosestPoint(const Geodetic2D& pos) const
{
  Angle lat = pos.latitude().nearestAngleInInterval(_lower.latitude(), _upper.latitude());
  Angle lon = pos.longitude().nearestAngleInInterval(_lower.longitude(), _upper.longitude());
  return Geodetic2D(lat, lon);
}

