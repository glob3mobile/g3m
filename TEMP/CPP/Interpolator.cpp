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
                                   const Geodetic2D& position) const {
  return interpolation(sw,
                       ne,
                       valueSW,
                       valueSE,
                       valueNE,
                       valueNW,
                       position._latitude,
                       position._longitude);
}

double Interpolator::interpolation(const Geodetic2D& sw,
                                   const Geodetic2D& ne,
                                   double valueSW,
                                   double valueSE,
                                   double valueNE,
                                   double valueNW,
                                   const Angle& latitude,
                                   const Angle& longitude) const {

  const double swLatRadians = sw._latitude._radians;
  const double swLonRadians = sw._longitude._radians;
  const double neLatRadians = ne._latitude._radians;
  const double neLonRadians = ne._longitude._radians;

  const double deltaLonRadians = neLonRadians - swLonRadians;
  const double deltaLatRadians = neLatRadians - swLatRadians;

  const double u = (longitude._radians - swLonRadians) / deltaLonRadians;
  const double v = (neLatRadians -  latitude._radians) / deltaLatRadians;

  return interpolation(valueSW,
                       valueSE,
                       valueNE,
                       valueNW,
                       u,
                       v);
}
