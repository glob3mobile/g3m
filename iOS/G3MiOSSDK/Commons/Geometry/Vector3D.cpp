//
//  Vector3D.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 31/05/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#include "Vector3D.hpp"
#include "MutableVector3D.hpp"
#include "Angle.hpp"


Vector3D Vector3D::normalized() const {
    double d = length();
    return Vector3D(_x / d, _y /d, _z / d);
}


Angle Vector3D::angleBetween(const Vector3D& other) const {
  Vector3D v1 = normalized();
  Vector3D v2 = other.normalized();

  double c = v1.dot(v2);
  if (c > 1.0) c = 1.0;
  else if (c < -1.0) c = -1.0;
  
  return Angle::fromRadians(acos(c));
}

Vector3D Vector3D::rotateAroundAxis(const Vector3D& axis,
                                    Angle theta) const {
  const double u = axis.x();
  const double v = axis.y();
  const double w = axis.z();
  
  const double cosTheta = theta.cosinus();
  const double sinTheta = theta.sinus();
  
  const double ms = axis.squaredLength();
  const double m = sqrt(ms);
  
  return Vector3D(
                  ((u * (u * _x + v * _y + w * _z)) +
                   (((_x * (v * v + w * w)) - (u * (v * _y + w * _z))) * cosTheta) +
                   (m * ((-w * _y) + (v * _z)) * sinTheta)) / ms,
                  
                  ((v * (u * _x + v * _y + w * _z)) +
                   (((_y * (u * u + w * w)) - (v * (u * _x + w * _z))) * cosTheta) +
                   (m * ((w * _x) - (u * _z)) * sinTheta)) / ms,
                  
                  ((w * (u * _x + v * _y + w * _z)) +
                   (((_z * (u * u + v * v)) - (w * (u * _x + v * _y))) * cosTheta) +
                   (m * (-(v * _x) + (u * _y)) * sinTheta)) / ms);
}

MutableVector3D Vector3D::asMutableVector3D() const {
  return MutableVector3D(_x, _y, _z);
}
