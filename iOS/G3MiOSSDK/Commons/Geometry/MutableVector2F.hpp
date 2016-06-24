//
//  MutableVector2F.hpp
//  G3MiOSSDK
//
<<<<<<< HEAD
//  Created by Diego Gomez Deck on 10/23/14.
=======
//  Created by Diego Gomez Deck on 2/25/15.
>>>>>>> 882166c33bdf9946c54ea507ad5e1c47fb3e83e0
//
//

#ifndef __G3MiOSSDK__MutableVector2F__
#define __G3MiOSSDK__MutableVector2F__

class Vector2F;

class MutableVector2F {
<<<<<<< HEAD
private:
  float _x;
  float _y;

public:
=======
public:
  float _x;
  float _y;

>>>>>>> 882166c33bdf9946c54ea507ad5e1c47fb3e83e0
  MutableVector2F() :
  _x(0),
  _y(0)
  {
  }

<<<<<<< HEAD
  MutableVector2F(float x,
                  float y) :
=======
  MutableVector2F(float x, float y) :
>>>>>>> 882166c33bdf9946c54ea507ad5e1c47fb3e83e0
  _x(x),
  _y(y)
  {
  }

<<<<<<< HEAD
  float x() const {
    return _x;
  }

  float y() const {
    return _y;
  }

  Vector2F asVector2F() const;
=======
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
>>>>>>> 882166c33bdf9946c54ea507ad5e1c47fb3e83e0

  MutableVector2F& operator=(const MutableVector2F& that) {
    _x = that._x;
    _y = that._y;
    return *this;
  }

<<<<<<< HEAD
=======
  static MutableVector2F zero() {
    return MutableVector2F(0, 0);
  }

  static MutableVector2F nan();

  Vector2F asVector2F() const;

  bool isNan() const;

>>>>>>> 882166c33bdf9946c54ea507ad5e1c47fb3e83e0
};

#endif
