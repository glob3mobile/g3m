//
//  CartoCSSTokens.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 9/17/13.
//
//

#include "CartoCSSTokens.hpp"

#include "IStringBuilder.hpp"


const std::string ErrorCartoCSSToken::description() const {
  IStringBuilder* isb = IStringBuilder::newStringBuilder();
  isb->addString("[Error description=\"");
  isb->addString(_description);
  isb->addString("\" at ");
  isb->addInt(_position);
  isb->addString("]");
  const std::string s = isb->getString();
  delete isb;
  return s;
}


const std::string ExpressionCartoCSSToken::description() const {
  IStringBuilder* isb = IStringBuilder::newStringBuilder();
  isb->addString("[Expression source=\"");
  isb->addString(_source);
  isb->addString("\"]");
  const std::string s = isb->getString();
  delete isb;
  return s;
}


const std::string StringCartoCSSToken::description() const {
  IStringBuilder* isb = IStringBuilder::newStringBuilder();
  isb->addString("[String \"");
  isb->addString(_str);
  isb->addString("\"]");
  const std::string s = isb->getString();
  delete isb;
  return s;
}

void StringCartoCSSToken::appendString(const std::string& str) {
  _str = _str + str;
}
