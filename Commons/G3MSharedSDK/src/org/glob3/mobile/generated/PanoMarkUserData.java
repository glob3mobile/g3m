package org.glob3.mobile.generated; 
//
//  PanoDownloadListener.cpp
//  G3MiOSSDK
//
//  Created by Eduardo de la Montaña on 7/12/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//


//
//  PanoDownloadListener.hpp
//  G3MiOSSDK
//
//  Created by Eduardo de la Montaña on 7/12/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//



public class PanoMarkUserData extends MarkUserData
{
  private final String _name;

  private URL _url;


  public PanoMarkUserData(String name, URL url)
  {
     _name = name;
     _url = url;
  }

  public final String getName()
  {
    return _name;
  }

  public final URL getUrl()
  {
    return _url;
  }

  public void dispose()
  {
    _url = null;
  }
}