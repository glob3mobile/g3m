//
//  MutableVector2D.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 31/05/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#ifndef G3MiOSSDK_MutableVector2D
#define G3MiOSSDK_MutableVector2D

#include "IMathUtils.hpp"

#include "Angle.hpp"
#include "Vector2D.hpp"

class MutableVector2D {
private:
  double _x;
  double _y;
  
public:
  
  MutableVector2D(): _x(0.0), _y(0.0) {}
  
  Vector2D asVector2D() const{ 
    Vector2D v(_x, _y);
    return v;
  }

  MutableVector2D(const double x,
                  const double y): _x(x), _y(y) {
    
  }
  
  MutableVector2D(const MutableVector2D &v): _x(v.x()), _y(v.y()) {
    
  }
  
  static MutableVector2D nan() {
    return MutableVector2D(NAND, NAND);
  }
  
  bool isEquals(double x, double y) const{
    return _x == x && _y == y;
  }
  
  bool isNan() const {
    return ISNAN(_x) || ISNAN(_y);
  }

  MutableVector2D normalized() const;
  
  double length() const {
    return IMathUtils::instance()->sqrt(squaredLength());
  }
  
  double squaredLength() const {
    return _x * _x + _y * _y ;
  }
  
  MutableVector2D add(const MutableVector2D& v) const {
    return MutableVector2D(_x + v._x,
                           _y + v._y);
  }
  
  MutableVector2D sub(const MutableVector2D& v) const {
    return MutableVector2D(_x - v._x,
                           _y - v._y);
  }
  
  MutableVector2D times(const MutableVector2D& v) const {
    return MutableVector2D(_x * v._x,
                           _y * v._y);
  }
  
  MutableVector2D times(const double magnitude) const {
    return MutableVector2D(_x * magnitude,
                           _y * magnitude);
  }
  
  MutableVector2D div(const MutableVector2D& v) const {
    return MutableVector2D(_x / v._x,
                           _y / v._y);
  }
  
  MutableVector2D div(const double v) const {
    return MutableVector2D(_x / v,
                           _y / v);
  }
  
  Angle angle() const { 
    double a = IMathUtils::instance()->atan2(_y, _x);
    return Angle::fromRadians(a);
  }
  
  double x() const {
    return _x;
  }
  
  double y() const {
    return _y;
  }
  
  const std::string description() const;
#ifdef JAVA_CODE
  @Override
  public String toString() {
    return description();
  }
#endif

};


#endif
