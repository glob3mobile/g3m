//
//  Vector3D.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 31/05/12.
//

#ifndef G3MiOSSDK_Vector3D
#define G3MiOSSDK_Vector3D

#include <string>

class MutableMatrix44D;
class MutableVector3D;
class Angle;


class Vector3D {
private:

  Vector3D& operator=(const Vector3D& that);

public:

  static const Vector3D ZERO;
  static const Vector3D NANV;
  static const Vector3D UP_X;
  static const Vector3D DOWN_X;
  static const Vector3D UP_Y;
  static const Vector3D DOWN_Y;
  static const Vector3D UP_Z;
  static const Vector3D DOWN_Z;

  const double _x;
  const double _y;
  const double _z;


  Vector3D(const double x,
           const double y,
           const double z) :
  _x(x),
  _y(y),
  _z(z) {
  }

  ~Vector3D() {
  }

  Vector3D(const Vector3D &v) :
  _x(v._x),
  _y(v._y),
  _z(v._z)
  {
  }

  bool isNan() const;

  bool isEquals(const Vector3D& v) const {
    return (v._x == _x &&
            v._y == _y &&
            v._z == _z);
  }

  bool isZero() const {
    return (_x == 0) && (_y == 0) && (_z == 0);
  }

  const Vector3D normalized() const;

  double length() const;

  double squaredLength() const {
    return _x * _x + _y * _y + _z * _z;
  }

  double dot(const Vector3D& v) const {
    return _x * v._x + _y * v._y + _z * v._z;
  }

  bool isPerpendicularTo(const Vector3D& v) const;

  Vector3D add(const Vector3D& v) const {
    return Vector3D(_x + v._x,
                    _y + v._y,
                    _z + v._z);
  }

  Vector3D add(double d) const {
    return Vector3D(_x + d,
                    _y + d,
                    _z + d);
  }

  Vector3D sub(const Vector3D& v) const {
    return Vector3D(_x - v._x,
                    _y - v._y,
                    _z - v._z);
  }

  Vector3D sub(const MutableVector3D& v) const;

  Vector3D sub(double d) const {
    return Vector3D(_x - d,
                    _y - d,
                    _z - d);
  }

  Vector3D times(const Vector3D& v) const {
    return Vector3D(_x * v._x,
                    _y * v._y,
                    _z * v._z);
  }

  Vector3D times(const double magnitude) const {
    return Vector3D(_x * magnitude,
                    _y * magnitude,
                    _z * magnitude);
  }

  Vector3D div(const Vector3D& v) const {
    return Vector3D(_x / v._x,
                    _y / v._y,
                    _z / v._z);
  }

  Vector3D div(const double v) const {
    return Vector3D(_x / v,
                    _y / v,
                    _z / v);
  }

  Vector3D cross(const Vector3D& other) const {
    return Vector3D(_y * other._z - _z * other._y,
                    _z * other._x - _x * other._z,
                    _x * other._y - _y * other._x);
  }

  Angle angleBetween(const Vector3D& other) const;
  double angleInRadiansBetween(const Vector3D& other) const;
  Angle signedAngleBetween(const Vector3D& other, const Vector3D& up) const;

  static double normalizedDot(const Vector3D& a,
                              const Vector3D& b);

  static double normalizedDot(const Vector3D& a,
                              const MutableVector3D& b);

  static double angleInRadiansBetween(const Vector3D& a,
                                      const Vector3D& b);

  static double angleInRadiansBetween(const Vector3D& a,
                                      const MutableVector3D& b);

  static Angle angleBetween(const Vector3D& a,
                            const Vector3D& b);

  Vector3D rotateAroundAxis(const Vector3D& axis,
                            const Angle& theta) const;

  Vector3D transformedBy(const MutableMatrix44D& m, const double homogeneus) const;

  MutableVector3D asMutableVector3D() const;

  double maxAxis() const;
  double minAxis() const;

  double axisAverage() const;

  Vector3D projectionInPlane(const Vector3D& normal) const;

  const std::string description() const;
#ifdef JAVA_CODE
  @Override
  public String toString() {
    return description();
  }
#endif

  const Vector3D clamp(const Vector3D& min,
                       const Vector3D& max) const;

  const double squaredDistanceTo(const Vector3D& that) const;

  const double distanceTo(const Vector3D& that) const;
  
};


#endif
