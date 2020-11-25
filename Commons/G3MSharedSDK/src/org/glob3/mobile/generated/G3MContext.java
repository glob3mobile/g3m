package org.glob3.mobile.generated;
//
//  G3MContext.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 1/29/16.
//
//

//
//  G3MContext.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 1/29/16.
//
//


//class IFactory;
//class IStringUtils;
//class IThreadUtils;
//class ILogger;
//class IMathUtils;
//class IJSONParser;
//class Planet;
//class IDownloader;
//class EffectsScheduler;
//class IStorage;
//class SurfaceElevationProvider;



public class G3MContext
{
  private final IFactory _factory;
  private final IStringUtils _stringUtils;
  private final IThreadUtils _threadUtils;
  private final ILogger _logger;
  private final IMathUtils _mathUtils;
  private final IJSONParser _jsonParser;
  private final Planet _planet;
  private IDownloader _downloader;
  private EffectsScheduler _effectsScheduler;
  private IStorage _storage;
  private SurfaceElevationProvider _surfaceElevationProvider;
  private ViewMode _viewMode;

  public G3MContext(IFactory factory, IStringUtils stringUtils, IThreadUtils threadUtils, ILogger logger, IMathUtils mathUtils, IJSONParser jsonParser, Planet planet, IDownloader downloader, EffectsScheduler effectsScheduler, IStorage storage, SurfaceElevationProvider surfaceElevationProvider, ViewMode viewMode)
  {
     _factory = factory;
     _stringUtils = stringUtils;
     _threadUtils = threadUtils;
     _logger = logger;
     _mathUtils = mathUtils;
     _jsonParser = jsonParser;
     _planet = planet;
     _downloader = downloader;
     _effectsScheduler = effectsScheduler;
     _storage = storage;
     _surfaceElevationProvider = surfaceElevationProvider;
     _viewMode = viewMode;
  }

  public void dispose()
  {
  }

  public final IFactory getFactory()
  {
    return _factory;
  }

  public final IStringUtils getStringUtils()
  {
    return _stringUtils;
  }

  public final ILogger getLogger()
  {
    return _logger;
  }

  public final IMathUtils getMathUtils()
  {
    return _mathUtils;
  }

  public final IJSONParser getJSONParser()
  {
    return _jsonParser;
  }

  public final Planet getPlanet()
  {
    return _planet;
  }

  public final IDownloader getDownloader()
  {
    return _downloader;
  }

  public final IStorage getStorage()
  {
    return _storage;
  }

  public final EffectsScheduler getEffectsScheduler()
  {
    return _effectsScheduler;
  }

  public final IThreadUtils getThreadUtils()
  {
    return _threadUtils;
  }

  public final SurfaceElevationProvider getSurfaceElevationProvider()
  {
    return _surfaceElevationProvider;
  }

  public final ViewMode getViewMode()
  {
    return _viewMode;
  }

  public final void setViewMode(ViewMode viewMode)
  {
    _viewMode = viewMode;
  }

}