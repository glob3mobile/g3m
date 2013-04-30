//
//  FloatBufferBuilderFromGeodetic.cpp
//  G3MiOSSDK


#include "FloatBufferBuilderFromGeodetic.hpp"

void FloatBufferBuilderFromGeodetic::add(const Angle& latitude,
                                         const Angle& longitude,
                                         const double height) {
  const Vector3D vector = _ellipsoid->toCartesian(latitude, longitude, height);

  if (_centerStrategy == CenterStrategy::firstVertex()) {
    if (_values.size() == 0) {
      setCenter(vector);
    }
  }

  if (_centerStrategy == CenterStrategy::noCenter()) {
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
