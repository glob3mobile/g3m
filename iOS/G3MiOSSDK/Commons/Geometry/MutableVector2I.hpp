//
//  MutableVector2I.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/20/12.
//
//

#ifndef __G3MiOSSDK__MutableVector2I__
#define __G3MiOSSDK__MutableVector2I__

class Vector2I;


class MutableVector2I {
private:
  int _x;
  int _y;
  
public:
  MutableVector2I() :
  _x(0),
  _y(0)
  {
    
  }

  MutableVector2I(int x, int y) :
  _x(x),
  _y(y)
  {
    
  }
  
  MutableVector2I(const MutableVector2I& that) :
  _x(that._x),
  _y(that._y)
  {
    
  }
  
  MutableVector2I& operator=(const MutableVector2I& that) {
    _x = that._x;
    _y = that._y;
    return *this;
  }
  
  static MutableVector2I zero() {
    return MutableVector2I(0, 0);
  }
  
  int x() const {
    return _x;
  }
  
  int y() const {
    return _y;
  }
  
  Vector2I asVector2I() const;
};

#endif
