package org.glob3.mobile.generated;//
//  IDownloader.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 27/07/12.
//


//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class URL;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class G3MContext;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IBufferDownloadListener;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IImageDownloadListener;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class TimeInterval;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class FrameTasksExecutor;


public abstract class IDownloader
{
  public void dispose()
  {
  }

  public abstract void initialize(G3MContext context, FrameTasksExecutor frameTasksExecutor);

  public abstract void onResume(G3MContext context);

  public abstract void onPause(G3MContext context);

  public abstract void onDestroy(G3MContext context);

  public abstract void start();

  public abstract void stop();

  public abstract long requestBuffer(URL url, long priority, TimeInterval timeToCache, boolean readExpired, IBufferDownloadListener listener, boolean deleteListener);

  public abstract long requestImage(URL url, long priority, TimeInterval timeToCache, boolean readExpired, IImageDownloadListener listener, boolean deleteListener);

  public abstract void cancelRequest(long requestId);

  public abstract String statistics();

}
