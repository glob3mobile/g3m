//
//  G3MContext.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 1/29/16.
//
//

#ifndef G3MContext_hpp
#define G3MContext_hpp

class IFactory;
class IStringUtils;
class IThreadUtils;
class ILogger;
class IMathUtils;
class IJSONParser;
class Planet;
class IDownloader;
class EffectsScheduler;
class IStorage;
class SurfaceElevationProvider;

#include "ViewMode.hpp"


class G3MContext {
private:
  const IFactory*           _factory;
  const IStringUtils*       _stringUtils;
  const IThreadUtils*       _threadUtils;
  const ILogger*            _logger;
  const IMathUtils*         _mathUtils;
  const IJSONParser*        _jsonParser;
  const Planet*             _planet;
  IDownloader*              _downloader;
  EffectsScheduler*         _effectsScheduler;
  IStorage*                 _storage;
  SurfaceElevationProvider* _surfaceElevationProvider;
  ViewMode                  _viewMode;

public:
  G3MContext(const IFactory*           factory,
             const IStringUtils*       stringUtils,
             const IThreadUtils*       threadUtils,
             const ILogger*            logger,
             const IMathUtils*         mathUtils,
             const IJSONParser*        jsonParser,
             const Planet*             planet,
             IDownloader*              downloader,
             EffectsScheduler*         effectsScheduler,
             IStorage*                 storage,
             SurfaceElevationProvider* surfaceElevationProvider,
             ViewMode                  viewMode) :
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
  _viewMode(viewMode)
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

  SurfaceElevationProvider* getSurfaceElevationProvider() const {
    return _surfaceElevationProvider;
  }

  ViewMode getViewMode() const {
    return _viewMode;
  }

  void setViewMode(ViewMode viewMode) {
    _viewMode = viewMode;
  }

};

#endif
