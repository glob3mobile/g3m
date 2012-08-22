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
#include "Rectangle.hpp"

#include "IStringBuilder.hpp"

const std::string TextureSpec::description() const {
  IStringBuilder *isb = IStringBuilder::newStringBuilder();
  isb->add("(")->add(_id)->add(" ")->add(_width)->add("x")->add(_height)->add(")");
  std::string s = isb->getString();
  delete isb;
  return s;  
}


class TextureHolder {
public:
  const TextureSpec _textureSpec;
  GLTextureID _glTextureId;
  
  long _referenceCounter;
  
  TextureHolder(const TextureSpec& textureSpec) :
  _referenceCounter(1),
  _textureSpec(textureSpec),
  _glTextureId(GLTextureID::invalid())
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
    
    IStringBuilder *isb = IStringBuilder::newStringBuilder();
    isb->add("(#")->add(_glTextureId.getGLTextureID())->add(", counter=")->add(_referenceCounter)->add(")");
    std::string s = isb->getString();
    delete isb;
    return s;  
  }
};

const GLTextureID TexturesHandler::getGLTextureIdFromFileName(const std::string filename,
                                                              int textureWidth,
                                                              int textureHeight) {
  const IImage* image = _factory->createImageFromFileName(filename);
  
  const GLTextureID texId = getGLTextureId(image,
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


const GLTextureID TexturesHandler::getGLTextureIdIfAvailable(const TextureSpec& textureSpec) {
  for (int i = 0; i < _textureHolders.size(); i++) {
    TextureHolder* holder = _textureHolders[i];
    if (holder->hasSpec(textureSpec)) {
      holder->retain();
      
      showHolders("getGLTextureIdIfAvailable(): retained " + holder->description());
      
      return holder->_glTextureId;
    }
  }
  
  return GLTextureID::invalid();
}

const GLTextureID TexturesHandler::getGLTextureId(const std::vector<const IImage*> images,
                                                  const TextureSpec& textureSpec) {
  GLTextureID previousId = getGLTextureIdIfAvailable(textureSpec);
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

const GLTextureID TexturesHandler::getGLTextureId(const std::vector<const IImage*> images,
                                                  const std::vector<const Rectangle*> rectangles,
                                                  const TextureSpec& textureSpec) {
  GLTextureID previousId = getGLTextureIdIfAvailable(textureSpec);
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

const GLTextureID TexturesHandler::getGLTextureId(const IImage *image,
                                                  const TextureSpec& textureSpec) {
  std::vector<const IImage*> images;
  images.push_back(image);
  return getGLTextureId(images, textureSpec);
}

void TexturesHandler::retainGLTextureId(const GLTextureID& glTextureId) {
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

void TexturesHandler::releaseGLTextureId(const GLTextureID& glTextureId) {
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
