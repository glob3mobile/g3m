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


IndexedMesh::~IndexedMesh() {
  if (_ownsIndices) {
    delete _indices;
  }

#ifdef JAVA_CODE
  super.dispose();
#endif

}

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
                         const float colorsIntensity,
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
             colorsIntensity,
             depthTest,
             normals,
             polygonOffsetFill,
             polygonOffsetFactor,
             polygonOffsetUnits),
_indices(indices),
_ownsIndices(ownsIndices)
{

}

void IndexedMesh::rawRender(const G3MRenderContext* rc) const {
  GL* gl = rc->getGL();
  gl->drawElements(_primitive, _indices, _glState, *rc->getGPUProgramManager());
}
