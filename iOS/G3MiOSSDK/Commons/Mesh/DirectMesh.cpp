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
                         IFloatBuffer* vertices,
                         float lineWidth,
                         Color* flatColor,
                         IFloatBuffer* colors,
                         const float colorsIntensity) :
AbstractMesh(primitive,
             owner,
             center,
             vertices,
             lineWidth,
             flatColor,
             colors,
             colorsIntensity)
{
 /* _glState->enableVerticesPosition();
  if (_colors) 
    _glState->enableVertexColor(_colors, _colorsIntensity);
  if (_flatColor) {   
    _glState->enableFlatColor(*_flatColor, _colorsIntensity);
    if (_flatColor->isTransparent()) {
      _glState->enableBlend();
    }
  }*/
}

void DirectMesh::rawRender(const G3MRenderContext* rc) const {
  GL *gl = rc->getGL();

  const int verticesCount = getVertexCount();
  gl->drawArrays(_primitive, 0, verticesCount);
}
