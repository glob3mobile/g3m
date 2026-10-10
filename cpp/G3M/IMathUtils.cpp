//
//  IMathUtils.cpp
//  G3M
//
//  Created by José Miguel S N on 29/08/12.
//

#include "IMathUtils.hpp"
#include "Angle.hpp"
#include "Geodetic2D.hpp"
#include "ILogger.hpp"


IMathUtils* IMathUtils::_instance = NULL;


Geodetic2D IMathUtils::greatCircleIntermediatePoint(const Angle& fromLat,
                                                    const Angle& fromLon,
                                                    const Angle& toLat,
                                                    const Angle& toLon,
                                                    const double alpha) const {
  // http://fraserchapman.blogspot.com/2008/09/intermediate-points-on-great-circle.html

  const double fromLatRad = fromLat._radians;
  const double toLatRad   = toLat._radians;
  const double fromLonRad = fromLon._radians;
  const double toLonRad   = toLon._radians;

  const double cosFromLat = cos(fromLatRad);
  const double cosToLat   = cos(toLatRad);

  const double d = 2 * asin(sqrt(pow((sin((fromLatRad - toLatRad) / 2)), 2)
                                         + (cosFromLat * cosToLat * pow(sin((fromLonRad - toLonRad) / 2), 2))));

  const double A = sin((1 - alpha) * d) / sin(d);
  const double B = sin(alpha * d) / sin(d);
  const double x = (A * cosFromLat * cos(fromLonRad)) + (B * cosToLat * cos(toLonRad));
  const double y = (A * cosFromLat * sin(fromLonRad)) + (B * cosToLat * sin(toLonRad));
  const double z = (A * sin(fromLatRad)) + (B * sin(toLatRad));
  const double latRad = atan2(z, sqrt(pow(x, 2) + pow(y, 2)));
  const double lngRad = atan2(y, x);

  return Geodetic2D(Angle::fromRadians(latRad),
                    Angle::fromRadians(lngRad));
}

const int IMathUtils::toUInt16(const unsigned char b1,
                               const unsigned char b2) const {
  // LittleEndian
#ifdef C_CODE
  return (b1 | (b2 << 8));
#endif
#ifdef JAVA_CODE
  return (((short) (b1 & 0xFF)) |
          ((short) (b2 & 0xFF) << 8));
#endif
}

long long IMathUtils::bit64(int index) const {
  if ((index < 0) || (index > 63)) {
    ILogger::instance()->logError("IMathUtils::bit64: index %d out of 0..63", index);
    return 0;
  }
  // the cast (not a literal suffix) is what cpp2java turns into Java's long
  return ((long long) 1) << index;
}
