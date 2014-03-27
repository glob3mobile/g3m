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
#include "IStringBuilder.hpp"
#include "GL.hpp"
#include "TextureIDReference.hpp"

const std::string TextureSpec::description() const {
  IStringBuilder* isb = IStringBuilder::newStringBuilder();
  isb->addString("(");
  isb->addString(_id);
  isb->addString(" ");
  isb->addInt(_width);
  isb->addString("x");
  isb->addInt(_height);
  isb->addString(")");
  const std::string s = isb->getString();
  delete isb;
  return s;
}

class TextureHolder {
public:
#ifdef C_CODE
  const TextureSpec   _textureSpec;
  const IGLTextureId* _glTextureId;
#endif
#ifdef JAVA_CODE
  public final TextureSpec _textureSpec;
  public IGLTextureId _glTextureId;
#endif

  long _referenceCounter;

  TextureHolder(const TextureSpec& textureSpec) :
  _referenceCounter(1),
  _textureSpec(textureSpec),
  _glTextureId(NULL)
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
    IStringBuilder* isb = IStringBuilder::newStringBuilder();
    isb->addString("(#");
    isb->addString(_glTextureId->description());
    isb->addString(", counter=");
    isb->addInt(_referenceCounter);
    isb->addString(")");
    const std::string s = isb->getString();
    delete isb;
    return s;
  }
};

//void TexturesHandler::showHolders(const std::string& message) const {
//  if (false) {
//    std::string holdersString = ">>>> " + message + ", Holders=(";
//    for (int i = 0; i < _textureHolders.size(); i++) {
//      TextureHolder* holder = _textureHolders[i];
//
//      if (i > 0) {
//        holdersString += ", ";
//      }
//      holdersString += holder->description();
//    }
//    holdersString += ")";
//
//    ILogger::instance()->logInfo("%s\n", holdersString.c_str() );
//  }
//}

const IGLTextureId* TexturesHandler::getGLTextureIdIfAvailable(const TextureSpec& textureSpec) {
  const int _textureHoldersSize = _textureHolders.size();
  for (int i = 0; i < _textureHoldersSize; i++) {
    TextureHolder* holder = _textureHolders[i];
    if (holder->hasSpec(textureSpec)) {
      holder->retain();

      //showHolders("getGLTextureIdIfAvailable(): retained " + holder->description());

      return holder->_glTextureId;
    }
  }

  return NULL;
}


const TextureIDReference* TexturesHandler::getTextureIDReference(const IImage* image,
                                                                 int format,
                                                                 const std::string& name,
                                                                 bool generateMipmap) {

  TextureSpec textureSpec(name,
                          image->getWidth(),
                          image->getHeight(),
                          generateMipmap);

  const IGLTextureId* previousId = getGLTextureIdIfAvailable(textureSpec);
  if (previousId != NULL) {
    return new TextureIDReference(previousId, this);
  }

  TextureHolder* holder = new TextureHolder(textureSpec);
  holder->_glTextureId = _gl->uploadTexture(image, format, textureSpec.generateMipmap());


  if (_verbose) {
    ILogger::instance()->logInfo("Uploaded texture \"%s\" to GPU with texId=%s" ,
                                 textureSpec.description().c_str(),
                                 holder->_glTextureId->description().c_str() );
  }

  _textureHolders.push_back(holder);

  //showHolders("getGLTextureId(): created holder " + holder->description());

  return new TextureIDReference(holder->_glTextureId, this);
}

void TexturesHandler::retainGLTextureId(const IGLTextureId* glTextureId) {
  if (glTextureId == NULL) {
    return;
  }

  for (int i = 0; i < _textureHolders.size(); i++) {
    TextureHolder* holder = _textureHolders[i];

    if (holder->_glTextureId->isEquals(glTextureId)) {
      holder->retain();

      //showHolders("retainGLTextureId(): retained holder " + holder->description());

      return;
    }
  }

  ILogger::instance()->logInfo("break (point) on me 6\n");
}

void TexturesHandler::releaseGLTextureId(const IGLTextureId* glTextureId) {
  if (glTextureId == NULL) {
    return;
  }

  for (int i = 0; i < _textureHolders.size(); i++) {
    TextureHolder* holder = _textureHolders[i];

    if (holder->_glTextureId->isEquals(glTextureId)) {
      holder->release();

      //showHolders("releaseGLTextureId(  ): released holder " + holder->description());

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
    ILogger::instance()->logWarning("WARNING: The TexturesHandler is destroyed, but the inner textures were not released.\n");
  }
}

