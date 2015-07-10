//
//  Context.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_InitializationContext
#define G3MiOSSDK_InitializationContext


class IFactory;
class Camera;
class Planet;
class TexturesHandler;
class IDownloader;
class ILogger;
class GL;
class EffectsScheduler;
class IStringUtils;
class IThreadUtils;
class IMathUtils;
class IJSONParser;
class IStorage;
class OrderedRenderable;
class GPUProgramManager;
class SurfaceElevationProvider;
class G3MWidget;

#include "ITimer.hpp"
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

  G3MWidget*          _widget;

  SurfaceElevationProvider* _surfaceElevationProvider;

public:
  G3MContext(G3MWidget*                widget,
             const IFactory*           factory,
             const IStringUtils*       stringUtils,
             const IThreadUtils*       threadUtils,
             const ILogger*            logger,
             const IMathUtils*         mathUtils,
             const IJSONParser*        jsonParser,
             const Planet*             planet,
             IDownloader*              downloader,
             EffectsScheduler*         effectsScheduler,
             IStorage*                 storage,
             SurfaceElevationProvider* surfaceElevationProvider) :
  _factory(factory),
  _stringUtils(stringUtils),
  _threadUtils(threadUtils),
  _logger(logger),
  _mathUtils(mathUtils),
  _jsonParser(jsonParser),
  _planet(planet),
  _downloader(downloader),
  _effectsScheduler(effectsScheduler),
  _storage(storage),
  _surfaceElevationProvider(surfaceElevationProvider),
  _widget(widget)
  {
  }

  virtual ~G3MContext() {

  }

  G3MWidget* getWidget() const{
    return _widget;
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

  SurfaceElevationProvider* getSurfaceElevationProvider() const {
    return _surfaceElevationProvider;
  }

};

//************************************************************



class G3MEventContext: public G3MContext {
public:
  G3MEventContext(G3MWidget*                widget,
                  const IFactory*           factory,
                  const IStringUtils*       stringUtils,
                  const IThreadUtils*       threadUtils,
                  const ILogger*            logger,
                  const IMathUtils*         mathUtils,
                  const IJSONParser*        jsonParser,
                  const Planet*             planet,
                  IDownloader*              downloader,
                  EffectsScheduler*         scheduler,
                  IStorage*                 storage,
                  SurfaceElevationProvider* surfaceElevationProvider) :
  G3MContext(widget,
             factory,
             stringUtils,
             threadUtils,
             logger,
             mathUtils,
             jsonParser,
             planet,
             downloader,
             scheduler,
             storage,
             surfaceElevationProvider) {
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
  ITimer*             _frameStartTimer;
  GPUProgramManager*  _gpuProgramManager;

  mutable std::vector<OrderedRenderable*>* _orderedRenderables;

  long long _frameCounter;

public:
  G3MRenderContext(G3MWidget*                widget,
                   FrameTasksExecutor*       frameTasksExecutor,
                   const IFactory*           factory,
                   const IStringUtils*       stringUtils,
                   const IThreadUtils*       threadUtils,
                   const ILogger*            logger,
                   const IMathUtils*         mathUtils,
                   const IJSONParser*        jsonParser,
                   const Planet*             planet,
                   GL*                       gl,
                   const Camera*             currentCamera,
                   Camera*                   nextCamera,
                   TexturesHandler*          texturesHandler,
                   IDownloader*              downloader,
                   EffectsScheduler*         scheduler,
                   ITimer*                   frameStartTimer,
                   IStorage*                 storage,
                   GPUProgramManager*        gpuProgramManager,
                   SurfaceElevationProvider* surfaceElevationProvider) :
  G3MContext(widget,
             factory,
             stringUtils,
             threadUtils,
             logger,
             mathUtils,
             jsonParser,
             planet,
             downloader,
             scheduler,
             storage,
             surfaceElevationProvider),
  _frameTasksExecutor(frameTasksExecutor),
  _gl(gl),
  _currentCamera(currentCamera),
  _nextCamera(nextCamera),
  _texturesHandler(texturesHandler),
  _frameStartTimer(frameStartTimer),
  _orderedRenderables(NULL),
  _gpuProgramManager(gpuProgramManager),
  _frameCounter(0)
  {

  }

  void clearForNewFrame() {
    _frameStartTimer->start();

    delete _orderedRenderables;
    _orderedRenderables = NULL;

    _frameCounter++;
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

  const ITimer* getFrameStartTimer() const {
    return _frameStartTimer;
  }

  FrameTasksExecutor* getFrameTasksExecutor() const {
    return _frameTasksExecutor;
  }
  
  GPUProgramManager* getGPUProgramManager() const {
    return _gpuProgramManager;
  }

  long long frameCounter() const{
    return _frameCounter;
  }

  virtual ~G3MRenderContext();

  /*
   Get the OrderedRenderables, sorted by distanceFromEye()
   */
  std::vector<OrderedRenderable*>* getSortedOrderedRenderables() const;
  
  void addOrderedRenderable(OrderedRenderable* orderedRenderable) const;
  
};



#endif
