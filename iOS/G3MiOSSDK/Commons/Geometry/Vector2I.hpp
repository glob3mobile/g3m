//
//  Vector2I.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/19/12.
//
//

#ifndef __G3MiOSSDK__Vector2I__
#define __G3MiOSSDK__Vector2I__

#include "Angle.hpp"
#include "MutableVector2I.hpp"
#include "IStringBuilder.hpp"

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
  
  Vector2I add(const Vector2I& that) const {
    return Vector2I(_x + that._x,
                    _y + that._y);
  }
  
  Vector2I sub(const Vector2I& that) const {
    return Vector2I(_x - that._x,
                    _y - that._y);
  }
  
  Vector2I div(double v) const;
  
  double length() const;
  
  double squaredLength() const {
    return _x * _x + _y * _y ;
  }
  
  Angle orientation() const;
  
  MutableVector2I asMutableVector2I() const {
    return MutableVector2I(_x, _y);
  }

  const std::string description() const {
    IStringBuilder* isb = IStringBuilder::newStringBuilder();
    isb->addString("(V2I ");
    isb->addDouble(_x);
    isb->addString(", ");
    isb->addDouble(_y);
    isb->addString(")");
    const std::string s = isb->getString();
    delete isb;
    return s;
  }
#ifdef JAVA_CODE
  @Override
  public String toString() {
    return description();
  }
#endif

  bool isEquals(const Vector2I& that) const;
};

#endif
