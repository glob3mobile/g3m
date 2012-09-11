//
//  TextureMapping.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 12/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_TextureMapping_hpp
#define G3MiOSSDK_TextureMapping_hpp

//#include <vector>
#include "MutableVector2D.hpp"
#include "GLTextureId.hpp"

class RenderContext;
class IFloatBuffer;

class TextureMapping {
public:
  
  virtual ~TextureMapping() {
  }
  
  virtual void bind(const RenderContext* rc) const = 0;
  
};



class SimpleTextureMapping : public TextureMapping {
private:
#ifdef C_CODE
  const GLTextureId  _glTextureId;
#else
  GLTextureId  _glTextureId;
#endif
  IFloatBuffer* _texCoords;
  const bool    _ownedTexCoords;

  MutableVector2D    _translation;
  MutableVector2D    _scale;
  
public:
  
  SimpleTextureMapping(const GLTextureId& glTextureId,
                       IFloatBuffer* texCoords,
                       bool ownedTexCoords) :
  _glTextureId(glTextureId),
  _texCoords(texCoords),
  _translation(0, 0),
  _scale(1, 1),
  _ownedTexCoords(ownedTexCoords)
  {
    
  }
  
//  SimpleTextureMapping(const GLTextureId& glTextureId,
//                       std::vector<MutableVector2D> texCoords);
  
  void setTranslationAndScale(const Vector2D& translation,
                              const Vector2D& scale) {
    _translation = translation.asMutableVector2D();
    _scale       = scale.asMutableVector2D();
  }
  
  virtual ~SimpleTextureMapping();
  
  const GLTextureId getGLTextureId() const {
    return _glTextureId;
  }

  IFloatBuffer* getTexCoords() const { return _texCoords;}
  
  void bind(const RenderContext* rc) const;
  
};
#endif
