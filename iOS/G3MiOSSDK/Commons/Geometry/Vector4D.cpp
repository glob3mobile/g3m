//
//  Vector4D.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/6/12.
//
//

#include "Vector4D.hpp"

#include "IStringBuilder.hpp"

const std::string Vector4D::description() const {
  IStringBuilder* isb = IStringBuilder::newStringBuilder();
  isb->addString("(V4D ");
  isb->addDouble(_x);
  isb->addString(", ");
  isb->addDouble(_y);
  isb->addString(", ");
  isb->addDouble(_z);
  isb->addString(", ");
  isb->addDouble(_w);
  isb->addString(")");
  const std::string s = isb->getString();
  delete isb;
  return s;
}
