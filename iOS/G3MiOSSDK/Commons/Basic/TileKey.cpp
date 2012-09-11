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
  IStringBuilder *isb = IStringBuilder::newStringBuilder();
  isb->add("(level=")->add(_level)->add(", row=")->add(_row)->add(", col=")->add(_column)->add(")");
  std::string s = isb->getString();
  delete isb;
  return s;  
}

const std::string TileKey::tinyDescription() const {
  IStringBuilder *isb = IStringBuilder::newStringBuilder();
  isb->add(_level)->add("-")->add(_row)->add("/")->add(_column);
  std::string s = isb->getString();
  delete isb;
  return s; 
}
