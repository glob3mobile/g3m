//
//  Geodetic3D.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 31/05/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#include "Geodetic2D.hpp"

#include "IStringBuilder.hpp"
#include "Vector3D.hpp"

#include <math.h>

bool Geodetic2D::isBetween(const Geodetic2D& min,
                           const Geodetic2D& max) const {
  return
  _latitude.isBetween(min._latitude, max._latitude) &&
  _longitude.isBetween(min._longitude, max._longitude);
}

bool Geodetic2D::closeTo(const Geodetic2D &other) const {
  if (!_latitude.closeTo(other._latitude)) {
    return false;
  }
  
  return _longitude.closeTo(other._longitude);
}


const std::string Geodetic2D::description() const {
  IStringBuilder* isb = IStringBuilder::newStringBuilder();
  isb->addString("(lat=");
  isb->addString(_latitude.description());
  isb->addString(", lon=");
  isb->addString(_longitude.description());
  isb->addString(")");
  const std::string s = isb->getString();
  delete isb;
  return s;
}

Angle Geodetic2D::angleTo(const Geodetic2D& other) const
{
//  const double cos1 = _latitude.cosinus();
//  const Vector3D normal1(cos1 * _longitude.cosinus(),
//                         cos1 * _longitude.sinus(),
//                         _latitude.sinus());
//  const double cos2 = other._latitude.cosinus();
//  const Vector3D normal2(cos2 * other._longitude.cosinus(),
//                         cos2 * other._longitude.sinus(),
//                         other._latitude.sinus());

  const double cos1 = COS(_latitude._radians);
  const Vector3D normal1(cos1 * COS(_longitude._radians),
                         cos1 * SIN(_longitude._radians),
                         SIN(_latitude._radians));
  const double cos2 = COS(other._latitude._radians);
  const Vector3D normal2(cos2 * COS(other._longitude._radians),
                         cos2 * SIN(other._longitude._radians),
                         SIN(other._latitude._radians));

  return Angle::fromRadians(asin(normal1.cross(normal2).squaredLength()));

}

