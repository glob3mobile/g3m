//
//  MutableVector2F.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/23/14.
//
//

#ifndef __G3MiOSSDK__MutableVector2F__
#define __G3MiOSSDK__MutableVector2F__

class Vector2F;

class MutableVector2F {
private:
  float _x;
  float _y;

public:
  MutableVector2F() :
  _x(0),
  _y(0)
  {
  }

  MutableVector2F(float x,
                  float y) :
  _x(x),
  _y(y)
  {
  }

  float x() const {
    return _x;
  }

  float y() const {
    return _y;
  }

  Vector2F asVector2F() const;

  MutableVector2F& operator=(const MutableVector2F& that) {
    _x = that._x;
    _y = that._y;
    return *this;
  }

};

#endif
