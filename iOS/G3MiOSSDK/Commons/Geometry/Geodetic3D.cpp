//
//  Geodetic3D.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 31/05/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#include "Geodetic3D.hpp"

#include <sstream>

const std::string Geodetic3D::description() const {
  std::ostringstream buffer;
  buffer << "(lat=";
  buffer << _latitude.description();
  buffer << ", lon=";
  buffer << _longitude.description();
  buffer << ", height=";
  buffer << _height;
  buffer << ")";
  return buffer.str();
}
