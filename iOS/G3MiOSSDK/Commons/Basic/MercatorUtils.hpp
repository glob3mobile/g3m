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
#include "Vector2D.hpp"

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

  static double getMercatorV(const Angle& latitude) {
    if (latitude._degrees >= _upperLimitInDegrees) {
      return 0;
    }
    if (latitude._degrees <= _lowerLimitInDegrees) {
      return 1;
    }

    const IMathUtils* mu = IMathUtils::instance();
    const double pi4 = PI * 4;

//    const double latSin = latitude.sinus();
    const double latSin = SIN(latitude._radians);
    return 1.0 - ( ( mu->log( (1.0 + latSin) / (1.0 - latSin) ) / pi4 ) + 0.5 );
  }

  static const Angle toLatitude(double v) {
    const IMathUtils* mu = IMathUtils::instance();

    const double exp = mu->exp(-2 * PI * (1.0 - v - 0.5));
    const double atan = mu->atan(exp);
    return Angle::fromRadians((PI / 2) - 2 * atan);
  }

  static const Angle calculateSplitLatitude(const Angle& lowerLatitude,
                                            const Angle& upperLatitude) {
    const double middleV = (getMercatorV(lowerLatitude) +
                            getMercatorV(upperLatitude)) / 2;

    return toLatitude(middleV);
  }

//  /**
//   Converts given lat/lon in WGS84 Datum to XY in Spherical Mercator EPSG:900913
//   */
//  static Vector2D toMeters(const Angle& latitude,
//                           const Angle& longitude) {
//    const IMathUtils* mu = IMathUtils::instance();
//    const double pi = mu->pi();
//
//		const double mx = longitude._degrees * _originShift / 180.0;
//
//    double my = mu->log( mu->tan( (90 + latitude._degrees) * pi / 360.0 ) ) / (pi / 180.0);
//		my = my * _originShift / 180.0;
//
//		return Vector2D(mx, my);
//  }

  static double longitudeToMeters(const Angle& longitude) {
		return longitude._degrees * _originShift / 180.0;
  }

  static double latitudeToMeters(const Angle& latitude) {
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

};

#endif
