package org.glob3.mobile.generated; 
//
//  Downloader.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 26/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

//
//  Downloader.h
//  G3MiOSSDK
//
//  Created by José Miguel S N on 26/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//





public class Download
{
  public String _url;
  public int _priority;
  public java.util.ArrayList<IDownloadListener > _listeners = new java.util.ArrayList<IDownloadListener >();

  public Download(String url, int priority, IDownloadListener listener)
  {
	  _url = url;
	  _priority = priority;
	_listeners.add(listener);
  }

  public final void addListener(IDownloadListener listener)
  {
	  _listeners.add(listener);
  }
}