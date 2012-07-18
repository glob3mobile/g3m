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
  
public:
  
  TexturesHandler(IGL* const  gl, const IFactory const * factory): _gl(gl), _factory(factory){}
  
  ~TexturesHandler();
  
  int getTextureIdFromFileName(const std::string& filename,
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
