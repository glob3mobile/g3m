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
  
  int getTextureIdFromFileName(const std::string& filename,
                               int textureWidth,
                               int textureHeight);
  
  int getTextureId(const std::vector<const IImage*>& images,
                   const std::string& textureId,
                   int textureWidth,
                   int textureHeight);
  
  int getTextureId(const std::vector<const IImage*>& images,
                   const std::vector<const Rectangle*>& rectangles,
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
