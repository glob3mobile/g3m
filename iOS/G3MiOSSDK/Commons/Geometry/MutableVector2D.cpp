//
//  MutableVector2D.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 31/05/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
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
