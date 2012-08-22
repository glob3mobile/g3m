//
//  URL.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 27/07/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#include "URL.hpp"

#include <sstream>

const std::string URL::description() const {
  std::ostringstream buffer;
  buffer << "URL(";
  buffer << getPath();
  buffer << ")";
  return buffer.str();
}
