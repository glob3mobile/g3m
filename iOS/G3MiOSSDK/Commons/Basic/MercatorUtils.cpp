//
//  MercatorUtils.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/7/13.
//
//

#include "MercatorUtils.hpp"

const Angle MercatorUtils::_upperLimit = Angle::fromDegrees(85.0511287798);
const Angle MercatorUtils::_lowerLimit = Angle::fromDegrees(-85.0511287798);

double MercatorUtils::_upperLimitInRadians = Angle::fromDegrees(85.0511287798)._radians;
double MercatorUtils::_lowerLimitInRadians = Angle::fromDegrees(-85.0511287798)._radians;

double MercatorUtils::_upperLimitInDegrees = 85.0511287798;
double MercatorUtils::_lowerLimitInDegrees = -85.0511287798;
