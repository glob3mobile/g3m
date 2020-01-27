//
//  Geodetic3D.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 31/05/12.
//

#include "Geodetic3D.hpp"

#include "IStringBuilder.hpp"
#include "IMathUtils.hpp"
#include "Geodetic2D.hpp"


Geodetic3D::Geodetic3D(const Geodetic2D& g2,
                       const double height):
_latitude(g2._latitude),
_longitude(g2._longitude),
_height(height)
{
}

bool Geodetic3D::isNan() const {
  return _latitude.isNan() || _longitude.isNan() || ISNAN(_height);
}

Geodetic2D Geodetic3D::asGeodetic2D() const {
  return Geodetic2D(_latitude, _longitude);
}

const std::string Geodetic3D::description() const {
  IStringBuilder* isb = IStringBuilder::newStringBuilder();
  isb->addString("(lat=");
  isb->addString(_latitude.description());
  isb->addString(", lon=");
  isb->addString(_longitude.description());
  isb->addString(", height=");
  isb->addDouble(_height);
  isb->addString(")");
  const std::string s = isb->getString();
  delete isb;
  return s;
}

bool Geodetic3D::isEquals(const Geodetic3D& that) const {
  return (_latitude.isEquals(that._latitude)   &&
          _longitude.isEquals(that._longitude) &&
          (_height == that._height));
}

Geodetic3D Geodetic3D::linearInterpolation(const Geodetic3D& from,
                                           const Geodetic3D& to,
                                           double alpha) {
  return Geodetic3D(Angle::linearInterpolation(from._latitude,  to._latitude,  alpha),
                    Angle::linearInterpolation(from._longitude, to._longitude, alpha),
                    IMathUtils::instance()->linearInterpolation(from._height, to._height, alpha)
                    );
}

Geodetic3D Geodetic3D::linearInterpolationFromDegrees(const double fromLatitudeDegrees,
                                                      const double fromLongitudeDegrees,
                                                      const double fromHeight,
                                                      const double toLatitudeDegrees,
                                                      const double toLongitudeDegrees,
                                                      const double toHeight,
                                                      double alpha) {
  return Geodetic3D(Angle::linearInterpolationFromDegrees(fromLatitudeDegrees,  toLatitudeDegrees,  alpha),
                    Angle::linearInterpolationFromDegrees(fromLongitudeDegrees, toLongitudeDegrees, alpha),
                    IMathUtils::instance()->linearInterpolation(fromHeight, toHeight, alpha)
                    );
}

Geodetic3D Geodetic3D::linearInterpolationFromRadians(const double fromLatitudeRadians,
                                                      const double fromLongitudeRadians,
                                                      const double fromHeight,
                                                      const double toLatitudeRadians,
                                                      const double toLongitudeRadians,
                                                      const double toHeight,
                                                      double alpha) {
  return Geodetic3D(Angle::linearInterpolationFromRadians(fromLatitudeRadians,  toLatitudeRadians,  alpha),
                    Angle::linearInterpolationFromRadians(fromLongitudeRadians, toLongitudeRadians, alpha),
                    IMathUtils::instance()->linearInterpolation(fromHeight, toHeight, alpha)
                    );
}

Geodetic3D Geodetic3D::cosineInterpolation(const Geodetic3D& from,
                                           const Geodetic3D& to,
                                           double alpha) {
  return Geodetic3D(Angle::cosineInterpolation(from._latitude,  to._latitude,  alpha),
                    Angle::cosineInterpolation(from._longitude, to._longitude, alpha),
                    IMathUtils::instance()->cosineInterpolation(from._height, to._height, alpha)
                    );
}
