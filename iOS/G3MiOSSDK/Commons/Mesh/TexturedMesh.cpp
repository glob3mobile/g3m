//
//  TexturedMesh.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 12/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "TexturedMesh.hpp"

void TexturedMesh::render(const G3MRenderContext* rc) const {
  _mesh->render(rc);
}

void TexturedMesh::createGLState(){
  _textureMapping->modifyGLState(_glState);
}

void TexturedMesh::render(const G3MRenderContext* rc, const GLState* parentState){
  _glState.setParent(parentState);
  _mesh->render(rc, &_glState);
}
