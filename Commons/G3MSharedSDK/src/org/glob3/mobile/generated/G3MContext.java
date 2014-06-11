package org.glob3.mobile.generated; 
//
//  Context.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

//
//  Context.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//



//class IFactory;
//class Camera;
//class Planet;
//class TexturesHandler;
//class IDownloader;
//class ILogger;
//class GL;
//class EffectsScheduler;
//class ITimer;
//class IStringUtils;
//class IThreadUtils;
//class IMathUtils;
//class IJSONParser;
//class IStorage;
//class OrderedRenderable;
//class GPUProgramManager;
//class SurfaceElevationProvider;


public class G3MContext
{
  protected final IFactory _factory;
  protected final IStringUtils _stringUtils;
  protected final IThreadUtils _threadUtils;
  protected final ILogger _logger;
  protected final IMathUtils _mathUtils;
  protected final IJSONParser _jsonParser;
  protected final Planet _planet;
  protected IDownloader _downloader;
  protected EffectsScheduler _effectsScheduler;
  protected IStorage _storage;

  protected SurfaceElevationProvider _surfaceElevationProvider;

  public G3MContext(IFactory factory, IStringUtils stringUtils, IThreadUtils threadUtils, ILogger logger, IMathUtils mathUtils, IJSONParser jsonParser, Planet planet, IDownloader downloader, EffectsScheduler effectsScheduler, IStorage storage, SurfaceElevationProvider surfaceElevationProvider)
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

}