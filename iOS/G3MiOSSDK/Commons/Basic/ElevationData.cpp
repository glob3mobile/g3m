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
                             const Vector2I& resolution) :
_sector(sector),
_width(resolution._x),
_height(resolution._y),
_stepInLongitudeRadians( sector.getDeltaLongitude().radians() / resolution._x ),
_stepInLatitudeRadians( sector.getDeltaLatitude().radians() / resolution._y )

{
}

//double ElevationData::getElevationAt(int x, int y) const {
//  //return _buffer->get( (x * _width) + y );
//  //return _buffer->get( (x * _height) + y );
//
//  //        const double height = elevationData->getElevationAt(x, extent._y-1-y);
//
////  const int a = (_height-1-(y+_margin)) * _width;
////  const int a1 = _height-1-(y+_margin);
//
////  const int index = ((_height-1-(y+_margin)) * _width) + (x+_margin);
//  const int index = ((_height-1-y) * _width) + x;
//  if ((index < 0) ||
//      (index >= _buffer->size())) {
//    printf("break point on me\n");
//  }
//  return _buffer->get( index );
//}

Vector2I ElevationData::getExtent() const {
  return Vector2I(_width, _height);
}
