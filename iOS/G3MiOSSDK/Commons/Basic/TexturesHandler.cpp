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
#include "TextureBuilder.hpp"

#include <sstream>

const std::string TextureSpec::description() const {
  std::ostringstream buffer;
  buffer << "(";
  buffer << _id;
  buffer << " ";
  buffer << _width;
  buffer << "x";
  buffer << _height;
  buffer << ")";
  return buffer.str();
}


class TextureHolder {
public:
  const TextureSpec _textureSpec;
  GLTextureId _glTextureId;
  
  long _referenceCounter;
  
  TextureHolder(const TextureSpec& textureSpec) :
  _referenceCounter(1),
  _textureSpec(textureSpec),
  _glTextureId(GLTextureId::invalid())
  {
    
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
  
  bool hasSpec(const TextureSpec& textureSpec) {
    return _textureSpec.equalsTo(textureSpec);
  }
  
  const std::string description() const {
    std::ostringstream buffer;
    buffer << "(#";
    buffer << _glTextureId.getGLTextureId();
    buffer << ", counter=";
    buffer << _referenceCounter;
    buffer << ")";
    return buffer.str();
    
    ////    return "TextureHolder(textureSpec=" + _textureSpec.description() + ", glTextureId=" + _glTextureId.description() + ")";
    //    return "TextureHolder(#" + _glTextureId.description() + ", counter=" + _referenceCounter + ")";
  }
};


const GLTextureId TexturesHandler::getGLTextureIdFromFileName(const std::string filename,
                                                              int textureWidth,
                                                              int textureHeight) {
  const IImage* image = _factory->createImageFromFileName(filename);
  
  const GLTextureId texId = getGLTextureId(image,
                                           TextureSpec(filename, // filename as the id
                                                       textureWidth,
                                                       textureHeight));
  _factory->deleteImage(image);
  
  return texId;
}

void TexturesHandler::showHolders(const std::string message) const {
  if (false) {
    std::string holdersString = ">>>> " + message + ", Holders=(";
    for (int i = 0; i < _textureHolders.size(); i++) {
      TextureHolder* holder = _textureHolders[i];
      
      if (i > 0) {
        holdersString += ", ";
      }
      holdersString += holder->description();
    }
    holdersString += ")";
    
    printf("%s\n", holdersString.c_str() );
  }
}


const GLTextureId TexturesHandler::getGLTextureIdIfAvailable(const TextureSpec& textureSpec) {
  for (int i = 0; i < _textureHolders.size(); i++) {
    TextureHolder* holder = _textureHolders[i];
    if (holder->hasSpec(textureSpec)) {
      holder->retain();
      
      showHolders("getGLTextureIdIfAvailable(): retained " + holder->description());
      
      return holder->_glTextureId;
    }
  }
  
  return GLTextureId::invalid();
}

const GLTextureId TexturesHandler::getGLTextureId(const std::vector<const IImage*> images,
                                                  const TextureSpec& textureSpec) {
  GLTextureId previousId = getGLTextureIdIfAvailable(textureSpec);
  if (previousId.isValid()) {
    return previousId;
  }
  
  TextureHolder* holder = new TextureHolder(textureSpec);
  holder->_glTextureId = _textureBuilder->createTextureFromImages(_gl,
                                                                  images,
                                                                  textureSpec.getWidth(),
                                                                  textureSpec.getHeight());
  
  if (_verbose) {
    ILogger::instance()->logInfo("Uploaded texture \"%s\" to GPU with texId=%s" ,
                                 textureSpec.description().c_str(),
                                 holder->_glTextureId.description().c_str() );
  }
  
  _textureHolders.push_back(holder);
  
  showHolders("getGLTextureId(): created holder " + holder->description());
  
  return holder->_glTextureId;
}

const GLTextureId TexturesHandler::getGLTextureId(const std::vector<const IImage*> images,
                                                  const std::vector<const Rectangle*> rectangles,
                                                  const TextureSpec& textureSpec) {
  GLTextureId previousId = getGLTextureIdIfAvailable(textureSpec);
  if (previousId.isValid()) {
    return previousId;
  }
  
  TextureHolder* holder = new TextureHolder(textureSpec);
  holder->_glTextureId = _textureBuilder->createTextureFromImages(_gl,
                                                                  _factory,
                                                                  images,
                                                                  rectangles,
                                                                  textureSpec.getWidth(),
                                                                  textureSpec.getHeight());
  
  if (_verbose) {
    ILogger::instance()->logInfo("Uploaded texture \"%s\" to GPU with texId=%s" ,
                                 textureSpec.description().c_str(),
                                 holder->_glTextureId.description().c_str());
  }
  
  _textureHolders.push_back(holder);
  
  showHolders("getGLTextureId(): created holder " + holder->description());
  
  return holder->_glTextureId;
}

const GLTextureId TexturesHandler::getGLTextureId(const IImage *image,
                                                  const TextureSpec& textureSpec) {
  std::vector<const IImage*> images;
  images.push_back(image);
  return getGLTextureId(images, textureSpec);
}

void TexturesHandler::retainGLTextureId(const GLTextureId& glTextureId) {
  if (!glTextureId.isValid()) {
    return;
  }
  
  for (int i = 0; i < _textureHolders.size(); i++) {
    TextureHolder* holder = _textureHolders[i];
    
    if (holder->_glTextureId.isEqualsTo(glTextureId)) {
      holder->retain();
      
      showHolders("retainGLTextureId(): retained holder " + holder->description());
      
      return;
    }
  }
  
  printf("break (point) on me 6\n");
}

void TexturesHandler::releaseGLTextureId(const GLTextureId& glTextureId) {
  if (!glTextureId.isValid()) {
    return;
  }
  
  for (int i = 0; i < _textureHolders.size(); i++) {
    TextureHolder* holder = _textureHolders[i];
    
    if (holder->_glTextureId.isEqualsTo(glTextureId)) {
      holder->release();
      
      showHolders("releaseGLTextureId(  ): released holder " + holder->description());
      
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
