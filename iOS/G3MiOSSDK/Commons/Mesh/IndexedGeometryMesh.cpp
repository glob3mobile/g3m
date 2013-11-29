//
//  IndexedGeometryMesh.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 23/06/13.
//
//

#include "IndexedGeometryMesh.hpp"

#include "GL.hpp"
#include "IShortBuffer.hpp"

#include "DirectMesh.hpp"
#include "FloatBufferBuilderFromCartesian3D.hpp"
#include "CompositeMesh.hpp"
#include "Sphere.hpp"

IndexedGeometryMesh::~IndexedGeometryMesh() {
  if (_ownsIndices) {
    delete _indices;
  }

#ifdef JAVA_CODE
  super.dispose();
#endif

}

IndexedGeometryMesh::IndexedGeometryMesh(const int       primitive,
                                         const Vector3D& center,
                                         IFloatBuffer*   vertices,
                                         bool            ownsVertices,
                                         IFloatBuffer*   normals,
                                         bool            ownsNormals,
                                         IShortBuffer*   indices,
                                         bool            ownsIndices,
                                         float           lineWidth,
                                         float           pointSize,
                                         bool            depthTest) :
AbstractGeometryMesh(primitive,
                     center,
                     vertices,
                     ownsVertices,
                     normals,
                     ownsNormals,
                     lineWidth,
                     pointSize,
                     depthTest),
_indices(indices),
_ownsIndices(ownsIndices)
{
//  ILogger::instance()->logInfo("Created an IndexedGeometryMesh with %d vertices, %d indices, %d normals",
//                               vertices->size(),
//                               indices->size(),
//                               normals->size());
}

IndexedGeometryMesh::IndexedGeometryMesh(const int primitive,
                                         const Vector3D& center,
                                         IFloatBuffer* vertices,
                                         bool ownsVertices,
                                         IShortBuffer* indices,
                                         bool ownsIndices,
                                         float lineWidth,
                                         float pointSize,
                                         bool depthTest) :
AbstractGeometryMesh(primitive,
                     center,
                     vertices,
                     ownsVertices,
                     NULL, // normals
                     false, // ownsNormals
                     lineWidth,
                     pointSize,
                     depthTest),
_indices(indices),
_ownsIndices(ownsIndices)
{
//  ILogger::instance()->logInfo("Created an IndexedGeometryMesh with %d vertices, %d indices",
//                               vertices->size(),
//                               indices->size());
}

void IndexedGeometryMesh::rawRender(const G3MRenderContext* rc) const{
  GL* gl = rc->getGL();
  gl->drawElements(_primitive, _indices, _glState, *rc->getGPUProgramManager());
}

Mesh* IndexedGeometryMesh::createNormalsMesh() const{

  DirectMesh* verticesMesh = new DirectMesh(GLPrimitive::points(),
                                            false,
                                            _center,
                                            _vertices,
                                            1.0,
                                            3.0,
                                            new Color(Color::red()));

  FloatBufferBuilderFromCartesian3D* fbb = FloatBufferBuilderFromCartesian3D::builderWithoutCenter();

  BoundingVolume* volume = getBoundingVolume();
  Sphere* sphere = volume->createSphere();
  double normalsSize = sphere->getRadius() / 100.0;
  delete sphere;

  const int size = _vertices->size();
  for (int i = 0; i < size; i+=3) {

    Vector3D v(_vertices->get(i), _vertices->get(i+1), _vertices->get(i+2));
    Vector3D n(_normals->get(i), _normals->get(i+1), _normals->get(i+2));

    Vector3D v_n = v.add(n.normalized().times(normalsSize));

    fbb->add(v);
    fbb->add(v_n);
  }

  DirectMesh* normalsMesh = new DirectMesh(GLPrimitive::lines(),
                                           true,
                                           _center,
                                           fbb->create(),
                                           2.0,
                                           1.0,
                                           new Color(Color::blue()));

  delete fbb;

  CompositeMesh* compositeMesh = new CompositeMesh();
  compositeMesh->addMesh(verticesMesh);
  compositeMesh->addMesh(normalsMesh);

  return compositeMesh;
  
}
