//
//  Vector2F.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/9/13.
//
//

#ifndef __G3MiOSSDK__Vector2F__
#define __G3MiOSSDK__Vector2F__

#include "IMathUtils.hpp"
#include "MutableVector2F.hpp"
class Vector2I;

class Vector2F {
private:
  Vector2F& operator=(const Vector2F& v);

public:

  static Vector2F zero() {
    return Vector2F(0, 0);
  }

  static Vector2F nan() {
    return Vector2F(NANF, NANF);
  }

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

  Vector2F add(const Vector2F& that) const {
    return Vector2F(_x + that._x,
                    _y + that._y);
  }

  Vector2F sub(const Vector2F& that) const {
    return Vector2F(_x - that._x,
                    _y - that._y);
  }

  const double squaredDistanceTo(const Vector2I& that) const;

  const double squaredDistanceTo(const Vector2F& that) const;

  double squaredLength() const {
    return _x * _x + _y * _y ;
  }

  double length() const;

  Vector2F div(double v) const;

  MutableVector2F asMutableVector2F() const {
    return MutableVector2F(_x, _y);
  }

};

#endif
