package org.glob3.mobile.generated; 
//
//  INetwork.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 27/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//




public abstract class INetwork
{

  public abstract void request(String url, IDownloadListener dl);
  // a virtual destructor is needed for conversion to Java
  public void dispose()
  {
  }
}