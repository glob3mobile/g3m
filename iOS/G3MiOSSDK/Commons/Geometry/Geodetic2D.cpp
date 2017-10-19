//
//  Geodetic3D.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 31/05/12.
//

#include "Geodetic2D.hpp"
#include "Geodetic3D.hpp"

#include "IStringBuilder.hpp"
#include "Vector3D.hpp"
#include "IMathUtils.hpp"

Geodetic2D Geodetic2D::Geo2DNull = Geodetic2D::fromDegrees(NAND, NAND);

bool Geodetic2D::isBetween(const Geodetic2D& min,
                           const Geodetic2D& max) const {
  return
  _latitude.isBetween(min._latitude, max._latitude) &&
  _longitude.isBetween(min._longitude, max._longitude);
}

bool Geodetic2D::closeTo(const Geodetic2D &other) const {
  if (!_latitude.closeTo(other._latitude)) {
    return false;
  }

  return _longitude.closeTo(other._longitude);
}

const std::string Geodetic2D::description() const {
  IStringBuilder* isb = IStringBuilder::newStringBuilder();
  isb->addString("(lat=");
  isb->addString(_latitude.description());
  isb->addString(", lon=");
  isb->addString(_longitude.description());
  isb->addString(")");
  const std::string s = isb->getString();
  delete isb;
  return s;
}

Angle Geodetic2D::angleTo(const Geodetic2D& that) const {
  const double cos1 = COS(_latitude._radians);
  const Vector3D normal1(cos1 * COS(_longitude._radians),
                         cos1 * SIN(_longitude._radians),
                         SIN(_latitude._radians));
  const double cos2 = COS(that._latitude._radians);
  const Vector3D normal2(cos2 * COS(that._longitude._radians),
                         cos2 * SIN(that._longitude._radians),
                         SIN(that._latitude._radians));

  return Angle::fromRadians(asin(normal1.cross(normal2).squaredLength()));
}

double Geodetic2D::bearingInRadians(const Angle& fromLatitude,
                                    const Angle& fromLongitude,
                                    const Angle& toLatitude,
                                    const Angle& toLongitude) {
  const IMathUtils* mu = IMathUtils::instance();

  const double deltaLonRad = toLongitude._radians - fromLongitude._radians;

  const double toLatCos = COS(toLatitude._radians);

  const double y = SIN(deltaLonRad) * toLatCos;
  const double x = COS(fromLatitude._radians) * SIN(toLatitude._radians) - SIN(fromLatitude._radians) * toLatCos * COS(deltaLonRad);
  const double radians = mu->atan2(y, x);
  return radians;

  //  const double pi2 = PI*2;
  //  return mu->mod(radians + pi2, pi2);

  //  const double r1 = mu->mod(radians, pi2);
  //  const double r2 = mu->mod(radians + pi2, pi2);
  //  return (mu->abs(r1) < mu->abs(r2)) ? r1 : r2;
}

double Geodetic2D::bearingInDegrees(const Angle& fromLatitude,
                                    const Angle& fromLongitude,
                                    const Angle& toLatitude,
                                    const Angle& toLongitude) {
  return TO_DEGREES(bearingInRadians(fromLatitude,
                                     fromLongitude,
                                     toLatitude,
                                     toLongitude));
}

Geodetic3D Geodetic2D::asGeodetic3DWithHeight(double height) const{
  return Geodetic3D(_latitude, _longitude, height);
}
