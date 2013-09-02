//
//  IndexedGeometryMesh.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 23/06/13.
//
//

#include "IndexedGeometryMesh.hpp"

#include "IndexedMesh.hpp"
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

IndexedGeometryMesh::IndexedGeometryMesh(const int primitive,
                         const Vector3D& center,
                         IFloatBuffer* vertices, bool ownsVertices,
                         IShortBuffer* indices, bool ownsIndices,
                         float lineWidth,
                         float pointSize,
                         bool depthTest) :
AbstractGeometryMesh(primitive,
             ownsVertices,
             center,
             vertices,
             lineWidth,
             pointSize,
             depthTest),
_indices(indices),
_ownsIndices(ownsIndices)
{
  
}

void IndexedGeometryMesh::rawRender(const G3MRenderContext* rc) const{
  GL* gl = rc->getGL();
  gl->drawElements(_primitive, _indices, _glState, *rc->getGPUProgramManager());
}