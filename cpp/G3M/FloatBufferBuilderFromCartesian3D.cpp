//
//  FloatBufferBuilderFromCartesian3D.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 11/9/16.
//
//

#include "FloatBufferBuilderFromCartesian3D.hpp"

#include "Vector3D.hpp"


FloatBufferBuilderFromCartesian3D::FloatBufferBuilderFromCartesian3D(CenterStrategy centerStrategy,
                                                                     const Vector3D& center):
_centerStrategy(centerStrategy)
{
  setCenter(center._x, center._y, center._z);
}

void FloatBufferBuilderFromCartesian3D::setCenter(double x, double y, double z) {
  _cx = (float) x;
  _cy = (float) y;
  _cz = (float) z;
}

Vector3D FloatBufferBuilderFromCartesian3D::getCenter() {
  return Vector3D(_cx, _cy, _cz);
}

FloatBufferBuilderFromCartesian3D* FloatBufferBuilderFromCartesian3D::builderWithoutCenter() {
  return new FloatBufferBuilderFromCartesian3D(NO_CENTER, Vector3D::ZERO);
}

FloatBufferBuilderFromCartesian3D* FloatBufferBuilderFromCartesian3D::builderWithFirstVertexAsCenter() {
  return new FloatBufferBuilderFromCartesian3D(FIRST_VERTEX, Vector3D::ZERO);
}

FloatBufferBuilderFromCartesian3D* FloatBufferBuilderFromCartesian3D::builderWithGivenCenter(const Vector3D& center) {
  return new FloatBufferBuilderFromCartesian3D(GIVEN_CENTER, center);
}

void FloatBufferBuilderFromCartesian3D::add(const Vector3D& vector) {
  add(vector._x, vector._y, vector._z);
}

void FloatBufferBuilderFromCartesian3D::add(double x, double y, double z) {
  if (_centerStrategy == FIRST_VERTEX) {
    if (_values.size() == 0) {
      setCenter(x, y, z);
    }
  }

  if (_centerStrategy == NO_CENTER) {
    _values.push_back( (float) x );
    _values.push_back( (float) y );
    _values.push_back( (float) z );
  }
  else {
    _values.push_back( (float) (x - _cx) );
    _values.push_back( (float) (y - _cy) );
    _values.push_back( (float) (z - _cz) );
  }
}

void FloatBufferBuilderFromCartesian3D::add(float x, float y, float z) {
  if (_centerStrategy == FIRST_VERTEX) {
    if (_values.size() == 0) {
      setCenter(x, y, z);
    }
  }

  if (_centerStrategy == NO_CENTER) {
    _values.push_back( x );
    _values.push_back( y );
    _values.push_back( z );
  }
  else {
    _values.push_back( x - _cx );
    _values.push_back( y - _cy );
    _values.push_back( z - _cz );
  }
}
