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
//class Downloader;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class ILogger;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IGL;

public class Context
{
  protected final IFactory _factory;
  protected final ILogger _logger;
  protected final Planet _planet;
  protected final IDownloader _downloader;

  protected final Downloader _downloaderOLD;


  protected Context(IFactory factory, ILogger logger, Planet planet, Downloader downloaderOLD, IDownloader downloader)
  {
	  _factory = factory;
	  _logger = logger;
	  _planet = planet;
	  _downloader = downloader;
	  _downloaderOLD = downloaderOLD;
  }


//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const IFactory* getFactory() const
  public final IFactory getFactory()
  {
	return _factory;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const ILogger* getLogger() const
  public final ILogger getLogger()
  {
	return _logger;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const Planet* getPlanet() const
  public final Planet getPlanet()
  {
	return _planet;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Downloader* const getDownloaderOLD() const
  public final Downloader getDownloaderOLD()
  {
	return _downloaderOLD;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const IDownloader* getDownloader() const
  public final IDownloader getDownloader()
  {
	return _downloader;
  }
}