//
//  Sector.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 22/06/12.
//

#include "Sector.hpp"

#include "Vector2D.hpp"
#include "Vector2F.hpp"
#include "IStringBuilder.hpp"
#include "IMathUtils.hpp"


const Sector Sector::FULL_SPHERE = Sector::fromDegrees(-90, -180, 90, 180);
const Sector Sector::NAN_SECTOR = Sector::fromDegrees(NAND, NAND, NAND, NAND);

Sector::Sector(const Geodetic2D& lower,
               const Geodetic2D& upper) :
_lower(lower),
_upper(upper),
_deltaLatitude(upper._latitude.sub(lower._latitude)),
_deltaLongitude(upper._longitude.sub(lower._longitude)),
_center(Angle::midAngle(lower._latitude, upper._latitude),
        Angle::midAngle(lower._longitude, upper._longitude))
{
  //    if (_deltaLatitude._degrees == 0) {
  //        printf("NO AREA");
  //    }
}

Sector::Sector(const Sector& sector) :
_lower(sector._lower),
_upper(sector._upper),
_deltaLatitude(sector._deltaLatitude),
_deltaLongitude(sector._deltaLongitude),
_center(sector._center)
{
  //    if (_deltaLatitude._degrees == 0) {
  //        printf("NO AREA");
  //    }
}

Sector Sector::fullSphere() {
  return FULL_SPHERE;
}

bool Sector::isNan() const {
  return ISNAN(_lower._latitude._degrees);
}

bool Sector::contains(const Angle& latitude,
                      const Angle& longitude) const {
  return (latitude.isBetween(_lower._latitude, _upper._latitude) &&
          longitude.isBetween(_lower._longitude, _upper._longitude));
}

bool Sector::fullContains(const Sector& that) const {
  //return contains(that._upper) && contains(that._lower);
  return (contains(that._upper._latitude, that._upper._longitude) &&
          contains(that._lower._latitude, that._lower._longitude));
}

bool Sector::touchesWith(const Sector &that) const {
  // from Real-Time Collision Detection - Christer Ericson
  //   page 79

  // Exit with no intersection if separated along an axis
  if ((_upper._latitude._radians < that._lower._latitude._radians) ||
      (_lower._latitude._radians > that._upper._latitude._radians)) {
    return false;
  }
  if ((_upper._longitude._radians < that._lower._longitude._radians) ||
      (_lower._longitude._radians > that._upper._longitude._radians)) {
    return false;
  }

  // Overlapping on all axes means Sectors are intersecting
  return true;
}

// (u,v) are similar to texture coordinates inside the Sector
// (u,v)=(0,0) in NW point, and (1,1) in SE point
const Geodetic2D Sector::getInnerPoint(double u, double v) const {
  return Geodetic2D(Angle::linearInterpolation( _lower._latitude,  _upper._latitude,  1.0 - v ),
                    Angle::linearInterpolation( _lower._longitude, _upper._longitude,       u ) );
}

const Angle Sector::getInnerPointLongitude(double u) const {
  return Angle::linearInterpolation( _lower._longitude, _upper._longitude, u );
}

const Angle Sector::getInnerPointLatitude(double v) const {
  return Angle::linearInterpolation( _lower._latitude, _upper._latitude,  1.0 - v);
}

const Vector2D Sector::getUVCoordinates(const Geodetic2D& point) const {
  return getUVCoordinates(point._latitude, point._longitude);
}

Vector2D Sector::getUVCoordinates(const Angle& latitude,
                                  const Angle& longitude) const {
  return Vector2D((longitude._radians        - _lower._longitude._radians) / _deltaLongitude._radians,
                  (_upper._latitude._radians - latitude._radians         ) / _deltaLatitude._radians);
}

Vector2F Sector::getUVCoordinatesF(const Angle& latitude,
                                   const Angle& longitude) const {
  return Vector2F((float) ((longitude._radians        - _lower._longitude._radians) / _deltaLongitude._radians),
                  (float) ((_upper._latitude._radians - latitude._radians         ) / _deltaLatitude._radians));
}

Sector::~Sector() {
}


Sector Sector::intersection(const Sector& that) const {
  const Angle lowLat = Angle::max(_lower._latitude,  that._lower._latitude);
  const Angle lowLon = Angle::max(_lower._longitude, that._lower._longitude);

  const Angle upLat = Angle::min(_upper._latitude,  that._upper._latitude);
  const Angle upLon = Angle::min(_upper._longitude, that._upper._longitude);

  if (lowLat.lowerThan(upLat) && lowLon.lowerThan(upLon)) {
    const Geodetic2D low(lowLat, lowLon);
    const Geodetic2D up(upLat, upLon);

    return Sector(low, up);
  }

  return Sector::fromDegrees(0, 0, 0, 0); //invalid
}

