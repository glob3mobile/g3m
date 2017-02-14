//
//  TextureIDReference.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/9/14.
//
//

#include "TextureIDReference.hpp"
#include "TexturesHandler.hpp"

TextureIDReference::~TextureIDReference() {
  _texHandler->releaseGLTextureID(_id);
}

TextureIDReference* TextureIDReference::createCopy() const {
  _texHandler->retainGLTextureID(_id);
  return new TextureIDReference(_id, _isPremultiplied, _texHandler);
}
