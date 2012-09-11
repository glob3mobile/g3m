//
//  Geodetic3D.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 31/05/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#include "Geodetic3D.hpp"

#include "IStringBuilder.hpp"

const std::string Geodetic3D::description() const {
  IStringBuilder *isb = IStringBuilder::newStringBuilder();
  isb->add("(lat=")->add(_latitude.description())->add(", lon=")->add(_longitude.description());
  isb->add(", height=")->add(_height)->add(")");
  std::string s = isb->getString();
  delete isb;
  return s;
}
