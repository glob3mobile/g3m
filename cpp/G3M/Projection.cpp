//
//  Projection.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 11/14/16.
//
//

#include "Projection.hpp"

#include "Vector2D.hpp"
#include "MutableVector2D.hpp"
#include "Geodetic2D.hpp"


Projection::Projection() {

}

Projection::~Projection() {
#ifdef JAVA_CODE
  super.dispose();
#endif
}


Vector2D Projection::getUV(const Angle& latitude,
                           const Angle& longitude) const {
  return Vector2D(getU(longitude),
                  getV(latitude));
}

void Projection::getUV(const Angle& latitude,
                       const Angle& longitude,
                       MutableVector2D& result) const {
  result.set(getU(longitude),
             getV(latitude));
}

Vector2D Projection::getUV(const Geodetic2D& position) const {
  return Vector2D(getU(position._longitude),
                  getV(position._latitude));
}

void Projection::getUV(const Geodetic2D& position,
                       MutableVector2D& result) const {
  result.set(getU(position._longitude),
             getV(position._latitude));
}
