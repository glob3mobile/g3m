package org.glob3.mobile.generated; 
//
//  InitializationContext.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

//
//  InitializationContext.h
//  G3MiOSSDK
//
//  Created by José Miguel S N on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//



//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IFactory;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Camera;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Planet;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class TexturesHandler;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IDownloader;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class ILogger;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GL;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class EffectsScheduler;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class ITimer;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IStringUtils;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IThreadUtils;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class TextureBuilder;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IMathUtils;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IJSONParser;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IStorage;

public class Context
{
  protected final IFactory _factory;
  protected final IStringUtils _stringUtils;
  protected final IThreadUtils _threadUtils;
  protected ILogger _logger;
  protected final IMathUtils _mathUtils;
  protected final IJSONParser _jsonParser;
  protected Planet _planet;
  protected IDownloader _downloader;
  protected EffectsScheduler _effectsScheduler;
	protected IStorage _storage;

  protected Context(IFactory factory, IStringUtils stringUtils, IThreadUtils threadUtils, ILogger logger, IMathUtils mathUtils, IJSONParser jsonParser, Planet planet, IDownloader downloader, EffectsScheduler effectsScheduler, IStorage storage)
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
}