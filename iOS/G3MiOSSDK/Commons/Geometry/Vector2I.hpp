//
//  Vector2I.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/19/12.
//
//

#ifndef __G3MiOSSDK__Vector2I__
#define __G3MiOSSDK__Vector2I__

class Vector2I {
public:
  const int _x;
  const int _y;
  
  Vector2I(int x, int y) :
  _x(x),
  _y(y)
  {
    
  }
  
  Vector2I(const Vector2I& that) :
  _x(that._x),
  _y(that._y)
  {
    
  }

  static Vector2I zero() {
    return Vector2I(0, 0);
  }
  
  bool isZero() const {
    return (_x == 0) && (_y == 0);
  }
};

#endif
