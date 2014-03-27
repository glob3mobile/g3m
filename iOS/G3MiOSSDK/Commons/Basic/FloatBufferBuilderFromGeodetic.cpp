//
//  FloatBufferBuilderFromGeodetic.cpp
//  G3MiOSSDK


#include "FloatBufferBuilderFromGeodetic.hpp"

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
