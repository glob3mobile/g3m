//
//  StraightLine.hpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 04/02/16.
//  Copyright (c) 2016 Universidad de Las Palmas. All rights reserved.
//

#ifndef G3MiOSSDK_StraightLine
#define G3MiOSSDK_StraightLine

#include "Vector3D.hpp"

class StraightLine {
  
private:
  const Vector3D _point;
  const Vector3D _vector;

public:

  StraightLine(const Vector3D& point,
               const Vector3D& vector):
  _point(point),
  _vector(vector.normalized())
  {
  }

  StraightLine transformedBy(const MutableMatrix44D &m) const {
    return StraightLine(_point.transformedBy(m, 1), _vector.transformedBy(m, 0));
  }
  
  const double squaredDistanceToPoint(const Vector3D& point) const {
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
  
};


#endif
