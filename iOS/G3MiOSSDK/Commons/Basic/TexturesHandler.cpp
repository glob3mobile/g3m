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

class TextureHolder {
public:
  const std::string _textureId;
  const int         _textureWidth;
  const int         _textureHeight;
  int _glTextureId;
  
  long _referenceCounter;
  
  TextureHolder(const std::string textureId,
                const int textureWidth,
                const int textureHeight) :
  _textureId(textureId),
  _textureWidth(textureWidth),
  _textureHeight(textureHeight)
  {
    _referenceCounter = 1;
    _glTextureId = -1;
  }
  
  ~TextureHolder() {
  }
  
  void retain() {
    _referenceCounter++;
  }
  
  void release() {
    _referenceCounter--;
  }
  
  bool isRetained() {
    return _referenceCounter > 0;
  }
  
  bool equalsTo(const std::string textureId,
                const int textureWidth,
                const int textureHeight) {
    if (_textureWidth != textureWidth) {
      return false;
    }
    if (_textureHeight != textureHeight) {
      return false;
    }
    
    if (_textureId.compare(textureId) != 0) {
      return false;
    }
    
    return true;
  }
  
  bool equalsTo(const TextureHolder* other) {
    return equalsTo(other->_textureId,
                    other->_textureWidth,
                    other->_textureHeight);
  }
};


int TexturesHandler::getTextureIdFromFileName(const RenderContext* rc,
                                              const std::string &filename,
                                              int textureWidth,
                                              int textureHeight) {
  const IImage* image = rc->getFactory()->createImageFromFileName(filename);
  
  const int texId = getTextureId(rc,
                                 image,
                                 filename, // filename as the textureId
                                 textureWidth,
                                 textureHeight);
  
  delete image;
  
  return texId;
}

int TexturesHandler::getTextureIdIfAvailable(const std::string &textureId,
                                             int textureWidth,
                                             int textureHeight) {
  for (int i = 0; i < _textureHolders.size(); i++) {
    TextureHolder* holder = _textureHolders[i];
    if (holder->equalsTo(textureId, textureWidth, textureHeight)) {
      holder->retain();
      
      return holder->_glTextureId;
    }
  }
  
  return -1;
}

int TexturesHandler::getTextureId(const RenderContext* rc,
                                  const IImage *image, 
                                  const std::string &textureId,
                                  int textureWidth,
                                  int textureHeight) {
  
  for (int i = 0; i < _textureHolders.size(); i++) {
    TextureHolder* holder = _textureHolders[i];
    if (holder->equalsTo(textureId, textureWidth, textureHeight)) {
      holder->retain();
      
      return holder->_glTextureId;
    }
  }
  
  TextureHolder* holder = new TextureHolder(textureId, textureWidth, textureHeight);
  
  holder->_glTextureId = rc->getGL()->uploadTexture(image, textureWidth, textureHeight);
  
  rc->getLogger()->logInfo("Uploaded texture \"%s\" (%dx%d) to GPU with texId=%d" ,
                           textureId.c_str(),
                           textureWidth,
                           textureHeight,
                           holder->_glTextureId);
  
  _textureHolders.push_back(holder);
  
  return holder->_glTextureId;
}

void TexturesHandler::takeTexture(const RenderContext* rc,
                                  int glTextureId) {
  for (int i = 0; i < _textureHolders.size(); i++) {
    TextureHolder* holder = _textureHolders[i];
    
    if (holder->_glTextureId == glTextureId) {
      holder->release();
      
      if (!holder->isRetained()) {
#ifdef C_CODE
        _textureHolders.erase(_textureHolders.begin() + i);
#endif
#ifdef JAVA_CODE
        _textureHolders.remove(i);
#endif
        
        rc->getGL()->deleteTexture(holder->_glTextureId);
        
        delete holder;
      }
      
      return;
    }
  }
}

TexturesHandler::~TexturesHandler() {
  if (_textureHolders.size() > 0) {
    printf("WARNING: The TexturesHandler is destroyed, but the inner textures were not released.\n"); 
  }
}
