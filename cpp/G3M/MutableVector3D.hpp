//
//  MutableVector3D.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 31/05/12.
//

#ifndef G3M_MutableVector3D
#define G3M_MutableVector3D

class Vector3D;
class Angle;
class MutableMatrix44D;


class MutableVector3D {
public:
  double _x;
  double _y;
  double _z;


  MutableVector3D() :
  _x(0),
  _y(0),
  _z(0)
  {
  }

  MutableVector3D(const double x,
                  const double y,
                  const double z) :
  _x(x),
  _y(y),
  _z(z)
  {
  }

  MutableVector3D(const MutableVector3D &v) :
  _x(v._x),
  _y(v._y),
  _z(v._z)
  {
  }

  explicit MutableVector3D(const Vector3D &v);

  void set(const double x,
           const double y,
           const double z) {
    _x = x;
    _y = y;
    _z = z;
  }

  void copyFrom(const MutableVector3D& that) {
    _x = that._x;
    _y = that._y;
    _z = that._z;
  }

  void copyFrom(const Vector3D& that);

  MutableVector3D normalized() const;
  void normalize();

  static MutableVector3D nan();

  bool equalTo(const MutableVector3D& v) const {
    return (v._x == _x && v._y == _y && v._z == _z);
  }

  bool isNan() const;

  bool isZero() const {
    return (_x == 0) && (_y == 0) && (_z == 0);
  }

  double length() const;

  double squaredLength() const {
    return _x * _x + _y * _y + _z * _z;
  }

  double dot(const MutableVector3D& v) const {
    return _x * v._x + _y * v._y + _z * v._z;
  }

  MutableVector3D add(const MutableVector3D& v) const {
    return MutableVector3D(_x + v._x,
                           _y + v._y,
                           _z + v._z);
  }

  void addInPlace(const MutableVector3D& that) {
    _x += that._x;
    _y += that._y;
    _z += that._z;
  }

  void addInPlace(const Vector3D& that);

  MutableVector3D sub(const MutableVector3D& v) const {
    return MutableVector3D(_x - v._x,
                           _y - v._y,
                           _z - v._z);
  }

  Vector3D sub(const Vector3D& v) const;

  MutableVector3D times(const MutableVector3D& v) const {
    return MutableVector3D(_x * v._x,
                           _y * v._y,
                           _z * v._z);
  }

  MutableVector3D times(const Vector3D& v) const;

  MutableVector3D times(const double magnitude) const {
    return MutableVector3D(_x * magnitude,
                           _y * magnitude,
                           _z * magnitude);
  }

  MutableVector3D div(const MutableVector3D& v) const {
    return MutableVector3D(_x / v._x,
                           _y / v._y,
                           _z / v._z);
  }

  MutableVector3D div(const double v) const {
    return MutableVector3D(_x / v,
                           _y / v,
                           _z / v);
  }

  MutableVector3D cross(const MutableVector3D& other) const {
    return MutableVector3D(_y * other._z - _z * other._y,
                           _z * other._x - _x * other._z,
                           _x * other._y - _y * other._x);
  }

  Angle angleBetween(const MutableVector3D& other) const;

  MutableVector3D rotatedAroundAxis(const MutableVector3D& other,
                                    const Angle& theta) const;

  double x() const {
    return _x;
  }

  double y() const {
    return _y;
  }

  double z() const {
    return _z;
  }

  MutableVector3D transformedBy(const MutableMatrix44D &m,
                                const double homogeneus) const;

  Vector3D asVector3D() const;

  void putSub(const MutableVector3D& a,
              const Vector3D& b);

  static double normalizedDot(const MutableVector3D& a,
                              const MutableVector3D& b);

  static double angleInRadiansBetween(const MutableVector3D& a,
                                      const MutableVector3D& b);

  MutableVector3D rotateAroundAxis(const MutableVector3D& axis,
                                   const Angle& theta) const;

  const double squaredDistanceTo(const Vector3D& that) const;

  const double squaredDistanceTo(const MutableVector3D& that) const {
    const double dx = _x - that._x;
    const double dy = _y - that._y;
    const double dz = _z - that._z;
    return (dx * dx) + (dy * dy) + (dz * dz);
  }
  
};


#endif
