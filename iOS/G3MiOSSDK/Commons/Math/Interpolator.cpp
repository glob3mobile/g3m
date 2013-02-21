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
//#include "Sector.hpp"

double Interpolator::interpolateHeight(const Geodetic2D& lower,
                                       const Geodetic2D& upper,
                                       double valueSW,
                                       double valueSE,
                                       double valueNE,
                                       double valueNW,
                                       const Geodetic2D& position) {
  return interpolateHeight(lower,
                           upper,
                            valueSW,
                            valueSE,
                            valueNE,
                            valueNW,
                           position.latitude(),
                           position.longitude());
}

double Interpolator::interpolateHeight(const Geodetic2D& lower,
                                       const Geodetic2D& upper,
                                       double valueSW,
                                       double valueSE,
                                       double valueNE,
                                       double valueNW,
                                       const Angle& latitude,
                                       const Angle& longitude) {

  const Angle deltaLongitude = upper.longitude().sub(lower.longitude());
  const Angle deltaLatitude  = upper.latitude().sub(lower.latitude());

  const double u = longitude.sub(lower.longitude()).div(deltaLongitude);
  const double v = upper.latitude().sub(latitude).div(deltaLatitude);


//  const Vector2D uv = sector.getUVCoordinates(position);
//  const double u = uv._x;
//  //  const double v = 1.0 - uv._y;
//  const double v = uv._y;

  //  const double pll = (1.0 - u) * (1.0 - v);
  //  const double plr = u         * (1.0 - v);
  //  const double pur = u         * v;
  //  const double pul = (1.0 - u) * v;
  const double pll = (1.0 - u) * v;
  const double plr = u         * v;
  const double pur = u         * (1.0 - v);
  const double pul = (1.0 - u) * (1.0 - v);

  const double ll = valueSW;
  const double lr = valueSE;
  const double ur = valueNE;
  const double ul = valueNW;

  const double interpolatedHeight = (pll * ll) + (plr * lr) + (pur * ur) + (pul * ul);

  return interpolatedHeight;
}

