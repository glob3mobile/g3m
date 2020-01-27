//
//  MutableVector3D.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 31/05/12.
//

#include "MutableVector3D.hpp"

#include "Vector3D.hpp"
#include "Angle.hpp"
#include "IMathUtils.hpp"
#include "MutableMatrix44D.hpp"


MutableVector3D::MutableVector3D(const Vector3D &v) :
_x(v._x),
_y(v._y),
_z(v._z)
{
}

void MutableVector3D::copyFrom(const Vector3D& that) {
  _x = that._x;
  _y = that._y;
  _z = that._z;
}

MutableVector3D MutableVector3D::nan() {
  return MutableVector3D(NAND, NAND, NAND);
}

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

bool MutableVector3D::isNan() const {
  return (ISNAN(_x) ||
          ISNAN(_y) ||
          ISNAN(_z));
}

double MutableVector3D::length() const {
  return IMathUtils::instance()->sqrt(squaredLength());
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

void MutableVector3D::addInPlace(const Vector3D& that) {
  _x += that._x;
  _y += that._y;
  _z += that._z;
}

Vector3D MutableVector3D::sub(const Vector3D& v) const {
  return Vector3D(_x - v._x,
                  _y - v._y,
                  _z - v._z);
}

MutableVector3D MutableVector3D::times(const Vector3D& v) const {
  return MutableVector3D(_x * v._x,
                         _y * v._y,
                         _z * v._z);
}

MutableVector3D MutableVector3D::transformedBy(const MutableMatrix44D &m,
                                               const double homogeneus) const {
  return MutableVector3D(_x * m.get0() + _y * m.get4() + _z * m.get8() + homogeneus * m.get12(),
                         _x * m.get1() + _y * m.get5() + _z * m.get9() + homogeneus * m.get13(),
                         _x * m.get2() + _y * m.get6() + _z * m.get10() + homogeneus * m.get14());
}

Vector3D MutableVector3D::asVector3D() const {
  return Vector3D(_x, _y, _z);
}

void MutableVector3D::putSub(const MutableVector3D& a,
                             const Vector3D& b) {
  _x = a._x - b._x;
  _y = a._y - b._y;
  _z = a._z - b._z;
}

const double MutableVector3D::squaredDistanceTo(const Vector3D& that) const {
  const double dx = _x - that._x;
  const double dy = _y - that._y;
  const double dz = _z - that._z;
  return (dx * dx) + (dy * dy) + (dz * dz);
}
