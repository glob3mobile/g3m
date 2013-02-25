//
//  ElevationData.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/17/13.
//
//

#include "ElevationData.hpp"

#include "Vector2I.hpp"

ElevationData::ElevationData(const Sector& sector,
                             const Vector2I& resolution,
                             double noDataValue) :
_sector(sector),
_width(resolution._x),
_height(resolution._y),
_noDataValue(noDataValue)
{
}

Vector2I ElevationData::getExtent() const {
  return Vector2I(_width, _height);
}
