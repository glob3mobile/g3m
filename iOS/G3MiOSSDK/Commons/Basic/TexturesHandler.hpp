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
class TextureKey;




class TexturesHandler {
private:
  std::vector<TextureKey*> _textures;
  
public:
  int __Diego_destructor_must_delete_TextureKeys;
  ~TexturesHandler() {}
  
  int getTextureIdFromFileName(const RenderContext* rc,
                               const std::string& filename,
                               int textureWidth,
                               int textureHeight);
  
  int getTextureId(const RenderContext* rc,
                   const IImage* image,
                   const std::string& textureId,
                   int textureWidth,
                   int textureHeight);
  
  void takeTexture(const RenderContext* rc,
                   int glTextureId);
  
};

#endif
