//
//  IndexedMesh.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 22/06/12.
//

#include <stdlib.h>

#include "IndexedMesh.hpp"
#include "GL.hpp"
#include "IShortBuffer.hpp"
#include "G3MRenderContext.hpp"


IndexedMesh::IndexedMesh(const int primitive,
                         const Vector3D& center,
                         IFloatBuffer* vertices,
                         bool ownsVertices,
                         IShortBuffer* indices,
                         bool ownsIndices,
                         float lineWidth,
                         float pointSize,
                         const Color* flatColor,
                         IFloatBuffer* colors,
                         bool depthTest,
                         IFloatBuffer* normals,
                         bool polygonOffsetFill,
                         float polygonOffsetFactor,
                         float polygonOffsetUnits) :
AbstractMesh(primitive,
             ownsVertices,
             center,
             vertices,
             lineWidth,
             pointSize,
             flatColor,
             colors,
             depthTest,
             normals,
             polygonOffsetFill,
             polygonOffsetFactor,
             polygonOffsetUnits),
_indices(indices),
_ownsIndices(ownsIndices)
{

}

IndexedMesh::~IndexedMesh() {
  if (_ownsIndices) {
    delete _indices;
  }

#ifdef JAVA_CODE
  super.dispose();
#endif
}

void IndexedMesh::renderMesh(const G3MRenderContext* rc,
                             GLState* glState) const {
  GL* gl = rc->getGL();
  gl->drawElements(_primitive,
                   _indices,
                   glState,
                   *rc->getGPUProgramManager());
}
