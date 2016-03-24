//
//  Vector2D.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 31/05/12.
//

#ifndef G3MiOSSDK_Vector2D
#define G3MiOSSDK_Vector2D

#include "IMathUtils.hpp"

#include "Angle.hpp"

class MutableVector2D;

class Vector2D {
private:
  
  
  Vector2D& operator=(const Vector2D& v);
  
public:
  const double _x;
  const double _y;

  static Vector2D zero() {
    return Vector2D(0, 0);
  }
  
  Vector2D(const double x,
           const double y): _x(x), _y(y) {
    
  }
  
  Vector2D(const Vector2D &v): _x(v._x), _y(v._y) {
    
  }
  
  double length() const {
    return IMathUtils::instance()->sqrt(squaredLength());
  }
  
  Angle orientation() const { return Angle::fromRadians(IMathUtils::instance()->atan2(_y, _x)); }
  
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
    double a = IMathUtils::instance()->atan2(_y, _x);
    return Angle::fromRadians(a);
  }

  static Vector2D nan() {
    return Vector2D(NAND, NAND);
  }
  
  double maxAxis() const {
    return (_x >= _y) ? _x : _y;
  }
  
  double minAxis() const {
    return (_x <= _y) ? _x : _y;
  }
  
  MutableVector2D asMutableVector2D() const;
  
  bool isNan() const {
//    return IMathUtils::instance()->isNan(_x) || IMathUtils::instance()->isNan(_y);
    
    if (_x != _x) {
      return true;
    }
    if (_y != _y) {
      return true;
    }
    return false;
  }
  
  const std::string description() const;
#ifdef JAVA_CODE
  @Override
  public String toString() {
    return description();
  }
#endif
  
  static Vector2D intersectionOfTwoLines(const Vector2D& p1, const Vector2D& r1,
                                         const Vector2D& p2, const Vector2D& r2);
  
  double dot(const Vector2D& v) const {
    return _x * v._x + _y * v._y;
  }
  
  static bool isPointInsideTriangle(const Vector2D& p,
                          const Vector2D& cornerA,
                          const Vector2D& cornerB,
                          const Vector2D& cornerC) {
    
    const double alpha = (((cornerB._y - cornerC._y) * (p._x - cornerC._x)) + ((cornerC._x - cornerB._x) * (p._y - cornerC._y)))
    / (((cornerB._y - cornerC._y) * (cornerA._x - cornerC._x)) + ((cornerC._x - cornerB._x) * (cornerA._y - cornerC._y)));
    const double beta = (((cornerC._y - cornerA._y) * (p._x - cornerC._x)) + ((cornerA._x - cornerC._x) * (p._y - cornerC._y)))
    / (((cornerB._y - cornerC._y) * (cornerA._x - cornerC._x)) + ((cornerC._x - cornerB._x) * (cornerA._y - cornerC._y)));
    const double gamma = 1.0 - alpha - beta;
    
    if ((alpha > 0) && (beta > 0) && (gamma > 0)) {
      return true;
    }
    return false;
  }
  
  static bool segmentsIntersect(const Vector2D& a,
                                           const Vector2D& b,
                                           const Vector2D& c,
                                           const Vector2D& d) {
    //http://www.smipple.net/snippet/sparkon/%5BC%2B%2B%5D%202D%20lines%20segment%20intersection%20
    const double den = (((d._y - c._y) * (b._x - a._x)) - ((d._x - c._x) * (b._y - a._y)));
    const double num1 = (((d._x - c._x) * (a._y - c._y)) - ((d._y - c._y) * (a._x - c._x)));
    const double num2 = (((b._x - a._x) * (a._y - c._y)) - ((b._y - a._y) * (a._x - c._x)));
    const double u1 = num1 / den;
    const double u2 = num2 / den;
    
    if ((den == 0) && (num1 == 0) && (num2 == 0)) {
      /* The two lines are coincidents */
      return false;
    }
    if (den == 0) {
      /* The two lines are parallel */
      return false;
    }
    if ((u1 < 0) || (u1 > 1) || (u2 < 0) || (u2 > 1)) {
      /* Lines do not collide */
      return false;
    }
    /* Lines DO collide */
    return true;
  }

};


#endif
