//
//  TextureIDReference.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 1/9/14.
//
//

#include "TextureIDReference.hpp"
#include "TexturesHandler.hpp"
#include "GLConstants.hpp"

TextureIDReference::~TextureIDReference() {
  _texHandler->releaseGLTextureID(_id);
}

TextureIDReference* TextureIDReference::createCopy() const {
  _texHandler->retainGLTextureID(_id);
  return new TextureIDReference(_id, _isPremultiplied, _texHandler);
}

int TextureIDReference::getSourceBlendFactor() const {
  return _isPremultiplied ? GLBlendFactor::one() : GLBlendFactor::srcAlpha();
}
