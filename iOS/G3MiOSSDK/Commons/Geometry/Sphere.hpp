//
//  Sphere.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 04/06/13.
//
//

#ifndef __G3MiOSSDK__Sphere__
#define __G3MiOSSDK__Sphere__

#include "Geometry.hpp"
#include "Vector3D.hpp"

class Sphere: public Geometry3D {
private:
  const Vector3D _center;
  const double   _radius;
  const double   _radiusSquared;

public:
  Sphere(const Vector3D& center, double radius):
  _center(center),
  _radius(radius),
  _radiusSquared(radius*radius)
  {
  }

  Vector3D getCenter() const{
    return _center;
  }

  double getRadius() const {
    return _radius;
  }

  double getRadiusSquared() const {
    return _radiusSquared;
  }

};

#endif
