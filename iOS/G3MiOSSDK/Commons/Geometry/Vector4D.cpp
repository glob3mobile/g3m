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
  IStringBuilder *isb = IStringBuilder::newStringBuilder();
  isb->add("(V4D ")->add(_x)->add(", ")->add(_y)->add(", ")->add(_z)->add(", ")->add(_w)->add(")");
  std::string s = isb->getString();
  delete isb;
  return s;
}
