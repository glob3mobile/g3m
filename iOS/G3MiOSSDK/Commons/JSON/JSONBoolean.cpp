//
//  JSONBoolean.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/7/12.
//
//

#include "JSONBoolean.hpp"

#include "IStringBuilder.hpp"

const std::string JSONBoolean::description() const {
  IStringBuilder *isb = IStringBuilder::newStringBuilder();
  isb->addBool(_value);
  const std::string s = isb->getString();
  delete isb;
  return s;
}
