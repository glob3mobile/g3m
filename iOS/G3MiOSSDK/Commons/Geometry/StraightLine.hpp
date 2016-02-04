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
  
  const double distanceToPoint(const Vector3D& point) const {
    return _point.sub(point).cross(_vector).length() / _vector.length();
  }
  
};


#endif
