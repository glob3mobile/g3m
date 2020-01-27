//
//  ByteBuffer_iOS.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 10/09/12.
//


#include "ByteBuffer_iOS.hpp"

#include "G3MSharedSDK/IStringBuilder.hpp"


const std::string ByteBuffer_iOS::description() const {
  IStringBuilder* isb = IStringBuilder::newStringBuilder();
  isb->addString("(ByteBuffer_iOS: size=");
  isb->addLong(_size);

//  isb->addString(" [");
//  for (size_t i = 0; i < _size; i++) {
//    if (i != 0) {
//      isb->addString(",");
//    }
//    isb->addInt(_values[i]);
//  }
//  isb->addString("]");

  isb->addString(")");
  const std::string s = isb->getString();
  delete isb;
  return s;
}

const std::string ByteBuffer_iOS::getAsString() const {
  return std::string(_values, _values + _size);
}
