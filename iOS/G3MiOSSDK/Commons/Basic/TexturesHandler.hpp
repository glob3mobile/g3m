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




class TexturesHandler {
private:
  std::vector<TextureHolder*> _textureHolders;
  
public:
  ~TexturesHandler();
  
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
