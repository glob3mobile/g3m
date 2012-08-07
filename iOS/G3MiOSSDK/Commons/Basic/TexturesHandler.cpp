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
  
  bool hasKey(const std::string textureId,
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
};


int TexturesHandler::getTextureIdFromFileName(const std::string &filename,
                                              int textureWidth,
                                              int textureHeight) {
  const IImage* image = _factory->createImageFromFileName(filename);
  
  const int texId = getTextureId(image,
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
    if (holder->hasKey(textureId, textureWidth, textureHeight)) {
      holder->retain();
      
      return holder->_glTextureId;
    }
  }
  
  return -1;
}

int TexturesHandler::getTextureId(const std::vector<const IImage*>& images,
                                  const std::string& textureId,
                                  int textureWidth,
                                  int textureHeight) {
  int previousId = getTextureIdIfAvailable(textureId, textureWidth, textureHeight);
  if (previousId >= 0) {
    return previousId;
  }
  
  TextureHolder* holder = new TextureHolder(textureId, textureWidth, textureHeight);
  holder->_glTextureId = _texBuilder->createTextureFromImages(_gl, images, textureWidth, textureHeight);
  
  if (_verbose) {
    ILogger::instance()->logInfo("Uploaded texture \"%s\" (%dx%d) to GPU with texId=%d" ,
                                 textureId.c_str(),
                                 textureWidth,
                                 textureHeight,
                                 holder->_glTextureId);
  }
  
  _textureHolders.push_back(holder);
  
  return holder->_glTextureId;
}

int TexturesHandler::getTextureId(const std::vector<const IImage*>& images,
                                  const std::vector<const Rectangle*>& rectangles,
                                  const std::string& textureId,
                                  int textureWidth,
                                  int textureHeight) {
  int previousId = getTextureIdIfAvailable(textureId, textureWidth, textureHeight);
  if (previousId >= 0) {
    return previousId;
  }
  
  TextureHolder* holder = new TextureHolder(textureId, textureWidth, textureHeight);
  holder->_glTextureId = _texBuilder->createTextureFromImages(_gl, _factory, images, rectangles, textureWidth, textureHeight);
  
  if (_verbose) {
    ILogger::instance()->logInfo("Uploaded texture \"%s\" (%dx%d) to GPU with texId=%d" ,
                                 textureId.c_str(),
                                 textureWidth,
                                 textureHeight,
                                 holder->_glTextureId);
  }
  
  _textureHolders.push_back(holder);
  
  return holder->_glTextureId;
}

int TexturesHandler::getTextureId(const IImage *image,
                                  const std::string &textureId,
                                  int textureWidth,
                                  int textureHeight) {
  std::vector<const IImage*> images;
  images.push_back(image);
  return getTextureId(images, textureId, textureWidth, textureHeight);
}

void TexturesHandler::takeTexture(int glTextureId) {
  for (int i = 0; i < _textureHolders.size(); i++) {
    TextureHolder* holder = _textureHolders[i];
    
    if (holder->_glTextureId == glTextureId) {
      holder->release();
      
      if (!holder->isRetained()) {
        _gl->deleteTexture(holder->_glTextureId);
        
#ifdef C_CODE
        _textureHolders.erase(_textureHolders.begin() + i);
#endif
#ifdef JAVA_CODE
        _textureHolders.remove(i);
#endif
        
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
