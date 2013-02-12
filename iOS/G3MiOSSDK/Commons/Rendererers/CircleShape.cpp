//
//  CircleShape.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/5/12.
//
//

#include "CircleShape.hpp"

#include "FloatBufferBuilderFromCartesian3D.hpp"
#include "DirectMesh.hpp"
#include "GLConstants.hpp"
#include "IMathUtils.hpp"

Mesh* CircleShape::createMesh(const G3MRenderContext* rc) {

  FloatBufferBuilderFromCartesian3D vertices(CenterStrategy::noCenter(), Vector3D::zero());

  // first is the center
  vertices.add(0.0, 0.0, 0.0);

  const double twicePi = IMathUtils::instance()->pi() * 2;

  for (int i = 0; i <= _steps; i++) {
    const double angleInRadians = i * twicePi / _steps;
    const double x = _radius * IMathUtils::instance()->cos(angleInRadians);
    const double y = _radius * IMathUtils::instance()->sin(angleInRadians);
    vertices.add(x, y, 0);
  }

  Color* color = (_color == NULL) ? NULL : new Color(*_color);

  return new DirectMesh(GLPrimitive::triangleFan(),
                        true,
                        Vector3D::zero(),
                        vertices.create(),
                        1,
                        1,
                        color);
}
