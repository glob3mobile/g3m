package org.glob3.mobile.generated;//
//  G3MContext.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/29/16.
//
//

//
//  G3MContext.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/29/16.
//
//


//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IFactory;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IStringUtils;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IThreadUtils;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class ILogger;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IMathUtils;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IJSONParser;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Planet;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IDownloader;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class EffectsScheduler;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IStorage;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
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

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const IFactory* getFactory() const
  public final IFactory getFactory()
  {
	return _factory;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const IStringUtils* getStringUtils() const
  public final IStringUtils getStringUtils()
  {
	return _stringUtils;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const ILogger* getLogger() const
  public final ILogger getLogger()
  {
	return _logger;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const IMathUtils* getMathUtils() const
  public final IMathUtils getMathUtils()
  {
	return _mathUtils;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const IJSONParser* getJSONParser() const
  public final IJSONParser getJSONParser()
  {
	return _jsonParser;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const Planet* getPlanet() const
  public final Planet getPlanet()
  {
	return _planet;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: IDownloader* getDownloader() const
  public final IDownloader getDownloader()
  {
	return _downloader;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: IStorage* getStorage() const
  public final IStorage getStorage()
  {
	return _storage;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: EffectsScheduler* getEffectsScheduler() const
  public final EffectsScheduler getEffectsScheduler()
  {
	return _effectsScheduler;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const IThreadUtils* getThreadUtils() const
  public final IThreadUtils getThreadUtils()
  {
	return _threadUtils;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: SurfaceElevationProvider* getSurfaceElevationProvider() const
  public final SurfaceElevationProvider getSurfaceElevationProvider()
  {
	return _surfaceElevationProvider;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: ViewMode getViewMode() const
  public final ViewMode getViewMode()
  {
	return _viewMode;
  }

  public final void setViewMode(ViewMode viewMode)
  {
	_viewMode = viewMode;
  }

}
