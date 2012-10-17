//
//  Geodetic3D.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 31/05/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#include "Geodetic2D.hpp"

#include "IStringBuilder.hpp"

bool Geodetic2D::isBetween(const Geodetic2D& min,
                           const Geodetic2D& max) const {
  return
  _latitude.isBetween(min.latitude(), max.latitude()) &&
  _longitude.isBetween(min.longitude(), max.longitude());
}

bool Geodetic2D::closeTo(const Geodetic2D &other) const {
  if (!_latitude.closeTo(other._latitude)) {
    return false;
  }
  
  return _longitude.closeTo(other._longitude);
}


const std::string Geodetic2D::description() const {
  IStringBuilder *isb = IStringBuilder::newStringBuilder();
  isb->addString("(lat=");
  isb->addString(_latitude.description());
  isb->addString(", lon=");
  isb->addString(_longitude.description());
  isb->addString(")");
  const std::string s = isb->getString();
  delete isb;
  return s;
}
