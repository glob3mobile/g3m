//
//  WebMercatorProjection.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/14/16.
//
//

#include "WebMercatorProjection.hpp"

#include <stddef.h>

#include "Angle.hpp"
#include "IMathUtils.hpp"


WebMercatorProjection* WebMercatorProjection::INSTANCE = NULL;

const double WebMercatorProjection::_upperLimitRadians = Angle::fromDegrees(85.0511287798)._radians;
const double WebMercatorProjection::_lowerLimitRadians = Angle::fromDegrees(-85.0511287798)._radians;

//const double WebMercatorProjection::_upperLimitDegrees = 85.0511287798;
//const double WebMercatorProjection::_lowerLimitDegrees = -85.0511287798;


WebMercatorProjection::WebMercatorProjection() {

}

WebMercatorProjection::~WebMercatorProjection() {
#ifdef JAVA_CODE
  super.dispose();
#endif
}

WebMercatorProjection* WebMercatorProjection::instance() {
  if (INSTANCE == NULL) {
    INSTANCE = new WebMercatorProjection();
  }
  return INSTANCE;
}

const std::string WebMercatorProjection::getEPSG() const {
  return "EPSG:3857";
}

double WebMercatorProjection::getU(const Angle& longitude) const {
  return (longitude._radians + PI) / (PI*2);
}

double WebMercatorProjection::getV(const Angle& latitude) const {
  const double latitudeRadians = latitude._radians;

  if (latitudeRadians >= _upperLimitRadians) {
    return 0;
  }
  if (latitudeRadians <= _lowerLimitRadians) {
    return 1;
  }

  const IMathUtils* mu = IMathUtils::instance();
  const double pi4 = PI * 4;
  const double latSin = SIN(latitudeRadians);
  return 0.5 - ( mu->log( (1.0 + latSin) / (1.0 - latSin) ) / pi4 );
}
