//
//  FloatBufferBuilderFromGeodetic.cpp
//  G3MiOSSDK


#include "FloatBufferBuilderFromGeodetic.hpp"

void FloatBufferBuilderFromGeodetic::add(const Angle& latitude,
                                         const Angle& longitude,
                                         const double height) {
  const Vector3D vector = _ellipsoid->toCartesian(latitude, longitude, height);

  if (_centerStrategy == CenterStrategy::firstVertex() && _values.size() == 0) {
    setCenter(vector);
  }

  float x = (float) vector._x;
  float y = (float) vector._y;
  float z = (float) vector._z;
  if (_centerStrategy != CenterStrategy::noCenter()) {
    x -= _cx;
    y -= _cy;
    z -= _cz;
  }

  _values.push_back(x);
  _values.push_back(y);
  _values.push_back(z);
}
