//
//  TileKey.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 09/08/12.
//
//

#include "TileKey.hpp"

#include "IStringBuilder.hpp"

const std::string TileKey::description() const {  
  IStringBuilder* isb = IStringBuilder::newStringBuilder();
  isb->addString("(level=");
  isb->addInt(_level);
  isb->addString(", row=");
  isb->addInt(_row);
  isb->addString(", col=");
  isb->addInt(_column);
  isb->addString(")");
  std::string s = isb->getString();
  delete isb;
  return s;  
}

const std::string TileKey::tinyDescription() const {
  IStringBuilder* isb = IStringBuilder::newStringBuilder();
  isb->addInt(_level);
  isb->addString("-");
  isb->addInt(_row);
  isb->addString("/");
  isb->addInt(_column);
  std::string s = isb->getString();
  delete isb;
  return s; 
}
