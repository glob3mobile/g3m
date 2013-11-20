//
//  NormalsUtils.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/15/13.
//
//

#include "NormalsUtils.hpp"

#include "IFloatBuffer.hpp"
#include "IShortBuffer.hpp"
#include "IFactory.hpp"

Vector3F NormalsUtils::getVertex(const IFloatBuffer* vertices,
                                 short index) {
  return Vector3F(vertices->get(index*3),
                  vertices->get(index*3 + 1),
                  vertices->get(index*3 + 2));
}

void NormalsUtils::addNormal(IFloatBuffer* normals,
                             int index,
                             const Vector3F& normal) {
  normals->rawAdd(index*3    , normal._x);
  normals->rawAdd(index*3 + 1, normal._y);
  normals->rawAdd(index*3 + 2, normal._z);
}

Vector3F NormalsUtils::calculateNormal(const IFloatBuffer* vertices,
                                       short index0,
                                       short index1,
                                       short index2) {
  const Vector3F vertex0 = getVertex(vertices, index0);
  const Vector3F vertex1 = getVertex(vertices, index1);
  const Vector3F vertex2 = getVertex(vertices, index2);

  const Vector3F p10 = vertex1.sub(vertex0);
  const Vector3F p20 = vertex2.sub(vertex0);

  return p10.cross(p20);
}

IFloatBuffer* NormalsUtils::createTriangleSmoothNormals(const IFloatBuffer* vertices,
                                                        const IShortBuffer* indices) {

  const int verticesSize = vertices->size();
  IFloatBuffer* normals = IFactory::instance()->createFloatBuffer(verticesSize);
  for (int i = 0; i < verticesSize; i++) {
    normals->rawPut(i, 0);
  }

  const int indicesSize = indices->size();
  for (int i = 0; i < indicesSize; i += 3) {
    const short index0 = indices->get(i);
    const short index1 = indices->get(i + 1);
    const short index2 = indices->get(i + 2);

    const Vector3F normal = calculateNormal(vertices, index0, index1, index2);
    addNormal(normals, index0, normal);
    addNormal(normals, index1, normal);
    addNormal(normals, index2, normal);
  }

  for (int i = 0; i < verticesSize; i += 3) {
    const float x = normals->get(i);
    const float y = normals->get(i + 1);
    const float z = normals->get(i + 2);

    const Vector3F normal = Vector3F(x, y, z).normalized();
    normals->rawPut(i    , normal._x);
    normals->rawPut(i + 1, normal._y);
    normals->rawPut(i + 2, normal._z);
  }

  return normals;
}

IFloatBuffer* NormalsUtils::createTriangleStripSmoothNormals(const IFloatBuffer* vertices,
                                                             const IShortBuffer* indices) {
  const int verticesSize = vertices->size();
  IFloatBuffer* normals = IFactory::instance()->createFloatBuffer(verticesSize);
  for (int i = 0; i < verticesSize; i++) {
    normals->rawPut(i, 0);
  }

  short index0 = indices->get(0);
  short index1 = indices->get(1);

  const int indicesSize = indices->size();
  for (int i = 2; i < indicesSize; i++) {
    const short index2 = indices->get(i);

    const Vector3F normal = (i % 2 == 0)
    /*                          */ ? calculateNormal(vertices, index0, index1, index2)
    /*                          */ : calculateNormal(vertices, index0, index2, index1);
    addNormal(normals, index0, normal);
    addNormal(normals, index1, normal);
    addNormal(normals, index2, normal);

    index0 = index1;
    index1 = index2;
  }

  //http://stackoverflow.com/questions/3485034/convert-triangle-strips-to-triangles

  for (int i = 0; i < verticesSize; i += 3) {
    const float x = normals->get(i);
    const float y = normals->get(i + 1);
    const float z = normals->get(i + 2);

    const Vector3F normal = Vector3F(x, y, z).normalized();
    normals->rawPut(i    , normal._x);
    normals->rawPut(i + 1, normal._y);
    normals->rawPut(i + 2, normal._z);
  }
  
  return normals;
}
