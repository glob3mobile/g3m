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

void SimpleTextureMapping::bind(const G3MRenderContext* rc, const GLState& parentState) const {
  if (_texCoords != NULL) {
    GL* gl = rc->getGL();
    
    GLState state(parentState);
//    state.scaleTextureCoordinates(_scale);
//    state.translateTextureCoordinates(_translation);

    gl->transformTexCoords(_scale, _translation);
    state.bindTexture(_glTextureId);
    //gl->bindTexture(_glTextureId);
    
    state.setTextureCoordinates(_texCoords, 2, 0);
    gl->setState(state);
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
