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
