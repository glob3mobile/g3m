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

double Interpolator::interpolation(const Geodetic2D& sw,
                                   const Geodetic2D& ne,
                                   double valueSW,
                                   double valueSE,
                                   double valueNE,
                                   double valueNW,
                                   const Geodetic2D& position) {
  return interpolation(sw,
                       ne,
                       valueSW,
                       valueSE,
                       valueNE,
                       valueNW,
                       position.latitude(),
                       position.longitude());
}

double Interpolator::interpolation(const Geodetic2D& sw,
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

  return interpolation(valueSW,
                       valueSE,
                       valueNE,
                       valueNW,
                       u,
                       v);
}
