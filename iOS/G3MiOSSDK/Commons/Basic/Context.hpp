//
//  InitializationContext.h
//  G3MiOSSDK
//
//  Created by José Miguel S N on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_InitializationContext_h
#define G3MiOSSDK_InitializationContext_h


class IFactory;
class Camera;
class Planet;
class TexturesHandler;
class IDownloader;
class Downloader;
class ILogger;
class GL;
class EffectsScheduler;


class Context {
protected:
  const IFactory*    _factory;
  const ILogger*     _logger;
  const Planet*      _planet;
  const IDownloader* _downloader;
  Downloader* const  _downloaderOLD;
  EffectsScheduler*  _scheduler;
  
  Context(const IFactory *factory,
          const ILogger* logger,
          const Planet* planet,
          Downloader* const downloaderOLD,
          IDownloader* downloader,
          EffectsScheduler* scheduler) :
  _factory(factory),
  _logger(logger),
  _planet(planet),
  _downloader(downloader),
  _downloaderOLD(downloaderOLD),
  _scheduler(scheduler)
  {
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
  
  EffectsScheduler* getEffectsScheduler() const {
    return _scheduler;
  }
};

//************************************************************


class InitializationContext: public Context {
public:
  InitializationContext(IFactory *factory,
                        ILogger* logger,
                        const Planet* planet,
                        Downloader* const downloaderOLD,
                        IDownloader* downloader,
                        EffectsScheduler* scheduler) :
  Context(factory, logger, planet, downloaderOLD, downloader, scheduler) {
  }
};

//************************************************************


class RenderContext: public Context {
private:
  GL*              _gl;
  Camera*          _camera;
  TexturesHandler* _texturesHandler;
  
public:
  RenderContext(IFactory *factory,
                ILogger* logger,
                const Planet* planet,
                GL *gl,
                Camera* camera,
                TexturesHandler* texturesHandler,
                Downloader* const downloaderOLD,
                IDownloader* downloader,
                EffectsScheduler* scheduler) :
  Context(factory, logger, planet, downloaderOLD, downloader, scheduler),
  _gl(gl),
  _camera(camera),
  _texturesHandler(texturesHandler) {
    
  }
  
  GL* getGL() const {
    return _gl;
  }
  
  Camera* getCamera() const {
    return _camera;
  }
  
  TexturesHandler* getTexturesHandler() const {
    return _texturesHandler;
  }
    
    
};

//************************************************************


enum Gesture {
  None,                  
  Drag,
  Zoom,
  Rotate,
  DoubleDrag
};



class CameraContext: public Context {
private:
  GL*            _gl;
  Camera*        _camera;
  Gesture*       _gesture;
  
public:
  CameraContext(IFactory *factory,
                ILogger* logger,
                const Planet* planet,
                GL *gl,
                Camera* camera,
                Gesture *gesture,
                Downloader* const downloaderOLD,
                IDownloader* downloader,
                EffectsScheduler* scheduler) :
  Context(factory, logger, planet, downloaderOLD, downloader, scheduler),
  _gl(gl),
  _camera(camera),
  _gesture(gesture) 
  {}
  
  GL* getGL() const {
    return _gl;
  }
  
  Camera* getCamera() const {
    return _camera;
  }
  
  Gesture* getGesture() const { return _gesture; }
  
};



#endif
