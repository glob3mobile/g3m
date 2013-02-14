//
//  EllipsoidShape.cpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 02/13/13.
//
//

#include <math.h>

#include "EllipsoidShape.hpp"

#include "FloatBufferBuilderFromCartesian3D.hpp"
#include "FloatBufferBuilderFromGeodetic.hpp"
#include "ShortBufferBuilder.hpp"
#include "IndexedMesh.hpp"
#include "GLConstants.hpp"
#include "CompositeMesh.hpp"

Mesh* EllipsoidShape::createBorderMesh(const G3MRenderContext* rc) {

  // create vertices
  if (_resolution<3) _resolution = 3;
  FloatBufferBuilderFromCartesian3D vertices(CenterStrategy::noCenter(), Vector3D::zero());
  const double pi = IMathUtils::instance()->pi();
  const double incAngle = pi/(_resolution-1);
  for (int j=0; j<_resolution; j++) {
    double lat = pi/2 - j*incAngle;
    double z = _radiusX * sin(lat);
    double c = _radiusX * cos(lat);
    for (int i=0; i<2*_resolution-1; i++) {
      double lon = -pi + i*incAngle;
      double x = c * cos(lon);
      double y = c * sin(lon);
      vertices.add(x, y, z);
    }
  }

  /*
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
   */
  
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
                         vertices.getCenter(),
                         vertices.create(),
                         indices.create(),
                         _borderWidth,
                         1,
                         borderColor);
}

Mesh* EllipsoidShape::createSurfaceMesh(const G3MRenderContext* rc) {
  const float lowerX = (float) -(_radiusX / 2);
  const float upperX = (float) +(_radiusX / 2);
  const float lowerY = (float) -(_radiusY / 2);
  const float upperY = (float) +(_radiusY / 2);
  const float lowerZ = (float) -(_radiusZ / 2);
  const float upperZ = (float) +(_radiusZ / 2);

  float v[] = {
    lowerX, lowerY, lowerZ,
    lowerX, upperY, lowerZ,
    lowerX, upperY, upperZ,
    lowerX, lowerY, upperZ,
    upperX, lowerY, lowerZ,
    upperX, upperY, lowerZ,
    upperX, upperY, upperZ,
    upperX, lowerY, upperZ
  };

  const int numIndices = 23;
  short i[] = {
    3, 0, 7, 4, 6, 5, 2, 1, 3, 0, 0,
    2, 2, 3, 6, 7, 7,
    5, 5, 4, 1, 0, 0
  };

  FloatBufferBuilderFromCartesian3D vertices(CenterStrategy::noCenter(), Vector3D::zero());
  ShortBufferBuilder indices;

  const unsigned int numVertices = 8;
  for (unsigned int n=0; n<numVertices; n++) {
    vertices.add(v[n*3], v[n*3+1], v[n*3+2]);
  }

  for (unsigned int n=0; n<numIndices; n++) {
    indices.add(i[n]);
  }

  return new IndexedMesh(GLPrimitive::triangleStrip(),
                         true,
                         vertices.getCenter(),
                         vertices.create(),
                         indices.create(),
                         _borderWidth,
                         1,
                         _surfaceColor);
}


Mesh* EllipsoidShape::createMesh(const G3MRenderContext* rc) {
  if (_borderWidth > 0) {
    CompositeMesh* compositeMesh = new CompositeMesh();
    //compositeMesh->addMesh(createSurfaceMesh(rc));
    compositeMesh->addMesh(createBorderMesh(rc));
    return compositeMesh;
  }

  return createSurfaceMesh(rc);
}
