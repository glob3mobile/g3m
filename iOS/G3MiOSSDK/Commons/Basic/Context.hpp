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
class ILogger;
class GL;
class EffectsScheduler;
class ITimer;
class IStringUtils;
class IThreadUtils;
class TextureBuilder;
class IMathUtils;
class IJSONParser;
class IStorage;
class OrderedRenderable;

#include <vector>

class G3MContext {
protected:
  const IFactory*     _factory;
  const IStringUtils* _stringUtils;
  const IThreadUtils* _threadUtils;
  const ILogger*      _logger;
  const IMathUtils*   _mathUtils;
  const IJSONParser*  _jsonParser;
  const Planet*       _planet;
  IDownloader*        _downloader;
  EffectsScheduler*   _effectsScheduler;
  IStorage*           _storage;

public:
  G3MContext(const IFactory*     factory,
             const IStringUtils* stringUtils,
             const IThreadUtils* threadUtils,
             const ILogger*      logger,
             const IMathUtils*   mathUtils,
             const IJSONParser*  jsonParser,
             const Planet*       planet,
             IDownloader*        downloader,
             EffectsScheduler*   effectsScheduler,
             IStorage*           storage) :
  _factory(factory),
  _stringUtils(stringUtils),
  _threadUtils(threadUtils),
  _logger(logger),
  _mathUtils(mathUtils),
  _jsonParser(jsonParser),
  _planet(planet),
  _downloader(downloader),
  _effectsScheduler(effectsScheduler),
  _storage(storage)
  {
  }

  virtual ~G3MContext() {

  }

  const IFactory* getFactory() const {
    return _factory;
  }

  const IStringUtils* getStringUtils() const {
    return _stringUtils;
  }

  const ILogger* getLogger() const {
    return _logger;
  }

  const IMathUtils* getMathUtils() const {
    return _mathUtils;
  }

  const IJSONParser* getJSONParser() const {
    return _jsonParser;
  }

  const Planet* getPlanet() const {
    return _planet;
  }

  IDownloader* getDownloader() const {
    return _downloader;
  }

  IStorage* getStorage() const {
    return _storage;
  }

  EffectsScheduler* getEffectsScheduler() const {
    return _effectsScheduler;
  }

  const IThreadUtils* getThreadUtils() const {
    return _threadUtils;
  }
};

//************************************************************



class G3MEventContext: public G3MContext {
public:
  G3MEventContext(const IFactory*     factory,
                  const IStringUtils* stringUtils,
                  const IThreadUtils* threadUtils,
                  const ILogger*      logger,
                  const IMathUtils*   mathUtils,
                  const IJSONParser*  jsonParser,
                  const Planet*       planet,
                  IDownloader*        downloader,
                  EffectsScheduler*   scheduler,
                  IStorage*           storage) :
  G3MContext(factory,
             stringUtils,
             threadUtils,
             logger,
             mathUtils,
             jsonParser,
             planet,
             downloader,
             scheduler,
             storage) {
  }
};

//************************************************************


class FrameTasksExecutor;

#ifdef C_CODE
bool MyDataSortPredicate(const OrderedRenderable* or1,
                         const OrderedRenderable* or2);
#endif

class G3MRenderContext: public G3MContext {
private:
  FrameTasksExecutor* _frameTasksExecutor;
  GL*                 _gl;
  const Camera*       _currentCamera;
  Camera*             _nextCamera;
  TexturesHandler*    _texturesHandler;
  TextureBuilder*     _textureBuilder;
  ITimer*             _frameStartTimer;

  mutable std::vector<OrderedRenderable*>* _orderedRenderables;

public:
  G3MRenderContext(FrameTasksExecutor* frameTasksExecutor,
                   const IFactory*     factory,
                   const IStringUtils* stringUtils,
                   const IThreadUtils* threadUtils,
                   const ILogger*      logger,
                   const IMathUtils*   mathUtils,
                   const IJSONParser*  jsonParser,
                   const Planet*       planet,
                   GL*                 gl,
                   const Camera*       currentCamera,
                   Camera*             nextCamera,
                   TexturesHandler*    texturesHandler,
                   TextureBuilder*     textureBuilder,
                   IDownloader*        downloader,
                   EffectsScheduler*   scheduler,
                   ITimer*             frameStartTimer,
                   IStorage*           storage) :
  G3MContext(factory,
             stringUtils,
             threadUtils,
             logger,
             mathUtils,
             jsonParser,
             planet,
             downloader,
             scheduler,
             storage),
  _frameTasksExecutor(frameTasksExecutor),
  _gl(gl),
  _currentCamera(currentCamera),
  _nextCamera(nextCamera),
  _texturesHandler(texturesHandler),
  _textureBuilder(textureBuilder),
  _frameStartTimer(frameStartTimer),
  _orderedRenderables(NULL)
  {

  }

  GL* getGL() const {
    return _gl;
  }

  const Camera* getCurrentCamera() const {
    return _currentCamera;
  }

  Camera* getNextCamera() const {
    return _nextCamera;
  }

  TexturesHandler* getTexturesHandler() const {
    return _texturesHandler;
  }

  TextureBuilder* getTextureBuilder() const {
    return _textureBuilder;
  }

  const ITimer* getFrameStartTimer() const {
    return _frameStartTimer;
  }

  FrameTasksExecutor* getFrameTasksExecutor() const {
    return _frameTasksExecutor;
  }

  virtual ~G3MRenderContext();

  /*
   Get the OrderedRenderables, sorted by distanceFromEye()
   */
  std::vector<OrderedRenderable*>* getSortedOrderedRenderables() const;
  
  void addOrderedRenderable(OrderedRenderable* orderedRenderable) const;
  
};



#endif
