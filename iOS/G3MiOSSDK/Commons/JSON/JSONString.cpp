//
//  JSONString.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/7/12.
//
//

#include "JSONString.hpp"
#include "IStringBuilder.hpp"

#include "JSONVisitor.hpp"

const std::string JSONString::description() const {
  IStringBuilder* isb = IStringBuilder::newStringBuilder();

  isb->addString("\"");
  isb->addString(_value);
  isb->addString("\"");

  const std::string s = isb->getString();
  delete isb;
  return s;
}

void JSONString::acceptVisitor(JSONVisitor* visitor) const {
  visitor->visitString(this);
}
