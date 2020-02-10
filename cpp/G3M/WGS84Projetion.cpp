//
//  WGS84Projetion.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 11/14/16.
//
//

#include "WGS84Projetion.hpp"

#include <stddef.h>

#include "Angle.hpp"
#include "IMathUtils.hpp"
#include "Sector.hpp"


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

const Angle WGS84Projetion::getInnerPointLongitude(double u) const {
  return Angle::fromRadians(IMathUtils::instance()->linearInterpolation(-PI, PI, u));
}

const Angle WGS84Projetion::getInnerPointLatitude(double v) const {
  return Angle::fromRadians(IMathUtils::instance()->linearInterpolation(-HALF_PI, HALF_PI, 1.0 - v));
}

const Angle WGS84Projetion::getInnerPointLongitude(const Sector& sector,
                                                   double u) const {
  return Angle::linearInterpolation(sector._lower._longitude,
                                    sector._upper._longitude,
                                    u);
}

const Angle WGS84Projetion::getInnerPointLatitude(const Sector& sector,
                                                  double v) const {
  return Angle::linearInterpolation(sector._lower._latitude,
                                    sector._upper._latitude,
                                    1.0 - v);
}
