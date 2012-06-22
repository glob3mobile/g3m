//
//  TexturesHandler.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 19/06/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#include "TexturesHandler.hpp"

#include "IImage.hpp"
#include "Context.hpp"

class TextureKey {
public:
  const std::string _textureId;
  const int         _textureWidth;
  const int         _textureHeight;
  int _glTextureId;
  
  long _referenceCounter;
  
  TextureKey(const std::string textureId,
             const int textureWidth,
             const int textureHeight) :
  _textureId(textureId),
  _textureWidth(textureWidth),
  _textureHeight(textureHeight)
  {
    _referenceCounter = 1;
    _glTextureId = -1;
  }
  
  ~TextureKey() {}
  
  void retain() {
    _referenceCounter++;
  }
  
  void release() {
    _referenceCounter--;
  }
  
  bool isRetained() {
    return _referenceCounter > 0;
  }
  
  bool equalsTo(const TextureKey* other) {
    if (_textureWidth != other->_textureWidth) {
      return false;
    }
    if (_textureHeight != other->_textureHeight) {
      return false;
    }
    
    if (_textureId.compare(other->_textureId) != 0) {
      return false;
    }
    
    return true;
  }
};


int TexturesHandler::getTextureIdFromFileName(const RenderContext* rc,
                                              const std::string &filename,
                                              int textureWidth,
                                              int textureHeight) {
  IImage* image = rc->getFactory()->createImageFromFileName(filename);
  
  return getTextureId(rc,
                      image,
                      filename, // filename as the textureId
                      textureWidth,
                      textureHeight);
}

int TexturesHandler::getTextureId(const RenderContext* rc,
                                  const IImage *image, 
                                  const std::string &textureId,
                                  int textureWidth,
                                  int textureHeight) {
  
  TextureKey* key = new TextureKey(textureId, textureWidth, textureHeight);
  
  for (int i = 0; i < _textures.size(); i++) {
    TextureKey* each = _textures[i];
    if (each->equalsTo(key)) {
      each->retain();
      return each->_glTextureId;
    }
  }
  
  key->_glTextureId = rc->getGL()->uploadTexture(*image,
                                                 textureWidth,
                                                 textureHeight);
  
  rc->getLogger()->logInfo("Uploaded texture \"%s\" (%dx%d) to GPU with texId=%d" ,
                           textureId.c_str(),
                           textureWidth,
                           textureHeight,
                           key->_glTextureId);
  
  _textures.push_back(key);
  
  return key->_glTextureId;
}

void TexturesHandler::takeTexture(const RenderContext* rc,
                                  int glTextureId) {
  for (int i = 0; i < _textures.size(); i++) {
    TextureKey* each = _textures[i];
    
    if (each->_glTextureId == glTextureId) {
      each->release();
      
      if (!each->isRetained()) {
        _textures.erase(_textures.begin() + i);
        
        rc->getGL()->deleteTexture(each->_glTextureId);
        
        delete each;
      }
      
      return;
    }
  }
  
}
