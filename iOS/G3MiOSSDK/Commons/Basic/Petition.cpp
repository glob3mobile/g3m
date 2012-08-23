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
  isb->add("Petition(url=")->add(_url.description())->add(", sector=")->add(_sector->description())->add(", buffer=");
  if (_buffer == NULL) {
    isb->add("NULL");
  }
  else {
    isb->add(_buffer->description() );
  }
  isb->add(", downloadID=")->add(_downloadID)->add(", transparentImage=")->addBool(_transparentImage)->add(")");
  std::string s = isb->getString();
  
  delete isb;
  return s;

}
