//
//  ByteBuffer_iOS.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 10/09/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//


#include "ByteBuffer_iOS.hpp"

#include "IStringBuilder.hpp"

const std::string ByteBuffer_iOS::description() const {
  IStringBuilder* isb = IStringBuilder::newStringBuilder();
  isb->addString("(ByteBuffer_iOS: size=");
  isb->addInt(_size);

//  isb->addString(" [");
//  for (int i = 0; i < _size; i++) {
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
