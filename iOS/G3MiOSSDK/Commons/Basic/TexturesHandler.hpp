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


class TextureHolder {
public:
    const std::string _textureId;
    const int         _textureWidth;
    const int         _textureHeight;
    int _glTextureId;
    
    long _referenceCounter;
    
    TextureHolder(const std::string textureId,
                  const int textureWidth,
                  const int textureHeight) :
    _textureId(textureId),
    _textureWidth(textureWidth),
    _textureHeight(textureHeight)
    {
        _referenceCounter = 1;
        _glTextureId = -1;
    }
    
    ~TextureHolder() {
    }
    
    void retain() {
        _referenceCounter++;
    }
    
    void release() {
        _referenceCounter--;
    }
    
    bool isRetained() {
        return _referenceCounter > 0;
    }
    
    bool hasKey(const std::string textureId,
                const int textureWidth,
                const int textureHeight) {
        if (_textureWidth != textureWidth) {
            return false;
        }
        if (_textureHeight != textureHeight) {
            return false;
        }
        
        if (_textureId.compare(textureId) != 0) {
            return false;
        }
        
        return true;
    }
};



class TexturesHandler {
private:
  std::vector<TextureHolder*> _textureHolders;
  
  IGL * const _gl;
  const IFactory * const _factory;
  const TextureBuilder* _texBuilder;
  
  const bool _verbose;
  
public:
  
  TexturesHandler(IGL* const  gl,
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
