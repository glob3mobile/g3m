//
//  GEOObject.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/29/12.
//
//

#include "GEOObject.hpp"

#include "Sector.hpp"


GEOObject::~GEOObject() {
  delete _sector;
}

const Sector* GEOObject::getSector() const {
  if (_sector == NULL) {
    _sector = calculateSector();
  }
  return _sector;
}
