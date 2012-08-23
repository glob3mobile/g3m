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
  public void dispose()
  {
  }

  public abstract void onDownload(Response response);
  public abstract void onError(Response response);
  public abstract void onCancel(URL url);

  /* this method will be call, before onCancel, when the data arrived before the cancelation */
  public abstract void onCanceledDownload(Response response);
}