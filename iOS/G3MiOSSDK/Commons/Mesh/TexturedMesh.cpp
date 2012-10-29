//
//  TexturedMesh.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 12/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "TexturedMesh.hpp"

#include "GL.hpp"

void TexturedMesh::render(const RenderContext* rc) const
{
  GL *gl = rc->getGL();

  if (_transparent) {
    gl->enableBlend();
  }

  gl->enableTextures();
  gl->enableTexture2D();
  
  _textureMapping->bind(rc);
  
  _mesh->render(rc);
  
  gl->disableTexture2D();
  gl->disableTextures();

  if (_transparent) {
    gl->disableBlend();
  }

}
