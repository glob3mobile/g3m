//
//  FloatBufferBuilderFromGeodetic.cpp
//  G3M


#include "FloatBufferBuilderFromGeodetic.hpp"

#include "Planet.hpp"
#include "Geodetic3D.hpp"
#include "Geodetic2D.hpp"
#include "Vector3D.hpp"


FloatBufferBuilderFromGeodetic::FloatBufferBuilderFromGeodetic(CenterStrategy centerStrategy,
                                                               const Planet* planet,
                                                               const Vector3D& center):
_planet(planet),
_centerStrategy(centerStrategy)
{
  setCenter( center );
}

void FloatBufferBuilderFromGeodetic::setCenter(const Vector3D& center) {
  _cx = (float) center._x;
  _cy = (float) center._y;
  _cz = (float) center._z;
}

void FloatBufferBuilderFromGeodetic::setCenter(const MutableVector3D& center) {
  _cx = (float) center._x;
  _cy = (float) center._y;
  _cz = (float) center._z;
}

void FloatBufferBuilderFromGeodetic::addCartesianVector() {
  if (_centerStrategy == FIRST_VERTEX) {
    if (_values.size() == 0) {
      setCenter(_cartesianVector);
    }
  }

  if (_centerStrategy == NO_CENTER) {
    _values.push_back( (float) _cartesianVector._x );
    _values.push_back( (float) _cartesianVector._y );
    _values.push_back( (float) _cartesianVector._z );
  }
  else {
    _values.push_back( (float) (_cartesianVector._x - _cx) );
    _values.push_back( (float) (_cartesianVector._y - _cy) );
    _values.push_back( (float) (_cartesianVector._z - _cz) );
  }
}

void FloatBufferBuilderFromGeodetic::addDegrees(const double latitudeDegrees,
                                                const double longitudeDegress,
                                                const double height) {
  _planet->toCartesianFromDegrees(latitudeDegrees,
                                  longitudeDegress,
                                  height,
                                  _cartesianVector);

  addCartesianVector();
}

void FloatBufferBuilderFromGeodetic::add(const Angle& latitude,
                                         const Angle& longitude,
                                         const double height) {
  _planet->toCartesian(latitude,
                       longitude,
                       height,
                       _cartesianVector);

  addCartesianVector();
}

FloatBufferBuilderFromGeodetic::FloatBufferBuilderFromGeodetic(CenterStrategy centerStrategy,
                                                               const Planet* planet,
                                                               const Geodetic2D& center):
_planet(planet),
_centerStrategy(centerStrategy)
{
  setCenter( _planet->toCartesian(center) );
}

FloatBufferBuilderFromGeodetic::FloatBufferBuilderFromGeodetic(CenterStrategy centerStrategy,
                                                               const Planet* planet,
                                                               const Geodetic3D& center):
_planet(planet),
_centerStrategy(centerStrategy)
{
  setCenter( _planet->toCartesian(center) );
}

FloatBufferBuilderFromGeodetic* FloatBufferBuilderFromGeodetic::builderWithGivenCenter(const Planet* planet,
                                                                                       const Vector3D& center) {
  return new FloatBufferBuilderFromGeodetic(GIVEN_CENTER, planet, center);
}

FloatBufferBuilderFromGeodetic* FloatBufferBuilderFromGeodetic::builderWithGivenCenter(const Planet* planet,
                                                                                       const Geodetic2D& center) {
  return new FloatBufferBuilderFromGeodetic(GIVEN_CENTER, planet, center);
}

FloatBufferBuilderFromGeodetic* FloatBufferBuilderFromGeodetic::builderWithGivenCenter(const Planet* planet,
                                                                                       const Geodetic3D& center) {
  return new FloatBufferBuilderFromGeodetic(GIVEN_CENTER, planet, center);
}

FloatBufferBuilderFromGeodetic* FloatBufferBuilderFromGeodetic::builderWithoutCenter(const Planet* planet) {
  return new FloatBufferBuilderFromGeodetic(NO_CENTER, planet, Vector3D::ZERO);
}

FloatBufferBuilderFromGeodetic* FloatBufferBuilderFromGeodetic::builderWithFirstVertexAsCenter(const Planet* planet) {
  return new FloatBufferBuilderFromGeodetic(FIRST_VERTEX, planet, Vector3D::ZERO);
}

void FloatBufferBuilderFromGeodetic::add(const Geodetic3D& position) {
  add(position._latitude,
      position._longitude,
      position._height);
}

void FloatBufferBuilderFromGeodetic::add(const Geodetic2D& position) {
  add(position._latitude,
      position._longitude,
      0.0);
}

void FloatBufferBuilderFromGeodetic::add(const Geodetic2D& position,
                                         const double height) {
  add(position._latitude,
      position._longitude,
      height);
}

Vector3D FloatBufferBuilderFromGeodetic::getCenter() {
  return Vector3D(_cx, _cy, _cz);
}
