//
//  MercatorUtils.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/7/13.
//
//

#include "MercatorUtils.hpp"

#include "IMathUtils.hpp"

const Angle MercatorUtils::_upperLimit = Angle::fromDegrees(85.0511287798);
const Angle MercatorUtils::_lowerLimit = Angle::fromDegrees(-85.0511287798);

double MercatorUtils::_upperLimitInRadians = Angle::fromDegrees(85.0511287798)._radians;
double MercatorUtils::_lowerLimitInRadians = Angle::fromDegrees(-85.0511287798)._radians;

double MercatorUtils::_upperLimitInDegrees = 85.0511287798;
double MercatorUtils::_lowerLimitInDegrees = -85.0511287798;

double MercatorUtils::_originShift = 2.0 * 3.14159265358979323846264338327950288 * 6378137.0 / 2.0;



double MercatorUtils::getMercatorV(const Angle& latitude) {
  if (latitude._degrees >= _upperLimitInDegrees) {
    return 0;
  }
  if (latitude._degrees <= _lowerLimitInDegrees) {
    return 1;
  }

  const IMathUtils* mu = IMathUtils::instance();
  const double pi4 = PI * 4;

  const double latSin = SIN(latitude._radians);
  return 1.0 - ( ( mu->log( (1.0 + latSin) / (1.0 - latSin) ) / pi4 ) + 0.5 );
}

const Angle MercatorUtils::toLatitude(double v) {
  const IMathUtils* mu = IMathUtils::instance();

  const double exp = mu->exp(-2 * PI * (1.0 - v - 0.5));
  const double atan = mu->atan(exp);
  return Angle::fromRadians((PI / 2) - 2 * atan);
}

double MercatorUtils::latitudeToMeters(const Angle& latitude) {
  if (latitude._degrees >= _upperLimitInDegrees) {
    return 20037508.342789244;
  }
  if (latitude._degrees <= _lowerLimitInDegrees) {
    return -20037508.342789244;
  }

  const IMathUtils* mu = IMathUtils::instance();

  double my = mu->log( mu->tan( (90 + latitude._degrees) * PI / 360.0 ) ) / (PI / 180.0);
		my = my * _originShift / 180.0;

		return my;
}
