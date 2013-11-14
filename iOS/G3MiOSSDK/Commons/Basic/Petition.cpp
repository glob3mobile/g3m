//
//  Petition.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 20/08/12.
//
//

#include "Petition.hpp"

#include "IStringBuilder.hpp"
#include "IFactory.hpp"

const std::string Petition::description() const {
  IStringBuilder* isb = IStringBuilder::newStringBuilder();
  isb->addString("Petition(url=");
  isb->addString(_url.description());
  isb->addString(", sector=");
  isb->addString(_sector->description());
  isb->addString(", buffer=");
  if (_image == NULL) {
    isb->addString("NULL");
  }
  else {
    isb->addString( _image->description() );
  }

  const std::string s = isb->getString();
  delete isb;
  return s;
}

void Petition::releaseImage() {
  IFactory::instance()->deleteImage(_image);
  _image = NULL;
}
