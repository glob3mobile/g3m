//
//  FloatBufferBuilderFromGeodetic.cpp
//  G3MiOSSDK


#include "FloatBufferBuilderFromGeodetic.hpp"

#include "EllipsoidalPlanet.hpp"
#include "Geodetic3D.hpp"
#include "Geodetic2D.hpp"
#include "Vector3D.hpp"


void FloatBufferBuilderFromGeodetic::setCenter(const Vector3D& center) {
  _cx = (float) center._x;
  _cy = (float) center._y;
  _cz = (float) center._z;
}

void FloatBufferBuilderFromGeodetic::add(const Angle& latitude,
                                         const Angle& longitude,
                                         const double height) {
  const Vector3D vector = _planet->toCartesian(latitude, longitude, height);

  if (_centerStrategy ==
#ifdef C_CODE
      FIRST_VERTEX
#else
      CenterStrategy.FIRST_VERTEX
#endif
      ) {
    if (_values.size() == 0) {
      setCenter(vector);
    }
  }

  if (_centerStrategy ==
#ifdef C_CODE
      NO_CENTER
#else
      CenterStrategy.NO_CENTER
#endif
      ) {
    _values.push_back( (float) vector._x );
    _values.push_back( (float) vector._y );
    _values.push_back( (float) vector._z );
  }
  else {
    _values.push_back( (float) (vector._x - _cx) );
    _values.push_back( (float) (vector._y - _cy) );
    _values.push_back( (float) (vector._z - _cz) );
  }
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

FloatBufferBuilderFromGeodetic* FloatBufferBuilderFromGeodetic::builderWithoutCenter(const Planet* planet) {
  return new FloatBufferBuilderFromGeodetic(
#ifdef C_CODE
                                            NO_CENTER,
#else
                                            CenterStrategy.NO_CENTER,
#endif
                                            planet, Vector3D::ZERO);
}

FloatBufferBuilderFromGeodetic* FloatBufferBuilderFromGeodetic::builderWithFirstVertexAsCenter(const Planet* planet) {
  return new FloatBufferBuilderFromGeodetic(
#ifdef C_CODE
                                            FIRST_VERTEX,
#else
                                            CenterStrategy.FIRST_VERTEX,
#endif
                                            planet, Vector3D::ZERO);
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
