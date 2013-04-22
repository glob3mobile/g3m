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

  double x = vector._x;
  double y = vector._y;
  double z = vector._z;
  if (_centerStrategy != CenterStrategy::noCenter()) {
    x -= _cx;
    y -= _cy;
    z -= _cz;
  }

  _values.push_back((float) x);
  _values.push_back((float) y);
  _values.push_back((float) z);
}
