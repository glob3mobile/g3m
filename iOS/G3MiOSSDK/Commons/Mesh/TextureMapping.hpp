//
//  TextureMapping.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 12/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_TextureMapping_hpp
#define G3MiOSSDK_TextureMapping_hpp

#include <vector>
#include "MutableVector2D.hpp"
#include "GLTextureID.hpp"

class RenderContext;


class TextureMapping {
public:
  
  virtual ~TextureMapping() {
  }
  
  virtual void bind(const RenderContext* rc) const = 0;
  
};



class SimpleTextureMapping : public TextureMapping {
private:
#ifdef C_CODE
  const GLTextureID  _glTextureId;
#else
  GLTextureID  _glTextureId;
#endif
  const float const* _texCoords;
  const bool _ownedTexCoords;

  MutableVector2D    _translation;
  MutableVector2D    _scale;
  
public:
  
  SimpleTextureMapping(const GLTextureID& glTextureId,
                       float texCoords[],
                       bool ownedTexCoords) :
  _glTextureId(glTextureId),
  _texCoords(texCoords),
  _translation(0, 0),
  _scale(1, 1),
  _ownedTexCoords(ownedTexCoords)
  {
    
  }
  
  SimpleTextureMapping(const GLTextureID& glTextureId,
                       std::vector<MutableVector2D> texCoords);
  
  void setTranslationAndScale(const Vector2D& translation,
                              const Vector2D& scale) {
    _translation = translation.asMutableVector2D();
    _scale       = scale.asMutableVector2D();
  }
  
  virtual ~SimpleTextureMapping() {
#ifdef C_CODE
    if (_ownedTexCoords) {
      delete[] _texCoords;
    }
#endif
  }
  
  const GLTextureID getGLTextureID() const {
    return _glTextureId;
  }

#ifdef C_CODE
  const float* getTexCoords() const { return _texCoords;}
#else
  const float[] getTexCoords() const { return _texCoords;}
#endif
  
  void bind(const RenderContext* rc) const;
  
};
#endif
