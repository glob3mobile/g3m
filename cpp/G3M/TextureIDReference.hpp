//
//  TextureIDReference.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 1/9/14.
//
//

#ifndef __G3M__TextureIDReference__
#define __G3M__TextureIDReference__

class IGLTextureID;
class TexturesHandler;

class TextureIDReference {
private:
  const IGLTextureID* _id;
  const bool          _isPremultiplied;
  TexturesHandler*    _texHandler;

private:
  TextureIDReference(const TextureIDReference& that);

public:
  TextureIDReference(const IGLTextureID* id,
                     bool                isPremultiplied,
                     TexturesHandler*    texHandler) :
  _id(id),
  _isPremultiplied(isPremultiplied),
  _texHandler(texHandler)
  {
  }

  ~TextureIDReference();

  TextureIDReference* createCopy() const;

  const IGLTextureID* getID() const {
    return _id;
  }

  bool isPremultiplied() const {
    return _isPremultiplied;
  }
  
};

#endif
