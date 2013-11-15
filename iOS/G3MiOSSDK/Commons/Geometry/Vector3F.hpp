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
           const float z);

  ~Vector3F();

  Vector3F(const Vector3F &v);

  inline float dot(const Vector3F& v) const;

  Vector3F normalized() const;

  double length() const;

  double squaredLength() const;

  Vector3F sub(const Vector3F& that) const;

  Vector3F cross(const Vector3F& that) const;

};

#endif
