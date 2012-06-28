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
  
  bool equalsTo(const TextureHolder* other) {
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
  const IImage* image = rc->getFactory()->createImageFromFileName(filename);
  
  const int texId = getTextureId(rc,
                                 image,
                                 filename, // filename as the textureId
                                 textureWidth,
                                 textureHeight);
  
  delete image;
  
  return texId;;
}

int TexturesHandler::getTextureId(const RenderContext* rc,
                                  const IImage *image, 
                                  const std::string &textureId,
                                  int textureWidth,
                                  int textureHeight) {
  
  TextureHolder* candidate = new TextureHolder(textureId, textureWidth, textureHeight);
  
  for (int i = 0; i < _textureHolders.size(); i++) {
    TextureHolder* holder = _textureHolders[i];
    if (holder->equalsTo(candidate)) {
      holder->retain();
      delete candidate;
      
      return holder->_glTextureId;
    }
  }
  
  candidate->_glTextureId = rc->getGL()->uploadTexture(*image,
                                                       textureWidth,
                                                       textureHeight);
  
  rc->getLogger()->logInfo("Uploaded texture \"%s\" (%dx%d) to GPU with texId=%d" ,
                           textureId.c_str(),
                           textureWidth,
                           textureHeight,
                           candidate->_glTextureId);
  
  _textureHolders.push_back(candidate);
  
  return candidate->_glTextureId;
}

void TexturesHandler::takeTexture(const RenderContext* rc,
                                  int glTextureId) {
  for (int i = 0; i < _textureHolders.size(); i++) {
    TextureHolder* holder = _textureHolders[i];
    
    if (holder->_glTextureId == glTextureId) {
      holder->release();
      
      if (!holder->isRetained()) {
        _textureHolders.erase(_textureHolders.begin() + i);
        
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
