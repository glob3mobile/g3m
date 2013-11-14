//
//  JSONFloat.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/5/13.
//
//

#include "JSONFloat.hpp"

#include "JSONVisitor.hpp"
#include "IStringBuilder.hpp"

void JSONFloat::acceptVisitor(JSONVisitor* visitor) const {
  visitor->visitFloat(this);
}

const std::string JSONFloat::description() const {
  IStringBuilder* isb = IStringBuilder::newStringBuilder();

  isb->addString("float/");
  isb->addFloat(_value);

  const std::string s = isb->getString();
  delete isb;
  return s;
}
