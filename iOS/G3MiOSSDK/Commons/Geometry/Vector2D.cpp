//
//  Vector2D.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 31/05/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#include "Vector2D.hpp"

#include "MutableVector2D.hpp"

#include "IStringBuilder.hpp"

MutableVector2D Vector2D::asMutableVector2D() const {
  return MutableVector2D(_x, _y);
}

const std::string Vector2D::description() const {
  IStringBuilder *isb = IStringBuilder::newStringBuilder();
  isb->add("(V2D ")->add(_x)->add(", ")->add(_y)->add(")");
  std::string s = isb->getString();
  delete isb;
  return s;
}
