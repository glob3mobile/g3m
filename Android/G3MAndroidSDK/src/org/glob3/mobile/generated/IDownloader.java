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

public interface IDownloader
{
  void start();

  void stop();

  long requestBuffer(URL url, long priority, IBufferDownloadListener listener, boolean deleteListener);

  long requestImage(URL url, long priority, IImageDownloadListener listener, boolean deleteListener);

  void cancelRequest(long requestId);


  String statistics();

}