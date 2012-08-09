//
//  TextureMapping.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 12/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_TextureMapping_hpp
#define G3MiOSSDK_TextureMapping_hpp

#include "MutableVector2D.hpp"
#include <vector>

#include "TexturesHandler.hpp"

class RenderContext;


class TextureMapping
{
private:
  const GLTextureId      _textureId;
  const float const*     _texCoords;
  
  MutableVector2D        _translation;
  MutableVector2D        _scale;
  
  TexturesHandler* const _texturesHandler;

  const std::string _texID;
  const int _width;
  const int _height;
  
public:
  
  TextureMapping(const GLTextureId textureId,
                 float texCoords[],
                 TexturesHandler* const texturesHandler,
                 const std::string& texID,
                 int width, int height) :
  _textureId(textureId),
  _texCoords(texCoords),
  _texturesHandler(texturesHandler),
  _texID(texID),
  _width(width),
  _height(height),
  _translation(0, 0),
  _scale(1, 1)
  {

  }
  
  TextureMapping(const GLTextureId textureId,
                 std::vector<MutableVector2D> texCoords,
                 TexturesHandler* const texturesHandler,
                 const std::string& texID,
                 int width, int height);
  
  void setTranslationAndScale(const Vector2D& translation,
                              const Vector2D& scale){
    _translation = translation.asMutableVector2D();
    _scale       = scale.asMutableVector2D();
  }
  
  ~TextureMapping() {
#ifdef C_CODE
    delete[] _texCoords;
#endif
    
    if (_texturesHandler != NULL){
      _texturesHandler->takeTexture(_textureId);
    }
  }
  
  const GLTextureId getTextureId() const {
    return _textureId;
  }
  
  std::string getStringTexID() const {
    return _texID;
  }
  
  int getWidth() const {
    return _width;
  }
  
  int getHeight() const {
    return _height;
  }
  
  const float* getTexCoords() const {
    return _texCoords;
  }
  
  void bind(const RenderContext* rc) const;
  
};

#endif