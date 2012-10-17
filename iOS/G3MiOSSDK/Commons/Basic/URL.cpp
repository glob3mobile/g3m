//
//  URL.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 27/07/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#include "URL.hpp"

#include "IStringBuilder.hpp"

const std::string URL::description() const {
  IStringBuilder *isb = IStringBuilder::newStringBuilder();
  isb->addString("URL(");
  isb->addString(getPath());
  isb->addString(")");
  const std::string s = isb->getString();
  delete isb;
  return s; 
}
