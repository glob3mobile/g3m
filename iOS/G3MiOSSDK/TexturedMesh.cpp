//
//  TexturedMesh.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 12/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "TexturedMesh.hpp"

void TexturedMesh::render(const RenderContext* rc) const
{
  IGL *gl = rc->getGL();
  
  gl->enableTextures();
  gl->enableTexture2D();
  
  gl->bindTexture(_texMap->getID());
  gl->setTextureCoordinates(2, 0, _texMap->getCoords()); 
  
  _mesh->render(rc);
  
  gl->disableTexture2D();
  gl->disableTextures();
}
