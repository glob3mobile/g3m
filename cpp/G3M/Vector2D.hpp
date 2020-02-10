//
//  Vector2D.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 31/05/12.
//

#ifndef G3M_Vector2D
#define G3M_Vector2D

#include <string>

class Angle;
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

  double length() const;

  Angle orientation() const;

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

  Angle angle() const;

  static Vector2D nan();

  double maxAxis() const {
    return (_x >= _y) ? _x : _y;
  }

  double minAxis() const {
    return (_x <= _y) ? _x : _y;
  }

  MutableVector2D asMutableVector2D() const;

  bool isNan() const {
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
  
};


#endif
