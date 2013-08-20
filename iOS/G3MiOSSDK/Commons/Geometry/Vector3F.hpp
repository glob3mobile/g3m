//
//  Vector3F.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/9/13.
//
//

#ifndef __G3MiOSSDK__Vector3F__
#define __G3MiOSSDK__Vector3F__

#include "Vector3D.hpp"

class Vector3F {
private:

  Vector3F& operator=(const Vector3F& that);

public:
  const float _x;
  const float _y;
  const float _z;

  Vector3F(const float x,
           const float y,
           const float z): _x(x), _y(y), _z(z) {

  }

  ~Vector3F() {
  }

  Vector3F(const Vector3F &v): _x(v._x), _y(v._y), _z(v._z) {

  }

  inline float dot(const Vector3D& v) const {
    return ((_x * (float) v._x) +
            (_y * (float) v._y) +
            (_z * (float) v._z));
  }
  
  inline float dot(const Vector3F& v) const {
    return ((_x * v._x) +
            (_y * v._y) +
            (_z * v._z));
  }

  Vector3F normalized() const;

  double length() const {
    return IMathUtils::instance()->sqrt(squaredLength());
  }

  double squaredLength() const {
    return _x * _x + _y * _y + _z * _z;
  }

};

#endif