Sector Sector::mergedWith(const Sector& that) const {
  const Angle lowLat = Angle::min(_lower._latitude,  that._lower._latitude);
  const Angle lowLon = Angle::min(_lower._longitude, that._lower._longitude);
  const Geodetic2D low(lowLat, lowLon);

  const Angle upLat = Angle::max(_upper._latitude,  that._upper._latitude);
  const Angle upLon = Angle::max(_upper._longitude, that._upper._longitude);
  const Geodetic2D up(upLat, upLon);

  return Sector(low, up);
}

const Angle Sector::clampLatitude(const Angle& latitude) const {
  return latitude.clampedTo(_lower._latitude, _upper._latitude);
}

const Angle Sector::clampLongitude(const Angle& longitude) const {
  return longitude.clampedTo(_lower._longitude, _upper._longitude);
}

const Geodetic2D Sector::clamp(const Angle& latitude,
                               const Angle& longitude) const {
  if (contains(latitude, longitude)) {
    return Geodetic2D(latitude, longitude);
  }

  double latitudeInDegrees  = latitude._degrees;
  double longitudeInDegrees = longitude._degrees;

  const double upperLatitudeInDegrees  = _upper._latitude._degrees;
  if (latitudeInDegrees > upperLatitudeInDegrees) {
    latitudeInDegrees = upperLatitudeInDegrees;
  }

  const double upperLongitudeInDegrees = _upper._longitude._degrees;
  if (longitudeInDegrees > upperLongitudeInDegrees) {
    longitudeInDegrees = upperLongitudeInDegrees;
  }

  const double lowerLatitudeInDegrees  = _lower._latitude._degrees;
  if (latitudeInDegrees < lowerLatitudeInDegrees) {
    latitudeInDegrees = lowerLatitudeInDegrees;
  }

  const double lowerLongitudeInDegrees  = _lower._longitude._degrees;
  if (longitudeInDegrees < lowerLongitudeInDegrees) {
    longitudeInDegrees = lowerLongitudeInDegrees;
  }

  return Geodetic2D::fromDegrees(latitudeInDegrees, longitudeInDegrees);
}

const Geodetic2D Sector::clamp(const Geodetic2D& position) const {
  if (contains(position)) {
    return position;
  }

  double latitudeInDegrees = position._latitude._degrees;
  double longitudeInDegrees = position._longitude._degrees;

  const double upperLatitudeInDegrees  = _upper._latitude._degrees;
  if (latitudeInDegrees > upperLatitudeInDegrees) {
    latitudeInDegrees = upperLatitudeInDegrees;
  }

  const double upperLongitudeInDegrees = _upper._longitude._degrees;
  if (longitudeInDegrees > upperLongitudeInDegrees) {
    longitudeInDegrees = upperLongitudeInDegrees;
  }

  const double lowerLatitudeInDegrees  = _lower._latitude._degrees;
  if (latitudeInDegrees < lowerLatitudeInDegrees) {
    latitudeInDegrees = lowerLatitudeInDegrees;
  }

  const double lowerLongitudeInDegrees  = _lower._longitude._degrees;
  if (longitudeInDegrees < lowerLongitudeInDegrees) {
    longitudeInDegrees = lowerLongitudeInDegrees;
  }

  return Geodetic2D::fromDegrees(latitudeInDegrees, longitudeInDegrees);
}

const std::string Sector::description() const {
  IStringBuilder* isb = IStringBuilder::newStringBuilder();
  isb->addString("(Sector ");
  isb->addString(_lower.description());
  isb->addString(" - ");
  isb->addString(_upper.description());
  isb->addString(")");
  const std::string s = isb->getString();
  delete isb;
  return s;
}

const std::string Sector::id() const {
  IStringBuilder* isb = IStringBuilder::newStringBuilder();
  isb->addString("Sector|");
  isb->addDouble(_lower._latitude._degrees);
  isb->addString("|");
  isb->addDouble(_lower._longitude._degrees);
  isb->addString("|");
  isb->addDouble(_upper._latitude._degrees);
  isb->addString("|");
  isb->addDouble(_upper._longitude._degrees);
  isb->addString("|");
  const std::string s = isb->getString();
  delete isb;
  return s;
}

const Vector2D Sector::div(const Sector& that) const {
  const double scaleX = _deltaLongitude.div(that._deltaLongitude);
  const double scaleY = _deltaLatitude.div(that._deltaLatitude);
  return Vector2D(scaleX, scaleY);
}

Geodetic2D Sector::getClosesInnerPoint(const Geodetic2D& g) const {
  double lat = g._latitude._degrees;
  double lon = g._longitude._degrees;

  if (lat > _upper._latitude._degrees) {
    lat = _upper._latitude._degrees;
  }
  else {
    if (lat < _lower._latitude._degrees) {
      lat = _lower._latitude._degrees;
    }
  }

  if (lon > _upper._longitude._degrees) {
    lon = _upper._longitude._degrees;
  }
  else {
    if (lon < _lower._longitude._degrees) {
      lon = _lower._longitude._degrees;
    }
  }

  return Geodetic2D::fromDegrees(lat, lon);
}

