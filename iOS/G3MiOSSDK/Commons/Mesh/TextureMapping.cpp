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
  if (_texCoords != NULL) {
    GL* gl = rc->getGL();

    gl->transformTexCoords(_scale, _translation);
    gl->bindTexture(_glTextureId);
    gl->setTextureCoordinates(2, 0, _texCoords);
  }
  else {
    ILogger::instance()->logError("SimpleTextureMapping::bind() with _texCoords == NULL");
  }
}

SimpleTextureMapping::~SimpleTextureMapping() {
  if (_ownedTexCoords) {
    delete _texCoords;
  }
}
