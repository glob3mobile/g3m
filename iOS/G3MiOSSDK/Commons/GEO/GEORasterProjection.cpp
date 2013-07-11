//
//  GEORasterProjection.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/11/13.
//
//

#include "GEORasterProjection.hpp"


Vector2F GEORasterProjection::project(const Geodetic2D* position) const {

  int _TODO_mercator;
  
  const Vector2D uvCoordinates = _sector.getUVCoordinates(*position);

  return Vector2F((float) (uvCoordinates._x * _imageWidth),
                  (float) ((1.0 - uvCoordinates._y) * _imageHeight));
}
