package org.glob3.mobile.generated; 
//
//  IDownloadListener.h
//  G3MiOSSDK
//
//  Created by José Miguel S N on 26/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//



public abstract class IDownloadListener
{
  public abstract void onDownload(Response response);
  public abstract void onError(Response response);
  public abstract void onCancel(URL url);

  public void dispose()
  {

  }
}