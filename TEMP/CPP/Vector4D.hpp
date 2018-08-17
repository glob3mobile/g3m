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
  }
  
  Vector4D(const Vector4D &v):
  _x(v._x),
  _y(v._y),
  _z(v._z),
  _w(v._w)
  {
    
  }
  
  static Vector4D nan() {
    return Vector4D(NAND,NAND,NAND,NAND);
  }
  
  static Vector4D zero() {
    return Vector4D(0.0, 0.0, 0.0, 0.0);
  }
  
  bool isNan() const {
    return (ISNAN(_x) ||
            ISNAN(_y) ||
            ISNAN(_z) ||
            ISNAN(_w));
  }
  
  bool isZero() const {
    return (_x == 0) && (_y == 0) && (_z == 0) && (_w == 0);
  }
    
  const std::string description() const;
#ifdef JAVA_CODE
  @Override
  public String toString() {
    return description();
  }
#endif

  Vector4D transformedBy(const MutableMatrix44D &m) const;

};

#endif
