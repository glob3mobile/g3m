//
//  DirectMesh.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/1/12.
//
//

#include "DirectMesh.hpp"

#include "GL.hpp"
#include "G3MRenderContext.hpp"
#include "ErrorHandling.hpp"


DirectMesh::DirectMesh(const int primitive,
                       bool owner,
                       const Vector3D& center,
                       const IFloatBuffer* vertices,
                       float lineWidth,
                       float pointSize,
                       const Color* flatColor,
                       const IFloatBuffer* colors,
                       bool depthTest,
                       const IFloatBuffer* normals,
                       bool polygonOffsetFill,
                       float polygonOffsetFactor,
                       float polygonOffsetUnits) :
AbstractMesh(primitive,
             owner,
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
             polygonOffsetUnits)
{
  _renderVerticesCount = vertices->size() / 3;
}

void DirectMesh::rawRender(const G3MRenderContext* rc) const {
  GL* gl = rc->getGL();

  gl->drawArrays(_primitive,
                 0,
                 (int)_renderVerticesCount,
                 _glState,
                 *rc->getGPUProgramManager());
}

void DirectMesh::setRenderVerticesCount(size_t renderVerticesCount) {
  if (renderVerticesCount > getRenderVerticesCount()) {
    THROW_EXCEPTION("Invalid renderVerticesCount");
  }
  _renderVerticesCount = renderVerticesCount;
}
