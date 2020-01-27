//
//  Vector3F.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 2/9/13.
//
//

#ifndef __G3M__Vector3F__
#define __G3M__Vector3F__


class Vector3F {
private:

  Vector3F& operator=(const Vector3F& that);

public:
  static Vector3F zero() {
    return Vector3F(0,0,0);
  }

  const float _x;
  const float _y;
  const float _z;

  Vector3F(const float x,
           const float y,
           const float z) :
  _x(x),
  _y(y),
  _z(z)
  {
  }

  Vector3F(const Vector3F& that) :
  _x(that._x),
  _y(that._y),
  _z(that._z)
  {
  }

  ~Vector3F() {
  }

  float dot(const Vector3F& v) const {
    return ((_x * v._x) +
            (_y * v._y) +
            (_z * v._z));
  }

  double length() const;

  double squaredLength() const {
    return _x * _x + _y * _y + _z * _z;
  }

  Vector3F normalized() const {
    const double d = length();
    return Vector3F((float) (_x / d),
                    (float) (_y / d),
                    (float) (_z / d));
  }

  Vector3F add(const Vector3F& that) const {
    return Vector3F(_x + that._x,
                    _y + that._y,
                    _z + that._z);
  }

  Vector3F sub(const Vector3F& that) const {
    return Vector3F(_x - that._x,
                    _y - that._y,
                    _z - that._z);
  }

  Vector3F cross(const Vector3F& that) const {
    return Vector3F(_y * that._z - _z * that._y,
                    _z * that._x - _x * that._z,
                    _x * that._y - _y * that._x);
  }

  Vector3F div(const float v) const {
    return Vector3F(_x / v,
                    _y / v,
                    _z / v);
  }

  bool isZero() const {
    return ((_x == 0) &&
            (_y == 0) &&
            (_z == 0));
  }

  bool isNan() const;
  
};

#endif
