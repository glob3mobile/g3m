package org.glob3.mobile.generated; 
//
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



public class DownloaderImageBuilder implements IImageBuilder
{
  private final URL _url;
  private final long _priority;
  private final TimeInterval _timeToCache = new TimeInterval();
  private final boolean _readExpired;


//C++ TO JAVA CONVERTER TODO TASK: The following method format was not recognized, possibly due to an unrecognized macro:
  DownloaderImageBuilder(const URL& url, long priority = DownloadPriority.MEDIUM, const TimeInterval& timeToCache = TimeInterval.fromDays(30), const boolean readExpired = true) : _url(url), _priority(priority), _timeToCache(timeToCache), _readExpired(readExpired)
  {
  }

  public void dispose()
  {
    super.dispose();
  }

  public final void build(G3MContext context, IImageBuilderListener listener, boolean deleteListener)
  {
    IDownloader downloader = context.getDownloader();
  
    downloader.requestImage(_url, _priority, _timeToCache, _readExpired, new DownloaderImageBuilder_ImageDownloadListener(listener, deleteListener), true);
  }

}