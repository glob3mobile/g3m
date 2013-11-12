//
//  IntBuffer_iOS.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 06/09/12.
//
//

#include "IntBuffer_iOS.hpp"
#include <sstream>

long long IntBuffer_iOS::_nextID = 0;

const std::string IntBuffer_iOS::description() const {
  std::ostringstream oss;

  oss << "IntBuffer_iOS(";
  oss << "size=";
  oss << _size;
  oss << ", timestamp=";
  oss << _timestamp;
  oss << ", values=";
  oss << _values;
  oss << ")";

  return oss.str();
}
