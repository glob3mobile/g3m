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


//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#import "IBufferDownloadListener.hpp"
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#import "Mark.hpp"

//class JSONObject;


public class PanoMarkUserData extends MarkUserData
{
  private final String _name;
  private URL _url;

  public PanoMarkUserData(String name, URL url)
  {
     _name = name;
     _url = url;
  }

  public void dispose()
  {
    _url = null;
    super.dispose();
  }

  public final String getName()
  {
    return _name;
  }

  public final URL getUrl()
  {
    return _url;
  }

}
