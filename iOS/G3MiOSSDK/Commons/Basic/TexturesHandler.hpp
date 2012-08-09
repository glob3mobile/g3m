//
//  TexturesHandler.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 19/06/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#ifndef G3MiOSSDK_TexturesHandler_hpp
#define G3MiOSSDK_TexturesHandler_hpp

#include <string>
#include <vector>
#include "TextureBuilder.hpp"


class IImage;
class RenderContext;
class TextureHolder;
class GL;
class IFactory;


class TextureID {
private:
  const std::string _id;
  const int         _width;
  const int         _height;
  
  TextureID(const TextureID& that);
  
  void operator=(const TextureID& that);
  
public:
  TextureID(const std::string& id,
            const int          width,
            const int          height):
  _id(id),
  _width(width),
  _height(height)
  {
    
  }
  
  bool equalsTo(const TextureID& that) const {
    return (_id == that._id) && (_width == that._width) && (_height == that._height);
  }
  
  bool lowerThan(const TextureID& that) const {
    if (_id < that._id) {
      return true;
    }
    else if (_id > that._id) {
      return false;
    }
    
    if (_width < that._width) {
      return true;
    }
    else if (_width > that._width) {
      return false;
    }
    
    return (_height < that._height);
  }
  
#ifdef C_CODE
  bool operator<(const TextureID& that) const {
    return lowerThan(that);
  }
#endif
  
#ifdef JAVA_CODE
  TODO_implements_equals;
  TODO_implements_hashcode;
#endif
};


class TexturesHandler {
private:
  std::vector<TextureHolder*> _textureHolders;
  
  GL * const _gl;
  const IFactory * const _factory;
  const TextureBuilder* _texBuilder;
  
  const bool _verbose;
  
public:
  
  TexturesHandler(GL* const  gl,
                  const IFactory const * factory,
                  const TextureBuilder* texBuilder,
                  bool verbose):
  _gl(gl),
  _factory(factory),
  _texBuilder(texBuilder),
  _verbose(verbose)
  {
  }
  
  ~TexturesHandler();
  
  GLTextureId getTextureIdFromFileName(const std::string& filename,
                                       int textureWidth,
                                       int textureHeight);
  
  GLTextureId getTextureId(const std::vector<const IImage*>& images,
                           const std::string& textureId,
                           int textureWidth,
                           int textureHeight);
  
  GLTextureId getTextureId(const std::vector<const IImage*>& images,
                           const std::vector<const Rectangle*>& rectangles,
                           const std::string& textureId,
                           int textureWidth,
                           int textureHeight);
  
  GLTextureId getTextureId(const IImage* image,
                           const std::string& textureId,
                           int textureWidth,
                           int textureHeight);
  
  
  GLTextureId getTextureIdIfAvailable(const std::string& textureId,
                                      int textureWidth,
                                      int textureHeight);
  
  
  void takeTexture(const GLTextureId& glTextureId);
  
};

#endif
