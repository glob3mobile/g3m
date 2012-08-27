//
//  ByteBuffer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 15/08/12.
//
//

#include "ByteBuffer.hpp"

#include "IStringUtils.hpp"

#include <sstream>

const std::string ByteBuffer::description() const {
  std::ostringstream buffer;
  buffer << "ByteBuffer(length=";
  buffer << _length;
  buffer << ")";
  return buffer.str();
}

std::string ByteBuffer::getDataAsString() const {
  return IStringUtils::instance()->createString(_data, _length);
}
