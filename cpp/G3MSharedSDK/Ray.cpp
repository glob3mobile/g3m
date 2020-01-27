//
//  Ray.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/24/17.
//
//

#include "Ray.hpp"

#include "IStringBuilder.hpp"

Ray::Ray(const Vector3D& origin,
         const Vector3D& direction):
_origin(origin),
_direction(direction.normalized())
{
}

Ray::~Ray() {
}

const std::string Ray::description() const {
  IStringBuilder* isb = IStringBuilder::newStringBuilder();
  isb->addString("(Ray origin=");
  isb->addString(_origin.description());
  isb->addString(", direction=");
  isb->addString(_direction.description());
  isb->addString(")");
  const std::string s = isb->getString();
  delete isb;
  return s;
}
