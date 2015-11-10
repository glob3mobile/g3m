//
//  TextureIDReference.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/9/14.
//
//

#ifndef __G3MiOSSDK__TextureIDReference__
#define __G3MiOSSDK__TextureIDReference__

class IGLTextureId;
class TexturesHandler;

class TextureIDReference {
private:
  const IGLTextureId* _id;
  const bool          _isPremultiplied;
  TexturesHandler*    _texHandler;

private:
  TextureIDReference(const TextureIDReference& that);

public:

  TextureIDReference(const IGLTextureId* id,
                     bool                isPremultiplied,
                     TexturesHandler*    texHandler) :
  _id(id),
  _isPremultiplied(isPremultiplied),
  _texHandler(texHandler)
  {
  }

  virtual ~TextureIDReference();

  TextureIDReference* createCopy() const;

  const IGLTextureId* getID() const {
    return _id;
  }

  bool isPremultiplied() const {
    return _isPremultiplied;
  }
  
};

#endif
