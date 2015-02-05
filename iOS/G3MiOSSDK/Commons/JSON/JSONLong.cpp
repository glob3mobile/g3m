//
//  JSONLong.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/5/13.
//
//

#include "JSONLong.hpp"

#include "JSONVisitor.hpp"
#include "IStringBuilder.hpp"

void JSONLong::acceptVisitor(JSONVisitor* visitor) const {
  visitor->visitLong(this);
}

const std::string JSONLong::description() const {
  IStringBuilder* isb = IStringBuilder::newStringBuilder();

  isb->addString("long/");
  isb->addLong(_value);

  const std::string s = isb->getString();
  delete isb;
  return s;
}
