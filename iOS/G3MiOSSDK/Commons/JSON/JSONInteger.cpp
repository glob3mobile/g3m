//
//  JSONInteger.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/5/13.
//
//

#include "JSONInteger.hpp"

#include "JSONVisitor.hpp"
#include "IStringBuilder.hpp"

void JSONInteger::acceptVisitor(JSONVisitor* visitor) const {
  visitor->visitInteger(this);
}

const std::string JSONInteger::description() const {
  IStringBuilder* isb = IStringBuilder::newStringBuilder();

  isb->addString("int/");
  isb->addInt(_value);

  const std::string s = isb->getString();
  delete isb;
  return s;
}
