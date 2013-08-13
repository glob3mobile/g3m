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
  if (_owner) {
    delete _indices;
  }

  JAVA_POST_DISPOSE
}

IndexedGeometryMesh::IndexedGeometryMesh(const int primitive,
                         bool owner,
                         const Vector3D& center,
                         IFloatBuffer* vertices,
                         IShortBuffer* indices,
                         float lineWidth,
                         float pointSize,
                         bool depthTest) :
AbstractGeometryMesh(primitive,
             owner,
             center,
             vertices,
             lineWidth,
             pointSize,
             depthTest),
_indices(indices)
{
  
}

void IndexedGeometryMesh::rawRender(const G3MRenderContext* rc) const{
  GL* gl = rc->getGL();
  gl->drawElements(_primitive, _indices, &_glState, *rc->getGPUProgramManager());
}