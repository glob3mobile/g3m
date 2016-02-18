//
//  MutableVector2D.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 31/05/12.
//

#include "MutableVector2D.hpp"

#include "IStringBuilder.hpp"

const std::string MutableVector2D::description() const {
  IStringBuilder* isb = IStringBuilder::newStringBuilder();
  isb->addString("(MV2D ");
  isb->addDouble(_x);
  isb->addString(", ");
  isb->addDouble(_y);
  isb->addString(")");
  const std::string s = isb->getString();
  delete isb;
  return s;
}
