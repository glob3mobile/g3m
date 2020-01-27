//
//  Vector2S.hpp
//  G3M
//
//  Created by Jose Miguel SN on 16/2/16.
//
//

#ifndef Vector2S_hpp
#define Vector2S_hpp

class Vector2I;

class Vector2S{
public:
  short _x;
  short _y;
  
  Vector2S(short x, short y):_x(x),_y(y) {}
  
  Vector2I asVector2I() const;
  
  bool isEquals(const Vector2S& that) const {
    return ((_x == that._x) && (_y == that._y));
  }

  double squaredLength() const {
    return _x * _x + _y * _y ;
  }

  
};

#endif /* Vector2S_hpp */
