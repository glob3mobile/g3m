package org.glob3.mobile.generated;
//
//  DownloaderImageBuilder.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 1/2/14.
//
//

//
//  DownloaderImageBuilder.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 1/2/14.
//
//



public class DownloaderImageBuilder extends AbstractImageBuilder
{
  private final URL _url;
  private final TimeInterval _timeToCache;
  private final long _priority;
  private final boolean _readExpired;

  public void dispose()
  {
    super.dispose();
  }

  public DownloaderImageBuilder(URL url)
  {
     _url = url;
     _priority = DownloadPriority.MEDIUM;
     _timeToCache = new TimeInterval(TimeInterval.fromDays(30));
     _readExpired = true;
  }

  public DownloaderImageBuilder(URL url, long priority, TimeInterval timeToCache, boolean readExpired)
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


  public final void build(G3MContext context, IImageBuilderListener listener, boolean deleteListener)
  {
    IDownloader downloader = context.getDownloader();
  
    downloader.requestImage(_url, _priority, _timeToCache, _readExpired, new DownloaderImageBuilder_ImageDownloadListener(listener, deleteListener), true);
  }

}