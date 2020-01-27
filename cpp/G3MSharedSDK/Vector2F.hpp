//
//  Vector2F.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/9/13.
//
//

#ifndef __G3MiOSSDK__Vector2F__
#define __G3MiOSSDK__Vector2F__

class Vector2I;

#include <string>


class Vector2F {
private:
  Vector2F& operator=(const Vector2F& v);

public:
  static const Vector2F ZERO;

  static Vector2F zero() {
    return Vector2F::ZERO;
  }

  static Vector2F nan();

  const float _x;
  const float _y;


  Vector2F(const float x,
           const float y) :
  _x(x),
  _y(y)
  {
  }

  Vector2F(const Vector2F &v) :
  _x(v._x),
  _y(v._y)
  {
  }

  ~Vector2F() {
  }

  const double squaredDistanceTo(const Vector2I& that) const;

  const double squaredDistanceTo(const Vector2F& that) const;

  const double squaredDistanceTo(float x, float y) const;


  Vector2F add(const Vector2F& v) const {
    return Vector2F(_x + v._x,
                    _y + v._y);
  }

  bool isNan() const;

  Vector2F sub(const Vector2F& v) const {
    return Vector2F(_x - v._x,
                    _y - v._y);
  }

  Vector2F times(const float magnitude) const {
    return Vector2F(_x * magnitude,
                    _y * magnitude);
  }

  Vector2F div(float v) const {
    return Vector2F(_x / v, _y / v);
  }

  double length() const;

  double squaredLength() const {
    return _x * _x + _y * _y ;
  }

  Vector2F clampLength(float min, float max) const;

  const std::string description() const;

};

#endif
