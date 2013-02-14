//
//  EllipsoidShape.cpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 02/13/13.
//
//

#include <math.h>

#include "EllipsoidShape.hpp"

#include "ShortBufferBuilder.hpp"
#include "IndexedMesh.hpp"
#include "GLConstants.hpp"
#include "CompositeMesh.hpp"

Mesh* EllipsoidShape::createBorderMesh(const G3MRenderContext* rc, FloatBufferBuilderFromCartesian3D *vertices) {

  // create border indices for horizontal lines
  ShortBufferBuilder indices;
  short delta = (short) (2*_resolution - 1);
  for (short j=1; j<_resolution-1; j++) {
    for (short i=0; i<2*_resolution-2; i++) {
      indices.add((short) (i+j*delta));
      indices.add((short) (i+1+j*delta));
    }
  }

  // create border indices for vertical lines
  for (short i=0; i<2*_resolution-2; i++) {
    for (short j=0; j<_resolution-1; j++) {
      indices.add((short) (i+j*delta));
      indices.add((short) (i+(j+1)*delta));
    }
  }


  Color* borderColor = (_borderColor != NULL) ? _borderColor : _surfaceColor;

  return new IndexedMesh(GLPrimitive::lines(),
                         true,
                         vertices->getCenter(),
                         vertices->create(),
                         indices.create(),
                         _borderWidth,
                         1,
                         borderColor);
}

Mesh* EllipsoidShape::createSurfaceMesh(const G3MRenderContext* rc,
                                        FloatBufferBuilderFromCartesian3D* vertices,
                                        FloatBufferBuilderFromCartesian2D* texCoords) {
  
  // create surface indices
  ShortBufferBuilder indices;
  short delta = 2*_resolution - 1;
  for (short j=0; j<_resolution-1; j++) {
    if (j>0) indices.add(j*delta);
    for (short i=0; i<2*_resolution-1; i++) {
      indices.add(i+j*delta);
      indices.add(i+(j+1)*delta);
    }
    indices.add((j+2)*delta-1);
  }

  return new IndexedMesh(GLPrimitive::triangleStrip(),
                         true,
                         vertices->getCenter(),
                         vertices->create(),
                         indices.create(),
                         _borderWidth,
                         1,
                         _surfaceColor);
}


Mesh* EllipsoidShape::createMesh(const G3MRenderContext* rc) {
  
  // create vertices and texture coords
  if (_resolution<3) _resolution = 3;
  FloatBufferBuilderFromCartesian3D* vertices = new FloatBufferBuilderFromCartesian3D(CenterStrategy::noCenter(), Vector3D::zero());
  FloatBufferBuilderFromCartesian2D* texCoords = new FloatBufferBuilderFromCartesian2D;
  const double pi = IMathUtils::instance()->pi();
  const double incAngle = pi/(_resolution-1);
  for (int j=0; j<_resolution; j++) {
    double lat = pi/2 - j*incAngle;
    double s = sin(lat);
    double c = cos(lat);
    double z = _radiusZ * s;
    for (int i=0; i<2*_resolution-1; i++) {
      double lon = -pi + i*incAngle;
      double x = _radiusX * c * cos(lon);
      double y = _radiusY * c * sin(lon);
      vertices->add(x, y, z);
      float u = (float) i / (2*_resolution-2);
      float v = (_cozzi)? (float)(1-s)/2 : (float)j/(_resolution-1);
      texCoords->add(u, v);
    }
  }
  
  if (_borderWidth > 0) {
    CompositeMesh* compositeMesh = new CompositeMesh();
    compositeMesh->addMesh(createSurfaceMesh(rc, vertices, texCoords));
    compositeMesh->addMesh(createBorderMesh(rc, vertices));
    delete vertices;
    delete texCoords;
    return compositeMesh;
  }

  Mesh* mesh = createSurfaceMesh(rc, vertices, texCoords);
  delete vertices;
  delete texCoords;
  return mesh;
}
