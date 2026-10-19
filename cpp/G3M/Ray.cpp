//
//  Ray.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 3/24/17.
//
//

#include "Ray.hpp"

#include "IStringBuilder.hpp"
#include "MutableVector3D.hpp"


Ray::Ray(const Vector3D& origin,
         const Vector3D& direction):
_origin(origin),
_direction(direction.normalized())
{
}

Ray::~Ray() {
}

double Ray::distanceTo(const Vector3D& point) const {
  return _direction.cross(point._x - _origin._x,
                          point._y - _origin._y,
                          point._z - _origin._z).length();
}

double Ray::squaredDistanceTo(const Vector3D& point) const {
  return _direction.cross(point._x - _origin._x,
                          point._y - _origin._y,
                          point._z - _origin._z).squaredLength();
}

double Ray::distanceTo(const MutableVector3D& point) const {
  return _direction.cross(point._x - _origin._x,
                          point._y - _origin._y,
                          point._z - _origin._z).length();
}

double Ray::squaredDistanceTo(const MutableVector3D& point) const {
  return _direction.cross(point._x - _origin._x,
                          point._y - _origin._y,
                          point._z - _origin._z).squaredLength();
}

const std::string Ray::description() const {
  IStringBuilder* isb = IStringBuilder::newStringBuilder();
  isb->addString("(Ray origin=");
  isb->addString(_origin.description());
  isb->addString(", direction=");
  isb->addString(_direction.description());
  isb->addString(")");
  const std::string s = isb->getString();
  delete isb;
  return s;
}

bool Ray::closestPointsOnTwoRays(const Ray& ray1,
                                 const Ray& ray2,
                                 MutableVector3D& closestPointRay1,
                                 MutableVector3D& closestPointRay2) {
  
  closestPointRay1.set(0,0,0);
  closestPointRay2.set(0,0,0);
  
  const double a = ray1._direction.dot(ray1._direction);
  const double b = ray1._direction.dot(ray2._direction);
  const double e = ray2._direction.dot(ray2._direction);
  
  const double d = a*e - b*b;
  
  // lines are not parallel
  if (d != 0.0) {
    const Vector3D r = ray1._origin.sub(ray2._origin);
    const double c = ray1._direction.dot(r);
    const double f = ray2._direction.dot(r);
    
    const double s = (b*f - c*e) / d;
    const double t = (a*f - c*b) / d;
    
    closestPointRay1.set( ray1.pointAtTime(s) );
    closestPointRay2.set( ray2.pointAtTime(t) );
    
    return true;
  }
  
  return false;
}
