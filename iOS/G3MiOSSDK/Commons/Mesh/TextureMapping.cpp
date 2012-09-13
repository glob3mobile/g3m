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

//SimpleTextureMapping::SimpleTextureMapping(const GLTextureId& glTextureId,
//                                           std::vector<MutableVector2D> texCoords) :
//_glTextureId(glTextureId),
//_translation(0, 0),
//_scale(1, 1),
//_ownedTexCoords(true)
//{
//  const int texCoordsSize = texCoords.size();
//  float* texCoordsA = new float[2 * texCoordsSize];
//  int p = 0;
//  for (int i = 0; i < texCoordsSize; i++) {
//    texCoordsA[p++] = (float) texCoords[i].x();
//    texCoordsA[p++] = (float) texCoords[i].y();
//  }
//  _texCoords = texCoordsA;
//}

void SimpleTextureMapping::bind(const RenderContext* rc) const {
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
