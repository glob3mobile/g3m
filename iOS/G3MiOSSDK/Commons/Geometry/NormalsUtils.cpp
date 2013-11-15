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
  return Vector3F(vertices->get(index),
                  vertices->get(index + 1),
                  vertices->get(index + 2));
}

IFloatBuffer* NormalsUtils::createTriangleSmoothNormals(const IFloatBuffer* vertices,
                                                        const IShortBuffer* indices) {

  const int size = vertices->size();
  IFloatBuffer* normals = IFactory::instance()->createFloatBuffer(size);
  for (int i = 0; i < size; i++) {
    normals->rawPut(i, 0);
  }

  const int indicesSize = indices->size();
  for (int i = 0; i < indicesSize; i += 3) {
    const short index0 = indices->get(i);
    const short index1 = indices->get(i + 1);
    const short index2 = indices->get(i + 2);

    const Vector3F vertex0 = getVertex(vertices, index0);
    const Vector3F vertex1 = getVertex(vertices, index1);
    const Vector3F vertex2 = getVertex(vertices, index2);

    const Vector3F p10 = vertex1.sub(vertex0);
    const Vector3F p20 = vertex2.sub(vertex0);

    const Vector3F normal = p10.cross(p20);
    normals->rawAdd(i    , normal._x);
    normals->rawAdd(i + 1, normal._y);
    normals->rawAdd(i + 2, normal._z);
  }

  for (int i = 0; i < size; i += 3) {
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

//IFloatBuffer* NormalsUtils::createTriangleStripSmoothNormals(const IFloatBuffer* vertices,
//                                                             const IShortBuffer* indices) {
//  const int size = vertices->size();
//  IFloatBuffer* normals = IFactory::instance()->createFloatBuffer(size);
//  for (int i = 0; i < size; i++) {
//    normals->rawPut(i, 0);
//  }
//
//  int __TODO;
//  return normals;
//}
