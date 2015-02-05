//
//  GEORasterProjection.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/11/13.
//
//

#include "GEORasterProjection.hpp"
#include "MercatorUtils.hpp"

GEORasterProjection::GEORasterProjection(const Sector& sector,
                                         bool mercator,
                                         int imageWidth,
                                         int imageHeight) :
_sector(sector),
_mercator(mercator),
_imageWidth(imageWidth),
_imageHeight(imageHeight)
{
  if (_mercator) {
    const double mercatorLowerGlobalV = MercatorUtils::getMercatorV(sector._lower._latitude);
    _mercatorUpperGlobalV = MercatorUtils::getMercatorV(sector._upper._latitude);
    _mercatorDeltaGlobalV = mercatorLowerGlobalV - _mercatorUpperGlobalV;
  }
}


Vector2F GEORasterProjection::project(const Geodetic2D* position) const {
  const Vector2D uvCoordinates = _sector.getUVCoordinates(*position);

  double v;
  if (_mercator) {
    const double linearV = uvCoordinates._y;
    const Angle latitude = _sector.getInnerPointLatitude(linearV);
    const double mercatorGlobalV = MercatorUtils::getMercatorV(latitude);
    const double mercatorLocalV  = (mercatorGlobalV - _mercatorUpperGlobalV) / _mercatorDeltaGlobalV;
    v = mercatorLocalV;
  }
  else {
    v = uvCoordinates._y;
  }

  return Vector2F((float) (uvCoordinates._x * _imageWidth),
                  (float) (v * _imageHeight));
  
}
