package org.glob3.mobile.generated; 
//
//  URL.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 27/07/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

//
//  URL.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 27/07/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//



public class Url
{
  private final String _path;


  public Url(Url that)
  {
	  _path = that._path;

  }

  public Url()
  {
	  _path = "";
  }

  public Url(String path)
  {
	  _path = path;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: String getPath() const
  public final String getPath()
  {
	return _path;
  }
}