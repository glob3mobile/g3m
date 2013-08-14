//
//  Sector.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 22/06/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#include "Sector.hpp"
#include "Camera.hpp"
#include "Planet.hpp"

#include "IStringBuilder.hpp"

#include "GEORasterProjection.hpp"
#include "ICanvas.hpp"

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
  if (_upper._latitude.lowerThan(that._lower._latitude) ||
      _lower._latitude.greaterThan(that._upper._latitude)) {
    return false;
  }
  if (_upper._longitude.lowerThan(that._lower._longitude) ||
      _lower._longitude.greaterThan(that._upper._longitude)) {
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

Sector::~Sector() {
  delete _normalizedCartesianCenter;

#ifdef JAVA_CODE
  super.dispose();
#endif

}


bool Sector::isBackOriented(const G3MRenderContext *rc,
                            double minHeight,
                            const Planet* planet,
                            const Vector3D& cameraNormalizedPosition,
                            double cameraAngle2HorizonInRadians) const {
  const double dot = cameraNormalizedPosition.dot(getNormalizedCartesianCenter(planet));
#ifdef C_CODE
  const double angleInRadians = IMathUtils::instance()->acos(dot);
#endif
#ifdef JAVA_CODE
  final double angleInRadians = java.lang.Math.acos(dot);
#endif

  return ( (angleInRadians - getDeltaRadiusInRadians()) > cameraAngle2HorizonInRadians );
}

Sector Sector::intersection(const Sector& that) const {
  const Angle lowLat = Angle::max(_lower._latitude,  that._lower._latitude);
  const Angle lowLon = Angle::max(_lower._longitude, that._lower._longitude);
  const Geodetic2D low(lowLat, lowLon);

  const Angle upLat = Angle::min(_upper._latitude,  that._upper._latitude);
  const Angle upLon = Angle::min(_upper._longitude, that._upper._longitude);
  const Geodetic2D up(upLat, upLon);

  return Sector(low, up);
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
  IStringBuilder *isb = IStringBuilder::newStringBuilder();
  isb->addString("(Sector ");
  isb->addString(_lower.description());
  isb->addString(" - ");
  isb->addString(_upper.description());
  isb->addString(")");
  const std::string s = isb->getString();
  delete isb;
  return s;  
}

const Vector2D Sector::div(const Sector& that) const {
  const double scaleX = _deltaLongitude.div(that._deltaLongitude);
  const double scaleY = _deltaLatitude.div(that._deltaLatitude);
  return Vector2D(scaleX, scaleY);
}

void Sector::rasterize(ICanvas*                   canvas,
                       const GEORasterProjection* projection) const {

  const Vector2F l = projection->project(&_lower);
  const Vector2F u = projection->project(&_upper);
  
  const float left   = l._x;
  const float top    = l._y;
  const float width  = u._x - left;
  const float height = u._y - top;

  canvas->strokeRectangle(left, top, width, height);
}

const Vector3D Sector::getNormalizedCartesianCenter(const Planet* planet) const {
  if (_normalizedCartesianCenter == NULL) {
    _normalizedCartesianCenter = new Vector3D(planet->toCartesian(_center).normalized());
  }
  return *_normalizedCartesianCenter;
}
