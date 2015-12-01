//
//  WireframeUtils.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 1/12/15.
//
//

#include "WireframeUtils.hpp"

#include "ShortBufferBuilder.hpp"
#include "FloatBufferBuilderFromCartesian3D.hpp"
#include "IFloatBuffer.hpp"
#include "IndexedMesh.hpp"


IShortBuffer* WireframeUtils::createIndicesFromTriangleStrip(const IShortBuffer* triangleStripIndices){
  
  ShortBufferBuilder indices;
  
  const size_t size = triangleStripIndices->size();
  for (int i = 0; i < size; i ++) {
    indices.add(triangleStripIndices->get(i));
  }
  
  return indices.create();
}

AbstractMesh* WireframeUtils::createWireframeMesh(const IndexedGeometryMesh* mesh){
  
  const IFloatBuffer* vertices = mesh->getVertices();
  
  FloatBufferBuilderFromCartesian3D* newVertBuilder = FloatBufferBuilderFromCartesian3D::builderWithoutCenter();
  
  //Copying
  const size_t size = vertices->size();
  for (int i = 0; i < size; i = i+3) {
    newVertBuilder->add(vertices->get(i),
                        vertices->get(i+1),
                        vertices->get(i+2));
  }
  
  IShortBuffer* indices = createIndicesFromTriangleStrip(mesh->getIndices());
  
  
//  IndexedMesh::IndexedMesh(const int primitive,
//                           bool owner,
//                           const Vector3D& center,
//                           IFloatBuffer* vertices,
//                           IShortBuffer* indices,
//                           float lineWidth,
//                           float pointSize,
//                           const Color* flatColor,
//                           IFloatBuffer* colors,
//                           const float colorsIntensity,
//                           bool depthTest,
//                           IFloatBuffer* normals) :
  
  IndexedMesh* im = new IndexedMesh(GLPrimitive::lineStrip(),
                                    true,
                                    mesh->getCenter(),
                                    newVertBuilder->create(),
                                    indices,
                                    2.5,
                                    1.0,
                                    Color::newFromRGBA(1.0, 0.0, 0.0, 1.0),
                                    NULL,
                                    1.0,
                                    true,
                                    NULL,
                                    true, 10, 10);
  
  return im;
                                    
  
  
}

