//
//  DebugMeshes.cpp
//  G3M
//

#include "DebugMeshes.hpp"

#include "Box.hpp"
#include "Sphere.hpp"
#include "Ray.hpp"
#include "CoordinateSystem.hpp"
#include "Color.hpp"
#include "IMathUtils.hpp"
#include "FloatBufferBuilderFromCartesian3D.hpp"
#include "FloatBufferBuilderFromColor.hpp"
#include "ShortBufferBuilder.hpp"
#include "IndexedMesh.hpp"
#include "DirectMesh.hpp"
#include "GLConstants.hpp"


Mesh* DebugMeshes::createBoxWireframe(const Box& box,
                                      const Color& color) {
  const Vector3D lower = box._lower;
  const Vector3D upper = box._upper;

  double v[] = {
    lower._x, lower._y, lower._z,
    lower._x, upper._y, lower._z,
    lower._x, upper._y, upper._z,
    lower._x, lower._y, upper._z,
    upper._x, lower._y, lower._z,
    upper._x, upper._y, lower._z,
    upper._x, upper._y, upper._z,
    upper._x, lower._y, upper._z
  };

  short i[] = {
    0, 1, 1, 2, 2, 3, 3, 0,
    1, 5, 5, 6, 6, 2, 2, 1,
    5, 4, 4, 7, 7, 6, 6, 5,
    4, 0, 0, 3, 3, 7, 7, 4,
    3, 2, 2, 6, 6, 7, 7, 3,
    0, 1, 1, 5, 5, 4, 4, 0
  };

  FloatBufferBuilderFromCartesian3D* vertices = FloatBufferBuilderFromCartesian3D::builderWithFirstVertexAsCenter();
  const int numVertices = 8;
  for (int n = 0; n < numVertices; n++) {
    vertices->add(v[n*3], v[n*3+1], v[n*3+2]);
  }

  ShortBufferBuilder indices;
  const int numIndices = 48;
  for (int n = 0; n < numIndices; n++) {
    indices.add(i[n]);
  }

  Mesh* mesh = new IndexedMesh(GLPrimitive::lines(),
                               vertices->getCenter(),
                               vertices->create(),
                               true,
                               indices.create(),
                               true,
                               2,
                               1,
                               new Color(color));

  delete vertices;

  return mesh;
}

Mesh* DebugMeshes::createSphereWireframe(const Sphere& sphere,
                                         const Color& color) {
  const short resolution = 16;
  const double delta = PI / (resolution-1);

  // create vertices
  FloatBufferBuilderFromCartesian3D* vertices = FloatBufferBuilderFromCartesian3D::builderWithFirstVertexAsCenter();
  for (int i=0; i<2*resolution-2; i++) {
    const double longitude = -PI + i*delta;
    for (int j=0; j<resolution; j++) {
      const double latitude = -PI/2 + j*delta;
      const double h = COS(latitude);
      const double x = h * COS(longitude);
      const double y = h * SIN(longitude);
      const double z = SIN(latitude);
      vertices->add(Vector3D(x,y,z).times(sphere._radius).add(sphere._center));
    }
  }

  // create border indices for vertical lines
  ShortBufferBuilder indices;
  for (short i=0; i<2*resolution-2; i++) {
    for (short j=0; j<resolution-1; j++) {
      indices.add((short) (j+i*resolution));
      indices.add((short) (j+1+i*resolution));
    }
  }

  // create border indices for horizontal lines
  for (short j=1; j<resolution-1; j++) {
    for (short i=0; i<2*resolution-3; i++) {
      indices.add((short) (j+i*resolution));
      indices.add((short) (j+(i+1)*resolution));
    }
  }
  for (short j=1; j<resolution-1; j++) {
    const short i = (short) (2*resolution-3);
    indices.add((short) (j+i*resolution));
    indices.add((short) (j));
  }

  Mesh* mesh = new IndexedMesh(GLPrimitive::lines(),
                               vertices->getCenter(),
                               vertices->create(),
                               true,
                               indices.create(),
                               true,
                               1,
                               1,
                               new Color(color));

  delete vertices;

  return mesh;
}

Mesh* DebugMeshes::createRayMesh(const Ray& ray,
                                 const Color& color,
                                 float lineWidth) {
  FloatBufferBuilderFromCartesian3D* vertices = FloatBufferBuilderFromCartesian3D::builderWithGivenCenter(ray._origin);

  vertices->add( ray._origin );
  vertices->add( ray._origin.add(ray._direction.times(100000)) );

  Mesh* mesh = new DirectMesh(GLPrimitive::lineStrip(),
                              true,
                              vertices->getCenter(),
                              vertices->create(),
                              lineWidth,
                              1,
                              new Color(color),
                              NULL, // const IFloatBuffer* colors
                              true  // bool depthTest
                              );

  delete vertices;

  return mesh;
}

Mesh* DebugMeshes::createCoordinateSystemMesh(const CoordinateSystem& coordinateSystem,
                                              double size,
                                              const Color& xColor,
                                              const Color& yColor,
                                              const Color& zColor) {
  const Vector3D origin = coordinateSystem._origin;

  FloatBufferBuilderFromColor colors;

  FloatBufferBuilderFromCartesian3D* fbb = FloatBufferBuilderFromCartesian3D::builderWithGivenCenter(origin);
  fbb->add(origin);
  fbb->add(origin.add(coordinateSystem._x.normalized().times(size)));
  colors.add(xColor);
  colors.add(xColor);

  fbb->add(origin);
  fbb->add(origin.add(coordinateSystem._y.normalized().times(size)));
  colors.add(yColor);
  colors.add(yColor);

  fbb->add(origin);
  fbb->add(origin.add(coordinateSystem._z.normalized().times(size)));
  colors.add(zColor);
  colors.add(zColor);

  DirectMesh* dm = new DirectMesh(GLPrimitive::lines(),
                                  true,
                                  fbb->getCenter(),
                                  fbb->create(),
                                  5.0f,
                                  1.0f,
                                  NULL,
                                  colors.create(),
                                  false,
                                  NULL);

  delete fbb;

  return dm;
}
