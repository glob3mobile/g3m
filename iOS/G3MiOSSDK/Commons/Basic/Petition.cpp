//
//  Petition.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 20/08/12.
//
//

#include "Petition.hpp"

#include <sstream>

const std::string Petition::description() const {
  std::ostringstream buffer;
  buffer << "Petition(url=";
  buffer << _url.description();
  buffer << ", sector=";
  buffer << _sector->description();
  buffer << ", buffer=";
  if (_buffer == NULL) {
    buffer << "NULL";
  }
  else {
    buffer << _buffer->description();
  }
  buffer << ", downloadID=";
  buffer << _downloadID;
  buffer << ", transparentImage=";
  buffer << _transparentImage;
  buffer << ")";
  return buffer.str();
}
