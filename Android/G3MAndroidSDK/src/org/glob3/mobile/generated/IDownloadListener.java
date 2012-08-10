package org.glob3.mobile.generated; 
//
//  IDownloadListener.h
//  G3MiOSSDK
//
//  Created by José Miguel S N on 26/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//



public interface IDownloadListener
{

  void onDownload(Response response);
  void onError(Response response);
  void onCancel(Url url);
}