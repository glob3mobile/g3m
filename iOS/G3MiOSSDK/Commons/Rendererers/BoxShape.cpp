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
#include "DirectMesh.hpp"

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
  FloatBufferBuilderFromCartesian3D* vertices = FloatBufferBuilderFromCartesian3D::builderWithoutCenter();
  ShortBufferBuilder indices;

  const unsigned int numVertices = 8;
  for (unsigned int n=0; n<numVertices; n++) {
    vertices->add(v[n*3], v[n*3+1], v[n*3+2]);
  }

  for (unsigned int n=0; n<numIndices; n++) {
    indices.add(i[n]);
  }

  Color* borderColor = (_borderColor != NULL) ? new Color(*_borderColor) : new Color(*_surfaceColor);

  Mesh* result = new IndexedMesh(GLPrimitive::lines(),
                                 true,
                                 vertices->getCenter(),
                                 vertices->create(),
                                 indices.create(),
                                 (_borderWidth>1)? _borderWidth : 1,
                                 1,
                                 borderColor);

  delete vertices;

  return result;
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

  FloatBufferBuilderFromCartesian3D* vertices = FloatBufferBuilderFromCartesian3D::builderWithoutCenter();
  ShortBufferBuilder indices;

  const unsigned int numVertices = 8;
  for (unsigned int n=0; n<numVertices; n++) {
    vertices->add(v[n*3], v[n*3+1], v[n*3+2]);
  }

  for (unsigned int n=0; n<numIndices; n++) {
    indices.add(i[n]);
  }
  
  Color* surfaceColor = (_surfaceColor == NULL) ? NULL : new Color(*_surfaceColor);

  Mesh* result = new IndexedMesh(GLPrimitive::triangleStrip(),
                                 true,
                                 vertices->getCenter(),
                                 vertices->create(),
                                 indices.create(),
                                 (_borderWidth>1)? _borderWidth : 1,
                                 1,
                                 surfaceColor);

  delete vertices;

  return result;
}

Mesh* BoxShape::createSurfaceMeshWithNormals(const G3MRenderContext* rc) {
  const float lowerX = (float) -(_extentX / 2);
  const float upperX = (float) +(_extentX / 2);
  const float lowerY = (float) -(_extentY / 2);
  const float upperY = (float) +(_extentY / 2);
  const float lowerZ = (float) -(_extentZ / 2);
  const float upperZ = (float) +(_extentZ / 2);

  FloatBufferBuilderFromCartesian3D* vertices = FloatBufferBuilderFromCartesian3D::builderWithoutCenter();
  FloatBufferBuilderFromCartesian3D* normals  = FloatBufferBuilderFromCartesian3D::builderWithoutCenter();

  const float v[] = {
    //FACE 1
    lowerX, upperY, lowerZ,
    lowerX, upperY, upperZ,
    upperX, upperY, lowerZ,

    upperX, upperY, lowerZ,
    lowerX, upperY, upperZ,
    upperX, upperY, upperZ,

    //FACE 2
    lowerX, lowerY, lowerZ,
    lowerX, lowerY, upperZ,
    upperX, lowerY, lowerZ,

    upperX, lowerY, lowerZ,
    lowerX, lowerY, upperZ,
    upperX, lowerY, upperZ,

    //FACE 3
    lowerX, lowerY, upperZ,
    lowerX, upperY, upperZ,
    upperX, lowerY, upperZ,

    upperX, lowerY, upperZ,
    lowerX, upperY, upperZ,
    upperX, upperY, upperZ,

    //FACE 4
    lowerX, lowerY, lowerZ,
    lowerX, upperY, lowerZ,
    upperX, lowerY, lowerZ,

    upperX, lowerY, lowerZ,
    lowerX, upperY, lowerZ,
    upperX, upperY, lowerZ,

    //FACE 5
    upperX, lowerY, lowerZ,
    upperX, lowerY, upperZ,
    upperX, upperY, lowerZ,

    upperX, upperY, lowerZ,
    upperX, lowerY, upperZ,
    upperX, upperY, upperZ,
    
    //FACE 6
    lowerX, lowerY, lowerZ,
    lowerX, lowerY, upperZ,
    lowerX, upperY, lowerZ,

    lowerX, upperY, lowerZ,
    lowerX, lowerY, upperZ,
    lowerX, upperY, upperZ,
  };

  const float n[] = {
    //FACE 1
    0, 1, 0,
    //FACE 2
    0, -1, 0,
    //FACE 3
    0, 0, 1,
    //FACE 4
    0, 0, -1,
    //FACE 5
    1, 0, 0,
    //FACE 6
    -1, 0, 0
  };


  const unsigned int numFaces = 6;
  const unsigned int numVertices = 6 * numFaces;
  
  for (unsigned int i=0; i<numVertices; i++) {
    vertices->add(v[i*3], v[i*3+1], v[i*3+2]);
  }

  for (unsigned int i=0; i<numFaces; i++) {
    normals->add(n[i*3], n[i*3+1], n[i*3+2]);
    normals->add(n[i*3], n[i*3+1], n[i*3+2]);
    normals->add(n[i*3], n[i*3+1], n[i*3+2]);
    normals->add(n[i*3], n[i*3+1], n[i*3+2]);
    normals->add(n[i*3], n[i*3+1], n[i*3+2]);
    normals->add(n[i*3], n[i*3+1], n[i*3+2]);
  }

  Color* surfaceColor = (_surfaceColor == NULL) ? NULL : new Color(*_surfaceColor);

  Mesh* result = new DirectMesh(GLPrimitive::triangles(),
                                true,
                                vertices->getCenter(),
                                vertices->create(),
                                (_borderWidth>1)? _borderWidth : 1,
                                1,
                                surfaceColor,
                                NULL,
                                1,
                                true,
                                normals->create());

  delete vertices;
  delete normals;

  return result;
}


