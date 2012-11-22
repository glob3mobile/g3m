package org.glob3.mobile.generated; 
//
//  IDownloader.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 27/07/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

//
//  IDownloader.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 27/07/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//




//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IBufferDownloadListener;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IImageDownloadListener;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Context;

public abstract class IDownloader
{

  public abstract void onResume(Context context);

  public abstract void onPause(Context context);

  public abstract void onDestroy(Context context);

  public abstract void start();

  public abstract void stop();

  public abstract long requestBuffer(URL url, long priority, IBufferDownloadListener listener, boolean deleteListener);

  public abstract long requestImage(URL url, long priority, IImageDownloadListener listener, boolean deleteListener);

  public abstract void cancelRequest(long requestId);

  public void dispose()
  {
  }

  public abstract String statistics();

}