//
//  WGS84Projetion.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/14/16.
//
//

#include "WGS84Projetion.hpp"

#include <stddef.h>


WGS84Projetion* WGS84Projetion::INSTANCE = NULL;


WGS84Projetion::WGS84Projetion() {

}

WGS84Projetion::~WGS84Projetion() {
#ifdef JAVA_CODE
  super.dispose();
#endif
}

WGS84Projetion* WGS84Projetion::instance() {
  if (INSTANCE == NULL) {
    INSTANCE = new WGS84Projetion();
  }
  return INSTANCE;
}

const std::string WGS84Projetion::getEPSG() const {
  return "EPSG:4326";
}
