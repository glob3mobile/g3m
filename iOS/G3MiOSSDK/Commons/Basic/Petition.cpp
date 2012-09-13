//
//  Petition.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 20/08/12.
//
//

#include "Petition.hpp"

#include "IStringBuilder.hpp"

const std::string Petition::description() const {
  
  IStringBuilder *isb = IStringBuilder::newStringBuilder();
  isb->add("Petition(url=");
  isb->add(_url.description());
  isb->add(", sector=");
  isb->add(_sector->description());
  isb->add(", buffer=");
  if (_image == NULL) {
    isb->add("NULL");
  }
  else {
    isb->add(_image->description() );
  }
  std::string s = isb->getString();
  
  delete isb;
  return s;
}
