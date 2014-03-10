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
  TexturesHandler* _texHandler;

private:
  TextureIDReference(const TextureIDReference& that);

public:

  TextureIDReference(const IGLTextureId* id,
                     TexturesHandler* texHandler):
  _texHandler(texHandler), _id(id) {}

  virtual ~TextureIDReference();

  TextureIDReference* createCopy() const;

  const IGLTextureId* getID() const{
    return _id;
  }
  
};

#endif
