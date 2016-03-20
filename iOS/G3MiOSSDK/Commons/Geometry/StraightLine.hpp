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
  
  double squaredDistanceToPoint(const Vector3D& point) const;
  
  Vector3D projectPoint(const Vector3D& point) const;
  double signedDistanceToProjectedPoint(const Vector3D& point) const;
  Vector3D projectPointToNormalPlane(const Vector3D& point) const;
};


#endif
