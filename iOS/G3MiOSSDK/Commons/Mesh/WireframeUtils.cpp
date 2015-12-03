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
#include "BoundingVolume.hpp"
#include "Sphere.hpp"


IShortBuffer* WireframeUtils::createIndicesFromTriangleStrip(const IShortBuffer* triangleStripIndices, int lastIndex){
  
  ShortBufferBuilder indices;
  
  const size_t size = lastIndex < 0? triangleStripIndices->size() : lastIndex;
  for (int i = 0; i < size; i ++) {
    indices.add(triangleStripIndices->get(i));
  }
  
  return indices.create();
}

AbstractMesh* WireframeUtils::createWireframeMesh(const IndexedGeometryMesh* mesh, int lastVertex, int lastIndex){
  
  const IFloatBuffer* vertices = mesh->getVertices();
  
  Sphere* bv = mesh->getBoundingVolume()->createSphere();
  Vector3D centerOfMesh = bv->getCenter();
  double meshSize = bv->getRadius();
  
  
  FloatBufferBuilderFromCartesian3D* newVertBuilder = FloatBufferBuilderFromCartesian3D::builderWithoutCenter();
  
  //Copying
  const size_t size = lastVertex < 0? vertices->size() : lastVertex * 3;
  for (int i = 0; i < size; i = i+3) {
    
#warning UNTIL HAVING POLYGONOFFSET
    Vector3D v(vertices->get(i),
               vertices->get(i+1),
               vertices->get(i+2));
    
    Vector3D d = centerOfMesh.sub(v);
    Vector3D fv = v.add(d.times((0.01 * meshSize) / d.length()));
    
    newVertBuilder->add(fv);
  }
  
  IShortBuffer* indices = createIndicesFromTriangleStrip(mesh->getIndices(), lastIndex);
  
  
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
                                    true, -1, -1);
  
  return im;
                                    
  
  
}

