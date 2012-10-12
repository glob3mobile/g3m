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

#include "IStringBuilder.hpp"


Vector3D Vector3D::normalized() const {
  double d = length();
  return Vector3D(_x / d, _y /d, _z / d);
}

Angle Vector3D::angleBetween(const Vector3D& other) const {
  const Vector3D v1 = normalized();
  const Vector3D v2 = other.normalized();
  
  double c = v1.dot(v2);
  if (c > 1.0) c = 1.0;
  else if (c < -1.0) c = -1.0;
  
  return Angle::fromRadians(GMath.acos(c));
}

Vector3D Vector3D::rotateAroundAxis(const Vector3D& axis,
                                    const Angle& theta) const {
  const double u = axis._x;
  const double v = axis._y;
  const double w = axis._z;
  
  const double cosTheta = theta.cosinus();
  const double sinTheta = theta.sinus();
  
  const double ms = axis.squaredLength();
  const double m = GMath.sqrt(ms);
  
  return Vector3D(
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

double Vector3D::maxAxis() const {
  if (_x >= _y && _x >= _z) {
    return _x;
  }
  else if (_y >= _z) {
    return _y;
  }
  else {
    return _z;
  }
}

MutableVector3D Vector3D::asMutableVector3D() const {
  return MutableVector3D(_x, _y, _z);
}


Vector3D Vector3D::projectionInPlane(const Vector3D& normal) const
{
  Vector3D axis = normal.cross(*this);
  MutableMatrix44D m = MutableMatrix44D::createRotationMatrix(Angle::fromDegrees(90), axis);
  Vector3D projected = normal.transformedBy(m, 0).normalized();
  return projected.times(this->length());
}

Vector3D Vector3D::transformedBy(const MutableMatrix44D &m,
                       const double homogeneus) const {
  int todo_move_to_matrix;
  return Vector3D(_x * m.get(0) + _y * m.get(4) + _z * m.get(8) + homogeneus * m.get(12),
                  _x * m.get(1) + _y * m.get(5) + _z * m.get(9) + homogeneus * m.get(13),
                  _x * m.get(2) + _y * m.get(6) + _z * m.get(10) + homogeneus * m.get(14));
}


const std::string Vector3D::description() const {  
  IStringBuilder *isb = IStringBuilder::newStringBuilder();
  isb->addString("(V3D ");
  isb->addDouble(_x);
  isb->addString(", ");
  isb->addDouble(_y);
  isb->addString(", ");
  isb->addDouble(_z);
  isb->addString(")");
  const std::string s = isb->getString();
  delete isb;
  return s;
}
