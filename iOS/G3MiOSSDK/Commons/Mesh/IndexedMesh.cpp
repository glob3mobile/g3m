//
//  IndexedMesh.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 22/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include <stdlib.h>

#include "IndexedMesh.hpp"
#include "GL.hpp"
#include "IIntBuffer.hpp"

IndexedMesh::~IndexedMesh() {
  if (_owner) {
    delete _indices;
  }
}

IndexedMesh::IndexedMesh(const int primitive,
                         bool owner,
                         const Vector3D& center,
                         IFloatBuffer* vertices,
                         IIntBuffer* indices,
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
             colorsIntensity),
_indices(indices)
{
}

void IndexedMesh::rawRender(const G3MRenderContext* rc) const {
  GL *gl = rc->getGL();
  gl->drawElements(_primitive, _indices);
}
