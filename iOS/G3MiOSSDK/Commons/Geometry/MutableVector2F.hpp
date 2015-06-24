//
//  MutableVector2F.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/25/15.
//
//

#ifndef __G3MiOSSDK__MutableVector2F__
#define __G3MiOSSDK__MutableVector2F__

class Vector2F;

class MutableVector2F {
public:
  float _x;
  float _y;

  MutableVector2F() :
  _x(0),
  _y(0)
  {
  }

  MutableVector2F(float x, float y) :
  _x(x),
  _y(y)
  {
  }

  MutableVector2F(const MutableVector2F& that) :
  _x(that._x),
  _y(that._y)
  {
  }

  explicit MutableVector2F(const Vector2F& that);

  void set(float x, float y) {
    _x = x;
    _y = y;
  }

  void add(float x, float y) {
    _x += x;
    _y += y;
  }
  
  void times(float k) {
    _x *= k;
    _y *= k;
  }

  MutableVector2F& operator=(const MutableVector2F& that) {
    _x = that._x;
    _y = that._y;
    return *this;
  }

  static MutableVector2F zero() {
    return MutableVector2F(0, 0);
  }

  static MutableVector2F nan();

  Vector2F asVector2F() const;

  bool isNan() const;

};

#endif