Mesh* BoxShape::createMesh(const G3MRenderContext* rc) {

  Mesh* surface = NULL;
  if (_useNormals) {
    surface = createSurfaceMeshWithNormals(rc);
//    surface = createSurfaceMesh(rc);
  } else{
    surface = createSurfaceMesh(rc);
  }
  
  if (_borderWidth > 0) {
    CompositeMesh* compositeMesh = new CompositeMesh();
    compositeMesh->addMesh(surface);
    compositeMesh->addMesh(createBorderMesh(rc));
    return compositeMesh;
  }

  return surface;
}

std::vector<double> BoxShape::intersectionsDistances(const Vector3D& origin,
                                                     const Vector3D& direction) const {
  std::vector<double> distances;
  
//  double tmin=-1e10, tmax=1e10;
//  double t1, t2;
//  // transform 6 planes
//  MutableMatrix44D* M = createTransformMatrix(_planet);
//  const Quadric transformedFront = _frontQuadric.transformBy(*M);
//  const Quadric transformedBack = _backQuadric.transformBy(*M);
//  const Quadric transformedLeft = _leftQuadric.transformBy(*M);
//  const Quadric transformedRight = _rightQuadric.transformBy(*M);
//  const Quadric transformedTop = _topQuadric.transformBy(*M);
//  const Quadric transformedBottom = _bottomQuadric.transformBy(*M);
//  delete M;
//
//  // intersecction with X planes
//  std::vector<double> frontDistance = transformedFront.intersectionsDistances(origin, direction);
//  std::vector<double> backDistance = transformedBack.intersectionsDistances(origin, direction);
//  if (frontDistance.size()==1 && backDistance.size()==1) {
//    if (frontDistance[0] < backDistance[0]) {
//      t1 = frontDistance[0];
//      t2 = backDistance[0];
//    } else {
//      t2 = frontDistance[0];
//      t1 = backDistance[0];
//    }
//    if (t1 > tmin)
//      tmin = t1;
//    if (t2 < tmax)
//      tmax = t2;
//  }
//  
//  // intersections with Y planes
//  std::vector<double> leftDistance = transformedLeft.intersectionsDistances(origin, direction);
//  std::vector<double> rightDistance = transformedRight.intersectionsDistances(origin, direction);
//  if (leftDistance.size()==1 && rightDistance.size()==1) {
//    if (leftDistance[0] < rightDistance[0]) {
//      t1 = leftDistance[0];
//      t2 = rightDistance[0];
//    } else {
//      t2 = leftDistance[0];
//      t1 = rightDistance[0];
//    }
//    if (t1 > tmin)
//      tmin = t1;
//    if (t2 < tmax)
//      tmax = t2;
//  }
//  
//  // intersections with Z planes
//  std::vector<double> topDistance = transformedTop.intersectionsDistances(origin, direction);
//  std::vector<double> bottomDistance = transformedBottom.intersectionsDistances(origin, direction);
//  if (topDistance.size()==1 && bottomDistance.size()==1) {
//    if (topDistance[0] < bottomDistance[0]) {
//      t1 = topDistance[0];
//      t2 = bottomDistance[0];
//    } else {
//      t2 = topDistance[0];
//      t1 = bottomDistance[0];
//    }
//    if (t1 > tmin)
//      tmin = t1;
//    if (t2 < tmax)
//      tmax = t2;
//  }
//  
//  if (tmin < tmax) {
//    distances.push_back(tmin);
//    distances.push_back(tmax);
//  }

  return distances;
}

