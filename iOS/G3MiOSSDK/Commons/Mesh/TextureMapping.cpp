//
//  TextureMapping.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/07/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#include "TextureMapping.hpp"

#include "Context.hpp"
#include "GL.hpp"

void SimpleTextureMapping::bind(const G3MRenderContext* rc) const {
  GL* gl = rc->getGL();
  
  gl->transformTexCoords(_scale, _translation);
  gl->bindTexture(_glTextureId);
  gl->setTextureCoordinates(2, 0, _texCoords);
}

SimpleTextureMapping::~SimpleTextureMapping() {
  if (_ownedTexCoords) {
    delete _texCoords;
  }
}
