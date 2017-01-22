//
//  FloatBufferBuilderFromCartesian3D.cpp
//  G3MiOSSDK
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

Vector3D FloatBufferBuilderFromCartesian3D::getCenter() {
  return Vector3D(_cx, _cy, _cz);
}

FloatBufferBuilderFromCartesian3D* FloatBufferBuilderFromCartesian3D::builderWithoutCenter() {
#ifdef C_CODE
  return new FloatBufferBuilderFromCartesian3D(NO_CENTER, Vector3D::ZERO);
#endif
#ifdef JAVA_CODE
  return new FloatBufferBuilderFromCartesian3D(CenterStrategy.NO_CENTER, Vector3D.ZERO);
#endif
}

FloatBufferBuilderFromCartesian3D* FloatBufferBuilderFromCartesian3D::builderWithFirstVertexAsCenter() {
#ifdef C_CODE
  return new FloatBufferBuilderFromCartesian3D(FIRST_VERTEX, Vector3D::ZERO);
#endif
#ifdef JAVA_CODE
  return new FloatBufferBuilderFromCartesian3D(CenterStrategy.FIRST_VERTEX, Vector3D.ZERO);
#endif
}

FloatBufferBuilderFromCartesian3D* FloatBufferBuilderFromCartesian3D::builderWithGivenCenter(const Vector3D& center) {
#ifdef C_CODE
  return new FloatBufferBuilderFromCartesian3D(GIVEN_CENTER, center);
#endif
#ifdef JAVA_CODE
  return new FloatBufferBuilderFromCartesian3D(CenterStrategy.GIVEN_CENTER, center);
#endif
}

void FloatBufferBuilderFromCartesian3D::add(const Vector3D& vector) {
  add(vector._x,
      vector._y,
      vector._z);
}
