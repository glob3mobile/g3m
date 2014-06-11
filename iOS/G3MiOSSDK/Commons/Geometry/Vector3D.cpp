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

Vector3D Vector3D::zero = Vector3D(0,0,0);


Vector3D Vector3D::normalized() const {
  const double d = length();
  return Vector3D(_x / d, _y / d, _z / d);
}

double Vector3D::angleInRadiansBetween(const Vector3D& other) const {
  const Vector3D v1 = normalized();
  const Vector3D v2 = other.normalized();

  double c = v1.dot(v2);
  if (c > 1.0) {
    c = 1.0;
  }
  else if (c < -1.0) {
    c = -1.0;
  }

  return IMathUtils::instance()->acos(c);
}

Angle Vector3D::angleBetween(const Vector3D& other) const {
  return Angle::fromRadians( angleInRadiansBetween(other) );
}

Angle Vector3D::signedAngleBetween(const Vector3D& other,
                                   const Vector3D& up) const {
  const Angle angle = angleBetween(other);
  if (cross(other).dot(up) > 0) {
    return angle;
  }

  return angle.times(-1);
}


Vector3D Vector3D::rotateAroundAxis(const Vector3D& axis,
                                    const Angle& theta) const {
  const double u = axis._x;
  const double v = axis._y;
  const double w = axis._z;
  
//  const double cosTheta = theta.cosinus();
//  const double sinTheta = theta.sinus();
  const double cosTheta = COS(theta._radians);
  const double sinTheta = SIN(theta._radians);

  const double ms = axis.squaredLength();
  const double m = IMathUtils::instance()->sqrt(ms);
  
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

double Vector3D::minAxis() const {
  if (_x <= _y && _x <= _z) {
    return _x;
  }
  else if (_y <= _z) {
    return _y;
  }
  else {
    return _z;
  }
}

double Vector3D::axisAverage() const {
  return ((_x + _y + _z) / 3);
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
  //int __TODO_move_to_matrix;
  return Vector3D(_x * m.get0() + _y * m.get4() + _z * m.get8() + homogeneus * m.get12(),
                  _x * m.get1() + _y * m.get5() + _z * m.get9() + homogeneus * m.get13(),
                  _x * m.get2() + _y * m.get6() + _z * m.get10() + homogeneus * m.get14());
}


const std::string Vector3D::description() const {  
  IStringBuilder* isb = IStringBuilder::newStringBuilder();
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

const Vector3D Vector3D::clamp(const Vector3D& min,
                               const Vector3D& max) const {

  const IMathUtils* mu = IMathUtils::instance();

  const double x = mu->clamp(_x, min._x, max._x);
  const double y = mu->clamp(_y, min._y, max._y);
  const double z = mu->clamp(_z, min._z, max._z);

  return Vector3D(x, y, z);
}

const double Vector3D::distanceTo(const Vector3D& that) const {
  return IMathUtils::instance()->sqrt( squaredDistanceTo(that) );
}

const double Vector3D::squaredDistanceTo(const Vector3D& that) const {
  const double dx = _x - that._x;
  const double dy = _y - that._y;
  const double dz = _z - that._z;
  return (dx * dx) + (dy * dy) + (dz * dz);
}
