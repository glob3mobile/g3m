//
//  ShortBuffer_iOS.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/18/13.
//
//

#include "ShortBuffer_iOS.hpp"


#include <sstream>

GLuint ShortBuffer_iOS::_boundIBO = -1;

long long ShortBuffer_iOS::_nextID = 0;

const std::string ShortBuffer_iOS::description() const {
  std::ostringstream oss;

  oss << "ShortBuffer_iOS(";
  oss << "size=";
  oss << _size;
  oss << ", timestamp=";
  oss << _timestamp;
  oss << ", values=";
  oss << _values;
  oss << ")";

  return oss.str();
}
