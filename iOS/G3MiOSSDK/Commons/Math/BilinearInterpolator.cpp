//
//  BilinearInterpolator.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/21/13.
//
//

#include "BilinearInterpolator.hpp"

double BilinearInterpolator::interpolation(double valueSW,
                                           double valueSE,
                                           double valueNE,
                                           double valueNW,
                                           double u,
                                           double v) const {
  const double alphaSW = (1.0 - u) * v;
  const double alphaSE = u         * v;
  const double alphaNE = u         * (1.0 - v);
  const double alphaNW = (1.0 - u) * (1.0 - v);

  return (alphaSW * valueSW) + (alphaSE * valueSE) + (alphaNE * valueNE) + (alphaNW * valueNW);
}
