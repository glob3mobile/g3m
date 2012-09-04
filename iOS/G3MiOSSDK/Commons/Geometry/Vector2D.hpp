//
//  Vector2D.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 31/05/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#ifndef G3MiOSSDK_Vector2D_hpp
#define G3MiOSSDK_Vector2D_hpp

#include "IMathUtils.hpp"

#include "Angle.hpp"

class MutableVector2D;

class Vector2D {
private:
  const double _x;
  const double _y;
    
    
    Vector2D& operator=(const Vector2D& v);
  
public:
  
  Vector2D(const double x,
           const double y): _x(x), _y(y) {
    
  }
  
  Vector2D(const Vector2D &v): _x(v.x()), _y(v.y()) {
    
  }
  
  Vector2D normalized() const;
  
  double length() const {
    return GMath.sqrt(squaredLength());
  }
  
  Angle orientation() const { return Angle::fromRadians(GMath.atan2(_y, _x)); }
  
  double squaredLength() const {
    return _x * _x + _y * _y ;
  }
  
  Vector2D add(const Vector2D& v) const {
    return Vector2D(_x + v._x,
                    _y + v._y);
  }
  
  Vector2D sub(const Vector2D& v) const {
    return Vector2D(_x - v._x,
                    _y - v._y);
  }
  
  Vector2D times(const Vector2D& v) const {
    return Vector2D(_x * v._x,
                    _y * v._y);
  }
  
  Vector2D times(const double magnitude) const {
    return Vector2D(_x * magnitude,
                    _y * magnitude);
  }
  
  Vector2D div(const Vector2D& v) const {
    return Vector2D(_x / v._x,
                    _y / v._y);
  }
  
  Vector2D div(const double v) const {
    return Vector2D(_x / v,
                    _y / v);
  }
  
  Angle angle() const { 
    double a = GMath.atan2(_y, _x);
    return Angle::fromRadians(a);
  }
  
  double x() const {
    return _x;
  }
  
  double y() const {
    return _y;
  }
  
  static Vector2D nan() {
    return Vector2D(GMath.NanD(), GMath.NanD());
  }
  
  double maxAxis() const {
    return (_x >= _y) ? _x : _y;
  }
  
  double minAxis() const {
    return (_x <= _y) ? _x : _y;
  }
  
  MutableVector2D asMutableVector2D() const;

  bool isNan() const {
    return GMath.isNan(_x) ||  GMath.isNan(_y);
  }

  const std::string description() const;

};


#endif
