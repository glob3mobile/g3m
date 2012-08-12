//
//  Geodetic3D.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 31/05/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#include "Geodetic2D.hpp"

#include <sstream>

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
  std::ostringstream buffer;
  buffer << "(lat=";
  buffer << _latitude.description();
  buffer << ", lon=";
  buffer << _longitude.description();
  buffer << ")";
  return buffer.str();
}
