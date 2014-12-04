//
//  MutableVector3D.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 31/05/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#include "MutableVector3D.hpp"


MutableVector3D MutableVector3D::normalized() const {
  const double d = length();
  return MutableVector3D(_x / d, _y /d, _z / d);
}

void MutableVector3D::normalize() {
    const double d = length();
    _x /= d;
    _y /= d;
    _z /= d;
}


Angle MutableVector3D::angleBetween(const MutableVector3D& other) const {
  const MutableVector3D v1 = normalized();
  const MutableVector3D v2 = other.normalized();
  
  double c = v1.dot(v2);
  if (c > 1.0) c = 1.0;
  else if (c < -1.0) c = -1.0;
  
  return Angle::fromRadians(IMathUtils::instance()->acos(c));
}

MutableVector3D MutableVector3D::rotatedAroundAxis(const MutableVector3D& axis,
                                                   const Angle& theta) const {
  const double u = axis.x();
  const double v = axis.y();
  const double w = axis.z();
  
//  const double cosTheta = theta.cosinus();
//  const double sinTheta = theta.sinus();
  const double cosTheta = COS(theta._radians);
  const double sinTheta = SIN(theta._radians);

  const double ms = axis.squaredLength();
  const double m = IMathUtils::instance()->sqrt(ms);
  
  return MutableVector3D(
                         ((u * (u * _x + v * _y + w * _z)) +
                          (((_x * (v * v + w * w)) - (u * (v * _y + w * _z))) * cosTheta) +
                          (m * ((-w * _y) + (v * _z)) * sinTheta)) / ms,
                         
                         ((v * (u * _x + v * _y + w * _z)) +
                          (((_y * (u * u + w * w)) - (v * (u * _x + w * _z))) * cosTheta) +
                          (m * ((w * _x) - (u * _z)) * sinTheta)) / ms,
                         
                         ((w * (u * _x + v * _y + w * _z)) +
                          (((_z * (u * u + v * v)) - (w * (u * _x + v * _y))) * cosTheta) +
                          (m * (-(v * _x) + (u * _y)) * sinTheta)) / ms
                         );
}

double MutableVector3D::normalizedDot(const MutableVector3D& a,
                                      const MutableVector3D& b) {
  const double aLength = a.length();
  const double a_x = a._x / aLength;
  const double a_y = a._y / aLength;
  const double a_z = a._z / aLength;

  const double bLength = b.length();
  const double b_x = b._x / bLength;
  const double b_y = b._y / bLength;
  const double b_z = b._z / bLength;

  return ((a_x * b_x) +
          (a_y * b_y) +
          (a_z * b_z));
}

double MutableVector3D::angleInRadiansBetween(const MutableVector3D& a,
                                              const MutableVector3D& b) {
  double c = MutableVector3D::normalizedDot(a, b);
  if (c > 1.0) {
    c = 1.0;
  }
  else if (c < -1.0) {
    c = -1.0;
  }
  return IMathUtils::instance()->acos(c);
}

MutableVector3D MutableVector3D::rotateAroundAxis(const MutableVector3D& axis,
                                                  const Angle& theta) const {
  const double u = axis._x;
  const double v = axis._y;
  const double w = axis._z;

  const double cosTheta = COS(theta._radians);
  const double sinTheta = SIN(theta._radians);

  const double ms = axis.squaredLength();
  const double m = IMathUtils::instance()->sqrt(ms);

  return MutableVector3D(
                         ((u * (u * _x + v * _y + w * _z)) +
                          (((_x * (v * v + w * w)) - (u * (v * _y + w * _z))) * cosTheta) +
                          (m * ((-w * _y) + (v * _z)) * sinTheta)) / ms,

                         ((v * (u * _x + v * _y + w * _z)) +
                          (((_y * (u * u + w * w)) - (v * (u * _x + w * _z))) * cosTheta) +
                          (m * ((w * _x) - (u * _z)) * sinTheta)) / ms,

                         ((w * (u * _x + v * _y + w * _z)) +
                          (((_z * (u * u + v * v)) - (w * (u * _x + v * _y))) * cosTheta) +
                          (m * (-(v * _x) + (u * _y)) * sinTheta)) / ms
                         
                         );
}
