//
//  MutableVector2D.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 31/05/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#include "MutableVector2D.hpp"

#include <sstream>

const std::string MutableVector2D::description() const {
  std::ostringstream buffer;
  buffer << "(MV2D ";
  buffer << _x;
  buffer << ", ";
  buffer << _y;
  buffer << ")";
  return buffer.str();
}
