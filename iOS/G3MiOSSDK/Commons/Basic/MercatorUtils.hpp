//
//  MercatorUtils.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/7/13.
//
//

#ifndef __G3MiOSSDK__MercatorUtils__
#define __G3MiOSSDK__MercatorUtils__

#include "Angle.hpp"

class MercatorUtils {
private:
  MercatorUtils() {}

  static const Angle _upperLimit;
  static const Angle _lowerLimit;

  static double _upperLimitInRadians;
  static double _lowerLimitInRadians;

  static double _upperLimitInDegrees;
  static double _lowerLimitInDegrees;

public:

  static const Angle upperLimit() {
    return _upperLimit;
  }

  static const Angle lowerLimit() {
    return _lowerLimit;
  }

  static double upperLimitInDegrees() {
    return _upperLimitInDegrees;
  }

  static double lowerLimitInDegrees() {
    return _lowerLimitInDegrees;
  }

  static double upperLimitInRadians() {
    return _upperLimitInRadians;
  }

  static double lowerLimitInRadians() {
    return _lowerLimitInRadians;
  }

  static double clampIntoLimitsInRadians(const Angle& angle) {
    const double radians = angle._radians;
    if (radians < _lowerLimitInRadians) {
      return _lowerLimitInRadians;
    }
    if (radians > _upperLimitInRadians) {
      return _upperLimitInRadians;
    }
    return radians;
  }

  static double getMercatorV(const Angle& latitude) {
    if (latitude._degrees >= _upperLimitInDegrees) {
      return 0;
    }
    if (latitude._degrees <= _lowerLimitInDegrees) {
      return 1;
    }

    const IMathUtils* mu = IMathUtils::instance();
    const double pi4 = mu->pi() * 4;

    const double latSin = latitude.sinus();
    return 1.0 - ( ( mu->log( (1.0 + latSin) / (1.0 - latSin) ) / pi4 ) + 0.5 );
  }

  static const Angle toLatitude(double v) {
    const IMathUtils* mu = IMathUtils::instance();
    const double pi = mu->pi() ;

    const double exp = mu->exp(-2 * pi * (1.0 - v - 0.5));
    const double atan = mu->atan(exp);
    return Angle::fromRadians((pi / 2) - 2 * atan);
  }

  static const Angle calculateSplitLatitude(const Angle& lowerLatitude,
                                            const Angle& upperLatitude) {
    const double middleV = (getMercatorV(lowerLatitude) +
                            getMercatorV(upperLatitude)) / 2;

    return toLatitude(middleV);
  }
  
};

#endif
