//
//  StraightLine.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 5/2/16.
//
//

#include "StraightLine.hpp"

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
