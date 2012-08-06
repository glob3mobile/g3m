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


TextureMapping::TextureMapping(int textureId, std::vector<MutableVector2D> texCoords) :
_textureId(textureId)
{
  float* texCoordsA = new float[2 * texCoords.size()];
  int p = 0;
  for (int i = 0; i < texCoords.size(); i++) {
    texCoordsA[p++] = (float) texCoords[i].x();
    texCoordsA[p++] = (float) texCoords[i].y();
  }
  _texCoords = texCoordsA;
  
  _translation = MutableVector2D(0.0, 0.0);
  _scale       = MutableVector2D(1.0, 1.0);
}

void TextureMapping::bind(const RenderContext* rc) const {
  GL *gl = rc->getGL();
  
  gl->transformTexCoords( _scale.asVector2D(), _translation.asVector2D() );
  
  gl->bindTexture(_textureId);
  gl->setTextureCoordinates(2, 0, _texCoords);
}
