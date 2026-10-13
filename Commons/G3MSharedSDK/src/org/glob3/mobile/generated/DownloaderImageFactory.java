package org.glob3.mobile.generated;
//
//  DownloaderImageFactory.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 1/2/14.
//
//

//
//  DownloaderImageFactory.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 1/2/14.
//
//



public class DownloaderImageFactory extends AbstractImageFactory
{
  private final URL _url;
  private final TimeInterval _timeToCache;
  private final long _priority;
  private final boolean _readExpired;

  public void dispose()
  {
    super.dispose();
  }

  public DownloaderImageFactory(URL url)
  {
     _url = url;
     _priority = DownloadPriority.MEDIUM;
     _timeToCache = new TimeInterval(TimeInterval.fromDays(30));
     _readExpired = true;
  }

  public DownloaderImageFactory(URL url, long priority, TimeInterval timeToCache, boolean readExpired)
  {
     _url = url;
     _priority = priority;
     _timeToCache = timeToCache;
     _readExpired = readExpired;
  }

  public final boolean isMutable()
  {
    return false;
  }


  public final void create(G3MContext context, IImageFactoryListener listener, boolean deleteListener)
  {
    IDownloader downloader = context.getDownloader();
  
    downloader.requestImage(_url, _priority, _timeToCache, _readExpired, new DownloaderImageFactory_ImageDownloadListener(listener, deleteListener), true);
  }

}