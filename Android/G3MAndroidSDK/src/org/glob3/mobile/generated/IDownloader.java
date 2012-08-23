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
//class IDownloadListener;


public abstract class IDownloader
{
  public abstract void start();

  public abstract int request(URL url, int priority, IDownloadListener listener, boolean deleteListener);

  public abstract void cancelRequest(int requestId);

  public void dispose()
  {
  }

  public abstract String statistics();

}