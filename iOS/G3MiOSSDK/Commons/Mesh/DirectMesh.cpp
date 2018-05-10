//
//  DirectMesh.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/1/12.
//
//

#include "DirectMesh.hpp"
#include "GL.hpp"

DirectMesh::DirectMesh(const int primitive,
                       bool owner,
                       const Vector3D& center,
                       const IFloatBuffer* vertices,
                       float lineWidth,
                       float pointSize,
                       const Color* flatColor,
                       const IFloatBuffer* colors,
                       const float colorsIntensity,
                       bool depthTest,
                       const IFloatBuffer* normals,
                       bool polygonOffsetFill,
                       float polygonOffsetFactor,
                       float polygonOffsetUnits,
                       IFloatBuffer* valuesInColorRange,
                       const Color* colorRangeAt0,
                       const Color* colorRangeAt1) :
AbstractMesh(primitive,
             owner,
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
             polygonOffsetUnits,
             valuesInColorRange,
             colorRangeAt0,
             colorRangeAt1)
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
