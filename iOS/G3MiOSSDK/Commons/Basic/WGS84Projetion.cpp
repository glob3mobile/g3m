//
//  WGS84Projetion.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/14/16.
//
//

#include "WGS84Projetion.hpp"

#include <stddef.h>

#include "Angle.hpp"
#include "IMathUtils.hpp"


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

double WGS84Projetion::getU(const Angle& longitude) const {
  return (longitude._radians + PI) / (PI*2);
}

double WGS84Projetion::getV(const Angle& latitude) const {
  return (HALF_PI - latitude._radians) / PI;
}
