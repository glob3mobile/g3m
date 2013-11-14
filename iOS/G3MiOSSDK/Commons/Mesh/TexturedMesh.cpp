//
//  TexturedMesh.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 12/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "TexturedMesh.hpp"

void TexturedMesh::createGLState() {
  _textureMapping->modifyGLState(*_glState);
}

void TexturedMesh::rawRender(const G3MRenderContext* rc,
                             const GLState* parentState) const{
  _glState->setParent(parentState);
  _mesh->render(rc, _glState);
}
