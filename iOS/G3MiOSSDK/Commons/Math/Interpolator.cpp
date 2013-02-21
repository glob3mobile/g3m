//
//  Interpolator.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/20/13.
//
//

#include "Interpolator.hpp"

#include "Angle.hpp"
#include "Geodetic2D.hpp"

double Interpolator::interpolate(const Geodetic2D& sw,
                                 const Geodetic2D& ne,
                                 double valueSW,
                                 double valueSE,
                                 double valueNE,
                                 double valueNW,
                                 const Geodetic2D& position) {
  return interpolate(sw,
                     ne,
                     valueSW,
                     valueSE,
                     valueNE,
                     valueNW,
                     position.latitude(),
                     position.longitude());
}

double Interpolator::interpolate(const Geodetic2D& sw,
                                 const Geodetic2D& ne,
                                 double valueSW,
                                 double valueSE,
                                 double valueNE,
                                 double valueNW,
                                 const Angle& latitude,
                                 const Angle& longitude) {

  const double swLatRadians = sw.latitude().radians();
  const double swLonRadians = sw.longitude().radians();
  const double neLatRadians = ne.latitude().radians();
  const double neLonRadians = ne.longitude().radians();

  const double deltaLonRadians = neLonRadians - swLonRadians;
  const double deltaLatRadians = neLatRadians - swLatRadians;

  const double u = (longitude.radians() - swLonRadians) / deltaLonRadians;
  const double v = (neLatRadians -  latitude.radians()) / deltaLatRadians;

  return interpolate(sw,
                     ne,
                     valueSW,
                     valueSE,
                     valueNE,
                     valueNW,
                     u,
                     v);
}

double Interpolator::interpolate(const Geodetic2D& sw,
                                 const Geodetic2D& ne,
                                 double valueSW,
                                 double valueSE,
                                 double valueNE,
                                 double valueNW,
                                 double u,
                                 double v) {
  const double alphaSW = (1.0 - u) * v;
  const double alphaSE = u         * v;
  const double alphaNE = u         * (1.0 - v);
  const double alphaNW = (1.0 - u) * (1.0 - v);

  return (alphaSW * valueSW) + (alphaSE * valueSE) + (alphaNE * valueNE) + (alphaNW * valueNW);
}
