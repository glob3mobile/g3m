//
//  Ray.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 3/24/17.
//
//

#ifndef Ray_hpp
#define Ray_hpp

#include "Vector3D.hpp"
#include <string>


class Ray {
public:
  const Vector3D _origin;
  const Vector3D _direction;

  Ray(const Vector3D& origin,
      const Vector3D& direction);

  ~Ray();

  const std::string description() const;
#ifdef JAVA_CODE
  @Override
  public String toString() {
    return description();
  }
#endif

};

#endif
