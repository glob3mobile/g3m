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

TextureMapping::TextureMapping(const GLTextureID& textureId,
                               std::vector<MutableVector2D> texCoords/*,
                               TexturesHandler* texturesHandler,
                               const TextureSpec textureSpec*/) :
_textureId(textureId),
//_texturesHandler(texturesHandler),
//_textureSpec(textureSpec),
_translation(0, 0),
_scale(1, 1)
{
  const int texCoordsSize = texCoords.size();
  float* texCoordsA = new float[2 * texCoordsSize];
  int p = 0;
  for (int i = 0; i < texCoordsSize; i++) {
    texCoordsA[p++] = (float) texCoords[i].x();
    texCoordsA[p++] = (float) texCoords[i].y();
  }
  _texCoords = texCoordsA;
}

void TextureMapping::bind(const RenderContext* rc) const {
  GL *gl = rc->getGL();
  
  gl->transformTexCoords( _scale, _translation );
  
  gl->bindTexture(_textureId);
  gl->setTextureCoordinates(2, 0, _texCoords);
}
