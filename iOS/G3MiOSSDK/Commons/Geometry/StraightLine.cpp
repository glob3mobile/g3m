//
//  StraightLine.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 5/2/16.
//
//

#include "StraightLine.hpp"
#include "Plane.hpp"

double StraightLine::squaredDistanceToPoint(const Vector3D& point) const {
  // _vector is normalized
  // original formula: _point.sub(point).cross(_vector).length() / _vector.length()
  double ux = _point._x - point._x;
  double uy = _point._y - point._y;
  double uz = _point._z - point._z;
  double vx = uy * _vector._z - uz * _vector._y;
  double vy = uz * _vector._x - ux * _vector._z;
  double vz = ux * _vector._y - uy * _vector._x;
  return vx * vx + vy * vy + vz * vz;
}

Vector3D StraightLine::projectPoint(const Vector3D& point) const {
  const Plane plane(_vector, point);
  return plane.intersectionWithRay(_point, _vector);
}

double StraightLine::signedDistanceToProjectedPoint(const Vector3D& point) const {
  Vector3D proyectedPoint = projectPoint(point);
  double absVx = fabs(_vector._x);
  double absVy = fabs(_vector._y);
  double absVz = fabs(_vector._z);
  if (absVx>absVy && absVx>absVz)
    return (proyectedPoint._x - _point._x) / _vector._x;
  else if (absVy>absVz)
    return (proyectedPoint._y - _point._y) / _vector._y;
  else
    return (proyectedPoint._z - _point._z) / _vector._z;
}

Vector3D StraightLine::projectPointToNormalPlane(const Vector3D& point) const {
  const Plane plane(_vector, _point);
  return plane.intersectionWithRay(point, _vector);
}
