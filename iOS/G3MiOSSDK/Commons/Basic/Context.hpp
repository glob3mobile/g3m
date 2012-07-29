//
//  InitializationContext.h
//  G3MiOSSDK
//
//  Created by José Miguel S N on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_InitializationContext_h
#define G3MiOSSDK_InitializationContext_h

#include "IFactory.hpp"
#include "IGL.hpp"
#include "Planet.hpp"
#include "TexturesHandler.hpp"
#include "Downloader.hpp"


#include "IDownloader.hpp"

class Camera;

class Context {
protected:
  const IFactory * _factory;
  const ILogger*   _logger;
  const Planet*    _planet;
  const IDownloader* _downloader;

  Downloader* const _downloaderOLD;

  
  Context(const IFactory *factory,
          const ILogger* logger,
          const Planet* planet,
          Downloader* const downloaderOLD,
          IDownloader* downloader) :
  _factory(factory),
  _logger(logger),
  _planet(planet),
  _downloader(downloader),
  _downloaderOLD(downloaderOLD){
  }
  
public:
  
  const IFactory* getFactory() const {
    return _factory;
  }
  
  const ILogger* getLogger() const {
    return _logger;
  }
  
  const Planet* getPlanet() const {
    return _planet;
  }
  
  Downloader* const getDownloaderOLD() const {
    return _downloaderOLD;
  }

  const IDownloader* getDownloader() const {
    return _downloader;
  }
};


class InitializationContext: public Context {
public:
  InitializationContext(IFactory *factory,
                        ILogger* logger,
                        const Planet* planet,
                        Downloader* const downloaderOLD,
                        IDownloader* downloader) :
  Context(factory, logger, planet, downloaderOLD, downloader) {
  }
};


class RenderContext: public Context {
private:
  IGL*             _gl;
  Camera*          _camera;
  TexturesHandler* _texturesHandler;
  
public:
  RenderContext(IFactory *factory,
                ILogger* logger,
                const Planet* planet,
                IGL *gl,
                Camera* camera,
                TexturesHandler* texturesHandler,
                Downloader* const downloaderOLD,
                IDownloader* downloader) :
  Context(factory, logger, planet, downloaderOLD, downloader),
  _gl(gl),
  _camera(camera),
  _texturesHandler(texturesHandler) {
    
  }
  
  IGL* getGL() const {
    return _gl;
  }
  
  Camera* getCamera() const {
    return _camera;
  }
  
  TexturesHandler* getTexturesHandler() const {
    return _texturesHandler;
  }
    
    
};

#endif
