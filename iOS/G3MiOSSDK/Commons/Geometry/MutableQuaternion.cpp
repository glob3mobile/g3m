//
//  MutableQuaternion.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/19/17.
//
//

#include "MutableQuaternion.hpp"

#include <stddef.h>

#include "IMathUtils.hpp"


MutableQuaternion::MutableQuaternion() :
_x(0.0f),
_y(0.0f),
_z(0.0f),
_w(0.0f),
_temp(NULL)
{
}

MutableQuaternion::MutableQuaternion(float x, float y, float z, float w) :
_x(x),
_y(y),
_z(z),
_w(w),
_temp(NULL)
{
}

MutableQuaternion::~MutableQuaternion() {
  delete _temp;
}

void MutableQuaternion::setXYZW(float x, float y, float z, float w) {
  _x = x;
  _y = y;
  _z = z;
  _w = w;
}

void MutableQuaternion::setX(float x) {
  _x = x;
}

void MutableQuaternion::setY(float y) {
  _y = y;
}

void MutableQuaternion::setZ(float z) {
  _z = z;
}

void MutableQuaternion::setW(float w) {
  _w = w;
}

float MutableQuaternion::getX() const {
  return _x;
}

float MutableQuaternion::getY() const {
  return _y;
}

float MutableQuaternion::getZ() const {
  return _z;
}

float MutableQuaternion::getW() const {
  return _w;
}

void MutableQuaternion::copyFrom(const MutableQuaternion& that) {
  _x = that._x;
  _y = that._y;
  _z = that._z;
  _w = that._w;
}

float MutableQuaternion::dot(const MutableQuaternion& that) const {
  return (_x * that._x) + (_y * that._y) + (_z * that._z) + (_w * that._w);
}

void MutableQuaternion::multiplyBy(const MutableQuaternion& that,
                                   MutableQuaternion& output) const {
  if (&that == &output) {
    if (_temp == NULL) {
      _temp = new MutableQuaternion();
    }
    _temp->copyFrom(that);

    output._w = (_w * _temp->_w) - (_x * _temp->_x) - (_y * _temp->_y) - (_z * _temp->_z);
    output._x = ((_w * _temp->_x) + (_x * _temp->_w) + (_y * _temp->_z)) - (_z * _temp->_y);
    output._y = ((_w * _temp->_y) + (_y * _temp->_w) + (_z * _temp->_x)) - (_x * _temp->_z);
    output._z = ((_w * _temp->_z) + (_z * _temp->_w) + (_x * _temp->_y)) - (_y * _temp->_x);
  }
  else {
    output._w = (_w * that._w) - (_x * that._x) - (_y * that._y) - (_z * that._z);
    output._x = ((_w * that._x) + (_x * that._w) + (_y * that._z)) - (_z * that._y);
    output._y = ((_w * that._y) + (_y * that._w) + (_z * that._x)) - (_x * that._z);
    output._z = ((_w * that._z) + (_z * that._w) + (_x * that._y)) - (_y * that._x);
  }
}

void MutableQuaternion::slerp(const MutableQuaternion& that,
                              MutableQuaternion& output,
                              float t) const {
  if (_temp == NULL) {
    _temp = new MutableQuaternion();
  }

  float cosHalftheta = dot(that);

  if (cosHalftheta < 0) {
    cosHalftheta = -cosHalftheta;
    _temp->_x = -that._x;
    _temp->_y = -that._y;
    _temp->_z = -that._z;
    _temp->_w = -that._w;
  }
  else {
    _temp->copyFrom(that);
  }

  const IMathUtils* mu = IMathUtils::instance();

  if (mu->abs(cosHalftheta) >= 1.0) {
    output._x = _x;
    output._y = _y;
    output._z = _z;
    output._w = _w;
  }
  else {
    const double sinHalfTheta = mu->sqrt(1.0 - (cosHalftheta * cosHalftheta));
    const double halfTheta    = mu->acos(cosHalftheta);

    const double ratioA = mu->sin((1 - t) * halfTheta) / sinHalfTheta;
    const double ratioB = mu->sin(t * halfTheta) / sinHalfTheta;

    output._w = (float) ((_w * ratioA) + (_temp->_w * ratioB));
    output._x = (float) ((_x * ratioA) + (_temp->_x * ratioB));
    output._y = (float) ((_y * ratioA) + (_temp->_y * ratioB));
    output._z = (float) ((_z * ratioA) + (_temp->_z * ratioB));
  }

}
