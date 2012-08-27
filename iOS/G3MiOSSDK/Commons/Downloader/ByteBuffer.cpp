//
//  ByteBuffer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 15/08/12.
//
//

#include "ByteBuffer.hpp"

#include "IStringBuilder.hpp"

#include "IStringUtils.hpp"

const std::string ByteBuffer::description() const {
  IStringBuilder *isb = IStringBuilder::newStringBuilder();
  isb->add("ByteBuffer(length=")->add(_length)->add(")");
  std::string s = isb->getString();
  delete isb;
  return s;
}

std::string ByteBuffer::getDataAsString() const {
  return IStringUtils::instance()->createString(_data, _length);
}
