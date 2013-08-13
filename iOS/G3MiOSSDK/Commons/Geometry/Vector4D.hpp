//
//  Vector4D.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/6/12.
//
//

#ifndef __G3MiOSSDK__Vector4D__
#define __G3MiOSSDK__Vector4D__

#include "IMathUtils.hpp"
#include "MutableMatrix44D.hpp"

class Vector4D {
private:
  const double _x;
  const double _y;
  const double _z;
  const double _w;
  
  Vector4D& operator=(const Vector4D& that);
  
public:
  
  Vector4D(const double x,
           const double y,
           const double z,
           const double w):
  _x(x),
  _y(y),
  _z(z),
  _w(w)
  {
    
  }
  
  ~Vector4D() {
    JAVA_POST_DISPOSE
  }
  
  Vector4D(const Vector4D &v):
  _x(v._x),
  _y(v._y),
  _z(v._z),
  _w(v._w)
  {
    
  }
  
  static Vector4D nan() {
    const IMathUtils* mu = IMathUtils::instance();

    return Vector4D(mu->NanD(),
                    mu->NanD(),
                    mu->NanD(),
                    mu->NanD());
  }
  
  static Vector4D zero() {
    return Vector4D(0.0, 0.0, 0.0, 0.0);
  }
  
  bool isNan() const {
    const IMathUtils* mu = IMathUtils::instance();

    return (mu->isNan(_x) ||
            mu->isNan(_y) ||
            mu->isNan(_z) ||
            mu->isNan(_w));
  }
  
  bool isZero() const {
    return (_x == 0) && (_y == 0) && (_z == 0) && (_w == 0);
  }
  
//  double x() const {
//    return _x;
//  }
//  
//  double y() const {
//    return _y;
//  }
//  
//  double z() const {
//    return _z;
//  }
//  
//  double w() const {
//    return _w;
//  }
  
  const std::string description() const;

  Vector4D transformedBy(const MutableMatrix44D &m) const;

};

#endif
