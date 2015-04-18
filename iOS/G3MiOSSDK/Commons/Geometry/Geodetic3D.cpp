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
  IStringBuilder* isb = IStringBuilder::newStringBuilder();
  isb->addString("(lat=");
  isb->addString(_latitude.description());
  isb->addString(", lon=");
  isb->addString(_longitude.description());
  isb->addString(", height=");
  isb->addDouble(_height);
  isb->addString(")");
  const std::string s = isb->getString();
  delete isb;
  return s;
}

bool Geodetic3D::isEquals(const Geodetic3D& that) const {
  return (_latitude.isEquals(that._latitude)   &&
          _longitude.isEquals(that._longitude) &&
          (_height == that._height));
}
