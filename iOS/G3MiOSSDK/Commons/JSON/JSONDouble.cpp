//
//  JSONDouble.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/5/13.
//
//

#include "JSONDouble.hpp"

#include "JSONVisitor.hpp"
#include "IStringBuilder.hpp"

void JSONDouble::acceptVisitor(JSONVisitor* visitor) const {
  visitor->visitDouble(this);
}

const std::string JSONDouble::description() const {
  IStringBuilder* isb = IStringBuilder::newStringBuilder();

  isb->addString("double/");
  isb->addDouble(_value);

  const std::string s = isb->getString();
  delete isb;
  return s;
}
