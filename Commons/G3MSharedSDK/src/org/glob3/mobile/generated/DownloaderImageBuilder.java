package org.glob3.mobile.generated;//
//  DownloaderImageBuilder.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/2/14.
//
//

//
//  DownloaderImageBuilder.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/2/14.
//
//



public class DownloaderImageBuilder extends AbstractImageBuilder
{
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
  private final URL _url = new URL();
  private final TimeInterval _timeToCache = new TimeInterval();
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  public final URL _url = new internal();
  public final TimeInterval _timeToCache = new internal();
//#endif
  private final long _priority;
  private final boolean _readExpired;


  public DownloaderImageBuilder(URL url)
  {
	  _url = new URL(url);
	  _priority = DownloadPriority.MEDIUM;
	  _timeToCache = new TimeInterval(TimeInterval.fromDays(30));
	  _readExpired = true;
  }

  public DownloaderImageBuilder(URL url, long priority, TimeInterval timeToCache, boolean readExpired)
  {
	  _url = new URL(url);
	  _priority = priority;
	  _timeToCache = new TimeInterval(timeToCache);
	  _readExpired = readExpired;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isMutable() const
  public final boolean isMutable()
  {
	return false;
  }

  public void dispose()
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	super.dispose();
//#endif
  }

  public final void build(G3MContext context, IImageBuilderListener listener, boolean deleteListener)
  {
	IDownloader downloader = context.getDownloader();
  
	downloader.requestImage(_url, _priority, _timeToCache, _readExpired, new DownloaderImageBuilder_ImageDownloadListener(listener, deleteListener), true);
  }

}
