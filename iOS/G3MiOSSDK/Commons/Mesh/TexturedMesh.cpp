//
//  TexturedMesh.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 12/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "TexturedMesh.hpp"

void TexturedMesh::createGLState() {
}

void TexturedMesh::rawRender(const G3MRenderContext* rc,
                             const GLState* parentState) const{
#warning To Diego: As a textureMapping could be used by more than 1 TexturedMesh (I think) it's necessary to check glState consistency at every frame. Otherwise you should store somehow a list of every user of the mapping in order to change their states when any parameter of the mapping is updated. Method modifyGLState() is now much lighter though.
  _textureMapping->modifyGLState(*_glState);

  _glState->setParent(parentState);
  _mesh->render(rc, _glState);
}
