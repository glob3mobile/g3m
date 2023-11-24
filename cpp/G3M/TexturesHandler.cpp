//
//  TexturesHandler.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 19/06/12.
//

#include "TexturesHandler.hpp"


#include "IImage.hpp"
#include "G3MContext.hpp"
#include "IStringBuilder.hpp"
#include "GL.hpp"
#include "TextureIDReference.hpp"
#include "ILogger.hpp"


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
  const IGLTextureID* _glTextureID;
#endif
#ifdef JAVA_CODE
  public final TextureSpec _textureSpec;
  public IGLTextureID _glTextureID;
#endif

  long _referenceCounter;

  TextureHolder(const TextureSpec& textureSpec) :
  _referenceCounter(1),
  _textureSpec(textureSpec),
  _glTextureID(NULL)
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
    return _textureSpec.isEquals(textureSpec);
  }

  const std::string description() const {
    IStringBuilder* isb = IStringBuilder::newStringBuilder();
    isb->addString("(#");
    isb->addString(_glTextureID->description());
    isb->addString(", counter=");
    isb->addLong(_referenceCounter);
    isb->addString(")");
    const std::string s = isb->getString();
    delete isb;
    return s;
  }
};

const IGLTextureID* TexturesHandler::getGLTextureIDIfAvailable(const TextureSpec& textureSpec) {
  const size_t _textureHoldersSize = _textureHolders.size();
  for (size_t i = 0; i < _textureHoldersSize; i++) {
    TextureHolder* holder = _textureHolders[i];
    if (holder->hasSpec(textureSpec)) {
      holder->retain();

      return holder->_glTextureID;
    }
  }

  return NULL;
}


const TextureIDReference* TexturesHandler::getTextureIDReference(const IImage* image,
                                                                 int format,
                                                                 const std::string& name,
                                                                 bool generateMipmap,
                                                                 int wrapS,
                                                                 int wrapT) {

  const TextureSpec textureSpec(name,
                                image->getWidth(),
                                image->getHeight(),
                                generateMipmap,
                                wrapS,
                                wrapT);

  const IGLTextureID* previousID = getGLTextureIDIfAvailable(textureSpec);
  if (previousID != NULL) {
    return new TextureIDReference(previousID,
                                  image->isPremultiplied(),
                                  this);
  }

  TextureHolder* holder = new TextureHolder(textureSpec);
  holder->_glTextureID = _gl->uploadTexture(image, format, generateMipmap, wrapS, wrapT);

  if (_verbose) {
    ILogger::instance()->logInfo("Uploaded texture \"%s\" to GPU with texID=%s" ,
                                 textureSpec.description().c_str(),
                                 holder->_glTextureID->description().c_str() );
  }

  _textureHolders.push_back(holder);

  return new TextureIDReference(holder->_glTextureID,
                                image->isPremultiplied(),
                                this);
}

void TexturesHandler::retainGLTextureID(const IGLTextureID* glTextureID) {
  if (glTextureID == NULL) {
    return;
  }

  for (int i = 0; i < _textureHolders.size(); i++) {
    TextureHolder* holder = _textureHolders[i];

    if (holder->_glTextureID->isEquals(glTextureID)) {
      holder->retain();

      return;
    }
  }
}

void TexturesHandler::releaseGLTextureID(const IGLTextureID* glTextureID) {
  if (glTextureID == NULL) {
    return;
  }

  for (int i = 0; i < _textureHolders.size(); i++) {
    TextureHolder* holder = _textureHolders[i];

    if (holder->_glTextureID->isEquals(glTextureID)) {
      holder->release();

      if (!holder->isRetained()) {
        _gl->deleteTexture(holder->_glTextureID);

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

