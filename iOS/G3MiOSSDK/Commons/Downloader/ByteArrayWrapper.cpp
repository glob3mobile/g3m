//
//  ByteArrayWrapper.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 15/08/12.
//
//

#include "ByteArrayWrapper.hpp"

#include "IStringBuilder.hpp"

#include "IStringUtils.hpp"

const std::string ByteArrayWrapper::description() const {
  IStringBuilder *isb = IStringBuilder::newStringBuilder();
  isb->add("ByteArrayWrapper(length=")->add(_length)->add(")");
  std::string s = isb->getString();
  delete isb;
  return s;
}

std::string ByteArrayWrapper::getDataAsString() const {
  return IStringUtils::instance()->createString(_data, _length);
}
