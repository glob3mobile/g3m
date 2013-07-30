//
//  BoxShape.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/16/12.
//
//

#include "BoxShape.hpp"

#include "FloatBufferBuilderFromCartesian3D.hpp"
#include "ShortBufferBuilder.hpp"
#include "IndexedMesh.hpp"
#include "GLConstants.hpp"
#include "CompositeMesh.hpp"

Mesh* BoxShape::createBorderMesh(const G3MRenderContext* rc) {
  const float lowerX = (float) -(_extentX / 2);
  const float upperX = (float) +(_extentX / 2);
  const float lowerY = (float) -(_extentY / 2);
  const float upperY = (float) +(_extentY / 2);
  const float lowerZ = (float) -(_extentZ / 2);
  const float upperZ = (float) +(_extentZ / 2);

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

  const int numIndices = 48;
  short i[] = {
    0, 1, 1, 2, 2, 3, 3, 0,
    1, 5, 5, 6, 6, 2, 2, 1,
    5, 4, 4, 7, 7, 6, 6, 5,
    4, 0, 0, 3, 3, 7, 7, 4,
    3, 2, 2, 6, 6, 7, 7, 3,
    0, 1, 1, 5, 5, 4, 4, 0
  };

//  FloatBufferBuilderFromCartesian3D vertices(CenterStrategy::noCenter(), Vector3D::zero);
  FloatBufferBuilderFromCartesian3D vertices = FloatBufferBuilderFromCartesian3D::builderWithoutCenter();
  ShortBufferBuilder indices;

  const unsigned int numVertices = 8;
  for (unsigned int n=0; n<numVertices; n++) {
    vertices.add(v[n*3], v[n*3+1], v[n*3+2]);
  }

  for (unsigned int n=0; n<numIndices; n++) {
    indices.add(i[n]);
  }

  Color* borderColor = (_borderColor != NULL) ? new Color(*_borderColor) : new Color(*_surfaceColor);

  return new IndexedMesh(GLPrimitive::lines(),
                         true,
                         vertices.getCenter(),
                         vertices.create(),
                         indices.create(),
                         _borderWidth,
                         1,
                         borderColor);
}

Mesh* BoxShape::createSurfaceMesh(const G3MRenderContext* rc) {
  const float lowerX = (float) -(_extentX / 2);
  const float upperX = (float) +(_extentX / 2);
  const float lowerY = (float) -(_extentY / 2);
  const float upperY = (float) +(_extentY / 2);
  const float lowerZ = (float) -(_extentZ / 2);
  const float upperZ = (float) +(_extentZ / 2);

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

//  FloatBufferBuilderFromCartesian3D vertices(CenterStrategy::noCenter(), Vector3D::zero);
  FloatBufferBuilderFromCartesian3D vertices = FloatBufferBuilderFromCartesian3D::builderWithoutCenter();
  ShortBufferBuilder indices;

  const unsigned int numVertices = 8;
  for (unsigned int n=0; n<numVertices; n++) {
    vertices.add(v[n*3], v[n*3+1], v[n*3+2]);
  }

  for (unsigned int n=0; n<numIndices; n++) {
    indices.add(i[n]);
  }
  
  Color* surfaceColor = (_surfaceColor == NULL) ? NULL : new Color(*_surfaceColor);

  return new IndexedMesh(GLPrimitive::triangleStrip(),
                         true,
                         vertices.getCenter(),
                         vertices.create(),
                         indices.create(),
                         _borderWidth,
                         1,
                         surfaceColor);
}


Mesh* BoxShape::createMesh(const G3MRenderContext* rc) {
  if (_borderWidth > 0) {
    CompositeMesh* compositeMesh = new CompositeMesh();
    compositeMesh->addMesh(createSurfaceMesh(rc));
    compositeMesh->addMesh(createBorderMesh(rc));
    return compositeMesh;
  }

  return createSurfaceMesh(rc);
}
