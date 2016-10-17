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

  static double _originShift;

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

  static double getMercatorV(const Angle& latitude);

  static const Angle toLatitude(double v);

  static const Angle calculateSplitLatitude(const Angle& lowerLatitude,
                                            const Angle& upperLatitude) {
    const double middleV = (getMercatorV(lowerLatitude) +
                            getMercatorV(upperLatitude)) / 2;

    return toLatitude(middleV);
  }

  static double longitudeToMeters(const Angle& longitude) {
		return longitude._degrees * _originShift / 180.0;
  }

  static double latitudeToMeters(const Angle& latitude);

};

#endif
