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
class IGL;
class IFactory;



class TexturesHandler {
private:
  std::vector<TextureHolder*> _textureHolders;
  
  IGL * const _gl;
  const IFactory * const _factory;
  const TextureBuilder* _texBuilder;
  
public:
  
  TexturesHandler(IGL* const  gl, const IFactory const * factory, const TextureBuilder* texBuilder): 
  _gl(gl), _factory(factory), _texBuilder(texBuilder){}
  
  ~TexturesHandler();
  
  int getTextureIdFromFileName(const std::string& filename,
                               int textureWidth,
                               int textureHeight);
  
  int getTextureId(const std::vector<const IImage*>& images,
                   const std::string& textureId,
                   int textureWidth,
                   int textureHeight);
  
  int getTextureId(const IImage* image,
                   const std::string& textureId,
                   int textureWidth,
                   int textureHeight);

  
  int getTextureIdIfAvailable(const std::string& textureId,
                              int textureWidth,
                              int textureHeight);
  
  
  void takeTexture(int glTextureId);
  
};

#endif
