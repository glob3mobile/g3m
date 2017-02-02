//
//  Quaternion.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/27/17.
//
//

#include "Quaternion.hpp"

#include "IMathUtils.hpp"
#include "MutableMatrix44D.hpp"
#include "TaitBryanAngles.hpp"


const Quaternion Quaternion::NANQD = Quaternion(NANF, NANF, NANF, NANF);

Quaternion::Quaternion(double x,
                       double y,
                       double z,
                       double w) :
_x(x),
_y(y),
_z(z),
_w(w)
{
}

Quaternion::~Quaternion() {

}

double Quaternion::dot(const Quaternion& that) const {
  return (_x * that._x) + (_y * that._y) + (_z * that._z) + (_w * that._w);
}

Quaternion Quaternion::times(const Quaternion& that) const {
  //  const double x = (_w * that._x) + (_x * that._w) + (_y * that._z) - (_z * that._y);
  //  const double y = (_w * that._y) + (_y * that._w) + (_z * that._x) - (_x * that._z);
  //  const double z = (_w * that._z) + (_z * that._w) + (_x * that._y) - (_y * that._x);
  //  const double w = (_w * that._w) - (_x * that._x) - (_y * that._y) - (_z * that._z);

  const double x = (that._w * _x) + (that._x * _w) + (that._z * _y) - (that._y * _z);
  const double y = (that._w * _y) + (that._y * _w) + (that._x * _z) - (that._z * _x);
  const double z = (that._w * _z) + (that._z * _w) + (that._y * _x) - (that._x * _y);
  const double w = (that._w * _w) - (that._x * _x) - (that._y * _y) - (that._z * _z);

  return Quaternion(x, y, z, w);
}

Quaternion Quaternion::slerp(const Quaternion& that,
                             double t) const {
  double cosHalftheta = dot(that);

  double tempX;
  double tempY;
  double tempZ;
  double tempW;
  if (cosHalftheta < 0) {
    cosHalftheta = -cosHalftheta;
    tempX = -that._x;
    tempY = -that._y;
    tempZ = -that._z;
    tempW = -that._w;
  }
  else {
    tempX = that._x;
    tempY = that._y;
    tempZ = that._z;
    tempW = that._w;
  }

  const IMathUtils* mu = IMathUtils::instance();

  if (mu->abs(cosHalftheta) >= 1.0) {
    return *this;
  }

  const double sinHalfTheta = mu->sqrt(1.0 - (cosHalftheta * cosHalftheta));
  const double halfTheta    = mu->acos(cosHalftheta);

  const double ratioA = mu->sin((1 - t) * halfTheta) / sinHalfTheta;
  const double ratioB = mu->sin(t * halfTheta) / sinHalfTheta;

  const double w = (_w * ratioA) + (tempW * ratioB);
  const double x = (_x * ratioA) + (tempX * ratioB);
  const double y = (_y * ratioA) + (tempY * ratioB);
  const double z = (_z * ratioA) + (tempZ * ratioB);
  return Quaternion(x, y, z, w);
}

double Quaternion::norm() const {
  return (_w * _w) + (_x * _x) + (_y * _y) + (_z * _z);
}

Quaternion Quaternion::inversed() const {
  const double n = norm();
  if (n > 0.0) {
    const double invNorm = 1.0 / n;
    return Quaternion(-_x * invNorm,
                      -_y * invNorm,
                      -_z * invNorm,
                      +_w * invNorm);
  }
  return Quaternion::NANQD;
}

void Quaternion::toRotationMatrix(MutableMatrix44D& result) const {
  const double m00 = 1.0 - (2.0 * _y * _y) - (2.0 * _z * _z);
  const double m01 = (2.0 * _x * _y) + (2.0 * _z * _w);
  const double m02 = (2.0 * _x * _z) - (2.0 * _y * _w);
  const double m03 = 0.0;

  const double m10 = (2.0 * _x * _y) - (2.0 * _z * _w);
  const double m11 = 1.0 - (2.0 * _x * _x) - (2.0 * _z * _z);
  const double m12 = (2.0 * _y * _z) + (2.0 * _x * _w);
  const double m13 = 0.0;

  const double m20 = (2.0 * _x * _z) + (2.0 * _y * _w);
  const double m21 = (2.0 * _y * _z) - (2.0 * _x * _w);
  const double m22 = 1.0 - (2.0 * _x * _x) - (2.0 * _y * _y);
  const double m23 = 0.0;

  const double m30 = 0.0;
  const double m31 = 0.0;
  const double m32 = 0.0;
  const double m33 = 1.0;

//  result.setValue(m00, m10, m20, m30,
//                  m01, m11, m21, m31,
//                  m02, m12, m22, m32,
//                  m03, m13, m23, m33);
  result.setValue(m00, m01, m02, m03,
                  m10, m11, m12, m13,
                  m20, m21, m22, m23,
                  m30, m31, m32, m33);
}
