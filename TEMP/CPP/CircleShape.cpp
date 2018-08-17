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
  const IMathUtils* mu = IMathUtils::instance();

  FloatBufferBuilderFromCartesian3D* vertices = FloatBufferBuilderFromCartesian3D::builderWithoutCenter();

  // first is the center
  vertices->add(0.0, 0.0, 0.0);

  const double twicePi = PI * 2;

  for (int i = 0; i <= _steps; i++) {
    const double angleInRadians = i * twicePi / _steps;
    const double x = _radius * mu->cos(angleInRadians);
    const double y = _radius * mu->sin(angleInRadians);
    vertices->add(x, y, 0);
  }

  Color* color = (_color == NULL) ? NULL : new Color(*_color);

  if (_useNormals) {

    FloatBufferBuilderFromCartesian3D* normals = FloatBufferBuilderFromCartesian3D::builderWithoutCenter();
    for (int i = 0; i <= _steps+1; i++) {
      normals->add(0.0, 0.0, 1.0);
    }

    Mesh* result = new DirectMesh(GLPrimitive::triangleFan(),
                            true,
                            Vector3D::zero,
                            vertices->create(),
                            1,
                            1,
                            color,
                            NULL,
                            1,
                            true,
                            normals->create());

    delete normals;
    delete vertices;

    return result;

  }

  Mesh* result = new DirectMesh(GLPrimitive::triangleFan(),
                          true,
                          Vector3D::zero,
                          vertices->create(),
                          1,
                          1,
                          color);

  delete vertices;

  return result;
}
