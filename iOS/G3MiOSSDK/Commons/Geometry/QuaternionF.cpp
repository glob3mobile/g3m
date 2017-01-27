//
//  QuaternionF.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/27/17.
//
//

#include "QuaternionF.hpp"

#include "IMathUtils.hpp"


QuaternionF::QuaternionF(float x,
                         float y,
                         float z,
                         float w) :
_x(0.0f),
_y(0.0f),
_z(0.0f),
_w(0.0f)
{

}

QuaternionF::~QuaternionF() {

}

float QuaternionF::dot(const QuaternionF& that) const {
  return (_x * that._x) + (_y * that._y) + (_z * that._z) + (_w * that._w);
}

QuaternionF QuaternionF::multiplyBy(const QuaternionF& that) const {
  const float w = (_w * that._w) - (_x * that._x) - (_y * that._y) - (_z * that._z);
  const float x = ((_w * that._x) + (_x * that._w) + (_y * that._z)) - (_z * that._y);
  const float y = ((_w * that._y) + (_y * that._w) + (_z * that._x)) - (_x * that._z);
  const float z = ((_w * that._z) + (_z * that._w) + (_x * that._y)) - (_y * that._x);

  return QuaternionF(x, y, z, w);
}

QuaternionF QuaternionF::slerp(const QuaternionF& that,
                               float t) const {
  float cosHalftheta = dot(that);

  float tempX;
  float tempY;
  float tempZ;
  float tempW;
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

  const float w = (float) ((_w * ratioA) + (tempW * ratioB));
  const float x = (float) ((_x * ratioA) + (tempX * ratioB));
  const float y = (float) ((_y * ratioA) + (tempY * ratioB));
  const float z = (float) ((_z * ratioA) + (tempZ * ratioB));

  return QuaternionF(x, y, z, w);
}
